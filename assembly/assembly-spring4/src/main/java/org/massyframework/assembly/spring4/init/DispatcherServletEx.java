/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月14日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spring4.init;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.AssemblyContextReference;
import org.massyframework.assembly.ClassLoaderReference;
import org.massyframework.assembly.base.handle.Handler;
import org.massyframework.assembly.base.handle.HandlerRegistration;
import org.massyframework.assembly.base.handle.HandlerRegistry;
import org.massyframework.assembly.spring4.SpringWebAssemblyContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 扩展Spring的DispatcherServlet类，提供装配件上下文引用方法
 *
 */
public class DispatcherServletEx extends DispatcherServlet implements AssemblyContextReference, Handler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1500318052746897165L;
	public static final String ROOTAPPLICATIONCONTEXT_SHARE = "root.applicationContext.share";
	private volatile ServletConfig delegate;
	private volatile HandlerRegistry handlerRegistry;
	private volatile HandlerRegistration<SpringWebAssemblyContext> assemblyContextRegistration;
	
	/**
	 * 
	 */
	public DispatcherServletEx() {
		this.setPublishContext(false);
	}

	/**
	 * @param webApplicationContext
	 */
	public DispatcherServletEx(WebApplicationContext webApplicationContext) {
		super(webApplicationContext);
		this.setPublishContext(false);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ObjectReference#getReference()
	 */
	@Override
	public AssemblyContext getReference() {
		return this.assemblyContextRegistration == null ?
				null:
					this.assemblyContextRegistration.getHandler();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.Handler#init(org.massyframework.assembly.base.handle.HandlerRegistry)
	 */
	@Override
	public void init(HandlerRegistry handlerRegistry) {
		this.handlerRegistry = handlerRegistry;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.delegate = config;
		String text = config.getInitParameter(ROOTAPPLICATIONCONTEXT_SHARE);
		if ("true".equals(text)){
			super.init(new Config(config.getServletContext()));
		}else{
			super.init(new Config(
				new ServletContextWrapper(this.delegate.getServletContext())));
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.FrameworkServlet#createWebApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
		SpringWebAssemblyContext result =
				new SpringWebAssemblyContext();
		this.assemblyContextRegistration = this.handlerRegistry.register(result);
		
		result.setParent(parent);
		ClassLoader loader = ClassLoaderReference.adaptFrom(this.handlerRegistry.getReference());
		result.setClassLoader(loader);
		
		result.setConfigLocation(this.getContextConfigLocation());
		configureAndRefreshWebApplicationContext(result);
	
		return result;
	}
	
	/**
	 * ServletConfig封装, 更改默认配置文件的路径
	 */
	private class Config implements ServletConfig {
		
		private ServletContext servletContext;
				
		public Config(ServletContext servletContext){
			this.servletContext = servletContext;
		}

		@Override
		public String getServletName() {
			return delegate.getServletName();
		}

		@Override
		public ServletContext getServletContext() {
			return this.servletContext;
		}

		@Override
		public String getInitParameter(String name) {
			String result = delegate.getInitParameter(name);
			if (result == null){
				if (ContextLoader.CONFIG_LOCATION_PARAM.equals(name)){
					result = "classpath:META-INF/" + delegate.getServletName() + "-servlet.xml";
				}
			}
			return result;
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			Enumeration<String> em = delegate.getInitParameterNames();
			Vector<String> vector = new Vector<String>();
			while (em.hasMoreElements()){
				vector.add(em.nextElement());
			}
			vector.add(ContextLoader.CONFIG_LOCATION_PARAM);
			return vector.elements();
		}
		
	}
}
