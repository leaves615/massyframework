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
* @日   期:  2017年4月14日
*/
package org.massyframework.assembly.base.handle.support;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

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
 * Servlet内嵌装配件上下文管理器
 */
public class ServletContextManagment extends BootableContextManagement<Servlet> 
	implements ReadyingHandler{
	
	private Class<? extends Servlet> servletType;
	private volatile HandlerRegistration<Servlet> registration;
	private volatile ExportServiceRegistration<Servlet> serviceRegistration;
	/**
	 * 构造方法
	 */
	public ServletContextManagment() {
		super();
	}
	
	/**
	 * 构造方法
	 */
	public ServletContextManagment(Class<? extends Servlet> servletType) {
		super();
		this.servletType = servletType;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.BootableContextManagement#createBootable(org.massyframework.assembly.base.support.InitParams)
	 */
	@Override
	protected Servlet createBootable(InitParams initParams) throws Exception {
		Class<? extends Servlet> clazz = this.getServletClass(initParams);
		Servlet result = ClassLoaderUtils.newInstance(clazz);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.BootableContextManagement#getBootable()
	 */
	@Override
	protected Servlet getBootable() {
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
		this.registerServletToServletContext(initParams);
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
	public void doReadying() throws ReadyingException {
		if (this.registration == null){
			//输出Servlet
			try{
				
				InitParams initParams = this.getHandler(InitParams.class);
				this.registration = this.getHandlerRegistry().register(this.createBootable(initParams));
				
				ExportServiceRepository serviceRepository =
						ExportServiceRepositoryReference.adaptFrom(this.getAssembly());
					ExportServiceRegistry serviceRegistry =
							serviceRepository.findService(ExportServiceRegistry.class);
					
				Map<String, Object> props =
						new HashMap<String, Object>();
				props.put(Constants.SERVLET_NAME, ServletUtils.getServletName(initParams));
				this.serviceRegistration = serviceRegistry.register(Servlet.class, this.registration.getHandler(), props);
			}catch(Exception e){
				throw new ReadyingException(e.getMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ReadyingHandler#doUnreadying()
	 */
	@Override
	public void doUnreadied() {
		this.destroyBootable();
	}
	
	

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextManagement#canWork()
	 */
	@Override
	public boolean canWork() {
		Servlet servlet = this.getBootable();
		return servlet != null && servlet.getServletConfig() != null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextManagement#getAssemblyContext()
	 */
	@Override
	protected AssemblyContext getAssemblyContext() {
		Servlet servlet = this.getBootable();
		if (servlet == null) return null;
		if (servlet instanceof AssemblyContextReference){
			return ((AssemblyContextReference)servlet).getReference();
		}
		
		return null;
	}
	
	/**
	 * 注册Servlet到ServletContext
	 * @param initParams 初始化参数
	 */
	private void registerServletToServletContext(InitParams initParams){
		ExportServiceRepository serviceRepository =
				ExportServiceRepositoryReference.adaptFrom(this.getAssembly());
		ServletContext servletContext =
				serviceRepository.findService(ServletContext.class);
		if (servletContext == null){
			throw new ServiceNotFoundException("service cannot found: type=" + ServletContext.class + ".");
		}
		
		String servletName = ServletUtils.getServletName(initParams);
		
		int loadOnStartUp = ServletUtils.getServletLoadOnStartup(initParams);
		boolean asyncSupported = ServletUtils.getServeltAsyncSupported(initParams);
		String[] urlPatterns = ServletUtils.getServletUrlPatterns(initParams);
		if (urlPatterns.length == 0){
			throw new RuntimeException(Constants.SERVLET_URLPATTERNS + " cannot be empty.");
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("(&")
			.append("(")
				.append(Constants.ASSEMBLY_SYMBOLICNAME).append("=").append(this.getAssembly().getSymbolicName())
			.append(")")
			.append("(")
				.append(Constants.SERVLET_NAME).append("=").append(servletName)
			.append(")")
			.append(")");
		Map<String, String> params = ServletUtils.getServletInitParameter(initParams);
		params.put(PlaceHolderServlet.FILTERSTRING, builder.toString());
		params.put(Constants.ASSEMBLY_SYMBOLICNAME, this.getAssembly().getSymbolicName());
		
		//Tomcat 在使用class注册动态Servlet时会发生ClassNotFoundException,所以直接使用Servlet实例进行注册
		ServletRegistration.Dynamic registration =
				servletContext.addServlet(servletName, new PlaceHolderServlet());
				
		registration.addMapping(urlPatterns);
		registration.setInitParameters(
				params);
		registration.setAsyncSupported(asyncSupported);
		registration.setLoadOnStartup(loadOnStartUp);
	}
	
	/**
	 * 获取Filter类型
	 * @param initParams
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected Class<? extends Servlet> getServletClass(InitParams initParams) throws Exception{
		if (this.servletType != null){
			return this.servletType;
		}
		
		String className = ServletUtils.getFilterClassName(initParams);
		if (className == null){
			throw new IllegalStateException(
					"cannot found " + Constants.SERVLET_CLASSNAME + " parameter.");
		}		
		ClassLoader loader = ClassLoaderReference.adaptFrom(this.getAssembly());
		return (Class<Servlet>)loader.loadClass(className);
	}
	
}
