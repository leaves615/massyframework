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
* @日   期:  2017年4月9日
*/
package org.massyframework.assembly.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.util.Asserts;

/**
 * 在Web环境下，注入ServletContext
 */
final class ServletContextInitializer implements FrameworkInitializer {

	private final ServletContext context;
	/**
	 * 
	 */
	public ServletContextInitializer(ServletContext context) {
		Asserts.notNull(context, "context cannot be null.");
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializer#onStartup(org.massyframework.assembly.Framework)
	 */
	@Override
	public void onStartup(Framework framework) throws Exception {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(Constants.SERVICE_DESCRIPTION, "当前Web应用程序的Servlet上下文.");
		props.put(Constants.SERVICE_RANKING, Integer.MAX_VALUE);
		
		framework.addExportService(ServletContext.class, this.context, props);
		this.context.setAttribute(Framework.class.getName(), framework);
	}

}
