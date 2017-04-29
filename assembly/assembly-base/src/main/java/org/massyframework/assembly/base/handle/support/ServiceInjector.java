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
* @日   期:  2017年4月14日
*/
package org.massyframework.assembly.base.handle.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.InjectCallback;
import org.massyframework.assembly.ServiceNotFoundException;
import org.massyframework.assembly.base.handle.DependencyServiceResource;
import org.massyframework.assembly.base.handle.DependencyServiceResourceHandler;
import org.massyframework.assembly.base.handle.DependencyServiceResourceMatchHandler;
import org.massyframework.assembly.base.handle.ServiceInjectHandler;
import org.massyframework.assembly.util.CollectionUtils;

/**
 * 服务注入器
 */
final class ServiceInjector extends AbstractHandler implements ServiceInjectHandler {

	/**
	 * 
	 */
	public ServiceInjector() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ServiceInjectHandler#addInjectCallback(org.massyframework.assembly.InjectCallback)
	 */
	@Override
	public void addInjectCallback(InjectCallback callback) throws Exception {
		if (callback == null) return;
		
		DependencyServiceResourceHandler handler =
				this.findHandler(DependencyServiceResourceHandler.class);
		if (handler != null){
			List<DependencyServiceResource> resources =
						handler.getDependencyServiceResources();
			
			if (!CollectionUtils.isEmpty(resources)){
				DependencyServiceResourceMatchHandler matchHandler =
						this.getHandler(DependencyServiceResourceMatchHandler.class);
				ExportServiceRepository serviceRepository =
						ExportServiceRepositoryReference.adaptFrom(this.getAssembly());
				
				Map<String, Object> serviceMap = new HashMap<String, Object>();
				for (DependencyServiceResource resource: resources){
					ExportServiceReference<?> reference =
							matchHandler.getMatchedServiceReference(resource);
					Object service =  reference == null ? null :
						serviceRepository.getService(reference);
					if (service == null){
						throw new ServiceNotFoundException("service not found: dependency=" + resource + ".");
					}
					
					serviceMap.put(resource.getCName(), service);
				}
				
				callback.doInject(serviceMap);
			}
		}
	}

}
