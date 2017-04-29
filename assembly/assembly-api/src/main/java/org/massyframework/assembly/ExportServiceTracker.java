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
* @日   期:  2017年4月9日
*/
package org.massyframework.assembly;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.massyframework.assembly.util.Asserts;

/**
 * 输出服务跟踪，可以过滤去掉重复的服务注册和注销通知
 */
public class ExportServiceTracker<T>{
	
	private final ExportServiceRepository serviceRepository;
	private final Class<T> serviceType;
	private final String filterString;
	protected final Map<ExportServiceReference<T>, T> serviceMap = new ConcurrentHashMap<ExportServiceReference<T>, T>();
	private final ExportServiceTrackerCustomizer<T> customizer;
	
	private volatile EventAdapter eventAdapter;

	/**
	 * 构造方法
	 * @param serviceRepository 输出服务仓储
	 * @param serviceType 要跟踪的服务类型
	 * @param filterString 筛选服务的语句，为null表示跟踪所有serviceType类型的服务
	 */
	public ExportServiceTracker(ExportServiceRepository serviceRepository,
			Class<T> serviceType,
			String filterString){
		this(serviceRepository, serviceType, filterString, null);
	}
	
	/**
	 * 构造方法
	 * @param serviceRepository 输出服务仓储
	 * @param serviceType 要跟踪的服务类型
	 * @param filterString 筛选服务的语句，为null表示跟踪所有serviceType类型的服务
	 * @param customizer 自定义的输出跟踪接口
	 */
	public ExportServiceTracker(ExportServiceRepository serviceRepository,
			Class<T> serviceType,
			String filterString,
			ExportServiceTrackerCustomizer<T> customizer) {
		Asserts.notNull(serviceRepository, "serviceRepository cannot be null.");
		Asserts.notNull(serviceType, "serviceType cannot be null.");
		
		this.serviceRepository = serviceRepository;
		this.serviceType = serviceType;
		this.filterString = filterString;
		this.customizer = customizer;
	}
	
	
	
	/**
	 * 开始跟踪
	 */
	public final synchronized void open(){
		if (this.eventAdapter == null){
			this.eventAdapter = new EventAdapter();
			
			this.serviceRepository.addListener(this.eventAdapter, this.filterString);
			List<ExportServiceReference<T>> list =
					this.serviceRepository.getServiceReferences(this.serviceType, filterString);
			for (ExportServiceReference<T> reference: list){
				T service = this.serviceRepository.getService(reference);
				if (this.serviceMap.putIfAbsent(reference, service) == null){
					this.addService(reference, service);
				}
			}
		}
	}
	
	/**
	 * 结束跟踪
	 */
	public final synchronized void close(){
		if (this.eventAdapter != null){
			this.serviceRepository.removeListener(this.eventAdapter);
			this.eventAdapter = null;
			this.serviceMap.clear();
		}
	}
	
	/**
	 * 添加服务
	 * @param referencen 输出服务引用
	 * @param service 服务实例
	 */
	protected void addService(ExportServiceReference<T> reference, T service){
		if (this.customizer != null){
			this.customizer.addService(reference, service);
		}
	}
	
	/**
	 * 移除服务
	 * @param reference 输出服务引用
	 * @param service 服务实例
	 */
	protected void removeService(ExportServiceReference<T> reference, T service){
		if (this.customizer != null){
			this.customizer.removeService(reference, service);
		}
	}
	

	
	private class EventAdapter implements ExportServiceListener {
		
		public EventAdapter(){
			
		}

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.ExportServiceListener#onChanged(org.massyframework.assembly.ServiceEvent)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void onChanged(ServiceEvent event) {
			ExportServiceReference<T> reference = 
					(ExportServiceReference<T>)event.getServiceReference();
			T service = (T) serviceRepository.getService(reference);
			if (event.getType() == ServiceEvent.REGISTED){
				if (serviceMap.putIfAbsent(reference, service) == null){
					addService(reference, service);
				}
			}else{
				if (serviceMap.remove(reference) == service){
					removeService(reference, service);
				}
			}
		}
	}
}
