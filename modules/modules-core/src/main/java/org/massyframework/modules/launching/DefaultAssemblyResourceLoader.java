/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月15日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.modules.launching;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;
import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.protocol.URLFactory;
import org.massyframework.assembly.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缺省的装配件资源加载器
 */
public class DefaultAssemblyResourceLoader extends AbstractFrameworkInitializeLoader {
	
	public static final String ASSEMBILES           = "modules.assembly";
	public static final String ASSEMBLIES_EXTENSION = "modules.assembly.extension";
	public static final String DEFAULT_RESOURCE     = "META-INF/assembly/assembly.xml";

	private ModuleLoader moduleLoader;
	private Logger logger;
	private ServletContext servletContext;
	
	private Map<ModuleIdentifier, List<String>> assemblyResources =
			new LinkedHashMap<ModuleIdentifier, List<String>>();
	
	/**
	 * 构造方法
	 */
	public DefaultAssemblyResourceLoader(
			List<FrameworkInitializer> initializers, 
			Map<String, String> configuration, 
			ModuleLoader moduleLoader,
			ServletContext servletContext) {
		super(initializers);
		Asserts.notNull(moduleLoader, "moduleLoader cannot be null.");
		Asserts.notNull(servletContext, "servletContext cannot be null.");
		
		String assemblies = configuration == null ?
				null :
					configuration.get(ASSEMBILES);
		String extensions = configuration == null?
				null :
					configuration.get(ASSEMBLIES_EXTENSION);
		
		this.logger = LoggerFactory.getLogger(DefaultAssemblyResourceLoader.class);
		this.moduleLoader = moduleLoader;
		this.servletContext = servletContext;
		
		this.assemblyResources = this.parser(assemblies, extensions);
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.launching.AbstractFrameworkInitializeLoader#getClassLoaderes()
	 */
	@Override
	protected List<ClassLoader> getClassLoaderes() throws Exception {
		List<ClassLoader> result = new ArrayList<ClassLoader>();
		
		for (ModuleIdentifier identifier: this.assemblyResources.keySet()){
			Module module = this.moduleLoader.loadModule(identifier);
			result.add(module.getClassLoader());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.launching.AbstractFrameworkInitializeLoader#getAssemblyResources()
	 */
	@Override
	public List<AssemblyResource> getAssemblyResources() throws Exception {
		List<AssemblyResource> result =
				new ArrayList<AssemblyResource>();
		for (Entry<ModuleIdentifier, List<String>> entry: this.assemblyResources.entrySet()){
			ModuleIdentifier identifier = entry.getKey();
			List<String> resources = entry.getValue();
			
			Module module = this.moduleLoader.loadModule(identifier);
			ClassLoader loader = module.getClassLoader();
			
			if (resources == null){
				Enumeration<URL> em = loader.getResources(DEFAULT_RESOURCE);
				while (em.hasMoreElements()){
					URL url = em.nextElement();
					AssemblyResource resource =
							new DefaultAssemblyResource(loader, url);
					result.add(resource);
				}
			}else{
				for (String resource: resources){
					AssemblyResource assemblyResource = this.createAssemblyResource(loader, resource);
					if (assemblyResource == null){
						if (logger.isWarnEnabled()){
							logger.warn("cannot found assembly resource: module=" + identifier.toString() + ", path=" + resource + ".");;
						}
					}else{
						result.add(assemblyResource);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 创建装配件资源，支持从classpath,servletpath和常用的http/file等协议加载配置资源
	 * @param loader 类加载器
	 * @param resource 资源
	 * @return  {@link AssemblyResource}
	 * @throws MalformedURLException 
	 */
	protected AssemblyResource createAssemblyResource(ClassLoader loader, String resource) throws MalformedURLException{
		URL url = null;
		if (URLFactory.hasClassPathPrefix(resource)){
			url = URLFactory.createClassPathUrl(resource, loader);
		}else{
			if (URLFactory.hasServletPathPrefix(resource)){
				url = URLFactory.createServletPathUrl(resource, servletContext);
			}
		}
		
		if (url == null){
			url = new URL(resource);
		}
		
		return new DefaultAssemblyResource(loader, url);
	}

	/**
	 * 解析
	 * @param assemblies
	 * @param extensions
	 */
	protected Map<ModuleIdentifier, List<String>> parser(String assemblies, String extensions){
		Map<ModuleIdentifier, List<String>> result =
				new LinkedHashMap<ModuleIdentifier, List<String>>();
		
		if (assemblies != null){
			assemblies = StringUtils.deleteWhitespace(assemblies);
		}
	
		String[] names = StringUtils.split(assemblies, ",");
		if (names != null){
			for (String name: names){
				if (!name.equals("")){
					ModuleIdentifier identifier =
							ModuleIdentifierUtils.parse(name);
					result.put(identifier, null);
				}
			}
		}
		
		if (extensions != null){
			extensions = StringUtils.replaceAll(extensions, "，", ",");
		}
		String[] texts = StringUtils.split(extensions, ",");
		if (texts != null){
			for (String text: texts){
				int index = StringUtils.indexOf(text, "@");
				if (index<0){
					if (logger.isWarnEnabled()){
						logger.warn("incorrect format:" + text + ".");
					}
					continue;
				}
				
				String resource = StringUtils.substring(text, index);
				String moduleName = StringUtils.substring(text, index + 1, text.length());
				ModuleIdentifier identifier = ModuleIdentifierUtils.parse(moduleName);
				
				List<String> resources = result.get(identifier);
				if (resources == null){
					resources = new ArrayList<String>();
					result.put(identifier, resources);
				}
				resources.add(resource);
			}
		}
		
		return result;
	}
}
