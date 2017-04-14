/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月14日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spring4.init;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import org.massyframework.assembly.util.Asserts;
import org.springframework.web.context.WebApplicationContext;

/**
 * 提供ServletCOntext封装，阻断Spring ContextLoaderListener和DispatcherServlet之间的联系
 */
class ServletContextWrapper implements ServletContext {
	
	private final ServletContext context;

	/**
	 * 
	 */
	public ServletContextWrapper(ServletContext context) {
		Asserts.notNull(context, "context cannot be null.");
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getContextPath()
	 */
	@Override
	public String getContextPath() {
		return this.context.getContextPath();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getContext(java.lang.String)
	 */
	@Override
	public ServletContext getContext(String uripath) {
		return this.context.getContext(uripath);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getMajorVersion()
	 */
	@Override
	public int getMajorVersion() {
		return this.context.getMajorVersion();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getMinorVersion()
	 */
	@Override
	public int getMinorVersion() {
		return this.context.getMinorVersion();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getEffectiveMajorVersion()
	 */
	@Override
	public int getEffectiveMajorVersion() {
		return this.context.getEffectiveMinorVersion();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getEffectiveMinorVersion()
	 */
	@Override
	public int getEffectiveMinorVersion() {
		return this.context.getEffectiveMinorVersion();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
	 */
	@Override
	public String getMimeType(String file) {
		return this.context.getMimeType(file);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
	 */
	@Override
	public Set<String> getResourcePaths(String path) {
		return this.context.getResourcePaths(path);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getResource(java.lang.String)
	 */
	@Override
	public URL getResource(String path) throws MalformedURLException {
		return this.context.getResource(path);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
	 */
	@Override
	public InputStream getResourceAsStream(String path) {
		return this.context.getResourceAsStream(path);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
	 */
	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return this.context.getRequestDispatcher(path);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
	 */
	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		return this.context.getNamedDispatcher(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServlet(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Servlet getServlet(String name) throws ServletException {
		return this.context.getServlet(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServlets()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Enumeration<Servlet> getServlets() {
		return this.context.getServlets();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServletNames()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Enumeration<String> getServletNames() {
		return this.context.getServletNames();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#log(java.lang.String)
	 */
	@Override
	public void log(String msg) {
		this.context.log(msg);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#log(java.lang.Exception, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void log(Exception exception, String msg) {
		this.context.log(exception, msg);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void log(String message, Throwable throwable) {
		this.context.log(message, throwable);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
	 */
	@Override
	public String getRealPath(String path) {
		return this.context.getRealPath(path);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServerInfo()
	 */
	@Override
	public String getServerInfo() {
		return this.context.getServerInfo();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
	 */
	@Override
	public String getInitParameter(String name) {
		return this.context.getInitParameter(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getInitParameterNames()
	 */
	@Override
	public Enumeration<String> getInitParameterNames() {
		return this.context.getInitParameterNames();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#setInitParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setInitParameter(String name, String value) {
		return this.context.setInitParameter(name, value);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String name) {
		if (WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE.equals(name)){
			return null;
		}
		
		return this.context.getAttribute(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getAttributeNames()
	 */
	@Override
	public Enumeration<String> getAttributeNames() {
		Vector<String> vector = new Vector<String>();
		Enumeration<String> em = this.context.getAttributeNames();
		while (em.hasMoreElements()){
			String name = em.nextElement();
			if (!WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE.equals(name)){
				vector.add(name);
			}
		}
		return vector.elements();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setAttribute(String name, Object object) {
		this.context.setAttribute(name, object);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
	 */
	@Override
	public void removeAttribute(String name) {
		this.context.removeAttribute(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServletContextName()
	 */
	@Override
	public String getServletContextName() {
		return this.context.getServletContextName();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#addServlet(java.lang.String, java.lang.String)
	 */
	@Override
	public Dynamic addServlet(String servletName, String className) {
		return this.context.addServlet(servletName, className);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#addServlet(java.lang.String, javax.servlet.Servlet)
	 */
	@Override
	public Dynamic addServlet(String servletName, Servlet servlet) {
		return this.context.addServlet(servletName, servlet);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#addServlet(java.lang.String, java.lang.Class)
	 */
	@Override
	public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
		return this.context.addServlet(servletName, servletClass);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#createServlet(java.lang.Class)
	 */
	@Override
	public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
		return this.context.createServlet(clazz);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServletRegistration(java.lang.String)
	 */
	@Override
	public ServletRegistration getServletRegistration(String servletName) {
		return this.context.getServletRegistration(servletName);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServletRegistrations()
	 */
	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return this.context.getServletRegistrations();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#addFilter(java.lang.String, java.lang.String)
	 */
	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, String className) {
		return this.context.addFilter(filterName, className);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#addFilter(java.lang.String, javax.servlet.Filter)
	 */
	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
		return this.context.addFilter(filterName, filter);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#addFilter(java.lang.String, java.lang.Class)
	 */
	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
		return this.context.addFilter(filterName, filterClass);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#createFilter(java.lang.Class)
	 */
	@Override
	public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
		return this.context.createFilter(clazz);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getFilterRegistration(java.lang.String)
	 */
	@Override
	public FilterRegistration getFilterRegistration(String filterName) {
		return this.context.getFilterRegistration(filterName);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getFilterRegistrations()
	 */
	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return this.context.getFilterRegistrations();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getSessionCookieConfig()
	 */
	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		return this.context.getSessionCookieConfig();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#setSessionTrackingModes(java.util.Set)
	 */
	@Override
	public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
		this.context.setSessionTrackingModes(sessionTrackingModes);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getDefaultSessionTrackingModes()
	 */
	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		return this.context.getDefaultSessionTrackingModes();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getEffectiveSessionTrackingModes()
	 */
	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		return this.context.getEffectiveSessionTrackingModes();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#addListener(java.lang.String)
	 */
	@Override
	public void addListener(String className) {
		this.context.addListener(className);

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#addListener(java.util.EventListener)
	 */
	@Override
	public <T extends EventListener> void addListener(T t) {
		this.context.addListener(t);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#addListener(java.lang.Class)
	 */
	@Override
	public void addListener(Class<? extends EventListener> listenerClass) {
		this.context.addListener(listenerClass);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#createListener(java.lang.Class)
	 */
	@Override
	public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
		return this.context.createListener(clazz);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getJspConfigDescriptor()
	 */
	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		return this.context.getJspConfigDescriptor();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getClassLoader()
	 */
	@Override
	public ClassLoader getClassLoader() {
		return this.context.getClassLoader();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#declareRoles(java.lang.String[])
	 */
	@Override
	public void declareRoles(String... roleNames) {
		this.context.declareRoles(roleNames);

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getVirtualServerName()
	 */
	@Override
	public String getVirtualServerName() {
		return this.context.getVirtualServerName();
	}

}
