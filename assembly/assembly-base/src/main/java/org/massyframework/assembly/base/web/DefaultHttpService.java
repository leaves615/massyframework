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

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.util.Asserts;
import org.massyframework.assembly.web.HttpResourceProcessor;

/**
 * 缺省的HttpService
 */
public class DefaultHttpService extends AbstractHttpService {

	private final ServletContext servletContext;
	private final ExportServiceRepository serviceRepository;
	private volatile HttpResourceProcessorManagement processorManagement;
	private volatile PageMappingFilter filter;
	/**
	 * 
	 */
	public DefaultHttpService(ServletContext servletContext, ExportServiceRepository serviceRepository) {
		Asserts.notNull(servletContext, "servletContext cannot be null.");
		Asserts.notNull(serviceRepository, "serviceRepository cannot be null.");
		this.servletContext = servletContext;
		this.serviceRepository = serviceRepository;
	}
	
	public void init(){
		this.initPageMappingFilter();
		this.initProcessorManagement();
	}
	
	private void initProcessorManagement(){
		this.processorManagement =
				new HttpResourceProcessorManagement(serviceRepository, this.servletContext);
		this.processorManagement.open();
	}
	
	private void initPageMappingFilter(){
		this.filter = new PageMappingFilter();
		FilterRegistration.Dynamic registration =
				this.servletContext.addFilter("pageMappingFilter", filter);
		registration.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		registration.setAsyncSupported(true);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.web.AbstractHttpService#getPageMappingFilter()
	 */
	@Override
	protected PageMappingFilter getPageMappingFilter() {
		return this.filter;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.web.AbstractHttpService#getServletContext()
	 */
	@Override
	protected ServletContext getServletContext() {
		return this.servletContext;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.web.AbstractHttpService#getDefault()
	 */
	@Override
	protected HttpResourceProcessor getDefault() {
		return this.processorManagement;
	}

}
