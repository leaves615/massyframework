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
* @日   期:  2017年5月13日
*/
package org.massyframework.assembly.struts2;

import java.util.Enumeration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.massyframework.assembly.util.Asserts;

/**
 * FilterConfig Wrapper
 *
 */
final class FilterConfigWrapper implements FilterConfig {
	
	private FilterConfig delegate;
	private ServletContextWrapper contextWrapper;

	/**
	 * 
	 */
	public FilterConfigWrapper(FilterConfig delegate) {
		Asserts.notNull(delegate, "delegate cannot be null.");
		this.delegate = delegate;
		this.contextWrapper = new ServletContextWrapper(
				delegate.getServletContext());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.FilterConfig#getFilterName()
	 */
	@Override
	public String getFilterName() {
		return this.delegate.getFilterName();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.FilterConfig#getServletContext()
	 */
	@Override
	public ServletContext getServletContext() {
		return this.contextWrapper;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.FilterConfig#getInitParameter(java.lang.String)
	 */
	@Override
	public String getInitParameter(String name) {
		return this.delegate.getInitParameter(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.FilterConfig#getInitParameterNames()
	 */
	@Override
	public Enumeration<String> getInitParameterNames() {
		return this.delegate.getInitParameterNames();
	}

}
