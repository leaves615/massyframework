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

import java.util.List;

/**
 * 输出的服务资源
 */
public interface ExportServiceResource{

	/**
	 * 输出服务在装配件上下文中的名称
	 * @return {@link String}
	 */
	String getCName();
	
	/**
	 * 输出类型
	 * @return {@link Class}数组
	 */
	Class<?>[] getExportTypes();
	
	/**
	 * 获取输出服务的属性
	 * @param key 属性的键
	 * @return {@link Object}, 属性不存在返回null.
	 */
	Object getProperty(String key);
	
	/**
	 * 所有属性键值
	 * @return {@link List}
	 */
	List<String> getPropertyKeys();
}
