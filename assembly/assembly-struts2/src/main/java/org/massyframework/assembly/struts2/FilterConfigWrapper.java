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
import java.util.Vector;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.massyframework.assembly.util.Asserts;

/**
 * FilterConfig Wrapper
 *
 */
final class FilterConfigWrapper implements FilterConfig {
	
	private static final String PROVIDERS = "configProviders";
	private FilterConfig delegate;
	private ServletContext context;
	
	private String configProviders;

	/**
	 * 
	 */
	public FilterConfigWrapper(FilterConfig delegate) {
		Asserts.notNull(delegate, "delegate cannot be null.");
		this.delegate = delegate;
		this.context = new StrutsServletContext(delegate.getServletContext());
		this.configProviders = delegate.getInitParameter(PROVIDERS);
		if (this.configProviders == null){
			this.configProviders = ClassLoaderProvider.class.getName();
		}else{
			this.configProviders = this.configProviders + "," + ClassLoaderProvider.class.getName();
		}
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
		return this.context;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.FilterConfig#getInitParameter(java.lang.String)
	 */
	@Override
	public String getInitParameter(String name) {
		if (PROVIDERS.equals(name)){
			return this.configProviders;
		}
		return this.delegate.getInitParameter(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.FilterConfig#getInitParameterNames()
	 */
	@Override
	public Enumeration<String> getInitParameterNames() {
		Vector<String> result = new Vector<String>();
		Enumeration<String> em = this.delegate.getInitParameterNames();
		while (em.hasMoreElements()){
			result.add(em.nextElement());
		}
		
		result.add(PROVIDERS);
		return result.elements();
	}

}
