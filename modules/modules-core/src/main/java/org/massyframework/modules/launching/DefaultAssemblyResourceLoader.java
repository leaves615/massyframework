/**
* @Copyright: 2017 smarabbit studio. 
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*   
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月15日
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
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.DefaultAssemblyResource;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.protocol.URLFactory;
import org.massyframework.assembly.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缺省的装配件资源加载器
 */
public class DefaultAssemblyResourceLoader extends AbstractFrameworkInitializeLoader {
	
	public static final String MODULES_STARTUP            = "modules.startup";
	public static final String MODULES_ASSEMBLY_RESOURCES = "modules.assembly.resources";
	public static final String DEFAULT_RESOURCE     = "META-INF/assembly/assembly.xml";

	private Logger logger;
	private ModuleLoader moduleLoader;
	private ServletContext servletContext;	
	private String identifiers;
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
				
		this.identifiers = configuration == null ?
				null :
					configuration.get(MODULES_STARTUP);
		
		String resources = configuration == null?
				null :
					configuration.get(MODULES_ASSEMBLY_RESOURCES);
		this.moduleLoader = moduleLoader;
		this.logger = LoggerFactory.getLogger(DefaultAssemblyResourceLoader.class);
		this.servletContext = servletContext;
		
		this.assemblyResources = this.parserAssemblyResources(resources);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.launching.AbstractFrameworkInitializeLoader#getClassLoaderes()
	 */
	@Override
	protected List<ClassLoader> getClassLoaderes() throws Exception {
		return this.parserClassLoader(this.identifiers);
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

	protected List<ClassLoader> parserClassLoader(String identifiers) 
			throws ModuleLoadException{
		List<ClassLoader> result = new ArrayList<ClassLoader>();
		if (identifiers != null){
			String[] names = StringUtils.split(identifiers, ",");
			for (String name: names){
				name = StringUtils.deleteWhitespace(name);
				if (!"".equals(name)){
					int index = StringUtils.indexOf(name, ":");
					String moduleName = name;
					String slot = "main";
					if (index != -1){
						moduleName = StringUtils.substring(name, 0, index);
						slot = StringUtils.substring(name, index + 1, name.length());
					}
					ModuleIdentifier identifier =
							ModuleIdentifier.create(moduleName, slot);
					Module module = this.moduleLoader.loadModule(identifier);
					result.add(module.getClassLoader());
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 解析装配件资源
	 * @param extensions
	 */
	protected Map<ModuleIdentifier, List<String>> parserAssemblyResources(String resources){
		Map<ModuleIdentifier, List<String>> result =
				new LinkedHashMap<ModuleIdentifier, List<String>>();		
		if (resources != null){
			resources = StringUtils.replaceAll(resources, "，", ",");
		}
		String[] texts = StringUtils.split(resources, ",");
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
				
				List<String> assemblyResources = result.get(identifier);
				if (assemblyResources == null){
					assemblyResources = new ArrayList<String>();
					result.put(identifier, assemblyResources);
				}
				assemblyResources.add(resource);
			}
		}
		
		return result;
	}
}
