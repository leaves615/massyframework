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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.base.handle.Handler;
import org.massyframework.assembly.base.handle.HandlerNotFoundException;
import org.massyframework.assembly.base.handle.HandlerRegistration;
import org.massyframework.assembly.base.handle.HandlerRegistry;
import org.massyframework.assembly.util.Asserts;

/**
 * 缺省的处理注册器
 */
public final class DefaultHandlerRegistry implements HandlerRegistry {

	private Assembly assembly;
	private List<RegistrationImpl<?>> registrations =
			new CopyOnWriteArrayList<RegistrationImpl<?>>();
	
	/**
	 * 
	 */
	public DefaultHandlerRegistry(Assembly assembly) {
		Asserts.notNull(assembly, "assembly cannot be null.");
		this.assembly = assembly;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ObjectReference#getReference()
	 */
	@Override
	public Assembly getReference() {
		return this.assembly;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.HandlerRegistry#findHandler(java.lang.Class)
	 */
	@Override
	public <T> T findHandler(Class<T> handlerType) {
		for (RegistrationImpl<?> registration: this.registrations){
			if (handlerType.isAssignableFrom(registration.getHandlerType())){
				return handlerType.cast(registration.getHandler());
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.HandlerRegistry#getHandler(java.lang.Class)
	 */
	@Override
	public <T> T getHandler(Class<T> handlerType) throws HandlerNotFoundException {
		T result = this.findHandler(handlerType);
		
		if (result == null){
			result = DefaultHandlerFactory.createHandler(handlerType);
			if (result != null){
				this.register(result);
			}
		}
		if (result == null){
			throw new HandlerNotFoundException(handlerType);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.HandlerRegistry#getHandlers(java.lang.Class)
	 */
	@Override
	public <T> List<T> getHandlers(Class<T> handlerType) {
		List<T> result = new ArrayList<T>();
		for (RegistrationImpl<?> registration: this.registrations){
			if (handlerType.isAssignableFrom(registration.getHandlerType())){
				result.add(handlerType.cast(registration.getHandler()));
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.HandlerRegistry#register(java.lang.Object)
	 */
	@Override
	public <T> HandlerRegistration<T> register(T handler) {
		RegistrationImpl<T> result = new RegistrationImpl<T>(handler);
		this.registrations.add(result);
		if (Handler.class.isAssignableFrom(result.getHandlerType())){
			((Handler)result.getHandler()).init(this);
		}
		return result;
	}
	
	/**
	 * 移除处理器
	 * @param registration
	 */
	protected void doUnregister(RegistrationImpl<?> registration){
		if (this.registrations.contains(registration)){
			if (Handler.class.isAssignableFrom(registration.getHandlerType())){
				((Handler)registration.getHandler()).destroy();
			}
			
			this.registrations.remove(registration);
		}
	}

	/**
	 * 注册器实现类
	 */
	private class RegistrationImpl<T> implements HandlerRegistration<T>{
		
		private T handler;

		public RegistrationImpl(T handler){
			Asserts.notNull(handler, "handler cannot be null.");
			this.handler = handler;
		}
		
		@Override
		public T getHandler() {
			return this.handler;
		}
		
		@Override
		public void unregister() {
						
		}
		
		/**
		 * 获取处理器类型
		 * @return {@link Class}
		 */
		public Class<?> getHandlerType(){
			return this.handler.getClass();
		}
		
	}
}
