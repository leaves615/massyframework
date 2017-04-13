/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle.support;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ServiceFactory;
import org.massyframework.assembly.base.handle.AssemblyContextHandler;

/**
 * 装配件上下文处理器工厂，根据配置文件的定义，提供具体的装配件上下文实例
 *
 */
public abstract class AssemblyContextHandlerFactory 
	implements ServiceFactory<AssemblyContextHandler> {

	/**
	 * 
	 */
	public AssemblyContextHandlerFactory() {
	
	}

	/**
	 * 是否需要注册ServletContext下的Servlet
	 * @param assembly 装配件
	 * @return <code>true</code>需要，<code>false</code>不需要
	 */
	protected boolean hasRegistWithServlet(Assembly assembly){
		String servletName = assembly.getInitParameter(Constants.SERVLET_NAME);
		return servletName != null;
	}
	
	/**
	 * 是否需要注册ServletContext下的Listener
	 * @param assembly 装配件
	 * @return <code>true</code>需要，<code>false</code>不需要
	 */
	protected boolean hasRegistWithListener(Assembly assembly){
		String className = assembly.getInitParameter(Constants.LISTENER_CLASSNAME);
		return className != null;
	}
	
	/**
	 * 是否需要注册ServletContext下的Filter
	 * @param assembly 装配件
	 * @return <code>true</code>需要，<code>false</code>不需要
	 */
	protected boolean hasRegistWithFilter(Assembly assembly){
		return false;
	}
}
