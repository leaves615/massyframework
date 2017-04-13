/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.massyframework.assembly.CustmizeAssemblyContext;
import org.massyframework.assembly.base.handle.DependencyServiceResource;
import org.massyframework.assembly.base.handle.ServiceInjectCallback;
import org.massyframework.assembly.base.handle.ServiceInjectHandler;

/**
 * 自定义装配件上下文封装器
 */
final class CustomizeAssemblyContextWrapper extends AssemblyContextWrapper<CustmizeAssemblyContext> {

	/**
	 * @param context
	 */
	public CustomizeAssemblyContextWrapper(CustmizeAssemblyContext context) {
		super(context);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AssemblyContextWrapper#init()
	 */
	@Override
	protected void init() {
		super.init();
		try{
			ServiceInjectHandler handler =
				this.getHandler(ServiceInjectHandler.class);
			handler.addInjectCallback(new Callback());
		}catch(Exception e){
			throw new RuntimeException(this + " init() failed", e);
		}
	}

	/**
	 * 回调
	 */
	protected class Callback implements ServiceInjectCallback {
		
		public Callback(){}

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.handle.ServiceInjectCallback#doInject(java.util.Map)
		 */
		@Override
		public void doInject(Map<DependencyServiceResource, Object> serviceMap) throws Exception {
			Map<String, Object> services = new HashMap<String,Object>();
			for (Entry<DependencyServiceResource, Object> entry: serviceMap.entrySet()){
				services.put(entry.getKey().getCName(), entry.getValue());
			}
			
			getReference().injectServices(services);
		}
		
	}
}