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
* @日   期:  2017年5月19日
*/
package org.massyframework.assembly.struts2;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ConfigurationProvider;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.inject.Context;
import com.opensymphony.xwork2.inject.Factory;
import com.opensymphony.xwork2.inject.Scope;
import com.opensymphony.xwork2.util.location.LocatableProperties;

/**
 * 类加载器提供者
 */
public class ClassLoaderProvider implements ConfigurationProvider {

	/**
	 * 
	 */
	public ClassLoaderProvider() {
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.config.ContainerProvider#destroy()
	 */
	@Override
	public void destroy() {

	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.config.ContainerProvider#init(com.opensymphony.xwork2.config.Configuration)
	 */
	@Override
	public void init(Configuration configuration) throws ConfigurationException {

	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.config.ContainerProvider#needsReload()
	 */
	@Override
	public boolean needsReload() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.config.ContainerProvider#register(com.opensymphony.xwork2.inject.ContainerBuilder, com.opensymphony.xwork2.util.location.LocatableProperties)
	 */
	@Override
	public void register(ContainerBuilder builder, LocatableProperties props) throws ConfigurationException {
		builder.factory(ClassLoader.class, "objectFactory.classloader", new FactoryImpl(), Scope.SINGLETON);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.config.PackageProvider#loadPackages()
	 */
	@Override
	public void loadPackages() throws ConfigurationException {
		
	}
	
	private class FactoryImpl implements Factory<ClassLoader> {
		
		private ClassLoader loader ;
		
		public FactoryImpl(){
			this.loader = Thread.currentThread().getContextClassLoader();
		}

		@Override
		public ClassLoader create(Context context) throws Exception {
			return this.loader;
		}
		
	}

}
