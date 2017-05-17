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
* @日   期:  2017年5月16日
*/
package org.massyframework.assembly.struts2;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.dispatcher.Dispatcher;
import org.massyframework.assembly.util.Asserts;

import com.opensymphony.xwork2.config.ConfigurationManager;

/**
 * @author huangkaihui
 *
 */
public class DispatcherEx extends Dispatcher {
	
	private ClassLoader loader;

	/**
	 * @param servletContext
	 * @param initParams
	 */
	public DispatcherEx(ServletContext servletContext, 
			Map<String, String> initParams, ClassLoader loader) {
		super(servletContext, initParams);
		
		Asserts.notNull(loader, "loader cannot be null.");
		this.loader = loader;
	}
	
		
	

	/* (non-Javadoc)
	 * @see org.apache.struts2.dispatcher.Dispatcher#createConfigurationManager(java.lang.String)
	 */
	@Override
	protected ConfigurationManager createConfigurationManager(String name) {
		ConfigurationManager result = super.createConfigurationManager(name);
		result.addContainerProvider( 
	            		new ClassLoaderContainerProvider(loader));
		return result;
	}


	public ClassLoader getClassLoader(){
		return this.loader;
	}
}
