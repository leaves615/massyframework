/**
 * 
 */
package org.massyframework.assembly.servlet.jasper;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.ServletContext;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.descriptor.TaglibDescriptor;

import org.apache.jasper.compiler.Localizer;
import org.apache.jasper.servlet.TldScanner;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.descriptor.tld.TaglibXml;
import org.apache.tomcat.util.descriptor.tld.TldResourcePath;
import org.massyframework.assembly.base.web.MixinClassLoader;
import org.massyframework.assembly.spec.JarEntrySpecification;
import org.massyframework.assembly.util.JarUtils;
import org.xml.sax.SAXException;

/**
 * @author huangkaihui
 *
 */
final class TldScannerEx extends TldScanner {
	
	
	
	private static final Log log = LogFactory.getLog(TldScannerEx.class);
    private static final String MSG = "org.apache.jasper.servlet.TldScanner";
    private static final String WEB_INF = "/WEB-INF/";
    private final ServletContext context;
    
    private final TldParserEx tldParser;
    private ClassLoader loader;
	
	/**
	 * @param context
	 * @param namespaceAware
	 * @param validation
	 * @param blockExternal
	 */
	public TldScannerEx(ServletContext context, boolean namespaceAware, boolean validation, boolean blockExternal) {
		super(context, namespaceAware, validation, blockExternal);
		this.tldParser = new TldParserEx(namespaceAware, validation, blockExternal);
		this.context = context;
	}
	
	

	@Override
	public void setClassLoader(ClassLoader classLoader) {
		this.loader = classLoader;
		this.tldParser.setClassLoader(classLoader);
	}
	
	
	@Override
	public void scanJars() {
		super.scanJars();
		if (this.loader != null){
			List<String> tldResources = this.getTldFiles();
			
			for (String resource: tldResources){
				
				try{
					this.parseTld(resource);
				}catch(Exception e){
					
				}
					
			}
		}
	}
	
	private List<String> getTldFiles(){
		List<String> result = new ArrayList<String>();
		if (this.loader != null){
		
			if (this.loader instanceof MixinClassLoader){
				List<ClassLoader> loaders =
						((MixinClassLoader)this.loader).getClassLoaders();
				
				JarEntrySpecification spec =
						new JarEntrySpecification("META-INF", ".tld");
				for (ClassLoader cl: loaders){
					try {
						Enumeration<URL> em = cl.getResources("META-INF");
						 //内部类
				        FilenameFilter filter = new FilenameFilter(){
				            @Override
				            public boolean accept(File dir, String name) {
				                return name.endsWith(".tld");          
				            }
				        };
						
						while (em.hasMoreElements()){
							URL url = em.nextElement();
							try{
								JarFile jarFile = JarUtils.extractJarFile(url);
								if (jarFile != null){
									List<JarEntry> entries = JarUtils.findJarEntries(jarFile, spec);
									for (JarEntry entry: entries){
										result.add(entry.getName());
									}
								}else{
									File file = new File(url.toURI());
									if (file.isDirectory()){
										File[] files = file.listFiles(filter);
										for (File tmp: files){
											result.add(tmp.getName());
										}
									}
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return result;
	}



	@Override
	protected void scanJspConfig() throws IOException, SAXException {
		JspConfigDescriptor jspConfigDescriptor = context.getJspConfigDescriptor();
        if (jspConfigDescriptor == null) {
            return;
        }

        Collection<TaglibDescriptor> descriptors = jspConfigDescriptor.getTaglibs();
        for (TaglibDescriptor descriptor : descriptors) {
            String taglibURI = descriptor.getTaglibURI();
            String resourcePath = descriptor.getTaglibLocation();
            // Note: Whilst the Servlet 2.4 DTD implies that the location must
            // be a context-relative path starting with '/', JSP.7.3.6.1 states
            // explicitly how paths that do not start with '/' should be
            // handled.
            if (!resourcePath.startsWith("/")) {
                resourcePath = WEB_INF + resourcePath;
            }
            if (this.getUriTldResourcePathMap().containsKey(taglibURI)) {
                log.warn(Localizer.getMessage(MSG + ".webxmlSkip",
                        resourcePath,
                        taglibURI));
                continue;
            }

            if (log.isTraceEnabled()) {
                log.trace(Localizer.getMessage(MSG + ".webxmlAdd",
                        resourcePath,
                        taglibURI));
            }

            URL url = context.getResource(resourcePath);
            if (url != null) {
                TldResourcePath tldResourcePath;
                if (resourcePath.endsWith(".jar")) {
                    // if the path points to a jar file, the TLD is presumed to be
                    // inside at META-INF/taglib.tld
                    tldResourcePath = new TldResourcePath(url, resourcePath, "META-INF/taglib.tld");
                } else {
                    tldResourcePath = new TldResourcePath(url, resourcePath);
                }
                // parse TLD but store using the URI supplied in the descriptor
                TaglibXml tld = tldParser.parse(tldResourcePath);
                this.getUriTldResourcePathMap().put(taglibURI, tldResourcePath);
                this.getTldResourcePathTaglibXmlMap().put(tldResourcePath, tld);
                if (tld.getListeners() != null) {
                    this.getListeners().addAll(tld.getListeners());
                }
            } else {
                log.warn(Localizer.getMessage(MSG + ".webxmlFailPathDoesNotExist",
                        resourcePath,
                        taglibURI));
                continue;
            }
        }
	}
	
	@Override
	protected void parseTld(TldResourcePath path) throws IOException, SAXException {
        if (this.getTldResourcePathTaglibXmlMap().containsKey(path)) {
            // TLD has already been parsed as a result of processing web.xml
            return;
        }
        TaglibXml tld = tldParser.parse(path);
        String uri = tld.getUri();
        if (uri != null) {
            if (!this.getUriTldResourcePathMap().containsKey(uri)) {
                this.getUriTldResourcePathMap().put(uri, path);
            }
        }
        this.getTldResourcePathTaglibXmlMap().put(path, tld);
        if (tld.getListeners() != null) {
            this.getListeners().addAll(tld.getListeners());
        }
    }

}
