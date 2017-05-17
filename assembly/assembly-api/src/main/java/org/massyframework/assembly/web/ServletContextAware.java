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
* @日   期:  2017年5月17日
*/
package org.massyframework.assembly.web;

import javax.servlet.ServletContext;

/**
 * ServletContext感知接口
 */
public interface ServletContextAware {

	/**
	 * 设置Servlet上下文
	 * @param servletContext
	 */
	void setServletContext(ServletContext servletContext);
	
	/**
	 * 尝试向<code>target</code>绑定Servelt上下文
	 * @param target {@link T}目标对象
	 * @param servletContext {@link ServletContext}
	 */
	public static <T> void maybeToBind(T target, ServletContext servletContext){
		if (target != null){
			if (target instanceof ServletContextAware){
				((ServletContextAware)target).setServletContext(servletContext);
			}
		}
		
		
	}
}
