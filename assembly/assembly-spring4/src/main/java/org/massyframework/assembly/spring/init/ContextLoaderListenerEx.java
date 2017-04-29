/**
* @Copyright: 2017 smarabbit studio Inc. All rights reserved.
*  
* @作   者：  huangkaihui
* @日   期:  2017年3月30日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spring.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.AssemblyContextReference;
import org.massyframework.assembly.ClassLoaderReference;
import org.massyframework.assembly.base.handle.Handler;
import org.massyframework.assembly.base.handle.HandlerRegistration;
import org.massyframework.assembly.base.handle.HandlerRegistry;
import org.massyframework.assembly.spring.SpringWebAssemblyContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * Spring ContextLoaderListener的扩展
 */
public final class ContextLoaderListenerEx extends ContextLoaderListener 
	implements AssemblyContextReference, Handler{

	private volatile HandlerRegistry handlerRegistry;
	private volatile HandlerRegistration<SpringWebAssemblyContext> assemblyContextRegistration;
	
	/**
	 * 
	 */
	public ContextLoaderListenerEx() {
	}

	/**
	 * @param context
	 */
	public ContextLoaderListenerEx(WebApplicationContext context) {
		super(context);
	}
	

	/* (non-Javadoc)
	 * @see org.springframework.web.context.ContextLoader#createWebApplicationContext(javax.servlet.ServletContext)
	 */
	@Override
	protected WebApplicationContext createWebApplicationContext(ServletContext sc) {
		SpringWebAssemblyContext result =
				new SpringWebAssemblyContext();
		this.assemblyContextRegistration = this.handlerRegistry.register(result);
		ClassLoader loader = ClassLoaderReference.adaptFrom(
				this.handlerRegistry.getReference());
		result.setClassLoader(loader);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.handle.Handler#init(org.massyframework.assembly.handle.HandlerRegistry)
	 */
	@Override
	public void init(HandlerRegistry handlerRegistry) {
		this.handlerRegistry = handlerRegistry;
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.handle.Handler#destroy()
	 */
	@Override
	public void destroy() {
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.context.ContextLoaderListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if (this.assemblyContextRegistration != null){
			this.assemblyContextRegistration.unregister();
			this.assemblyContextRegistration = null;
		}
		super.contextDestroyed(event);
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

	
	
	

}
