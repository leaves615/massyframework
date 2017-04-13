/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyEvent;
import org.massyframework.assembly.AssemblyListener;
import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.NameExistsException;
import org.massyframework.assembly.base.ExportServiceRegistry;
import org.massyframework.assembly.spec.Specification;
import org.massyframework.assembly.util.Asserts;
import org.slf4j.Logger;

/**
 * 抽象的装配键注册器
 */
abstract class AbstractAssemblyRegistry implements AssemblyRegistry {

	private final Assembly assembly;
	private final ExportServiceRepository serviceRepository;
	private List<AssemblyListener> listeners =
			new CopyOnWriteArrayList<AssemblyListener>();
	
	private Map<String, AssemblyRegistration> assemblyMap =
			new ConcurrentHashMap<String, AssemblyRegistration>();
	private Map<Long, AssemblyRegistration> idMap =
			new ConcurrentHashMap<Long, AssemblyRegistration>();
	private volatile ExecutorService executorService;
	
	/**
	 * 构造方法
	 * @param framework
	 * @param serviceRegistry
	 */
	public AbstractAssemblyRegistry(Assembly assembly, ExportServiceRepository serviceRepository) {
		Asserts.notNull(assembly, "assembly cannot be null.");
		Asserts.notNull(serviceRepository, "serviceRepository cannot be null.");
		
		this.assembly = assembly;
		RegistrationImpl registration = new RegistrationImpl(assembly);
		String symbolicName = assembly.getSymbolicName();
		this.assemblyMap.put(symbolicName, registration);
		this.idMap.put(assembly.getAssemblyId(), registration);
		this.serviceRepository = serviceRepository;
	}
	

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.runtime.AssemblyRegistry#addEventListener(org.massyframework.assembly.AssemblyListener)
	 */
	@Override
	public void addListener(AssemblyListener listener) {
		if (listener != null){
			this.listeners.add(listener);
		}
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.framework.AssemblyRegistry#filter(org.smarabbit.massy.spec.Specification)
	 */
	@Override
	public Assembly filter(Specification<Assembly> spec) {
		Asserts.notNull(spec, "spec cannot be null.");
		for (AssemblyRegistration registration:  this.assemblyMap.values()){
			Assembly assembly = this.getAsssmblyFromRegistration(registration);
			if (spec.isSatisfyBy(assembly)){
				return assembly;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.framework.AssemblyRegistry#filterAll(org.smarabbit.massy.spec.Specification)
	 */
	@Override
	public List<Assembly> filterAll(Specification<Assembly> spec) {
		Asserts.notNull(spec, "spec cannot be null.");
		List<Assembly> result = new ArrayList<Assembly>();
		for (AssemblyRegistration registration:  this.assemblyMap.values()){
			Assembly assembly = this.getAsssmblyFromRegistration(registration);
			if (spec.isSatisfyBy(assembly)){
				result.add(assembly);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.framework.AssemblyRegistry#getAssembly(long)
	 */
	@Override
	public Assembly findAssembly(long assemblyId){
		AssemblyRegistration registration = this.idMap.get(assemblyId);
		return this.getAsssmblyFromRegistration(registration);
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.framework.AssemblyRegistry#findAssembly(java.lang.String)
	 */
	@Override
	public Assembly findAssembly(String symbolicName) {
		if (symbolicName == null) return null;
		
		AssemblyRegistration registration = this.assemblyMap.get(symbolicName);
		return this.getAsssmblyFromRegistration(registration);
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.framework.AssemblyRegistry#getAssemblies()
	 */
	@Override
	public List<Assembly> getAssemblies() {
		List<Assembly> result = new ArrayList<Assembly>();
		for (AssemblyRegistration registration:  this.assemblyMap.values()){
			result.add(registration.getAssembly());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.runtime.AssemblyRegistry#register(org.massyframework.assembly.AssemblyResource)
	 */
	@Override
	public AssemblyRegistration installAndRegister(AssemblyResource resource) throws NameExistsException, Exception {
		Asserts.notNull(resource, "resource cannot be null.");
		
		AssemblyRegistrationImpl result =
				new AssemblyRegistrationImpl(this, resource);
		result.init();
		
		String symbolicName = result.getAssembly().getSymbolicName();
		if (this.assemblyMap.put(symbolicName, result) != null){
			throw new NameExistsException(symbolicName);
		}
		this.idMap.put(result.getAssembly().getAssemblyId(), result);

		Logger logger = LoggerReference.adaptFrom(assembly);
		if (logger != null){
			logger.debug("install assembly success: assembly=" + result.getAssembly());
		}
		result.onRegistComplete();
		
		this.notifyRegisted(result.getAssembly());
		return result;
	}


	/**
	 * 卸载装配件
	 * @param registration
	 */
	protected void doUnregister(AssemblyRegistrationImpl registration){
		if (registration == this.assemblyMap.get(registration.getAssembly().getSymbolicName())){
						
			registration.onUnregistering();
			this.assemblyMap.remove(registration.getAssembly().getSymbolicName());
			this.idMap.remove(registration.getAssembly().getAssemblyId());
			
			Logger logger = LoggerReference.adaptFrom(this.assembly);
			if (logger != null){
				if (logger.isDebugEnabled()){
					logger.debug("uninstall assembly success: assembly=" + registration.getAssembly() + ".");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.runtime.AssemblyRegistry#removeEventListener(org.massyframework.assembly.AssemblyListener)
	 */
	@Override
	public void removeListener(AssemblyListener listener) {
		if (listener != null){
			this.listeners.remove(listener);
		}
	}

	/**
	 * 获取输出服务仓储
	 * @return {@link ExportServiceRepository}
	 */
	public ExportServiceRepository getExportServiceRepository(){
		return this.serviceRepository;
	}
	
	/**
	 * 通知装配件已注册
	 * @param assembly
	 */
	protected void notifyRegisted(Assembly assembly){
		AssemblyEvent event =
				new AssemblyEvent(assembly, AssemblyEvent.INSTALLED);
		ExecutorService executor =
				this.getExecutorService();
		if (executor != null){
			executor.execute(new Task(event));
		}else{
			new Thread(new Task(event)).start();
		}
	}
	
	
	
	private void notifyAssesemblyEvent(AssemblyEvent event){
		Logger logger = LoggerReference.adaptFrom(this.assembly);
		for (AssemblyListener listener: this.listeners){
			try{
				listener.onChanged(event);
			}catch(Exception e){
				if (logger!= null){
					if (logger.isErrorEnabled()){
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
	}
	
	/**
	 * 获取ExecutorService
	 * @return
	 */
	private ExecutorService getExecutorService(){
		if (this.executorService == null){
			this.executorService = 
					this.getExportServiceRepository().findService(ExecutorService.class);
		}
		return this.executorService;
	}


	/**
	 * 从Registration中获取装配件
	 * @param registration 装配件注册凭据
	 * @return {@link Assembly}, 可能返回null.
	 */
	protected Assembly getAsssmblyFromRegistration(AssemblyRegistration registration){
		if (registration == null) {
			return null;
		}
		return registration.getAssembly();
	}

	/**
	 * 服务注册器
	 * @return
	 */
	ExportServiceRegistry getExportServiceRegistry(){
		return this.getExportServiceRepository()
				.findService(ExportServiceRegistry.class);
	}
	
	/**
	 * 注册凭据实现
	 */
	private class RegistrationImpl implements AssemblyRegistration {
		
		private Assembly assembly;
		
		public RegistrationImpl(Assembly assembly){
			this.assembly = assembly;
		}

		@Override
		public void unregister() {
		}

		@Override
		public Assembly getAssembly() {
			return this.assembly;
		}
		
	}
	
	/**
	 * 任务
	 */
	private class Task implements Runnable {
		
		private AssemblyEvent event;
		
		public Task(AssemblyEvent event){
			this.event = event;
		}

		@Override
		public void run() {
			notifyAssesemblyEvent(event);
		}
		
	}
}
