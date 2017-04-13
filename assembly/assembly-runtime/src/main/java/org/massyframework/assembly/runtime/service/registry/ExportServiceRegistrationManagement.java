/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime.service.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.Filter;
import org.massyframework.assembly.util.CollectionUtils;

/**
 * 提供某类服务注册凭据的管理和服务引用查询
 */
final class ExportServiceRegistrationManagement {

	private CopyOnWriteArrayList<ExportServiceRegistrationImpl<?>> registrations =
			new CopyOnWriteArrayList<ExportServiceRegistrationImpl<?>>();
		
	/**
	 * 
	 */
	public ExportServiceRegistrationManagement() {
		
	}

	/**
	 * 获取符合过滤条件的服务引用
	 * @param clazz 类型
	 * @param filter 过滤器
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public <T> List<ExportServiceReference<T>> getServiceReferences(Class<T> clazz, Filter filter){
		List<ExportServiceReference<T>> result =
				new ArrayList<ExportServiceReference<T>>();
		
		for (ExportServiceRegistrationImpl<?> registration: this.registrations){
			ExportServiceReference<?> reference = registration.getReference();
			if (filter.match(reference)){
				result.add((ExportServiceReference<T>)reference);
			}
		}
		
		return result;
	}
	
	/**
	 * 获取符合过滤条件的首个服务应用
	 * @param clazz 类型
	 * @param filter 过滤器
	 * @return {@link ExportServiceReference}
	 */
	@SuppressWarnings("unchecked")
	public <T> ExportServiceReference<T> getServiceReference(Class<T> clazz, Filter filter){
		for (ExportServiceRegistrationImpl<?> registration: this.registrations){
			ExportServiceReference<?> reference = registration.getReference();
			if (filter.match(reference)){
				return (ExportServiceReference<T>)reference;
			}
		}
		
		return null;
	}
		
	/**
	 * 添加服务注册凭据
	 * @param registration {@link ServiceRegistration}
	 */
	public void addServiceRegistration(ExportServiceRegistrationImpl<?> registration){
		if (registration != null){
			CollectionUtils.addInOrder(this.registrations, registration);
		}
	}
	
	/**
	 * 移除服务注册凭据
	 * @param registration {@link ServiceRegistration}
	 */
	public void removeServiceRegistration(ExportServiceRegistrationImpl<?> registration){
		if (registration != null){
			this.registrations.remove(registration);
		}
	}

}
