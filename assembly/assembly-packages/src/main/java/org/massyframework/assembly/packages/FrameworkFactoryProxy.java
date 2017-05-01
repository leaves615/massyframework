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
* @日   期:  2017年4月12日
*/
package org.massyframework.assembly.packages;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkFactory;
import org.massyframework.assembly.FrameworkInitializeLoader;
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
	public Framework createFramework(Map<String, String> configuration, FrameworkInitializeLoader initializeHandler)
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
	
	protected URLClassLoader createdEmbedClassLoader() throws IOException{
		URL[] jars = EmbedJarUtils.loadEmbedJars(FrameworkFactoryProxy.class);
		return new URLClassLoader(
				jars,
				this.getClass().getClassLoader());
	}

}
