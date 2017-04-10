/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import java.util.List;

import org.massyframework.assembly.AssemblyReference;

/**
 * 提供处理器的注册、查找方法
 */
public interface HandlerRegistry extends AssemblyReference{
	
	/**
	 * 按处理器类型查找首个符合要求的处理器实例
	 * @param handlerType 处理器类型
	 * @return {@link T}, 无满足要求的处理器可以返回null.
	 */
	<T> T findHandler(Class<T> handlerType);
	
	/**
	 * 按处理器类型获取首个符合要求的处理器实例
	 * @param handlerType 处理器类型
	 * @return {@link T}
	 * @throws HandlerNotFoundException 无匹配的处理器，则抛出处理器未找到例外
	 */
	<T> T getHandler(Class<T> handlerType) throws HandlerNotFoundException;
	
	/**
	 * 查找所有指定类型的处理器
	 * @param handlerType 处理器类型
	 * @return {@link List}，处理器集合
	 */
	<T> List<T> getHandlers(Class<T> handlerType);

	/**
	 * 注册处理器
	 * @param handler 处理器实例
	 * @return {@link HandlerRegistration}处理器注册凭据
	 */
	<T> HandlerRegistration<T> register(T handler);
}
