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
* @日   期:  2017年5月16日
*/
package org.massyframework.assembly.struts2;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ContainerProvider;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.inject.Scope;
import com.opensymphony.xwork2.util.location.LocatableProperties;

/**
 * @author huangkaihui
 *
 */
public class ClassLoaderContainerProvider implements ContainerProvider {

	private ClassLoaderFactory factory;
	/**
	 * 
	 */
	public ClassLoaderContainerProvider(ClassLoader classLoader) {
		this.factory = new ClassLoaderFactory(classLoader);
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
		builder.factory(ClassLoader.class, "objectFactory.classloader", factory, Scope.SINGLETON);

	}

}
