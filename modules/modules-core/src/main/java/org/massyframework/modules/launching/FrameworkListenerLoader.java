/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
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
