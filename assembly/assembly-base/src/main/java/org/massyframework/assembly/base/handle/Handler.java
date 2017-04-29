/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
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

/**
 * 装配件内部通过一系列的处理器，每个处理器为装配件提供某种特性的能力，从而完成装配件的全部职责和能力。<br>
 * 本接口为处理器提供初始化和销毁方法
 */
public interface Handler {

	/**
	 * 初始化处理<br>
	 * 在{@link HandlerRegistry#register(Object)}处理过程中，由{@link HandlerRegistry}调用本方法，执行初始化。
	 * @param handlerRegistry 处理注册器
	 */
	void init(HandlerRegistry handlerRegistry);
	
	/**
	 * 销毁处理<br>
	 * 当{@link HandlerRegistration#unregister()}时，由{@link HandlerRegistry}调用本方法。<br>
	 * 在本方法中应该释放持有的资源。
	 */
	void destroy();
}
