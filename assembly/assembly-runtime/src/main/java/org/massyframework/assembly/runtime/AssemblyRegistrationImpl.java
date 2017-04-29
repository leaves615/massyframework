/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.base.handle.LifecycleProcessHandler;
import org.massyframework.assembly.base.handle.RegisterableHandler;
import org.massyframework.assembly.base.handle.support.DefaultConfigFile;
import org.massyframework.assembly.base.handle.support.LifecycleEventAdapter;
import org.massyframework.assembly.runtime.resolve.DefaultXmlResolver;
import org.massyframework.assembly.util.Asserts;
import org.slf4j.Logger;

/**
 * 装配件注册凭据实现
 */
class AssemblyRegistrationImpl implements AssemblyRegistration {
	
	private final AbstractAssemblyRegistry registry;
	private DefaultAssembly assembly;
	private EventHandler eventHandler;
	
	/**
	 * 
	 */
	public AssemblyRegistrationImpl(AbstractAssemblyRegistry registry, 
			AssemblyResource resource){
		Asserts.notNull(registry, "registry cannot be null.");
		Asserts.notNull(resource, "resource cannot be null.");
		this.registry = registry;
		
		this.assembly = this.createAssembly(resource);		
	}
	
	/**
	 * 注册完成
	 */
	void onRegistComplete(){
		RegisterableHandler handler =
				assembly.getHandlerRegistry().getHandler(RegisterableHandler.class);
		handler.registCompleted();
	}
	
	/**
	 * 注销前处理
	 */
	void onUnregistering(){
		RegisterableHandler handler =
				assembly.getHandlerRegistry().getHandler(RegisterableHandler.class);
		handler.unregistering();
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.Registration#unregister()
	 */
	@Override
	public void unregister() {
		this.registry.doUnregister(this);
		LifecycleProcessHandler handler =
				this.assembly.getHandlerRegistry().getHandler(LifecycleProcessHandler.class);
		handler.removeListener(this.eventHandler);
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.framework.AssemblyRegistration#getAssembly()
	 */
	@Override
	public Assembly getAssembly() {
		return this.assembly;
	}
	
	/**
	 * 创建Assembly实例
	 * @param location 装配件路径
	 * @return {@link DefaultAssembly}
	 */
	private DefaultAssembly createAssembly(AssemblyResource resource){		
		DefaultAssembly result = new DefaultAssembly();
		
		//类加载器引用
		result.initAdaptObject(
				new DefaultClassLoaderReference(resource.getClassLoader()));
		
		DefaultConfigFile configFile =
				new DefaultConfigFile(resource);
		result.getHandlerRegistry().register(configFile);
		
		LifecycleProcessHandler handler =
				result.getHandlerRegistry().getHandler(LifecycleProcessHandler.class);
		this.eventHandler = new EventHandler();
		handler.addListener(this.eventHandler);
		
		return result;
	}
	
	/**
	 * 初始化
	 * @throws Exception
	 */
	void init() throws Exception{ 
		assembly.getHandlerRegistry().register(new DefaultXmlResolver());		
		LifecycleProcessHandler handler = 
				assembly.getHandlerRegistry().getHandler(LifecycleProcessHandler.class);
		try{
			handler.start();
		}catch(Exception e){
			Logger logger = LoggerReference.adaptFrom(assembly);
			if (logger != null){
				if (logger.isErrorEnabled()){
					logger.error("resolve assembly failed.", e);
				}
			}
			
			throw e;
		}
	}
		
	protected <S> S findService(Class<S> serviceType){
		return this.assembly.getExportServiceRepository().findService(serviceType);
	}
	
	private class EventHandler extends LifecycleEventAdapter {

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.base.handle.support.LifecycleEventAdapter#onActivated()
		 */
		@Override
		public void onActivated() {
			registry.notifyActived(getAssembly());
		}

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.base.handle.support.LifecycleEventAdapter#onInactivating()
		 */
		@Override
		public void onInactivating() {
			registry.notifyInactivating(getAssembly());
			super.onInactivating();
		}
	}
}
