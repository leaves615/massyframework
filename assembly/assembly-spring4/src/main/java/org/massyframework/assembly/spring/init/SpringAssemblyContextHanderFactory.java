/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月14日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
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
