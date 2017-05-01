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
* @日   期:  2017年4月14日
*/
package org.massyframework.assembly.spring.init;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.base.handle.AssemblyContextHandler;
import org.massyframework.assembly.base.handle.support.AssemblyContextHandlerFactory;
import org.massyframework.assembly.base.handle.support.ServletContextListenerContextManagement;
import org.massyframework.assembly.base.handle.support.ServletContextManagment;

/**
 * Spring装配件上下文处理工厂
 *
 */
public class SpringAssemblyContextHanderFactory extends AssemblyContextHandlerFactory {

	/**
	 * 
	 */
	public SpringAssemblyContextHanderFactory() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ServiceFactory#getService(org.massyframework.assembly.Assembly)
	 */
	@Override
	public AssemblyContextHandler getService(Assembly assembly) {
		if (this.hasRegistWithListener(assembly)){
			return new ServletContextListenerContextManagement(
					ContextLoaderListenerEx.class);
		}
		if (this.hasRegistWithServlet(assembly)){
			return new ServletContextManagment(DispatcherServletEx.class);
		}
		return new StandaloneContextManagement();
	}

}
