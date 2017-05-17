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

import org.massyframework.assembly.Assembly;

/**
 * Http服务， 提供注册页面映射、资源等能力
 */
public interface HttpService {

	/**
	 * 绑定映射,将<code>page</code>和特定的<code>alias</code>进行绑定。
	 * <code>alias</code>必须使用"/"开头<br>
	 * <code>page</code>必须使用"/"开头<br>
	 * 以下写法表明当用户访问/login.html页面时，系统会将请求转发到/account/login.html进行处理。
	 * <pre>
	 * httpService.registerMapping("/login.html", "/account/login.html");
	 * </pre>
	 * 
	 * @param alias 别名，去掉Servlet上下文路径后的部分
	 * @param page 页面路径， 去掉Servlet上下文路径后的部分 
	 * @param assembly 页面提供的装配件
	 * @return <code>true</code>注册成功，<code>false<code>注册失败，因<code>alias</code>已经被注册
	 */
	boolean bindMapping(String alias, String page, Assembly assembly);
	
	/**
	 * 将<code>resource>注册到URL 名称空间,当静态资源存放在自定义的独立类加载器中，通过资源注册方式可以让外部用户直接访问<br>
	 * <code>alias</code>必须使用"/"开头<br>
	 * <code>name</code>必须使用"/"开头<br>
	 * 以下写法表明系统将/account请求，与装配件路径下的/tmp目录相关联，如果一个请求是/account/test.jsp,实际上它指向装配件/tmp/test.jsp.
	 * @param alias 别名，去掉Servlet上下文路径后的部分
	 * @param name 资源在装配件类加载器的路径，去掉Servlet上下文路径后的部分
	 * @param assembly 资源所处的装配件
	 * @return <code>true</code>注册成功，<code>false<code>注册失败，因<code>alias</code>已经被注册
	 */
	boolean registerResources(String alias, String name, Assembly assembly);
}
