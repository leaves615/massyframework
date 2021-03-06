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
* @日   期:  2017年4月12日
*/
package org.massyframework.assembly.base.handle.support;

import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.ClassLoaderReference;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.base.handle.ActivationHandler;
import org.massyframework.assembly.base.handle.HandlerRegistration;

/**
 * 自定义独立装配件上下文管理器
 */
public class CustomizeAssemblyContextManagement extends AssemblyContextManagement 
	implements ActivationHandler {

	private volatile HandlerRegistration<AssemblyContext> registration;
	
	/**
	 * 
	 */
	public CustomizeAssemblyContextManagement() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextManagement#getAssemblyContext()
	 */
	@Override
	protected AssemblyContext getAssemblyContext() {
		return this.registration == null ? null : this.registration.getHandler();
	}
	
	/**
	 * 创建装配件上下文
	 * @throws Exception 
	 */
	protected AssemblyContext createAssemblyContext() throws Exception{
		String className = this.getAssembly().getInitParameter(
				Constants.ASSEMBLYCONTEXT_CLASSNAME);
		if (className == null){
			throw new RuntimeException(
					"connot found service contianer class: initParameter=" + Constants.ASSEMBLYCONTEXT_CLASSNAME + ".");
		}
		
		ClassLoader loader = ClassLoaderReference.adaptFrom(this.getAssembly());
		Class<?> clazz = loader.loadClass(className);
		
		AssemblyContext customize = (AssemblyContext)clazz.newInstance();
		AssemblyContextWrapper<AssemblyContext> result = 
				new CustomizeAssemblyContextWrapper(customize);
		
		return result;
	}
	
	/**
	 * 创建并注册装配件上下文
	 * @throws Exception
	 */
	protected final synchronized void createAndRegisterAssemblyContext() throws Exception{
		if (this.registration == null){
			AssemblyContext context = this.createAssemblyContext();
			this.registration = this.getHandlerRegistry().register(context);
		}
	}
	
	/**
	 * 注销并且释放装配件上下文
	 * @throws Exception
	 */
	protected final synchronized void detroyAndUnregisterAssemblyContext(){
		if (this.registration != null){
			this.registration.unregister();
			this.registration = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ActivationHandler#doStarting()
	 */
	@Override
	public void doStarting() throws Exception {
		super.doStarting();
		this.createAndRegisterAssemblyContext();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextManagement#doStopped()
	 */
	@Override
	public void doStopped() throws Exception {
		this.detroyAndUnregisterAssemblyContext();
		super.doStopped();
	}
}
