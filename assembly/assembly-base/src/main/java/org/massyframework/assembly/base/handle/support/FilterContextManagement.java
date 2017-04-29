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
* @日   期:  2017年4月13日
*/
package org.massyframework.assembly.base.handle.support;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.AssemblyContextReference;
import org.massyframework.assembly.ClassLoaderReference;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.ServiceNotFoundException;
import org.massyframework.assembly.base.ExportServiceRegistration;
import org.massyframework.assembly.base.ExportServiceRegistry;
import org.massyframework.assembly.base.handle.HandlerRegistration;
import org.massyframework.assembly.base.handle.ReadyingException;
import org.massyframework.assembly.base.handle.ReadyingHandler;
import org.massyframework.assembly.base.support.InitParams;
import org.massyframework.assembly.base.util.ServletUtils;
import org.massyframework.assembly.util.ClassLoaderUtils;

/**
 * 过滤器内嵌装配件上下文管理器
 */
public class FilterContextManagement extends BootableContextManagement<Filter> 
	implements ReadyingHandler{

	private Class<Filter> filterType;
	private volatile HandlerRegistration<Filter> registration;
	private volatile ExportServiceRegistration<Filter> serviceRegistration;
	
	/**
	 * 
	 */
	public FilterContextManagement() {
		super();
	}
	
	/**
	 * 
	 */
	public FilterContextManagement(Class<Filter> filterType) {
		super();
		this.filterType = filterType;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.BootableContextManagement#createBootable(org.massyframework.assembly.base.support.InitParams)
	 */
	@Override
	protected Filter createBootable(InitParams initParams) throws Exception {
		Class<Filter> clazz = this.getFilterClass(initParams);
		Filter result = ClassLoaderUtils.newInstance(clazz);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.BootableContextManagement#getBootable()
	 */
	@Override
	protected Filter getBootable() {
		return this.registration == null ?
					null:
						this.registration.getHandler();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.BootableContextManagement#destroyBootable()
	 */
	@Override
	protected void destroyBootable() {
		if (this.serviceRegistration != null){
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
			this.registration.unregister();
			this.registration = null;
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextManagement#init()
	 */
	@Override
	protected synchronized void init() {
		super.init();
		InitParams initParams = this.getHandler(InitParams.class);
		this.registerFilterToServletContext(initParams);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextManagement#destroy()
	 */
	@Override
	public synchronized void destroy() {
		super.destroy();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ReadyingHandler#doReadying()
	 */
	@Override
	public synchronized void doReadying() throws ReadyingException {
		if (this.registration == null){
			//输出Servlet
			try{
				InitParams initParams =this.getHandler(InitParams.class);
				this.registration = 
						this.getHandlerRegistry().register(
								this.createBootable(initParams));
				
				ExportServiceRepository serviceRepository =
					ExportServiceRepositoryReference.adaptFrom(this.getAssembly());
				ExportServiceRegistry serviceRegistry =
						serviceRepository.findService(ExportServiceRegistry.class);
				
				Map<String, Object> props = new HashMap<String, Object>();
				props.put(Constants.FILTER_NAME, ServletUtils.getFilterName(initParams));
				this.serviceRegistration = serviceRegistry.register(Filter.class, 
						this.registration.getHandler(), props);
			}catch(Exception e){
				throw new ReadyingException(e.getMessage(), e);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ReadyingHandler#doUnreadying()
	 */
	@Override
	public void doUnreadying() {
		this.destroyBootable();
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextManagement#getAssemblyContext()
	 */
	@Override
	protected AssemblyContext getAssemblyContext() {
		Filter filter = this.getBootable();
		if (filter == null) return null;
		if (filter instanceof AssemblyContextReference){
			return ((AssemblyContextReference)filter).getReference();
		}
		
		return null;
	}
	
	/**
	 * 注册Filter到ServletContext
	 * @param initParams 初始化参数
	 */
	private void registerFilterToServletContext(InitParams initParams){
		ExportServiceRepository serviceRepository =
				this.getHandler(ExportServiceRepository.class);
		ServletContext servletContext =
				serviceRepository.findService(ServletContext.class);
		if (servletContext == null){
			throw new ServiceNotFoundException("service cannot found: type=" + ServletContext.class + ".");
		}
		
		String filterName = ServletUtils.getFilterName(initParams);
		
		StringBuilder builder = new StringBuilder();
		builder.append("(&")
			.append("(")
				.append(Constants.ASSEMBLY_SYMBOLICNAME).append("=").append(this.getAssembly().getAssemblyId())
			.append(")")
			.append("(")
				.append(Constants.FILTER_NAME).append("=").append(filterName)
			.append(")")
			.append(")");
		Map<String, String> params = ServletUtils.getServletInitParameter(initParams);
		params.put(PlaceHolderServlet.FILTERSTRING, builder.toString());
		
		boolean asyncSupport = ServletUtils.getFilterAsyncSupported(initParams);
		EnumSet<DispatcherType> types = ServletUtils.getFilterDispatcherTypes(initParams);
		String[] urlPatterns = ServletUtils.getFilterUrlPatterns(initParams);
		
		FilterRegistration.Dynamic registration =
				servletContext.addFilter(filterName, PlaceHolderFilter.class);
		registration.setAsyncSupported(asyncSupport);
		registration.addMappingForUrlPatterns(types, true, urlPatterns);
		registration.setInitParameters(
				ServletUtils.getServletInitParameter(initParams));
	}

	/**
	 * 获取Filter类型
	 * @param initParams
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected Class<Filter> getFilterClass(InitParams initParams) throws Exception{
		if (this.filterType != null){
			return this.filterType;
		}
		
		String className = ServletUtils.getFilterClassName(initParams);
		if (className == null){
			throw new IllegalStateException(
					"cannot found " + Constants.FILTER_CLASSNAME + " parameter.");
		}		
		ClassLoader loader = ClassLoaderReference.adaptFrom(this.getAssembly());
		return (Class<Filter>)loader.loadClass(className);
	}
	
	
}
