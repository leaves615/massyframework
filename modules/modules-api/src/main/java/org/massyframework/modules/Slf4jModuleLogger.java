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
package org.massyframework.modules;

import org.jboss.modules.Main;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.log.ModuleLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用slf4j进行模块日志记录
 * @author huangkaihui
 *
 */
public class Slf4jModuleLogger implements ModuleLogger {

	private Logger logger = LoggerFactory.getLogger("modules");
	
	/**
	 * 
	 */
	public Slf4jModuleLogger() {
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#trace(java.lang.String)
	 */
	@Override
	public void trace(String message) {
		/*if (logger.isTraceEnabled()){
			logger.trace(message);
		}*/
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#trace(java.lang.String, java.lang.Object)
	 */
	@Override
	public void trace(String format, Object arg1) {
		/*if (logger.isTraceEnabled()){
			logger.trace(String.format(format, arg1));
		}*/
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#trace(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void trace(String format, Object arg1, Object arg2) {
		/*if (logger.isTraceEnabled()){
			logger.trace(String.format(format, arg1, arg2));
		}*/
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#trace(java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void trace(String format, Object arg1, Object arg2, Object arg3) {
		/*if (logger.isTraceEnabled()){
			logger.trace(String.format(format, arg1, arg2, arg3));
		}*/
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#trace(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void trace(String format, Object... args) {
		/*if (logger.isTraceEnabled()){
			logger.trace(String.format(format, args));
		}*/
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#trace(java.lang.Throwable, java.lang.String)
	 */
	@Override
	public void trace(Throwable t, String message) {
		if (logger.isTraceEnabled()){
			logger.trace(message, t);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#trace(java.lang.Throwable, java.lang.String, java.lang.Object)
	 */
	@Override
	public void trace(Throwable t, String format, Object arg1) {
		if (logger.isTraceEnabled()){
			logger.trace(String.format(format, arg1), t);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#trace(java.lang.Throwable, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void trace(Throwable t, String format, Object arg1, Object arg2) {
		if (logger.isTraceEnabled()){
			logger.trace(String.format(format, arg1, arg2), t);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#trace(java.lang.Throwable, java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void trace(Throwable t, String format, Object arg1, Object arg2,
			Object arg3) {
		if (logger.isTraceEnabled()){
			logger.trace(String.format(format, arg1, arg2, arg3), t);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#trace(java.lang.Throwable, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void trace(Throwable t, String format, Object... args) {
		if (logger.isTraceEnabled()){
			logger.trace(String.format(format, args), t);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#greeting()
	 */
	@Override
	public void greeting() {
		if (logger.isInfoEnabled()){
			logger.info(String.format("JBoss Modules version %s", Main.getVersionString()));
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#moduleDefined(org.jboss.modules.ModuleIdentifier, org.jboss.modules.ModuleLoader)
	 */
	@Override
	public void moduleDefined(ModuleIdentifier identifier,
			ModuleLoader moduleLoader) {
		if (logger.isDebugEnabled()){
			logger.debug(String.format("Module %s defined by %s", identifier, moduleLoader));
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#classDefineFailed(java.lang.Throwable, java.lang.String, org.jboss.modules.Module)
	 */
	@Override
	public void classDefineFailed(Throwable throwable, String className,
			Module module) {
		  if (this.logger.isWarnEnabled()){
			  logger.warn(String.format("Failed to define class %s in %s", className, module), throwable);
		  }
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#classDefined(java.lang.String, org.jboss.modules.Module)
	 */
	@Override
	public void classDefined(String name, Module module) {
		 /*if (logger.isTraceEnabled()){
			 logger.trace(String.format("Defined class %s in %s", name, module));
		 }*/
	}

	/* (non-Javadoc)
	 * @see org.jboss.modules.log.ModuleLogger#providerUnloadable(java.lang.String, java.lang.ClassLoader)
	 */
	@Override
	public void providerUnloadable(String name, ClassLoader loader) {		 
		 if (logger.isTraceEnabled()){
			 logger.trace(String.format("Could not load provider %s in %s", name, loader));
		 }
	}
}
