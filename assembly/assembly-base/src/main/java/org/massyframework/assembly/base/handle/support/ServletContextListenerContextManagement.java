/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月14日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;

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
import org.slf4j.Logger;

/**
 * Servlet上下文监听器内嵌装配件上下文管理器
 *
 */
public class ServletContextListenerContextManagement extends BootableContextManagement<ServletContextListener>
	implements ReadyingHandler{

	private Class<? extends ServletContextListener> listenerType;
	private volatile HandlerRegistration<ServletContextListener> registration;
	private volatile ExportServiceRegistration<ServletContextListener> serviceRegistration;
	
	/**
	 * 
	 */
	public ServletContextListenerContextManagement() {
		super();
	}
	
	/**
	 * 
	 */
	public ServletContextListenerContextManagement(
			Class<? extends ServletContextListener> listenerType) {
		super();
		this.listenerType = listenerType;
;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.BootableContextManagement#createBootable(org.massyframework.assembly.base.support.InitParams)
	 */
	@Override
	protected ServletContextListener createBootable(InitParams initParams) throws Exception {
		Class<? extends ServletContextListener> clazz = this.getServletContextListenerClass(initParams);
		ServletContextListener result = ClassLoaderUtils.newInstance(clazz);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.BootableContextManagement#getBootable()
	 */
	@Override
	protected ServletContextListener getBootable() {
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
		try{
			InitParams initParams = this.getHandler(InitParams.class);
			this.registerListenerToServletContext(initParams);
		}catch(Exception e){
			Logger logger = this.getLogger();
			if (logger != null){
				if (logger.isErrorEnabled()){
					logger.error( this + " init() failed.", e); 
				}
			}
		}
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
			//输出ServletContextListener
			try{
			
				InitParams initParams = this.getHandler(InitParams.class);
				this.registration = this.getHandlerRegistry().register(this.createBootable(initParams));
				
				ExportServiceRepository serviceRepository =
						ExportServiceRepositoryReference.adaptFrom(this.getAssembly());
					ExportServiceRegistry serviceRegistry =
							serviceRepository.findService(ExportServiceRegistry.class);
					
				Map<String, Object> props =
						new HashMap<String, Object>();
				props.put(Constants.LISTENER_CLASSNAME, this.getServletContextListenerClass(initParams).getName());
				this.serviceRegistration = serviceRegistry.register(ServletContextListener.class, this.registration.getHandler(), props);
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
		ServletContextListener listener = this.getBootable();
		if (listener == null) return null;
		if (listener instanceof AssemblyContextReference){
			return ((AssemblyContextReference)listener).getReference();
		}
		
		return null;
	}
	
	/**
	 * 注册ServletContextListener到ServletContext
	 * @param initParams 初始化参数
	 */
	private void registerListenerToServletContext(InitParams initParams) throws Exception{
		ExportServiceRepository serviceRepository =
				this.getHandler(ExportServiceRepository.class);
		ServletContext servletContext =
				serviceRepository.findService(ServletContext.class);
		if (servletContext == null){
			throw new ServiceNotFoundException("service cannot found: type=" + ServletContext.class + ".");
		}
		
		String className = this.getServletContextListenerClass(initParams).getName();
		Map<String, String> params = ServletUtils.getServletContextParameters(initParams);
		
		StringBuilder builder = new StringBuilder();
		builder.append("(&")
			.append("(")
				.append(Constants.ASSEMBLY_SYMBOLICNAME).append("=").append(this.getAssembly().getSymbolicName())
			.append(")")
			.append("(")
				.append(Constants.LISTENER_CLASSNAME).append("=").append(className)
			.append(")")
			.append(")");
		
		params.put(PlaceHolderServletContextListener.FILTERSTRING, builder.toString());
		
		servletContext.addListener(PlaceHolderServletContextListener.class);
		for (Entry<String, String> entry: params.entrySet()){
			servletContext.setInitParameter(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * 获取SerlvetContextListener类型
	 * @param initParams
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected Class<? extends ServletContextListener> getServletContextListenerClass(InitParams initParams) throws Exception{
		if (this.listenerType != null){
			return this.listenerType;
		}
		
		String className = ServletUtils.getServletContextListenerClassName(initParams);
		if (className == null){
			throw new IllegalStateException(
					"cannot found " + Constants.LISTENER_CLASSNAME + " parameter.");
		}		
		ClassLoader loader = ClassLoaderReference.adaptFrom(this.getAssembly());
		return (Class<ServletContextListener>)loader.loadClass(className);
	}
}
