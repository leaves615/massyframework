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
package org.massyframework.assembly.servlet.jasper;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
import org.apache.jasper.Constants;
import org.apache.jasper.compiler.TldCache;
import org.apache.jasper.servlet.JspServlet;
import org.apache.jasper.servlet.TldScanner;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.base.web.MixinClassLoader;
import org.massyframework.assembly.base.web.ServletContextWrapper;
import org.massyframework.assembly.util.ClassLoaderUtils;
import org.massyframework.assembly.web.HttpResource;
import org.massyframework.assembly.web.HttpResourceProcessor;
import org.massyframework.assembly.web.ServletContextAware;
import org.slf4j.Logger;

/**
 * jsp资源处理器
 */
public class JasparResourceProessor implements HttpResourceProcessor, ServletContextAware {
	
	private static final String LIB_PATH = "/WEB-INF/lib/";
	
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
				synchronized(this){
					if (result == null){
						MixinClassLoader jasperLoader = new MixinClassLoader(
							resource.getAssemblyClassLoader(), this.getClass().getClassLoader());
													
						if (this.servletClass == null){
							this.servletClass = (Class<? extends Servlet>) 
								jasperLoader.loadClass("org.apache.jasper.servlet.JspServlet");		
						}
						Servlet tmp = ClassLoaderUtils.newInstance(this.servletClass);
					
						ClassLoader loader = 
							ClassLoaderUtils.setThreadContextClassLoader(jasperLoader);
						try{					
							
							Context context =  new Context(
									jasperLoader, LoggerReference.adaptFrom(resource.getAssembly()));							
							context.scanTlds();
							tmp.init(new Config(context));
							
						
							result = this.servletMap.putIfAbsent(resource.getAssembly(), tmp);
							if (result == null){
								result = tmp;
							}
						}finally{
							ClassLoaderUtils.setThreadContextClassLoader(loader);
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	protected TldCache getTldCache(ServletContext context){
		Object result = context.getAttribute(TldCache.SERVLET_CONTEXT_ATTRIBUTE_NAME);
		if (result == null){
			return null;
		}
		
		if (result instanceof TldCache){
			return (TldCache)result;
		}
		return null;
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
		
		private MixinClassLoader loader;
		private Logger logger;
		private Object tldCache;
		private Object instanceManager;
		
		private Map<String, URL> libs;
		
		public Context(MixinClassLoader loader, Logger logger){
			this.loader = loader;
			this.logger = logger;
			this.libs = new HashMap<String, URL>();
			List<ClassLoader> loaders = this.loader.getClassLoaders();
			for (ClassLoader cl: loaders){
				if (cl instanceof URLClassLoader){
					URL[] urls = ((URLClassLoader)cl).getURLs();
					for (URL url: urls){
						String name = LIB_PATH + "__embed_" + UUID.randomUUID() + ".jar";
						this.libs.put(name, url);
					}
				}
			}
		}

		@Override
		public Object getAttribute(String name) {
			if (TldCache.SERVLET_CONTEXT_ATTRIBUTE_NAME.equals(name)){
				return this.tldCache;
			}
			
			if (InstanceManager.class.getName().equals(name)){
				return this.instanceManager;
			}
			return super.getAttribute(name);
		}



		@Override
		public Enumeration<String> getAttributeNames() {
			Vector<String> result = new Vector<String>();
			Enumeration<String> em = super.getAttributeNames();
			while (em.hasMoreElements()){
				result.add(em.nextElement());
			}
			if (this.tldCache != null){
				result.addElement(TldCache.SERVLET_CONTEXT_ATTRIBUTE_NAME);
			}
			if (this.instanceManager != null){
				result.add(InstanceManager.class.getName());
			}
			return result.elements();
		}



		@Override
		public void setAttribute(String name, Object object) {
			if (TldCache.SERVLET_CONTEXT_ATTRIBUTE_NAME.equals(name)){
				this.tldCache = object;
			}else{
				if (InstanceManager.class.getName().equals(name)){
					this.instanceManager = object;
				}else{
					super.setAttribute(name, object);
				}
			}
		}



		/* (non-Javadoc)
		 * @see org.massyframework.assembly.base.web.ServletContextWrapper#getRealPath(java.lang.String)
		 */
		@Override
		public String getRealPath(String path) {
			String result = super.getRealPath(path);
			
			if (this.logger != null){
				if (this.logger.isTraceEnabled()){
					String msg = result == null ?
						"cannot get real path: path=" + path + "." :
							"get real path " + path + " with:" + result +".";
					this.logger.trace(msg);
				}
				
			}
			return result;
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
			
			if (result == null){
				return this.libs.get(path);
			}
			
			if (logger != null){
				if (logger.isTraceEnabled()){
					String msg = result == null ?
							"cannot found resource: " + path + ".":
								"found resource "+ path + " with: " + result.getPath() + ".";
					logger.trace(msg);
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
			if (LIB_PATH.equals(path)){
				return this.getJarLibs(path);
			}else{
				Set<String> result = super.getResourcePaths(path);
				return result;
			}
		}

		protected Set<String> getJarLibs(String path){
			Set<String> result = new HashSet<String>();
			result.addAll(super.getResourcePaths(path));
			result.addAll(this.libs.keySet());
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
		
		/**
		 * 扫描tlds
		 * @throws ServletException
		 */
		protected void scanTlds() throws ServletException{
			this.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
			boolean validate = Boolean.parseBoolean(
	                this.getInitParameter(Constants.XML_VALIDATION_TLD_INIT_PARAM));
	        String blockExternalString = this.getInitParameter(
	                Constants.XML_BLOCK_EXTERNAL_INIT_PARAM);
	        boolean blockExternal = false;
	        if (blockExternalString == null) {
	            blockExternal = true;
	        } else {
	            blockExternal = Boolean.parseBoolean(blockExternalString);
	        }
	        
			// scan the application for TLDs
	        TldScanner scanner = new TldScannerEx(this, true, validate, blockExternal);
	        try {	        	
	        	scanner.setClassLoader(this.loader);
	            scanner.scan();
	         
	        } catch (Exception e) {
	            throw new ServletException(e);
	        }
	        	        
	        this.setAttribute(TldCache.SERVLET_CONTEXT_ATTRIBUTE_NAME,
	                new TldCache(this, scanner.getUriTldResourcePathMap(),
	                        scanner.getTldResourcePathTaglibXmlMap()));
		}
	}

}
