/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.modules.launching;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;
import org.massyframework.assembly.FrameworkFactory;
import org.massyframework.assembly.FrameworkInitializeLoader;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.util.Asserts;
import org.massyframework.assembly.util.ServiceLoaderUtils;
import org.massyframework.modules.Launcher;

/**
 * MassyFramework启动器
 */
public class MassyFrameworkLauncher implements Launcher {

	/**
	 * 
	 */
	public MassyFrameworkLauncher() {
		
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.modules.Launcher#launch(java.util.Map, javax.servlet.ServletContext)
	 */
	@Override
	public void launch(Map<String, String> configuration, ServletContext servletContext) throws Exception {
		Asserts.notNull(servletContext, "serveltContext cannot be null.");
		
		ModuleLoader moduleLoader = this.extractModuleLoader(servletContext);	
		Module module = this.loadModule(moduleLoader, "org.massyframework.assembly");
		
		FrameworkFactory factory =  
				ServiceLoaderUtils.loadFirstService(FrameworkFactory.class, module.getClassLoader());
		FrameworkInitializeLoader handler =
				this.createFrameworkInitializeHandler(servletContext, moduleLoader);
		factory.createFramework(configuration, handler);
	}
	
	/**
	 * 从ServletContext中提取ModuleLoader.
	 * @param servletContext Servlet上下文
	 * @return {@link ModuleLoader}
	 * @throws ServletException
	 */
	protected ModuleLoader extractModuleLoader(ServletContext servletContext) throws ServletException{
		Object object = servletContext.getAttribute(ModuleLoader.class.getName());
		ModuleLoader result = null;
		if ((object != null) && (object instanceof ModuleLoader)){
			result = ModuleLoader.class.cast(object);
		}
		
		if (result == null){
			throw new ServletException("cannot found ModuleLoader with ServletContext.");
		}
		
		return result;
	}
	
	/**
	 * 加载模块
	 * @param moduleLoader 模块加载器
	 * @param name 模块名称
	 * @return {@link Module}
	 * @throws Exception 发生非预期异常
	 */
	protected Module loadModule(ModuleLoader moduleLoader, String name) throws Exception{
		ModuleIdentifier identifier = ModuleIdentifier.create(name);
		return moduleLoader.loadModule(identifier);
	}

	/**
	 * 创建FrameworkInitializeHandler实例
	 * @param servletContext Servlet上下文
	 * @param moduleLoader 模块加载器
	 * @return {@link FrameworkInitializeLoader}
	 */
	protected FrameworkInitializeLoader createFrameworkInitializeHandler(
			ServletContext servletContext, ModuleLoader moduleLoader){
		List<FrameworkInitializer> handlers =
				new ArrayList<FrameworkInitializer>();
		handlers.add(new ServletContextInitializer(servletContext));
		
		return new ModuleInitializeLoader(handlers, moduleLoader);
	}
}
