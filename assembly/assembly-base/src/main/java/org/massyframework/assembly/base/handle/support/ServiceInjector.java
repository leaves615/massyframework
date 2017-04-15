/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月14日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
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
