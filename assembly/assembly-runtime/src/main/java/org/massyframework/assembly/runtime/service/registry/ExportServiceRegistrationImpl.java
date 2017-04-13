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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyAware;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.ServiceFactory;
import org.massyframework.assembly.base.ExportServiceRegistration;
import org.massyframework.assembly.util.Asserts;

/**
 * 实现{@link ExportServiceRegistration}
 */
final class ExportServiceRegistrationImpl<S> implements ExportServiceRegistration<S>,
	ServiceFactory<S>, Comparable<ExportServiceRegistrationImpl<?>>{

	private ExportServiceRegistryFactory registryFactory;
	private final Assembly assembly;
	private final Map<String, Object> props;
	private ExportServiceReference<S> reference;
	
	private final Object svcObj;
	private final ServiceFactory<S> factory;
	
	/**
	 * 构造方法
	 * @param serviceRegistry
	 * @param classes
	 * @param svcObj
	 * @param props
	 * @param assembly
	 */
	@SuppressWarnings("unchecked")
	public ExportServiceRegistrationImpl(
			ExportServiceRegistryFactory registryFactory,
			Class<?>[] classes, 
			Object svcObj,
			Map<String,Object> props, Assembly assembly) {
		Asserts.notNull(registryFactory, "registryFactory cannot be null.");
		Asserts.notEmpty(classes, "classes cannot be empty.");
		Asserts.notNull(svcObj, "svcObject cannot be null.");
		Asserts.notNull(assembly, "assembly cannot be null.");
		
		this.registryFactory = registryFactory;
		this.assembly = assembly;
		this.factory = svcObj instanceof ServiceFactory ?
				(ServiceFactory<S>)svcObj :
					null;
		this.svcObj = svcObj;
		
		
		Map<String,Object> map = props == null ?
				new HashMap<String, Object>() :
					new HashMap<String, Object>(props);
				
		map.put(Constants.SERVICE_ID, ServiceIdFactory.genericId());
		map.put(Constants.OBJECT_CLASS, classes);
		map.put(Constants.ASSEMBLY_SYMBOLICNAME, this.assembly.getSymbolicName());
		this.props = Collections.unmodifiableMap(map);
		
		AssemblyAware.maybeToBind(this.svcObj, assembly);
		AssemblyAware.maybeToBind(this.factory, assembly);
	}

	@Override
	public void unregister() {
		this.getExportServiceRegistryFactory().doUnregister(this);		
	}

	
	@Override
	public ExportServiceReference<S> getReference() {
		if (this.reference == null){
			synchronized(this){
				if (this.reference == null){
					this.reference = new ServiceReferenceImpl();
				}
			}
		}
		return this.reference;
	}
	
	/* (non-Javadoc)
	 * @see org.smarabbit.massy.service.ServiceFactory#getService(org.smarabbit.massy.Assembly)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public S getService(Assembly assembly) {
		if (this.factory != null){
			return this.factory.getService(assembly);
		}
		return (S)this.svcObj;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ExportServiceRegistrationImpl<?> o) {
		
		Integer ranking = this.getReference().getProperty(
				Constants.SERVICE_RANKING, Integer.class);
		if (ranking == null){
			ranking = new Integer(0);
		}
		Integer otherRanking = o.getReference().getProperty(
				Constants.SERVICE_RANKING, Integer.class);
		if (otherRanking == null){
			otherRanking = new Integer(0);
		}
		int result = ranking.compareTo(otherRanking);
		if (result == 0){
			Long id = this.getReference().getProperty(
					Constants.SERVICE_ID, Long.class);
			Long otherId = o.getReference().getProperty(
					Constants.SERVICE_ID, Long.class);
			result = otherId.compareTo(id);
		}
		return result;
	}

	/**
	 * 服务注册器工厂
	 * @return {@link ExportServiceRegistryFactory}
	 */
	public ExportServiceRegistryFactory getExportServiceRegistryFactory(){
		return this.registryFactory;
	}
	
	/**
	 * 装配件
	 * @return
	 */
	public Assembly getAssembly(){
		return this.assembly;
	}

	/**
	 * 服务引用实现
	 * @author huangkaihui
	 *
	 */
	private class ServiceReferenceImpl implements ExportServiceReference<S> {

		@Override
		public long getServiceId() {
			Long value = (Long)props.get(Constants.SERVICE_ID);
			return value.longValue();
		}


		@Override
		public Assembly getAssembly() {
			return assembly;
		}

		@Override
		public Object getProperty(String key) {
			if (key == null) return null;
			return props.get(key);
		}

		@Override
		public <T>  T getProperty(String key, Class<T> propType) {
			Asserts.notNull(propType, "propType cannot be null.");
			
			Object value = this.getProperty(key);
			return value == null ? 
					null :
						propType.cast(value);
		}

		@Override
		public List<String> getPropertyKeys() {
			return new ArrayList<String>(props.keySet());
		}
		
		
	}
}
