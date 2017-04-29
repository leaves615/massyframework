/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime.service.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceListener;
import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.Filter;
import org.massyframework.assembly.ServiceFactory;
import org.massyframework.assembly.ServiceNotFoundException;
import org.massyframework.assembly.base.ExportServiceRegistration;
import org.massyframework.assembly.base.ExportServiceRegistry;
import org.massyframework.assembly.util.Asserts;

/**
 * 输出服务仓储管理器
 */
final class ExportServiceRepositoryManagement  extends ExportServiceRegistryFactory{

	private final ServiceListenerManagement listenerManagement;
	
	private final Map<Assembly, ExportServiceRepositoryImpl> repositoryMap =
			new ConcurrentHashMap<Assembly, ExportServiceRepositoryImpl>();
	
	private Map<Long, ExportServiceRegistration<?>> idMap =
			new ConcurrentHashMap<Long, ExportServiceRegistration<?>>();
	private Map<Class<?>, ExportServiceRegistrationManagement> managementMap =
			new ConcurrentHashMap<Class<?>, ExportServiceRegistrationManagement>();

	/**
	 * 构造方法
	 */
	public ExportServiceRepositoryManagement() {
		this.listenerManagement = new ServiceListenerManagement();
	}
	
	

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ServiceFactory#getService(org.massyframework.assembly.Assembly)
	 */
	@Override
	public ExportServiceRegistry getService(Assembly assembly) {
		ExportServiceRepositoryImpl result = this.repositoryMap.get(assembly);
		
		if (result == null){
			ExportServiceRepositoryImpl tmp = new ExportServiceRepositoryImpl(this, assembly);
			result = this.repositoryMap.putIfAbsent(assembly, tmp);
			if (result == null){
				result = tmp;
			}
		}
		return result;
	}
	
	/**
	 * 获取指定装配件的输出服务仓储
	 * @param assembly 装配件
	 * @return {@link ExportServiceRepository}
	 */
	public ExportServiceRepository getExportServiceRepository(Assembly assembly){
		ExportServiceRepositoryImpl result =
				this.repositoryMap.get(assembly);
		if (result == null){
			ExportServiceRepositoryImpl tmp = new ExportServiceRepositoryImpl(this, assembly);
			result = this.repositoryMap.putIfAbsent(assembly, tmp);
			if (result == null){
				result = tmp;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.runtime.service.registry.ExportServiceRegistryFactory#addServiceRegistration(org.massyframework.assembly.runtime.service.registry.ExportServiceRegistrationImpl)
	 */
	@Override
	protected <T> void addServiceRegistration(ExportServiceRegistrationImpl<T> registration) {
		Long serviceId = registration.getReference()
				.getProperty(Constants.SERVICE_ID, Long.class);
		this.idMap.put(serviceId, registration);
		
		Class<?>[] objectClass = registration.getReference()
				.getProperty(Constants.OBJECT_CLASS, Class[].class);
		for (Class<?> clazz: objectClass){
			ExportServiceRegistrationManagement management =
					this.getOrCreateServiceRegistrationManagement(clazz);
			management.addServiceRegistration(registration);
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.runtime.service.registry.ExportServiceRegistryFactory#removeServiceRegistration(org.massyframework.assembly.runtime.service.registry.ExportServiceRegistrationImpl)
	 */
	@Override
	protected <T> void removeServiceRegistration(ExportServiceRegistrationImpl<T> registration) {
		Long serviceId = registration.getReference()
				.getProperty(Constants.SERVICE_ID, Long.class);
		if (this.idMap.remove(serviceId)!= null){
			Class<?>[] objectClass = registration.getReference()
					.getProperty(Constants.OBJECT_CLASS, Class[].class);
			for (Class<?> clazz: objectClass){
				ExportServiceRegistrationManagement management =
						this.managementMap.get(clazz);
				if (management != null){
					management.removeServiceRegistration(registration);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.runtime.service.registry.ExportServiceRegistryFactory#getServiceListenerManagement()
	 */
	@Override
	protected ServiceListenerManagement getServiceListenerManagement() {
		return this.listenerManagement;
	}
	
	/**
	 * 添加事件监听器
	 * @param listener
	 * @param filter
	 */
	void addListener(ExportServiceListener listener, Filter filter, Assembly assembly) {
		this.getServiceListenerManagement()
			.addListener(listener, filter, assembly);
	}	
	
	/**
	 * 移除事件监听器
	 * @param listener 事件监听器
	 * @param assembly 所属的装配件
	 */
	void removeListener(ExportServiceListener listener, Assembly assembly) {
		this.getServiceListenerManagement().removeListener(listener, assembly);
	}
	
	boolean containsService(Class<?> serviceType){
		if (serviceType == null) return false;
		return this.managementMap.get(serviceType) != null;
	}
	
	boolean containsService(String className){
		if (className == null) return false;
		for (Class<?> serviceType: this.managementMap.keySet()){
			if (className.equals(serviceType.getName())){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 创建筛选器
	 * @param filterString 筛选条件，可以为null
	 * @return {@link Filter}
	 */
	Filter createFilter(String filterString) {
		return filterString == null?
				EmptyFilter.getDefault() :
					LDAPFilterFactory.newInstance(filterString);
	}
	
	/**
	 * 按服务类型和过滤条件，查找首个符合要求的服务引用
	 * @param clazz 服务类型
	 * @param filter 过滤器
	 * @return {@link ExportServiceReference}输出服务引用
	 */
	<S> ExportServiceReference<S> findServiceReference(Class<S> clazz, Filter filter) {
		Asserts.notNull(clazz, "clazz cannot be null.");
		Asserts.notNull(filter, "filter cannot be null.");
		
		ExportServiceRegistrationManagement management =
				this.managementMap.get(clazz);
		if (management != null){
			return management.getServiceReference(clazz, filter);
		}
		return null;
	}
	
	/**
	 * 使用输出服务引用获取服务实例
	 * @param reference 输出服务引用
	 * @param assembly 需要使用服务的装配件
	 * @return {@link S}
	 */
	<S> S getService(ExportServiceReference<S> reference, Assembly assembly) throws ServiceNotFoundException{
		Long serviceId = reference.getProperty(Constants.SERVICE_ID, Long.class);
		ExportServiceRegistration<?> registration =
				this.idMap.get(serviceId);
		S result = this.retrieveServiceWith(registration, assembly);
		if (result == null){
			throw new ServiceNotFoundException("service not found: reference=" + reference + ".");
		}
		return result;
	}
	
	/**
	 * 按服务类型获取所有输出服务实例
	 * @param serviceType 服务类型
	 * @return {@link List}
	 */
	<S> List<S> getServices(Class<S> serviceType, Assembly assembly){
		Asserts.notNull(serviceType, "serviceType cannot be null.");
		
		
		ExportServiceRegistrationManagement management =
				this.managementMap.get(serviceType);
		if (management != null){
			return management.getServices(assembly);
		}
		return new ArrayList<S>();
	}
	
	/**
	 * 使用输出服务引用获取多个服务实例
	 * @param references 输出服务引用集合
	 * @return {@link List}
	 * @throws ServiceNotFoundException 服务未找到时抛出的例外
	 */
	List<Object> getServices(Collection<ExportServiceReference<?>> references, Assembly assembly) throws ServiceNotFoundException {
		List<Object> result = new ArrayList<Object>();
		if (references != null){
			for (ExportServiceReference<?> reference: references){
				result.add(this.getService(reference, assembly));
			}
		}
		return result;
	}
	

	/**
	 * 按服务类型和筛选条件获取满足要求的所有服务引用
	 * @param clazz 服务类型
	 * @param filter 筛选器
	 * @return {@link List}
	 */
	<S> List<ExportServiceReference<S>> getServiceReferences(Class<S> clazz, Filter filter) {
		Asserts.notNull(clazz, "clazz cannot be null.");
		Asserts.notNull(filter, "filter cannot be null.");
		
		ExportServiceRegistrationManagement management =
				this.managementMap.get(clazz);
		if (management != null){
			return management.getServiceReferences(clazz, filter);
		}
		return new ArrayList<ExportServiceReference<S>>();
	}
	
	/**
	 * 获取或者创建指定类型的服务注册凭据管理器
	 * @param clazz 类型
	 * @return {@link ExportServiceRegistrationManagement}
	 */
	private <T> ExportServiceRegistrationManagement getOrCreateServiceRegistrationManagement(Class<T> clazz){
		ExportServiceRegistrationManagement result = this.managementMap.get(clazz);
		if (result == null){
			ExportServiceRegistrationManagement tmp = new ExportServiceRegistrationManagement();
			result = this.managementMap.putIfAbsent(clazz, tmp);
			if (result == null){
				result = tmp;
			}
		}
		
		return result;
	}
	
	/**
	 * 从ServiceRegistration中取回服务
	 * @param registration 服务注册凭据
	 * @param assembly 需要服务的装配件
	 * @return {@link ExportServiceRegistration}, 可能返回null.
	 */
	@SuppressWarnings("unchecked")
	protected <S> S retrieveServiceWith(ExportServiceRegistration<?> registration, Assembly assembly){
		Asserts.notNull(assembly, "assembly cannot be null.");
		if (registration == null){
			return null;
		}
		
		if (registration instanceof ServiceFactory){
			S result = ((ServiceFactory<S>)registration).getService(assembly);			
			return result;
		}
		
		return null;
	}
}
