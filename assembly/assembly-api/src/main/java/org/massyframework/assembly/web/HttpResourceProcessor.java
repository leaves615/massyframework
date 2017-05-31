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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于处理在HttpService中注册的特定资源。
 */
public interface HttpResourceProcessor {

	/**
	 * 所支持资源扩展名
	 * @return {@link String}数组
	 */
	String[] getExtensionNames();
	
	/**
	 * 服务请求处理
	 * @param req {@link HttpServletRequest} http请求
	 * @param resp {@link HttpServletResponse} http响应
	 * @param resource {@link HttpResource}, Http资源
	 * @throws ServletException 非预期的Servlet异常
	 * @throws IOException IO对写异常
	 */
	void process(HttpServletRequest req, HttpServletResponse resp, 
			HttpResource resource) throws ServletException, IOException;
}
