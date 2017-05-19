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

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.massyframework.assembly.AssemblyNotReadyException;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.ExportServiceTracker;
import org.massyframework.assembly.ExportServiceTrackerCustomizer;
import org.massyframework.assembly.Framework;


/**
 * 用作占位符的Servlet，它作为装配件内Servlet的代理，先在ServletContext中占位，
 * 并当装配件进入工作状态后，将Http请求在转发给装配件内的Servlet处理
 */
public final class PlaceHolderServlet implements Servlet {

	public static final String FILTERSTRING = "service.filterString";
	private ServletConfig config;
	private final AtomicReference<Servlet> reference;
	private ExportServiceTracker<Servlet> serviceTracker;
	
	/**
	 * 
	 */
	public PlaceHolderServlet() {
		this.reference = new AtomicReference<Servlet>();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
		ExportServiceRepository serviceRepository =
				this.getExportServiceRepository();
		String filterString = this.getFilterString();
		
		Servlet servlet = serviceRepository.findService(Servlet.class, filterString);
		if (servlet != null){
			this.reference.set(servlet);
			this.doInit();
		}
		this.serviceTracker =
				new ExportServiceTracker<Servlet>(
						serviceRepository, Servlet.class, filterString, new Customizer());
		this.serviceTracker.open();
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
		Servlet servlet = this.reference.get();
		if (servlet != null){
			servlet.service(req, res);
		}else{
			throw new AssemblyNotReadyException(
					this.config.getInitParameter(Constants.ASSEMBLY_SYMBOLICNAME));
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#getServletInfo()
	 */
	@Override
	public String getServletInfo() {
		return "用作占位符的Servlet，它作为装配件内Servlet的代理";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#destroy()
	 */
	@Override
	public void destroy() {
		this.serviceTracker.close();
	}
	
	protected void doInit(){
		Servlet servlet = this.reference.get();
		if (servlet != null){
			try{
				servlet.init(this.getServletConfig());
			}catch(ServletException e){
				this.getServletConfig().getServletContext().log("servlet init failed.", e);
			}
		}
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
			return ExportServiceRepositoryReference.adaptFrom((Framework)obj);
		}else{
			throw new ServletException("cannot found Framework with ServletContext: attributeName=" + Framework.class.getName() + ".");
		}
	}
	
	/**
	 * 获取筛选字符串
	 * @return {@link String}
	 */
	protected String getFilterString(){
		return this.config.getInitParameter(FILTERSTRING);
	}
	
	private class Customizer implements ExportServiceTrackerCustomizer<Servlet> {

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.service.ExportServiceTrackerCustomizer#addService(org.massyframework.assembly.service.ExportServiceReference, java.lang.Object)
		 */
		@Override
		public synchronized void addService(ExportServiceReference<Servlet> referecne, Servlet service) {
			if (PlaceHolderServlet.this.reference.get() == null){
				PlaceHolderServlet.this.reference.set(service);
				doInit();
			}
		}

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.service.ExportServiceTrackerCustomizer#removeService(org.massyframework.assembly.service.ExportServiceReference, java.lang.Object)
		 */
		@Override
		public void removeService(ExportServiceReference<Servlet> reference, Servlet service) {
			if (PlaceHolderServlet.this.reference.get() == service){
				PlaceHolderServlet.this.reference.set(null);
			}
		}
	}
}
