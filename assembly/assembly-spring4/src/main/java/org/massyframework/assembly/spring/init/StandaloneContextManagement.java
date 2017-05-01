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
* @日   期:  2017年4月14日
*/
package org.massyframework.assembly.spring.init;

import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.ClassLoaderReference;
import org.massyframework.assembly.base.handle.HandlerRegistration;
import org.massyframework.assembly.base.handle.support.AssemblyContextManagement;
import org.massyframework.assembly.spring.SpringAssemblyContext;
import org.springframework.web.context.ContextLoader;

/**
 * 独立Spring装配件上下文管理器
 * @author huangkaihui
 *
 */
public final class StandaloneContextManagement extends AssemblyContextManagement {

	protected HandlerRegistration<SpringAssemblyContext> registration;
	/**
	 * 
	 */
	public StandaloneContextManagement() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextManagement#getAssemblyContext()
	 */
	@Override
	protected AssemblyContext getAssemblyContext() {
		return this.registration != null ?
				this.registration.getHandler() :
					null;
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextManagement#doStarting()
	 */
	@Override
	public void doStarting() throws Exception {
		super.doStarting();
		this.registerAssemblyContext();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextManagement#doStopped()
	 */
	@Override
	public void doStopped() throws Exception {
		this.unregisterAssemblyContext();
		super.doStopped();
	}
	
	/**
	 * 注册AssemblyContext
	 */
	protected synchronized void registerAssemblyContext(){
		//创建并启动applicationContext
		if (this.registration == null){
			SpringAssemblyContext applicationContext =
					this.createApplicationContext();
			this.registration = this.getHandlerRegistry().register(applicationContext);
			
			//设置类加载器
			ClassLoader loader = ClassLoaderReference.adaptFrom(this.getAssembly());
			applicationContext.setClassLoader(loader);
			
			//设置Spring 配置文件路径
			String location = this.getAssembly().getInitParameter(ContextLoader.CONFIG_LOCATION_PARAM);
			if (location == null){
				throw new RuntimeException("cannot found spring xml location with initParams: key=" + ContextLoader.CONFIG_LOCATION_PARAM);
			}
			applicationContext.setConfigLocation(location);		
			
			applicationContext.refresh();
		}
	}

	/**
	 * 注销AssemblyContext
	 */
	protected synchronized void unregisterAssemblyContext() {
		//注销并关闭ApplicationContext
		if (this.registration != null){
			SpringAssemblyContext applicationContext =
					this.registration.getHandler();
			this.registration.unregister();
			this.registration = null;
			
			applicationContext.close();
			applicationContext = null;
		}
	}

	/**
	 * 创建Spring装配件上下文
	 * @return
	 */
	protected SpringAssemblyContext createApplicationContext() {
		SpringAssemblyContext result = new SpringAssemblyContext();
		return result;
	}
}
