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
* @日   期:  2017年4月14日
*/
package org.massyframework.assembly.base.handle.support;

import org.massyframework.assembly.base.handle.DependencyServiceResourceHandler;
import org.massyframework.assembly.base.handle.ExportServiceResourceHandler;
import org.massyframework.assembly.base.handle.LifecycleProcessHandler;
import org.massyframework.assembly.base.handle.ServiceInjectHandler;

/**
 * 缺省的处理器工厂
 */
abstract class DefaultHandlerFactory {

	/**
	 * 创建处理器
	 * @param handlerType {@link Class}
	 * @return {@link H}, 可能返回null.
	 */
	public static <H> H createHandler(Class<H> handlerType){
		if (handlerType == LifecycleProcessHandler.class ){
			return handlerType.cast(
					new LifecycleManagement());
		}
		
		if (handlerType == DependencyServiceResourceHandler.class){
			return handlerType.cast(
					new DependencyServiceResourceMatcher());
		}
		
		if (handlerType == ExportServiceResourceHandler.class){
			return handlerType.cast(
					new ExportServiceResourceRepository());
		}
		
		if (handlerType == ServiceInjectHandler.class){
			return handlerType.cast(new ServiceInjector());
		}
		
		return null;
	}
}
