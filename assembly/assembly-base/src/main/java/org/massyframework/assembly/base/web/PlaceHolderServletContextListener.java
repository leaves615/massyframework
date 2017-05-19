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

import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.ExportServiceTracker;
import org.massyframework.assembly.ExportServiceTrackerCustomizer;
import org.massyframework.assembly.Framework;

/**
 * 用作占位符的ServletContextListener，它作为装配件内ServletContextListener的代理，先在ServletContext中占位，
 * 并当装配件进入工作状态后，将相关事件转发给装配件内的ServletContextListener处理
 */
public class PlaceHolderServletContextListener 
	implements ServletContextListener, 
	ServletContextAttributeListener, 
	HttpSessionActivationListener,
	HttpSessionAttributeListener, 
	HttpSessionBindingListener, 
	HttpSessionIdListener, 
	HttpSessionListener{
	
	public static final String FILTERSTRING = "servletContextListener.filterString";
	private final AtomicReference<ServletContextListener> reference;
	private volatile ExportServiceTracker<ServletContextListener> serviceTracker;
	private volatile ServletContextEvent event;
	/**
	 * 
	 */
	public PlaceHolderServletContextListener() {
		this.reference = new AtomicReference<ServletContextListener>();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		this.event = sce;
		
		try{
			ExportServiceRepository serviceRepository =
					this.getExportServiceRepository();
			String filterString = this.getFilterString();
			this.serviceTracker =
					new ExportServiceTracker<ServletContextListener>(
							serviceRepository, ServletContextListener.class, filterString, new Customizer());
			this.serviceTracker.open();
		}catch(Exception e){
			sce.getServletContext().log("cannot found ExportServiceRepository.", e);
		}
	}
	

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContextListener listener = this.reference.get();
		if (listener != null){
			listener.contextDestroyed(sce);
		}
		
		if (this.serviceTracker != null){
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}
	
	/**
	 * 获取输出服务仓储
	 * @return {@link ExportServiceRepository}
	 * @throws ServletException
	 */
	protected ExportServiceRepository getExportServiceRepository() throws ServletException{
		ServletContext servletContext = this.event.getServletContext();
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
		return this.event.getServletContext().getInitParameter(FILTERSTRING);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextAttributeListener#attributeAdded(javax.servlet.ServletContextAttributeEvent)
	 */
	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		ServletContextAttributeListener listener = this.asInterface(ServletContextAttributeListener.class);
		if (listener != null){
			listener.attributeAdded(event);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextAttributeListener#attributeRemoved(javax.servlet.ServletContextAttributeEvent)
	 */
	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
		ServletContextAttributeListener listener = this.asInterface(ServletContextAttributeListener.class);
		if (listener != null){
			listener.attributeRemoved(event);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextAttributeListener#attributeReplaced(javax.servlet.ServletContextAttributeEvent)
	 */
	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		ServletContextAttributeListener listener = this.asInterface(ServletContextAttributeListener.class);
		if (listener != null){
			listener.attributeReplaced(event);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionActivationListener#sessionWillPassivate(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionWillPassivate(HttpSessionEvent se) {
		HttpSessionActivationListener listener = this.asInterface(HttpSessionActivationListener.class);
		if (listener != null){
			listener.sessionWillPassivate(se);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionActivationListener#sessionDidActivate(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionDidActivate(HttpSessionEvent se) {
		HttpSessionActivationListener listener = this.asInterface(HttpSessionActivationListener.class);
		if (listener != null){
			listener.sessionDidActivate(se);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		HttpSessionAttributeListener listener = this.asInterface(HttpSessionAttributeListener.class);
		if (listener != null){
			listener.attributeAdded(event);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		HttpSessionAttributeListener listener = this.asInterface(HttpSessionAttributeListener.class);
		if (listener != null){
			listener.attributeRemoved(event);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		HttpSessionAttributeListener listener = this.asInterface(HttpSessionAttributeListener.class);
		if (listener != null){
			listener.attributeReplaced(event);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		HttpSessionBindingListener listener = this.asInterface(HttpSessionBindingListener.class);
		if (listener != null){
			listener.valueBound(event);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		HttpSessionBindingListener listener = this.asInterface(HttpSessionBindingListener.class);
		if (listener != null){
			listener.valueUnbound(event);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionIdListener#sessionIdChanged(javax.servlet.http.HttpSessionEvent, java.lang.String)
	 */
	@Override
	public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
		HttpSessionIdListener listener = this.asInterface(HttpSessionIdListener.class);
		if (listener != null){
			listener.sessionIdChanged(event, oldSessionId);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSessionListener listener = this.asInterface(HttpSessionListener.class);
		if (listener != null){
			listener.sessionCreated(se);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSessionListener listener = this.asInterface(HttpSessionListener.class);
		if (listener != null){
			listener.sessionDestroyed(se);
		}
	}
	
		
	/**
	 * 将委托服务转换为指定接口实例，不支持可返回null.
	 * @param interfaceType 接口类型
	 * @return {@link I}, 可以返回null.
	 */
	protected <I> I asInterface(Class<I> interfaceType){
		ServletContextListener listener = this.reference.get();
		if (listener != null){
			if (interfaceType.isAssignableFrom(listener.getClass())){
				return interfaceType.cast(listener);
			}
		}
		
		return null;
	}
	
	private class Customizer implements ExportServiceTrackerCustomizer<ServletContextListener> {

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.service.ExportServiceTrackerCustomizer#addService(org.massyframework.assembly.service.ExportServiceReference, java.lang.Object)
		 */
		@Override
		public synchronized void addService(ExportServiceReference<ServletContextListener> referecne, ServletContextListener service) {
			if (PlaceHolderServletContextListener.this.reference.get() == null){
				PlaceHolderServletContextListener.this.reference.set(service);
				service.contextInitialized(event);
			}
		}

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.service.ExportServiceTrackerCustomizer#removeService(org.massyframework.assembly.service.ExportServiceReference, java.lang.Object)
		 */
		@Override
		public void removeService(ExportServiceReference<ServletContextListener> reference, ServletContextListener service) {
			if (PlaceHolderServletContextListener.this.reference.get() == service){
				PlaceHolderServletContextListener.this.reference.set(null);
			}
		}
		
		
	}
}
