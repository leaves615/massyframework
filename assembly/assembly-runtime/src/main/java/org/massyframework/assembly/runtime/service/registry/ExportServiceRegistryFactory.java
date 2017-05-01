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
* @日   期:  2017年4月10日
*/
package org.massyframework.assembly.runtime.service.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.Filter;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.ServiceEvent;
import org.massyframework.assembly.ServiceFactory;
import org.massyframework.assembly.base.ExportServiceRegistration;
import org.massyframework.assembly.base.ExportServiceRegistry;
import org.massyframework.assembly.base.util.ExportServiceUtils;
import org.massyframework.assembly.util.Asserts;
import org.slf4j.Logger;

/**
 * 输出服务注册器
 */
public abstract class ExportServiceRegistryFactory  implements ServiceFactory<ExportServiceRegistry>{

	private final Map<Assembly, List<ExportServiceRegistration<?>>> registrations =
			new ConcurrentHashMap<Assembly, List<ExportServiceRegistration<?>>>();
	
	
	/**
	 * 
	 */
	public ExportServiceRegistryFactory() {
	}
	
	/**
	 * 全局模式下查找首个符合筛选条件的服务引用
	 * @param filter 筛选器
	 * @return {@link ExportServiceReference}， 可能返回null.
	 */
	protected ExportServiceReference<?> findServiceReference(Filter filter) {
		Asserts.notNull(filter, "filter cannot be null.");
	
		for (List<ExportServiceRegistration<?>> list: this.registrations.values()){
			for (ExportServiceRegistration<?> registration: list){
				ExportServiceReference<?> reference = registration.getReference();
				if (filter.match(reference)){
					return reference;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 全局模式下查找所有满足筛选条件的服务引用
	 * @param filter
	 * @return
	 */
	protected List<ExportServiceReference<?>> getServiceReferences(Filter filter) {
		Asserts.notNull(filter, "filter cannot be null.");
		List<ExportServiceReference<?>> result =
				new ArrayList<ExportServiceReference<?>>();
		for (List<ExportServiceRegistration<?>> list: this.registrations.values()){
			for (ExportServiceRegistration<?> registration: list){
				ExportServiceReference<?> reference = registration.getReference();
				if (filter.match(reference)){
					result.add(reference);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 执行服务注册
	 * @param classes 服务类型
	 * @param svcObj 服务对象
	 * @param props 服务属性
	 * @param assembly 要求注册的装配件
	 * @return {@link ExportServiceRegistration}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	ExportServiceRegistration<?> doRegister(Class<?>[] classes, Object svcObj, 
			Map<String, Object> props, Assembly assembly){
		
		ExportServiceRegistrationImpl result =
				new ExportServiceRegistrationImpl(this, classes, svcObj, props, assembly);
		
		List<ExportServiceRegistration<?>> list =
				this.registrations.get(assembly);
		if (list == null){
			List<ExportServiceRegistration<?>> tmp = 
					new CopyOnWriteArrayList<ExportServiceRegistration<?>>();
			list = this.registrations.putIfAbsent(assembly, tmp);
			if (list == null){
				list = tmp;
			}
		}
		list.add(result);
		this.addServiceRegistration(result);
		
		Logger logger = this.getLogger(assembly);
		if (logger != null){
			if (logger.isDebugEnabled()){
				logger.debug("register service success:" + 
					this.getObjectClassAndName(result.getReference()) + ".");
			}	
		}
		
		//事件处理
		ServiceEvent event = new ServiceEvent(result.getReference(), ServiceEvent.REGISTED);
		
		this.getServiceListenerManagement().maybeToSetExecutorService(result, assembly);
		this.getServiceListenerManagement().publishServiceEvent(event);
		
		return result;
	} 
		
	/**
	 * 取消服务注册
	 * @param registration 服务注册凭据
	 */
	void doUnregister(ExportServiceRegistrationImpl<?> registration){
		Assembly assembly = registration.getAssembly();
		List<ExportServiceRegistration<?>> list = this.registrations.get(assembly);
		if (list != null){
			if (list.contains(registration)){
				ServiceEvent event = new ServiceEvent(registration.getReference(), ServiceEvent.UNREGISTERING);
				this.getServiceListenerManagement().publishServiceEvent(event);
				
				this.removeServiceRegistration(registration);
				list.remove(registration);
				
				Logger logger = this.getLogger(assembly);
				if (logger != null){
					if (logger.isDebugEnabled()){
						logger.debug("unregister service success: " + 
							this.getObjectClassAndName(registration.getReference()) + ".");
					}	
				}
			}
		}
	}
	
	/**
	 * 获取ObjectClass名称
	 * @param classes 
	 * @return {@link String}
	 */
	protected String getObjectClassAndName(ExportServiceReference<?> reference){
		Class<?>[] classes = ExportServiceUtils.getObjectClass(reference);
		String[] names = ExportServiceUtils.getServiceName(reference);
		
		StringBuilder builder =
				new StringBuilder();
		builder.append("objectClass")
			.append("=").append("[");
		int size = classes.length;
		for (int i=0; i<size; i++){
			
			builder.append(classes[i].getName());
			if (i!=size-1){
				builder.append(",");
			}
		}
		builder.append("]");
		
		if (names.length != 0){
			builder.append(", ").append(Constants.SERVICE_NAME)
				.append("=").append("[");
			size = names.length;
			for (int i=0; i<size; i++){
				builder.append(names[i]);
				if (i!=size-1){
					builder.append(",");
				}
			}
			builder.append("]");
		}
		
		
		return builder.toString();
	}
	
	
	/**
	 * 获取日志记录器
	 * @return {@link Logger}
	 */
	protected Logger getLogger(Assembly assembly){
		return LoggerReference.adaptFrom(assembly);
	}
		
	/**
	 * 添加服务注册凭据
	 * @param registration
	 */
	protected abstract <T> void addServiceRegistration(
			ExportServiceRegistrationImpl<T> registration);
	
	/**
	 * 移除服务注册凭据
	 * @param registration
	 */
	protected abstract <T> void removeServiceRegistration(
			ExportServiceRegistrationImpl<T> registration);
	
	/**
	 * 服务事件监听管理器
	 * @return {@link ServieListenerManagement}
	 */
	protected abstract ServiceListenerManagement getServiceListenerManagement();
	
}
