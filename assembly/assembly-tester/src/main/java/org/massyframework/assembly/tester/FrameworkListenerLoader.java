/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月15日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.tester;

import java.util.List;

import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.FrameworkListener;
import org.massyframework.assembly.util.ServiceLoaderUtils;

/**
 * FrameworkListener加载器，加载以服务方式部署的FrameworkListener.
 */
public class FrameworkListenerLoader implements FrameworkInitializer {
	
	private final List<ClassLoader> classLoaders;

	/**
	 * 
	 */
	public FrameworkListenerLoader(List<ClassLoader> classLoaders) {
		this.classLoaders = classLoaders;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializer#onStartup(org.massyframework.assembly.Framework)
	 */
	@Override
	public void onStartup(Framework framework) throws Exception {
		if (this.classLoaders != null){
			
			for (ClassLoader loader: this.classLoaders){
				List<FrameworkListener> listeners =
						this.loadFromClassLoader(loader);
				if (!listeners.isEmpty()){
					for (FrameworkListener listener: listeners){
						framework.addListener(listener);
					}
				}
			}
		}
	}
	
	/**
	 * 使用服务定位方式加载FrameworkListener.
	 * @param loader 类加载器
	 * @return {@link List}
	 */
	protected List<FrameworkListener> loadFromClassLoader(ClassLoader loader){
		List<FrameworkListener> result =
				ServiceLoaderUtils.loadServicesAtClassLoader(FrameworkListener.class, loader);
		return result;
	}

}
