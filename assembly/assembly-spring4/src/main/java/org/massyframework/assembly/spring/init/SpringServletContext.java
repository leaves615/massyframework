/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
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

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.massyframework.assembly.base.web.ServletContextWrapper;
import org.massyframework.assembly.util.Asserts;
import org.springframework.web.context.WebApplicationContext;

/**
 * 提供ServletCOntext封装，阻断Spring ContextLoaderListener和DispatcherServlet之间的联系
 */
class SpringServletContext extends ServletContextWrapper {
	
	private final ServletContext context;

	/**
	 * 
	 */
	public SpringServletContext(ServletContext context) {
		Asserts.notNull(context, "context cannot be null.");
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String name) {
		if (WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE.equals(name)){
			return null;
		}
		
		return this.context.getAttribute(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getAttributeNames()
	 */
	@Override
	public Enumeration<String> getAttributeNames() {
		Vector<String> vector = new Vector<String>();
		Enumeration<String> em = this.context.getAttributeNames();
		while (em.hasMoreElements()){
			String name = em.nextElement();
			if (!WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE.equals(name)){
				vector.add(name);
			}
		}
		return vector.elements();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setAttribute(String name, Object object) {
		this.context.setAttribute(name, object);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.web.ServletContextWrapper#getInternalContext()
	 */
	@Override
	protected ServletContext getInternalContext() {
		return this.context;
	}
}
