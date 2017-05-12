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
* @日   期:  2017年5月10日
*/
package org.massyframework.assembly.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.DefaultAssemblyResource;
import org.massyframework.assembly.FrameworkInitializeLoader;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.protocol.URLFactory;
import org.massyframework.assembly.util.ServiceLoaderUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 单一类加载器加载装配件
 */
public class SingleableClassLoaderInitializeLoader implements FrameworkInitializeLoader {

	public static final String ASSEMBLY_RESOURCES = "assembly.resources";
	private List<FrameworkInitializer> initializers;
	private ClassLoader loader;
	private ServletContext servletContext;
	private String resources;
	
	public SingleableClassLoaderInitializeLoader(List<FrameworkInitializer> initializers){
		this(null, Thread.currentThread().getContextClassLoader(), null, null);
	}
	
	/**
	 * 构造方法
	 * @param initializers 初始化器
	 * @param loader 类加载器
	 * @param configuration 配置参数
	 */
	public SingleableClassLoaderInitializeLoader(List<FrameworkInitializer> initializers, ClassLoader loader,
			Map<String, String> configuration){
		this(initializers, loader, configuration, null);
	}
	
	/**
	 * 构造方法
	 * @param initializers 初始化器
	 * @param loader 类加载器
	 * @param configuration 配置参数
	 * @param servletContext Servlet上下文
	 */
	public SingleableClassLoaderInitializeLoader(List<FrameworkInitializer> initializers, ClassLoader loader,
			Map<String, String> configuration, ServletContext servletContext) {
		this.initializers = initializers;
		this.loader = loader;
		this.resources = configuration == null ?
				null:
					configuration.get(ASSEMBLY_RESOURCES);
		this.servletContext = servletContext;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializeLoader#getFrameworkInitializer()
	 */
	@Override
	public List<FrameworkInitializer> getFrameworkInitializer() throws Exception {
		List<FrameworkInitializer> result =
				ServiceLoaderUtils.loadServices(FrameworkInitializer.class, loader);
		if (!CollectionUtils.isEmpty(this.initializers)){
			result.addAll(0, this.initializers);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializeLoader#getAssemblyResources()
	 */
	@Override
	public List<AssemblyResource> getAssemblyResources() throws Exception {
		List<AssemblyResource> result =
				new ArrayList<AssemblyResource>();
		
		if (this.resources != null){
			String[] arr = StringUtils.split(this.resources, ",");
			for (String resource: arr){
				AssemblyResource res =
						this.createAssemblyResource(resource);
				result.add(res);
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
	protected AssemblyResource createAssemblyResource(String resource) throws MalformedURLException{
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
		
		return new DefaultAssemblyResource(this.loader, url);
	}

}
