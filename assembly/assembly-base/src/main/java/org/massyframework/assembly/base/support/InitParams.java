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
* @日   期:  2017年4月11日
*/
package org.massyframework.assembly.base.support;

import java.util.List;

/**
 * 初始化参数
 */
public interface InitParams {

	/**
	 * 初始化参数是否存在
	 * @param key 参数键
	 * @return <code>true</code>参数存在, <code>false</code>参数不存在
	 */
	boolean containsParameter(String key);

	/**
	 * 按键获取初始化参数值
	 * @param key 键
	 * @return {@link String}
	 */
	String getParameter(String key);
	
	/**
	 * 获取所有的初始化参数键
	 * @return {@link List}
	 */
	List<String> getParameterKeys();
}
