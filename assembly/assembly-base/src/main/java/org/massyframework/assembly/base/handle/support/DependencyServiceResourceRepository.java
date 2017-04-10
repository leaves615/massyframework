/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.massyframework.assembly.AssemblyStatus;
import org.massyframework.assembly.base.handle.DependencyServiceResource;
import org.massyframework.assembly.base.handle.DependencyServiceResourceHandler;
import org.massyframework.assembly.base.handle.LifecycleProcessHandler;
import org.massyframework.assembly.util.Asserts;

/**
 * 依赖服务仓储,实现{@link DependencyServiceResourceHandler}接口
 */
public class DependencyServiceResourceRepository extends AbstractHandler 
	implements DependencyServiceResourceHandler{

	private List<DependencyServiceResource> resources =
			new ArrayList<DependencyServiceResource>();
	private volatile EventAdapter eventAdapter;
	private volatile boolean resolved = false;
	
	/**
	 * 
	 */
	public DependencyServiceResourceRepository() {
	}

	/**
	 * 添加依赖依赖服务资源
	 */
	@Override
	public void add(DependencyServiceResource resource) {
		this.checkResolved();
		this.checkResource(resource);
		this.doAdd(resource);
	}

	/**
	 * 添加多个依赖服务资源
	 */
	@Override
	public void addAll(Collection<DependencyServiceResource> resources) {
		this.checkResolved();
		for (DependencyServiceResource resource: resources){
			this.checkResource(resource);
		}
		this.doAddAll(resources);
	}

	@Override
	public synchronized List<DependencyServiceResource> getDependencyServiceResources() {
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
	 * 检查dependencyServiceResource的有效性
	 * @param dependencyService
	 */
	private void checkResource(DependencyServiceResource resource){
		Asserts.notNull(resource.getServiceType(), "serviceType of dependency resource cannot be null.");
	}

	private synchronized void doAdd(DependencyServiceResource resource){
		this.resources.add(resource);
	}
	
	private synchronized void doAddAll(Collection<DependencyServiceResource> resources){
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
	 * 卸载装配件
	 */
	protected void onUninstall(){
		
	}
	
	
	/**
	 * 初始化
	 */
	@Override
	protected synchronized void init() {
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
	 * 析钩方法
	 */
	@Override
	public synchronized void destroy() {
		if (this.eventAdapter != null){
			LifecycleProcessHandler handler =
					this.getHandler(LifecycleProcessHandler.class);
			handler.removeListener(this.eventAdapter);
			this.eventAdapter = null;
		}
		super.destroy();
	}

	
	
	/**
	 * 事件适配器
	 */
	private class EventAdapter extends LifecycleEventAdapter {

		@Override
		public void onResolved() {
			setResolved();
		}

		@Override
		public void onUninstalling() {
			super.onUninstalling();
			onUninstall();
		}
		
	}
}
