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

import org.massyframework.assembly.AssemblyReference;

/**
 * 依赖服务资源
 */
public interface DependencyServiceResource extends AssemblyReference{

	/**
	 * 依赖服务被注入后，在装配件上下文中的名称
	 * @return {@link String}
	 */
	String getCName();
	
	/**
	 * 依赖服务的类型
	 * @return {@link Class}
	 */
	Class<?> getRequiredType();
	
	/**
	 * 服务筛选条件
	 * @return {@link String}
	 */
	String getFilterString();
}
