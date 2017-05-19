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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.util.ClassLoaderUtils;
import org.massyframework.assembly.web.HttpResource;
import org.massyframework.assembly.web.HttpResourceProcessor;
import org.massyframework.assembly.web.ServletContextAware;
import org.slf4j.Logger;

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
		initParams.put("logVerbosityLevel", "DEBUG");
		initParams.put("fork", "false");
		initParams.put("xpoweredBy", "false");
		initParams.put("compilerSourceVM", "1.7");
		initParams.put("compilerTargetVM", "1.7");
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
		
		ClassLoader loader = ClassLoaderUtils.setThreadContextClassLoader(
				servlet.getServletConfig().getServletContext().getClassLoader());
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
				ClassLoader jasperLoader = new JsparClassLoader(
						resource.getAssemblyClassLoader(), this.getClass().getClassLoader());
				if (this.servletClass == null){
					this.servletClass = (Class<? extends Servlet>) 
							jasperLoader.loadClass("org.apache.jasper.servlet.JspServlet");
							
				}
				Servlet tmp = ClassLoaderUtils.newInstance(this.servletClass);
				
				ClassLoader loader = 
						ClassLoaderUtils.setThreadContextClassLoader(jasperLoader);
				try{
					tmp.init(new Config(new Context(
							jasperLoader, LoggerReference.adaptFrom(resource.getAssembly()))));
					
					result = this.servletMap.putIfAbsent(resource.getAssembly(), tmp);
					if (result == null){
						result = tmp;
					}
				}finally{
					ClassLoaderUtils.setThreadContextClassLoader(loader);
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
	
	private class Context extends ServletContextWrapper {
		
		private ClassLoader loader;
		private Logger logger;
		
		public Context(ClassLoader loader, Logger logger){
			this.loader = loader;
			this.logger = logger;
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
									
			if (result == null){
				result = super.getResource(path);
			}
			
			if (logger != null){
				if (logger.isDebugEnabled()){
					String msg = result == null ?
							"cannot found resource: " + path + ".":
								"found resource "+ path + " with: " + result.getPath() + ".";
					logger.debug(msg);
				}
			}
			return result;
		}

		@Override
		public InputStream getResourceAsStream(String path) {
			try{
				URL result = this.getResource(path);
				if (result != null){
					return result.openStream();
				}
				return null;
			}catch(Exception e){
				servletContext.log(e.getMessage(), e);
			}
			return null;
		}
		
		/* (non-Javadoc)
		 * @see org.massyframework.assembly.base.web.ServletContextWrapper#getResourcePaths(java.lang.String)
		 */
		@Override
		public Set<String> getResourcePaths(String path) {
			Set<String> result = super.getResourcePaths(path);
			return result;
		}



		@Override
		public ClassLoader getClassLoader() {
			return this.loader;
		}

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.base.web.ServletContextWrapper#getInternalContext()
		 */
		@Override
		protected ServletContext getInternalContext() {
			return servletContext;
		}
		
	}

}
