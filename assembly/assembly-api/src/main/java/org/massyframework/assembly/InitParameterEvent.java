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
* @日   期:  2017年4月13日
*/
package org.massyframework.assembly;

import org.massyframework.assembly.util.Asserts;

/**
 * 初始化参数事件
 */
public final class InitParameterEvent extends FrameworkEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5163469246617567528L;

	private String key;
	private String value;
	
	/**
	 * 构造方法
	 * @param framework
	 */
	public InitParameterEvent(Framework framework, String key, String value) {
		super(framework);
		Asserts.notNull(key, "key cannot be null.");
		Asserts.notNull(value, "value cannot be null.");
		this.key = key;
		this.value = value;
	}

	/**
	 * 属性键
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 属性值
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
