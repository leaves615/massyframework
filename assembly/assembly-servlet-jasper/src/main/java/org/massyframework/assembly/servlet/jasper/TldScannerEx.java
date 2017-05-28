/**
 * 
 */
package org.massyframework.assembly.servlet.jasper;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.descriptor.TaglibDescriptor;

import org.apache.jasper.compiler.Localizer;
import org.apache.jasper.servlet.TldScanner;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.descriptor.tld.TaglibXml;
import org.apache.tomcat.util.descriptor.tld.TldResourcePath;
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
		this.tldParser.setClassLoader(classLoader);
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
