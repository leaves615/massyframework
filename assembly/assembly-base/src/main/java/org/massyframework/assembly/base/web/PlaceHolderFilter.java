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
* @日   期:  2017年4月10日
*/
package org.massyframework.assembly.base.web;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.ClassLoaderReference;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.ExportServiceTracker;
import org.massyframework.assembly.ExportServiceTrackerCustomizer;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.util.ClassLoaderUtils;


/**
 * 用作占位符的Filter，它作为装配件内Filter的代理，先在ServletContext中占位，
 * 并当装配件进入工作状态后，将Http请求在转发给装配件内的Filter处理
 *
 */
public class PlaceHolderFilter implements Filter {

	public static final String FILTERSTRING = "Filter.filterString";
	private volatile FilterConfig config;
	private final AtomicReference<Filter> reference;
	private volatile ExportServiceTracker<Filter> serviceTracker;
	private volatile ClassLoader loader;
	
	/**
	 * 
	 */
	public PlaceHolderFilter() {
		this.reference = new AtomicReference<Filter>();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
		
		ExportServiceRepository serviceRepository =
				this.getExportServiceRepository();
		String filterString = this.getFilterString();
		this.serviceTracker =
				new ExportServiceTracker<Filter>(
						serviceRepository, Filter.class, filterString, new Customizer());
		this.serviceTracker.open();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ClassLoader contextLoader = null;
		if (this.loader != null){
			contextLoader = 
					ClassLoaderUtils.setThreadContextClassLoader(this.loader);
		}
		
		try{
			Filter filter = this.reference.get();
			if (filter != null){
				filter.doFilter(request, response, chain);
			}
		}finally{
			if (contextLoader != null){
				ClassLoaderUtils.setThreadContextClassLoader(contextLoader);
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		this.serviceTracker.close();
	}
	
	/**
	 * 获取输出服务仓储
	 * @return {@link ExportServiceRepository}
	 * @throws ServletException
	 */
	protected ExportServiceRepository getExportServiceRepository() throws ServletException{
		ServletContext servletContext = this.config.getServletContext();
		Object obj = servletContext.getAttribute(Framework.class.getName());
		if (obj instanceof Framework){
			String name = this.config.getInitParameter(Constants.ASSEMBLY_SYMBOLICNAME);
			Assembly assembly = ((Framework)obj).getAssembly(name);
			if (assembly != null){
				this.loader = ClassLoaderReference.adaptFrom(assembly);
			}
			return ExportServiceRepositoryReference.adaptFrom((Framework)obj);
		}else{
			throw new ServletException("cannot found Framework with ServletContext: attributeName=" + Framework.class.getName() + ".");
		}
	}
	
	protected void doInit(){
		Filter filter = this.reference.get();
		if (filter != null){
			try{
				filter.init(this.config);
			}catch(ServletException e){
				this.config.getServletContext().log("filter init failed.", e);
			}
		}
	}
	
	/**
	 * 获取筛选字符串
	 * @return {@link String}
	 */
	protected String getFilterString(){
		return this.config.getInitParameter(FILTERSTRING);
	}

	private class Customizer implements ExportServiceTrackerCustomizer<Filter> {

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.service.ExportServiceTrackerCustomizer#addService(org.massyframework.assembly.service.ExportServiceReference, java.lang.Object)
		 */
		@Override
		public synchronized void addService(ExportServiceReference<Filter> referecne, Filter service) {
			if (PlaceHolderFilter.this.reference.get() == null){
				PlaceHolderFilter.this.reference.set(service);
				doInit();
			}
		}

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.service.ExportServiceTrackerCustomizer#removeService(org.massyframework.assembly.service.ExportServiceReference, java.lang.Object)
		 */
		@Override
		public void removeService(ExportServiceReference<Filter> reference, Filter service) {
			if (PlaceHolderFilter.this.reference.get() == service){
				PlaceHolderFilter.this.reference.set(null);
			}
		}
	}
}
