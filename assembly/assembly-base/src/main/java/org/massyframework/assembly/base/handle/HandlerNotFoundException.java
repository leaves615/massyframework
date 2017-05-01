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
package org.massyframework.assembly.base.handle;

import org.massyframework.assembly.AssemblyException;

/**
 * 当指定类型的处理器不存在时，抛出的例外
 */
public final class HandlerNotFoundException extends AssemblyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4474789388612801988L;

	private final Class<?> handlerType;
	
	/**
	 * 构造方法
	 * @param handlerType
	 */
	public HandlerNotFoundException(Class<?> handlerType) {
		super("handler cannot be found: type=" + handlerType + ".");
		this.handlerType = handlerType;
	}

	/**
	 * 处理器类型
	 * @return {@link Class}
	 */
	public Class<?> getHandlerType(){
		return this.handlerType;
	}
}
