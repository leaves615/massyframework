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
package org.massyframework.assembly.base.handle.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.AssemblyStatus;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.base.ExportServiceRegistration;
import org.massyframework.assembly.base.ExportServiceRegistry;
import org.massyframework.assembly.base.handle.ExportServiceResource;
import org.massyframework.assembly.base.handle.ExportServiceResourceHandler;
import org.massyframework.assembly.base.handle.LifecycleProcessHandler;
import org.massyframework.assembly.util.Asserts;

/**
 * 输出服务资源仓储，实现{@link ExportServiceResourceHandler}接口
 */
public class ExportServiceResourceRepository extends AbstractHandler 
	implements ExportServiceResourceHandler {

	private List<ExportServiceResource> resources =
			new ArrayList<ExportServiceResource>();
	private volatile EventAdapter eventAdapter;
	private volatile boolean resolved = false;
	
	private volatile List<ExportServiceRegistration<?>> registrations;
	
	/**
	 * 
	 */
	public ExportServiceResourceRepository() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ExportServiceResourceHandler#add(org.massyframework.assembly.base.handle.ExportServiceResource)
	 */
	@Override
	public void add(ExportServiceResource resource) {
		this.checkResolved();
		this.checkResource(resource);
		this.doAdd(resource);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ExportServiceResourceHandler#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<ExportServiceResource> resources) {
		this.checkResolved();
		for (ExportServiceResource resource: resources){
			this.checkResource(resource);
		}
		this.doAddAll(resources);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ExportServiceResourceHandler#getExportServiceResources()
	 */
	@Override
	public synchronized List<ExportServiceResource> getExportServiceResources() {
		return Collections.unmodifiableList(this.resources);
	}
	
	/**
	 * 是否已解析完成检查
	 */
	private void checkResolved(){
		if (this.isResolved()){
			throw new IllegalStateException("assembly is resolved, cannot add dependency resource.");
		}
	}
	
	/**
	 * 检查ExportServiceResource的有效性
	 * @param resource
	 */
	private void checkResource(ExportServiceResource resource){
		Asserts.notNull(resource.getExportTypes(), "exportTypes of export resource cannot be null.");
	}

	/**
	 * 添加输出服务资源
	 * @param resource
	 */
	private synchronized void doAdd(ExportServiceResource resource){
		this.resources.add(resource);
	}
	
	/**
	 * 批量添加输出服务资源
	 * @param resources
	 */
	private synchronized void doAddAll(Collection<ExportServiceResource> resources){
		this.resources.addAll(resources);
	}
	
	/**
	 * 判断是否解析完成
	 * @return <code>true</code>解析完成，<code>false</code>解析未完成
	 */
	public synchronized boolean isResolved(){
		return this.resolved; 
	}
	
	/**
	 * 设置解析完成
	 */
	private synchronized void setResolved(){
		this.resolved = true;
		this.onResolved();
	}
	
	/**
	 * 解析完成
	 */
	protected void onResolved(){
	}
	
	/**
	 * 初始化
	 */
	@Override
	protected void init() {
		super.init();
		if (this.getAssembly().getAssemblyStatus() == AssemblyStatus.RESOLVE){
			if (this.eventAdapter == null){
				this.eventAdapter = new EventAdapter();
				LifecycleProcessHandler handler =
						this.getHandler(LifecycleProcessHandler.class);
				handler.addListener(this.eventAdapter);
			}
		}else{
			this.setResolved();
		}
	}

	/**
	 * 析钩
	 */
	@Override
	public void destroy() {
		if (this.eventAdapter != null){
			LifecycleProcessHandler handler =
					this.getHandler(LifecycleProcessHandler.class);
			handler.removeListener(this.eventAdapter);
			this.eventAdapter = null;
		}
		super.destroy();
	}
	
	/**
	 * 输出服务
	 */
	protected synchronized void exportServices(){
		if (this.registrations == null){
			this.registrations = new ArrayList<ExportServiceRegistration<?>>();
			AssemblyContext context =
					this.getHandler(AssemblyContext.class);
			ExportServiceRepository serviceRepository =
					ExportServiceRepositoryReference.adaptFrom(this.getAssembly());
			ExportServiceRegistry serviceRegistry =
					serviceRepository.findService(ExportServiceRegistry.class);
			
			for (ExportServiceResource resource: this.resources){
				String cName = resource.getCName();
				Class<?>[] exportTypes = resource.getExportTypes();
				Map<String, Object> props = this.getServiceProperties(resource);
				
				Object service = context.getService(cName);
				registrations.add(
						serviceRegistry.register(exportTypes, service, props));
			}
		}
	}
	
	/**
	 * 取消输出服务
	 */
	protected synchronized void unexportServices(){
		if (this.registrations != null){
			List<ExportServiceRegistration<?>> list = this.registrations;
			this.registrations.clear();
			
			for (ExportServiceRegistration<?> registration: list){
				registration.unregister();
			}
			list.clear();
			this.registrations = null;
		}
	}
	
	/**
	 * 获取服务属性
	 * @param resource {@link ExportServiceResource}
	 * @return {@link Map}
	 */
	protected Map<String, Object> getServiceProperties(ExportServiceResource resource){
		if (resource instanceof SimpleExportServiceResource){
			return ((SimpleExportServiceResource)resource).getServiceProperties();
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> keys = resource.getPropertyKeys();
		for (String key: keys){
			result.put(key, resource.getProperty(key));
		}
		
		result.put(Constants.OBJECT_CLASS, resource.getExportTypes());
		return result;
	}

	/**
	 * 事件适配器
	 */
	private class EventAdapter extends LifecycleEventAdapter {

		@Override
		public void onResolved() {
			setResolved();
		}

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.base.handle.support.LifecycleEventAdapter#onActivated()
		 */
		@Override
		public void onActivated() {
			super.onActivated();
			exportServices();
		}

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.base.handle.support.LifecycleEventAdapter#onInactivating()
		 */
		@Override
		public void onInactivating() {
			unexportServices();
			super.onInactivating();
		}
		
		
	}
}
