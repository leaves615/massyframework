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
package org.massyframework.assembly;

import java.util.List;

/**
 * 装配键输出服务的引用
 */
public interface ExportServiceReference<S> {
	
	/**
	 * 提供服务实例的装配件
	 * @return {@link Assembly}
	 */
	Assembly getAssembly();
	
	/**
	 * 服务的编号，由运行框架生成<br>
	 * 存放在服务属性中，键为{@link Constants#SERVICE_ID}
	 * @return {@link long}
	 */
	long getServiceId();
		
	/**
	 * 获取服务属性
	 * @param key 属性的键
	 * @return {@link Object}, 属性不存在返回null.
	 */
	Object getProperty(String key);
	
	/**
	 * 获取服务属性
	 * @param key 属性的键
	 * @param propType 属性的类型
	 * @return {@link P},属性不存在返回null.
	 */
	<P> P getProperty(String key, Class<P> propType);
	
	/**
	 * 获取所有服务属性的键值
	 * @return {@link List}
	 */
	List<String> getPropertyKeys();
}
