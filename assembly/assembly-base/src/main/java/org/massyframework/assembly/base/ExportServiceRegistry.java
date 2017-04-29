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
* @日   期:  2017年4月10日
*/
package org.massyframework.assembly.base;

import java.util.Map;

import org.massyframework.assembly.ServiceFactory;

/**
 * 输出服务注册器
 */
public interface ExportServiceRegistry{

	/**
	 * 使用服务实例注册服务
	 * @param clazz 服务类型
	 * @param service 服务实例
	 * @param props 服务属性，可以为null.
	 * @return {@link ExportServiceRegistration}
	 */
	<S> ExportServiceRegistration<S> register(Class<S> clazz, S service, Map<String, Object> props);
	
	/**
	 * 注册服务工厂注册服务
	 * @param clazz 服务类型
	 * @param factory 服务工厂
	 * @param props 服务属性， 可以为null.
	 * @return {@link ExportServiceRegistration}
	 */
	<S> ExportServiceRegistration<S> register(Class<S> clazz, ServiceFactory<S> factory, Map<String, Object> props);
	
	/**
	 * 使用服务实例注册服务
	 * @param classes 服务类型集合
	 * @param service 服务实例
	 * @param props 服务属性, 可以为null.
	 * @return {@link ExportServiceRegistration}
	 */
	ExportServiceRegistration<?> register(Class<?>[] classes, Object service, Map<String, Object> props);
	
	/**
	 * 使用服务工厂注册服务
	 * @param classes 服务类型集合
	 * @param factory 服务工厂
	 * @param props 服务属性，可以为null.
	 * @return {@link ExportServiceRegistration}
	 */
	ExportServiceRegistration<?> register(Class<?>[] classes, ServiceFactory<?> factory, Map<String, Object> props);
	
}
