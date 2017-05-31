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
package org.massyframework.assembly.base.web;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.massyframework.assembly.util.Asserts;
import org.massyframework.assembly.web.HttpResource;
import org.massyframework.assembly.web.HttpResourceProcessor;

/**
 * 资源Servlet
 */
public class HttpResourceServlet implements Servlet {

	private ServletConfig config;
	private final HttpResource resource;
	private final HttpResourceProcessor callback;
	/**
	 * 
	 */
	public HttpResourceServlet(HttpResource resource, HttpResourceProcessor callback) {
		Asserts.notNull(resource, "resource cannot be null.");
		Asserts.notNull(callback, "callback cannot be null.");
		this.resource = resource;
		this.callback = callback;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#getServletConfig()
	 */
	@Override
	public ServletConfig getServletConfig() {
		return this.config;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		this.callback.process((
				HttpServletRequest)req, (HttpServletResponse)res, this.resource);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#getServletInfo()
	 */
	@Override
	public String getServletInfo() {
		return "HttpResource Servlet";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#destroy()
	 */
	@Override
	public void destroy() {
		
	}

}
