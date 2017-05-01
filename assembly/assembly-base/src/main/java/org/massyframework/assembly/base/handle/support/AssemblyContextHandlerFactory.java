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
* @日   期:  2017年4月10日
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
