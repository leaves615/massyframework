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

import java.util.List;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.base.handle.Handler;
import org.massyframework.assembly.base.handle.HandlerNotFoundException;
import org.massyframework.assembly.base.handle.HandlerRegistry;
import org.slf4j.Logger;

/**
 * 封装处理器需要使用的常见方法, 包括获取装配件，获取日志记录器，查找和发现其他处理器等。
 */
public abstract class AbstractHandler implements Handler {

	private volatile HandlerRegistry handlerRegistry;
	/**
	 * 
	 */
	public AbstractHandler() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.Handler#init(org.massyframework.assembly.base.handle.HandlerRegistry)
	 */
	@Override
	public final void init(HandlerRegistry handlerRegistry){
		this.handlerRegistry = handlerRegistry;
		this.init();
	}
	
	/**
	 * 初始化
	 */
	protected void init(){
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.Handler#destroy()
	 */
	@Override
	public void destroy() {
	}
	
	/**
	 * 按处理器类型查找处理器实例
	 * <br>如果存有多个同类型的处理器实例，只返回第一个实例
	 * @param handlerType 处理器类型
	 * @return {@link H}
	 * @throws IllegalArgumentException {@link #init(HandlerRegistry)}方法未被调用，导致handlerRegistry为null抛出例外
	 */
	protected <H> H findHandler(Class<H> handlerType) throws IllegalArgumentException{
		if (this.getHandlerRegistry() != null){
			return this.getHandlerRegistry().findHandler(handlerType);
		}
		
		throw new IllegalArgumentException("handlerRegistry cannot be null, please set it.");
	}
	
	/**
	 * 按处理器类型获取处理器实例
	 * <br>如果存有多个同类型的处理器实例，只返回第一个实例
	 * @param handlerType 处理器类型
	 * @return {@link H}
	 * @throws HandlerNotFoundException 无对应的处理器则抛出异常
	 * @throws IllegalArgumentException {@link #init(HandlerRegistry)}方法未被调用，导致handlerRegistry为null抛出例外
	 */
	protected <H> H getHandler(Class<H> handlerType) 
			throws HandlerNotFoundException, IllegalArgumentException{
		if (this.getHandlerRegistry() != null){
			return this.getHandlerRegistry().getHandler(handlerType);
		}
		
		throw new IllegalArgumentException("handlerRegistry cannot be null, please set it.");
	}
	
	/**
	 * 获取指定类型的所有处理器实例
	 * @param handlerType 处理器类型
	 * @return {@link List}
	 * @throws IllegalArgumentException  {@link #init(HandlerRegistry)}方法未被调用，导致handlerRegistry为null抛出例外
	 */
	protected <H> List<H> getHandlers(Class<H> handlerType) throws IllegalArgumentException{
		if (this.getHandlerRegistry() != null){
			return this.getHandlerRegistry().getHandlers(handlerType);
		}
		
		throw new IllegalArgumentException("handlerRegistry cannot be null, please set it.");
	}
	
	/**
	 * 装配件
	 * @return {@link Assembly}, 可能返回null.
	 */
	protected Assembly getAssembly(){
		return this.getHandlerRegistry() == null ? null : 
			this.getHandlerRegistry().getReference();
	}
	
	/**
	 * 处理注册器
	 * @return {@link HandlerRegistry}
	 */
	protected HandlerRegistry getHandlerRegistry(){
		return this.handlerRegistry;
	}
	
	/**
	 * 日志记录器
	 * @return {@link Logger}, 可能返回null.
	 */
	protected Logger getLogger(){
		return LoggerReference.adaptFrom(this.getAssembly());
	}

}
