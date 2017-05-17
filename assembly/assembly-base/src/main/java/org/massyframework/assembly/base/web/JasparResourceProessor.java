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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.util.ClassLoaderUtils;
import org.massyframework.assembly.web.HttpResource;
import org.massyframework.assembly.web.HttpResourceProcessor;
import org.massyframework.assembly.web.ServletContextAware;

/**
 * jsp资源处理器
 */
public class JasparResourceProessor implements HttpResourceProcessor, ServletContextAware {

	private String[] extensionNames = new String[]{"*.jsp"};
	private volatile ServletContext servletContext;
	private Map<String, String> initParams;
	private volatile Class<? extends Servlet> servletClass;
	
	private Map<Assembly, Servlet> servletMap =
			new ConcurrentHashMap<Assembly, Servlet>();
	/**
	 * 
	 */
	public JasparResourceProessor() {
		this.initParams = new HashMap<String, String>();
		initParams.put("logVerbosityLevel", "TRACE");
		initParams.put("fork", "false");
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.web.HttpResourceProcessor#getExtensionNames()
	 */
	@Override
	public String[] getExtensionNames() {
		return this.extensionNames;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.web.HttpResourceProcessor#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.massyframework.assembly.web.HttpResource)
	 */
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp, HttpResource resource)
			throws ServletException, IOException {
		Servlet servlet = this.getOrCreateJspServelt(resource);
		
		String path = resource.getName() + req.getPathInfo() ;
		req.setAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH, path);
		
		ClassLoader loader = ClassLoaderUtils.setThreadContextClassLoader(resource.getAssemblyClassLoader());
		try{
			servlet.service(req, resp);
		}finally{
			ClassLoaderUtils.setThreadContextClassLoader(loader);
		}
	}
	
	/**
	 * 获取或者创建和装配件相匹配的JspServlet
	 * @param assembly 装配件
	 * @return {@link JspServlet}
	 */
	@SuppressWarnings("unchecked")
	private Servlet getOrCreateJspServelt(HttpResource resource) throws ServletException{
		Servlet result = this.servletMap.get(resource.getAssembly());
		if (result == null){
			try{
				if (this.servletClass == null){
					this.servletClass =
							(Class<? extends Servlet>) this.getClass().getClassLoader().loadClass("org.apache.jasper.servlet.JspServlet");
				}
				Servlet tmp = (Servlet)ClassLoaderUtils.newInstance(this.servletClass);
				tmp.init(new Config(new Context(resource.getAssemblyClassLoader())));
				
				result = this.servletMap.putIfAbsent(resource.getAssembly(), tmp);
				if (result == null){
					result = tmp;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.web.ServletContextAware#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	private class Config implements ServletConfig {
		
		private Context context;
		
		public Config(Context context){
			this.context = context;
		}

		@Override
		public String getServletName() {
			return "jspServlet";
		}

		@Override
		public ServletContext getServletContext() {
			return context;
		}

		@Override
		public String getInitParameter(String name) {
			return initParams.get(name);
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			Vector<String> vector = new Vector<String>();
			for (String key: initParams.keySet()){
				vector.add(key);
			}
			return vector.elements();
		}
		
	}
	
	private class Context implements ServletContext {
		
		private ClassLoader loader;
		
		public Context(ClassLoader loader){
			this.loader = loader;
		}

		@Override
		public String getContextPath() {
			return servletContext.getContextPath();
		}

		@Override
		public ServletContext getContext(String uripath) {
			return servletContext.getContext(uripath);
		}

		@Override
		public int getMajorVersion() {
			return servletContext.getMajorVersion();
		}

		@Override
		public int getMinorVersion() {
			return servletContext.getMinorVersion();
		}

		@Override
		public int getEffectiveMajorVersion() {
			return servletContext.getEffectiveMajorVersion();
		}

		@Override
		public int getEffectiveMinorVersion() {
			return servletContext.getEffectiveMinorVersion();
		}

		@Override
		public String getMimeType(String file) {
			return servletContext.getMimeType(file);
		}

		@Override
		public Set<String> getResourcePaths(String path) {
			return servletContext.getResourcePaths(path);
		}

		@Override
		public URL getResource(String path) throws MalformedURLException {
			String resource = path;
			if (resource.startsWith("/")){
				resource = StringUtils.substring(resource, 1);
			}
			
			URL result = this.loader.getResource(resource);
			if (result == null){
				//支持Servlet3标准
				result = this.loader.getResource("META-INF/resources/" + resource);
			}
			return result;
		}

		@Override
		public InputStream getResourceAsStream(String path) {
			try{
				URL result = this.getResource(path);
				return result.openStream();
			}catch(Exception e){
				servletContext.log(e.getMessage(), e);
			}
			return null;
		}

		@Override
		public RequestDispatcher getRequestDispatcher(String path) {
			return servletContext.getRequestDispatcher(path);
		}

		@Override
		public RequestDispatcher getNamedDispatcher(String name) {
			return servletContext.getNamedDispatcher(name);
		}

		@SuppressWarnings("deprecation")
		@Override
		public Servlet getServlet(String name) throws ServletException {
			return servletContext.getServlet(name);
		}

		@SuppressWarnings("deprecation")
		@Override
		public Enumeration<Servlet> getServlets() {
			return servletContext.getServlets();
		}

		@SuppressWarnings("deprecation")
		@Override
		public Enumeration<String> getServletNames() {
			return servletContext.getServletNames();
		}

		@Override
		public void log(String msg) {
			servletContext.log(msg);
		}

		@SuppressWarnings("deprecation")
		@Override
		public void log(Exception exception, String msg) {
			servletContext.log(exception, msg);
		}

		@Override
		public void log(String message, Throwable throwable) {
			servletContext.log(message, throwable);
		}

		@Override
		public String getRealPath(String path) {
			return servletContext.getRealPath(path);
		}

		@Override
		public String getServerInfo() {
			return servletContext.getServerInfo();
		}

		@Override
		public String getInitParameter(String name) {
			return servletContext.getInitParameter(name);
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			return servletContext.getInitParameterNames();
		}

		@Override
		public boolean setInitParameter(String name, String value) {
			return servletContext.setInitParameter(name, value);
		}

		@Override
		public Object getAttribute(String name) {
			return servletContext.getAttribute(name);
		}

		@Override
		public Enumeration<String> getAttributeNames() {
			return servletContext.getAttributeNames();
		}

		@Override
		public void setAttribute(String name, Object object) {
			servletContext.setAttribute(name, object);
		}

		@Override
		public void removeAttribute(String name) {
			servletContext.removeAttribute(name);
			
		}

		@Override
		public String getServletContextName() {
			return servletContext.getServletContextName();
		}

		@Override
		public Dynamic addServlet(String servletName, String className) {
			return servletContext.addServlet(servletName, className);
		}

		@Override
		public Dynamic addServlet(String servletName, Servlet servlet) {
			return servletContext.addServlet(servletName, servlet);
		}

		@Override
		public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
			return servletContext.addServlet(servletName, servletClass);
		}

		@Override
		public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
			return servletContext.createServlet(clazz);
		}

		@Override
		public ServletRegistration getServletRegistration(String servletName) {
			return servletContext.getServletRegistration(servletName);
		}

		@Override
		public Map<String, ? extends ServletRegistration> getServletRegistrations() {
			return servletContext.getServletRegistrations();
		}

		@Override
		public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, String className) {
			return servletContext.addFilter(filterName, className);
		}

		@Override
		public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
			return servletContext.addFilter(filterName, filter);
		}

		@Override
		public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName,
				Class<? extends Filter> filterClass) {
			return servletContext.addFilter(filterName, filterClass);
		}

		@Override
		public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
			return servletContext.createFilter(clazz);
		}

		@Override
		public FilterRegistration getFilterRegistration(String filterName) {
			return servletContext.getFilterRegistration(filterName);
		}

		@Override
		public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
			return servletContext.getFilterRegistrations();
		}

		@Override
		public SessionCookieConfig getSessionCookieConfig() {
			return servletContext.getSessionCookieConfig();
		}

		@Override
		public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
			servletContext.setSessionTrackingModes(sessionTrackingModes);
		}

		@Override
		public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
			return servletContext.getDefaultSessionTrackingModes();
		}

		@Override
		public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
			return servletContext.getEffectiveSessionTrackingModes();
		}

		@Override
		public void addListener(String className) {
			servletContext.addListener(className);
		}

		@Override
		public <T extends EventListener> void addListener(T t) {
			servletContext.addListener(t);
			
		}

		@Override
		public void addListener(Class<? extends EventListener> listenerClass) {
			servletContext.addListener(listenerClass);
		}

		@Override
		public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
			return servletContext.createListener(clazz);
		}

		@Override
		public JspConfigDescriptor getJspConfigDescriptor() {
			return servletContext.getJspConfigDescriptor();
		}

		@Override
		public ClassLoader getClassLoader() {
			return this.loader;
		}

		@Override
		public void declareRoles(String... roleNames) {
			servletContext.declareRoles(roleNames);
		}

		@Override
		public String getVirtualServerName() {
			return servletContext.getVirtualServerName();
		}
		
	}

}
