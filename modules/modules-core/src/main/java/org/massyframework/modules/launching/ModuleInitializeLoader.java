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
* @日   期:  2017年4月9日
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
final class ModuleInitializeLoader extends  AbstractFrameworkInitializeLoader {
	
	private final Logger logger = 
			LoggerFactory.getLogger(ModuleInitializeLoader.class);
	private ModuleLoader moduleLoader;

	/**
	 * 构造方法
	 * @param handlers 前置的启动处理器
	 * @param moduleLoader 模块加载器
	 */
	public ModuleInitializeLoader(List<FrameworkInitializer> initializers, ModuleLoader moduleLoader) {
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
