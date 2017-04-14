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

import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;
import org.massyframework.assembly.FrameworkInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缺省的启动处理加载器
 */
final class ModuleInitializeHandler extends  AbstractFrameworkInitializeHandler {
	
	private final Logger logger = 
			LoggerFactory.getLogger(ModuleInitializeHandler.class);
	private ModuleLoader moduleLoader;

	/**
	 * 构造方法
	 * @param handlers 前置的启动处理器
	 * @param moduleLoader 模块加载器
	 */
	public ModuleInitializeHandler(List<FrameworkInitializer> initializers, ModuleLoader moduleLoader) {
		super(initializers);
		this.moduleLoader = moduleLoader;
	}
	
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.launching.AbstractLaunchingLoader#getClassLoaderes()
	 */
	@Override
	protected List<ClassLoader> getClassLoaderes() {
		List<ModuleIdentifier> identifiers =
				new ArrayList<ModuleIdentifier>();
		identifiers.add(ModuleIdentifier.create("org.massyframework.assembly", "main"));
/*		identifiers.add(ModuleIdentifier.create("org.massyframework.plugins.cli", "main"));
		identifiers.add(ModuleIdentifier.create("org.massyframework.plugins.spring4", "main"));
		identifiers.add(ModuleIdentifier.create("org.massyframework.plugins.spring4test", "main"));*/
		
		List<ClassLoader> result = new ArrayList<ClassLoader>();
		for (ModuleIdentifier identifier: identifiers){
			try{
				result.add(this.moduleLoader.loadModule(identifier).getClassLoader());
			}catch(Exception e){
				if (logger.isErrorEnabled()){
					logger.error("load module falied: identifier=" + identifier + ".", e);
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	protected ModuleLoader getModuleLoader(){
		return this.moduleLoader;
	}
}
