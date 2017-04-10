/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle.support;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceListener;
import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.Filter;
import org.massyframework.assembly.ServiceEvent;
import org.massyframework.assembly.base.handle.DependencyServiceResource;
import org.massyframework.assembly.base.handle.DependencyServiceResourceMatchHandler;
import org.massyframework.assembly.base.handle.ExportServiceRepositoryReference;
import org.massyframework.assembly.base.handle.LifecycleProcessHandler;
import org.massyframework.assembly.util.Asserts;
import org.slf4j.Logger;

/**
 * 依赖服务资源匹配处理器， 实现{@link DependencyServiceResourceMatchHandler}接口<br>
 * 依赖服务资源匹配处理器，需要在解析完成后，提交对所需依赖服务的监听，并对监听的服务事件执行匹配检查。
 */
public class DependencyServiceResourceMatcher extends DependencyServiceResourceRepository
		implements DependencyServiceResourceMatchHandler {

	//未匹配资源
	private final Set<DependencyServiceResource> unmatchs =
			new HashSet<DependencyServiceResource>();
	//已匹配资源
	private final Set<DependencyServiceResource> matcheds =
			new HashSet<DependencyServiceResource>();
	//已匹配关系
	private final Map<DependencyServiceResource, ExportServiceReference<?>> matchedMap =
			new HashMap<DependencyServiceResource, ExportServiceReference<?>>();
	
	private volatile DependencyServiceListener serviceListener;
	/**
	 * 
	 */
	public DependencyServiceResourceMatcher() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.DependencyServiceResourceMatchHandler#getMatchedServiceReference(org.massyframework.assembly.base.handle.DependencyServiceResource)
	 */
	@Override
	public synchronized ExportServiceReference<?> getMatchedServiceReference(DependencyServiceResource resource) {
		return this.matchedMap.get(resource);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.DependencyServiceResourceMatchHandler#getUnmatchDependencyServiceResources()
	 */
	@Override
	public synchronized Collection<DependencyServiceResource> getUnmatchDependencyServiceResources() {
		return Collections.unmodifiableCollection(this.unmatchs);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.DependencyServiceResourceMatchHandler#isAllMatched()
	 */
	@Override
	public synchronized boolean isAllMatched() {
		return this.unmatchs.isEmpty();
	}

	@Override
	protected synchronized void onResolved() {
		super.onResolved();
		this.unmatchs.addAll(this.getDependencyServiceResources());
		this.addServiceListener();
	}
	
	@Override
	protected synchronized void onUninstall() {
		super.onUninstall();
		this.removeServiceListener();
	}

	/**
	 * 添加服务监听器
	 */
	protected void addServiceListener(){
		if (this.serviceListener == null){
			ExportServiceRepository serviceRepository =
					ExportServiceRepositoryReference.adaptFrom(this.getAssembly());
			
			this.serviceListener = new DependencyServiceListener(serviceRepository);
			serviceRepository.addListener(this.serviceListener, this.serviceListener.getFilter());
			
			List<ExportServiceReference<?>> references =
					serviceRepository.getServiceReferences(this.serviceListener.getFilter());
			boolean isMatched = false;
			for (ExportServiceReference<?> reference: references){
				if (this.serviceListener.maybeToMatch(reference) != null){
					isMatched = true;
				}
			}
			
			if (isMatched){
				this.startAssembly();
			}
		}
	}
	
	/**
	 * 移除服务监听器
	 */
	protected void removeServiceListener(){
		if (this.serviceListener != null){
			ExportServiceListener listener = this.serviceListener;
			this.serviceListener = null;
			ExportServiceRepository serviceRepository =
					ExportServiceRepositoryReference.adaptFrom(this.getAssembly());
			serviceRepository.removeListener(listener);
		}
	}
	
	/**
	 * 尝试进行匹配
	 * @param resource
	 * @param reference
	 */
	protected synchronized boolean doMatch(DependencyServiceResource resource, ExportServiceReference<?> reference){
		if (this.matchedMap.putIfAbsent(resource, reference) == null){
			this.matcheds.add(resource);
			this.unmatchs.remove(resource);
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否匹配
	 * @param reference 依赖服务资源
	 * @return <code>已建立匹配关系</code>,<code>未建立匹配关系</code>
	 */
	protected synchronized DependencyServiceResource getMatchedResource(ExportServiceReference<?> reference){
		if (this.matchedMap.containsValue(reference)){
			for (Entry<DependencyServiceResource, ExportServiceReference<?>> entry: this.matchedMap.entrySet()){
				if (entry.getValue() == reference){
					return entry.getKey();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 移除匹配关系
	 */
	protected synchronized void doUnmatch(DependencyServiceResource resource){
		this.matchedMap.remove(resource);
		this.matcheds.remove(resource);
		this.unmatchs.add(resource);
	}
	
	/**
	 * 依赖资源是否已经匹配
	 * @param resource 依赖服务资源
	 * @return <code>true</code>
	 */
	protected synchronized boolean resouceIsMatched(DependencyServiceResource resource){
		return this.matcheds.contains(resource);
	}
	
	/**
	 * 启动装配件
	 */
	protected void startAssembly(){
		if (this.isAllMatched()){
			LifecycleProcessHandler handler =
					this.getHandler(LifecycleProcessHandler.class);
			try{
				handler.start();
			}catch(Exception e){
				
			}
		}
	}
	
	/**
	 * 停止装配件
	 */
	protected void stopAssembly(){
		LifecycleProcessHandler handler =
				this.getHandler(LifecycleProcessHandler.class);
		try{
			handler.stop();
		}catch(Exception e){
			
		}
	}
	/**
	 * 依赖服务监听
	 */
	private class DependencyServiceListener implements ExportServiceListener {
		
		
		private final Filter filter;
		private Map<DependencyServiceResource, Filter> map =
				new HashMap<DependencyServiceResource, Filter>();
		
		
		public DependencyServiceListener(ExportServiceRepository serviceRepository){
			List<DependencyServiceResource> resources =
					getDependencyServiceResources();
			Set<Class<?>> requiredTypes = new HashSet<Class<?>>();
			for (DependencyServiceResource resource: resources){
				this.map.put(resource, serviceRepository.createFilter(resource.getFilterString()));
				requiredTypes.add(resource.getServiceType());
			}
			this.filter = new DependencyServiceFilter(requiredTypes);
		}

		@Override
		public void onChanged(ServiceEvent event) {
			ExportServiceReference<?> reference = event.getServiceReference();
			if (event.getType() == ServiceEvent.REGISTED){
				//注册完成
				if (this.maybeToMatch(reference) != null){
					startAssembly();
				}
			}else{
				//准备注销
				DependencyServiceResource resource =
						getMatchedResource(reference);
				if (resource != null){
					stopAssembly();
					doUnmatch(resource);
				}
			}
		}
		
		/**
		 * 尝试匹配,并返回匹配成功的依赖服务资源
		 * @param reference 输出服务引用
		 * @return {@link DependencyServiceResource}，匹配成功则返回匹配的依赖服务资源，如不成功返回null.
		 */
		protected DependencyServiceResource maybeToMatch(ExportServiceReference<?> reference){
			
			for (Entry<DependencyServiceResource, Filter> entry: this.map.entrySet()){
				DependencyServiceResource resource = entry.getKey();
				if (!resouceIsMatched(resource)){
					if (entry.getValue().match(reference)){
						if (doMatch(resource, reference)){
							return entry.getKey();
						}
					}
				}
			}
			return null;
		}
				
		public Filter getFilter(){
			return this.filter;
		}
	}
	
	/**
	 * 服务过滤器
	 */
	private class DependencyServiceFilter implements Filter {
		
		private Set<Class<?>> requiredTypes;
		
		public DependencyServiceFilter(Set<Class<?>> requiredTypes){
			Asserts.notNull(requiredTypes, "requiredTypes cannot be null.");
			this.requiredTypes = requiredTypes;
		}

		@Override
		public boolean match(Map<String, Object> props) {
			try{
				Class<?>[] serviceTypes = this.getServiceTypes(props);
				for (Class<?> serviceType: serviceTypes){
					if (requiredTypes.contains(serviceType)){
						return true;
					}
				}
			}catch(Exception e){
				Logger logger = getLogger();
				if (logger != null){
					if (logger.isErrorEnabled()){
						logger.error("check export service types failed", e);
					}
				}
			}
			
			return false;
		}

		@Override
		public boolean match(ExportServiceReference<?> reference) {
			try{
				Class<?>[] serviceTypes = this.getServiceTypes(reference);
				for (Class<?> serviceType: serviceTypes){
					if (requiredTypes.contains(serviceType)){
						return true;
					}
				}
			}catch(Exception e){
				Logger logger = getLogger();
				if (logger != null){
					if (logger.isErrorEnabled()){
						logger.error("check export service types failed", e);
					}
				}
			}
			
			return false;
		}
		
		private Class<?>[] getServiceTypes(Map<String, Object> props){
			Object value = props.get(Constants.SERVICE_EXPORTTYPE);
			return (Class<?>[])value;
		}
		
		private Class<?>[] getServiceTypes(ExportServiceReference<?> reference){
			Object value = reference.getProperty(Constants.SERVICE_EXPORTTYPE);
			return (Class<?>[])value;
		}
	}
}
