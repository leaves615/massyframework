/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.packages;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkFactory;
import org.massyframework.assembly.FrameworkInitializeHandler;
import org.massyframework.assembly.util.EmbedJarUtils;

/**
 * FrameworkFactory代理，代理内部ClassLoader提供的FrameworkFactory服务。
 */
public final class FrameworkFactoryProxy implements FrameworkFactory {

	/**
	 * 
	 */
	public FrameworkFactoryProxy() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkFactory#createFramework(java.util.Map, org.massyframework.assembly.FrameworkInitializeHandler)
	 */
	@Override
	public Framework createFramework(Map<String, String> configuration, FrameworkInitializeHandler initializeHandler)
			throws Exception {
		try{
			ClassLoader loader = this.createdEmbedClassLoader();
			Class<?> clazz = loader.loadClass("org.massyframework.assembly.runtime.DefaultFrameworkFactory");
			FrameworkFactory factory=  FrameworkFactory.class.cast(clazz.newInstance());
			return factory.createFramework(configuration, initializeHandler);
		}catch(Exception e){
			throw e;
		}
	}
	
	protected ClassLoader createdEmbedClassLoader() throws IOException{
		URL[] jars = EmbedJarUtils.loadEmbedJars(FrameworkFactoryProxy.class);
		return new URLClassLoader(
				jars,
				this.getClass().getClassLoader());
	}

}
