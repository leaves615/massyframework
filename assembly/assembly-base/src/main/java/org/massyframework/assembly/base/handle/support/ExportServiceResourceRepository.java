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
import org.massyframework.assembly.base.handle.ExportServiceResource;
import org.massyframework.assembly.base.handle.ExportServiceResourceHandler;
import org.massyframework.assembly.base.handle.LifecycleProcessHandler;
import org.massyframework.assembly.util.Asserts;

/**
 * 输出服务资源仓储，实现{@link ExportServiceResourceHandler}接口
 * @author huangkaihui
 *
 */
public class ExportServiceResourceRepository extends AbstractHandler implements ExportServiceResourceHandler {

	private List<ExportServiceResource> resources =
			new ArrayList<ExportServiceResource>();
	private volatile EventAdapter eventAdapter;
	private volatile boolean resolved = false;
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
	 * 事件适配器
	 */
	private class EventAdapter extends LifecycleEventAdapter {

		@Override
		public void onResolved() {
			setResolved();
		}
	}
}