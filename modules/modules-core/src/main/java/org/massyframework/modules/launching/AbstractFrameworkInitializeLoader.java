/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.modules.launching;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.FrameworkInitializeLoader;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.util.ClassLoaderUtils;
import org.massyframework.assembly.util.ServiceLoaderUtils;

/**
 * 抽象的运行框架初始化加载器
 */
abstract class AbstractFrameworkInitializeLoader implements FrameworkInitializeLoader {

	private static final String PATH = "classpath:META-INF/assembly/assembly.xml";
	
	private List<FrameworkInitializer> initializers;
	
	/**
	 * 
	 * @param initializers
	 */
	public AbstractFrameworkInitializeLoader(List<FrameworkInitializer> initializers) {
		this.initializers = initializers;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializeHandler#getFrameworkInitializer()
	 */
	@Override
	public List<FrameworkInitializer> getFrameworkInitializer() throws Exception{
		List<ClassLoader> loaders = this.getClassLoaderes();
		List<FrameworkInitializer> result = new ArrayList<FrameworkInitializer>();
		for (ClassLoader loader: loaders){
			List<FrameworkInitializer> list = null;
			if (loader instanceof URLClassLoader){
				list = ServiceLoaderUtils.loadServicesAtClassLoader(
						FrameworkInitializer.class, (URLClassLoader)loader);
			}else{
				list = ServiceLoaderUtils.loadServices(FrameworkInitializer.class, loader);
			}
			result.addAll(list);
		}
		
		//添加直接使用FrameworkListener服务部署的监听器
		result.add(new FrameworkListenerLoader(loaders));
		
		if (this.initializers != null){
			result.addAll(0, this.initializers);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializeHandler#getAssemblyResources()
	 */
	@Override
	public List<AssemblyResource> getAssemblyResources() throws Exception{
		List<ClassLoader> loaders = this.getClassLoaderes();
		List<AssemblyResource> result = new ArrayList<AssemblyResource>();
		
		for (ClassLoader loader: loaders){
			try{
				List<AssemblyResource> list =
						this.loadAssemblyResourcesFromClassLoader(loader);
				result.addAll(list);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
	 * 从ClassLoader中加载AssemblyLocation集合
	 * @param loader 类加载器
	 * @return {@link List}
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	protected List<AssemblyResource> loadAssemblyResourcesFromClassLoader(ClassLoader loader)
			throws IOException, URISyntaxException{				
		List<AssemblyResource> result = new ArrayList<AssemblyResource>();

		if (loader instanceof URLClassLoader){
			List<URL> resources = ClassLoaderUtils.getResources(PATH, (URLClassLoader)loader);
			for (URL resource: resources){
				result.add(new DefaultAssemblyResource(loader, resource));
			}
		}else{
			Enumeration<URL> em = loader.getResources(PATH);
			while (em.hasMoreElements()){
				URL url = em.nextElement();
				result.add(new DefaultAssemblyResource(loader, url));
			}
		}
		
		return result;
	}
	
	/**
	 * 获取类加载集合
	 * @return {@link List}
	 */
	protected abstract List<ClassLoader> getClassLoaderes() throws Exception;
}
