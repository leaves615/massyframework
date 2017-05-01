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
* @日   期:  2017年4月9日
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
