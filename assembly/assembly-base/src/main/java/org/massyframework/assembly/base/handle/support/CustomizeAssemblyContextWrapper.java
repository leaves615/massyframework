/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
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

import java.util.Map;

import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.InjectCallback;
import org.massyframework.assembly.base.handle.ServiceInjectHandler;

/**
 * 自定义装配件上下文封装器
 */
final class CustomizeAssemblyContextWrapper extends AssemblyContextWrapper<AssemblyContext> {

	/**
	 * @param context
	 */
	public CustomizeAssemblyContextWrapper(AssemblyContext context) {
		super(context);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextWrapper#init()
	 */
	@Override
	protected void init() {
		super.init();
		try{
			ServiceInjectHandler handler =
				this.getHandler(ServiceInjectHandler.class);
			handler.addInjectCallback(new Callback());
		}catch(Exception e){
			throw new RuntimeException(this + " init() failed", e);
		}
	}

	/**
	 * 回调
	 */
	protected class Callback implements InjectCallback {
		
		public Callback(){}

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.handle.ServiceInjectCallback#doInject(java.util.Map)
		 */
		@Override
		public void doInject(Map<String, Object> serviceMap) throws Exception {
			AssemblyContext context = getReference();
			if (context instanceof InjectCallback){
				ExportServiceRepository serviceRepository =
						ExportServiceRepositoryReference.adaptFrom(getAssembly());
				if (serviceRepository != null){
					serviceMap.put(ExportServiceRepository.class.getName(), serviceRepository);
				}
				((InjectCallback)context).doInject(serviceMap);
			}
		}
		
	}
}
