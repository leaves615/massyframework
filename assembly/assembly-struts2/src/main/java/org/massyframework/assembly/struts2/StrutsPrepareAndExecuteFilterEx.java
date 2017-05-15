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

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;
import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.AssemblyContextReference;
import org.massyframework.assembly.ClassLoaderReference;
import org.massyframework.assembly.base.handle.Handler;
import org.massyframework.assembly.base.handle.HandlerRegistration;
import org.massyframework.assembly.base.handle.HandlerRegistry;
import org.massyframework.assembly.spring.SpringWebAssemblyContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.ContextLoader;

/**
 * 扩展Struts2的Filter,使用Spring容器，支持装配件
 */
public class StrutsPrepareAndExecuteFilterEx extends StrutsPrepareAndExecuteFilter 
	implements AssemblyContextReference, Handler {

	private volatile HandlerRegistry handlerRegistry;
	private volatile HandlerRegistration<SpringWebAssemblyContext> assemblyContextRegistration;
	
	private FilterConfig filterConfig;
	/**
	 * 
	 */
	public StrutsPrepareAndExecuteFilterEx() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ObjectReference#getReference()
	 */
	@Override
	public AssemblyContext getReference() {
		return this.assemblyContextRegistration == null ?
				null:
					this.assemblyContextRegistration.getHandler();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.Handler#init(org.massyframework.assembly.base.handle.HandlerRegistry)
	 */
	@Override
	public void init(HandlerRegistry handlerRegistry) {
		this.handlerRegistry = handlerRegistry;	
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public synchronized void init(FilterConfig filterConfig) throws ServletException {		
		if (this.filterConfig == null){
			this.filterConfig = new FilterConfigWrapper(filterConfig); 
			super.init(filterConfig);
			SpringWebAssemblyContext context = this.createWebApplication();
			if (context != null){
				this.assemblyContextRegistration = this.handlerRegistry.register(context);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter#destroy()
	 */
	@Override
	public synchronized void destroy() {
		if (this.assemblyContextRegistration != null){
			this.assemblyContextRegistration.unregister();
			this.assemblyContextRegistration = null;
			super.destroy();
		}
	}

	/**
	 * 创建Spring的WebApplication
	 * @return {@link SpringWebAssemblyContext}
	 */
	protected SpringWebAssemblyContext createWebApplication(){
		SpringWebAssemblyContext result = null;
		String configLocation = this.getHandlerRegistry().getReference().getInitParameter(
				ContextLoader.CONFIG_LOCATION_PARAM);
		if (configLocation != null){
			result = new SpringWebAssemblyContext();
			
			result.setConfigLocation(configLocation);			
			ClassLoader loader = ClassLoaderReference.adaptFrom(
					this.handlerRegistry.getReference());
			result.setClassLoader(loader);
			
			ServletContext sc = this.filterConfig.getServletContext();
			
			//以下代码来自Spring的ContextLoader.
			if (ObjectUtils.identityToString(result).equals(result.getId())) {
				// The application context id is still set to its original default value
				// -> assign a more useful id based on available information
				String idParam = sc.getInitParameter(ContextLoader.CONTEXT_ID_PARAM);
				if (idParam != null) {
					result.setId(idParam);
				}
				else {
					// Generate default id...
					result.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
							ObjectUtils.getDisplayString(sc.getContextPath()));
				}
			}

			result.setServletContext(sc);
			String configLocationParam = sc.getInitParameter(ContextLoader.CONFIG_LOCATION_PARAM);
			if (configLocationParam != null) {
				result.setConfigLocation(configLocationParam);
			}

			// The wac environment's #initPropertySources will be called in any case when the context
			// is refreshed; do it eagerly here to ensure servlet property sources are in place for
			// use in any post-processing or initialization that occurs below prior to #refresh
			ConfigurableEnvironment env = result.getEnvironment();
			if (env instanceof ConfigurableWebEnvironment) {
				((ConfigurableWebEnvironment) env).initPropertySources(sc, null);
			}
			result.refresh();
		}
		
		return result;
	}

	protected HandlerRegistry getHandlerRegistry(){
		return this.handlerRegistry;
	}
	
}
