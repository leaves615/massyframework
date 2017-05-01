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
* @日   期:  2017年4月12日
*/
package org.massyframework.assembly.base.handle.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.base.handle.ActivationHandler;
import org.massyframework.assembly.base.handle.AssemblyContextHandler;
import org.massyframework.assembly.base.handle.ExportServiceResource;
import org.massyframework.assembly.base.handle.LifecycleListener;
import org.massyframework.assembly.base.handle.LifecycleProcessHandler;
import org.massyframework.assembly.base.handle.ServiceNotMatchedException;

/**
 * 抽象的装配件上下文管理器，实现{@link AssemblyContextHandler}接口
 */
public abstract class AssemblyContextManagement extends AbstractHandler 
	implements AssemblyContextHandler, LifecycleListener, ActivationHandler {

	private boolean registedListener = false;
	/**
	 * 
	 */
	public AssemblyContextManagement() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.AssemblyContextHandler#canWork()
	 */
	@Override
	public boolean canWork() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.AssemblyContextHandler#getExportableServices(java.util.List)
	 */
	@Override
	public Map<ExportServiceResource, Object> getExportableServices(List<ExportServiceResource> resources)
			throws Exception {
		Map<ExportServiceResource, Object> result = new HashMap<ExportServiceResource, Object>();
		AssemblyContext context = this.getAssemblyContext(1000);
		if (context != null){
			for (ExportServiceResource resource: resources){
				Object service = context.getService(resource.getCName());
				if (service == null){
					throw new ServiceNotMatchedException(resource);
				}
				
				result.put(resource, service);
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AbstractHandler#init()
	 */
	@Override
	protected synchronized void init() {
		super.init();
		if (!this.registedListener){
			LifecycleProcessHandler handler =
					this.getHandler(LifecycleProcessHandler.class);
			handler.addListener(this);
			this.registedListener = true;
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AbstractHandler#destroy()
	 */
	@Override
	public synchronized void destroy() {
		if (this.registedListener){
			LifecycleProcessHandler handler =
					this.getHandler(LifecycleProcessHandler.class);
			handler.removeListener(this);
			this.registedListener = false;
		}
		super.destroy();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onInstalled()
	 */
	@Override
	public void onInstalled() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onResolved()
	 */
	@Override
	public void onResolved() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onReadied()
	 */
	@Override
	public void onReadied() {
	
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onActivated()
	 */
	@Override
	public void onActivated() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onInactivating()
	 */
	@Override
	public void onInactivating() {
	
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onUnreadying()
	 */
	@Override
	public void onUnreadying() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onUninstalling()
	 */
	@Override
	public void onUninstalling() {
	
	}
	

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ActivationHandler#doStarting()
	 */
	@Override
	public void doStarting() throws Exception {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ActivationHandler#doStopped()
	 */
	@Override
	public void doStopped() throws Exception {
		
	}

	/**
	 * 获取装配件上下文
	 * @param waitMilliseconds 如果装配件上下文为null，可以等待多长时间再此尝试
	 * @return {@link AssemblyContext},可能返回null.
	 */
	protected AssemblyContext getAssemblyContext(int waitMilliSeconds){
		AssemblyContext result = this.getAssemblyContext();
		if (result == null){
			//以100毫秒为单位，尝试多次获取
			int max = waitMilliSeconds -100;
			while (max > 0){
				try{
					Thread.sleep(100);
				}catch(Exception e){
					
				}
				
				result = this.getAssemblyContext();
				if (result != null){
					break;
				}
				
				max = max -100;
			}
		}
		
		return result;
	}
	
	protected abstract AssemblyContext getAssemblyContext();
}
