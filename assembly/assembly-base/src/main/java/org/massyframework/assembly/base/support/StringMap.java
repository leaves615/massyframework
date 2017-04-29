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
* @日   期:  2017年4月11日
*/
package org.massyframework.assembly.base.support;

import java.util.Map;
import java.util.TreeMap;

/**
 * 派生于TreeMap的字符串Map.
 */
public class StringMap extends TreeMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 130955206268661024L;

	/**
	 * 构造方法
	 */
	public StringMap() {
		super(StringComparator.COMPARATOR);
	}

	/**
	 * 构造方法
	 * @param map {@link Map}
	 */
	public StringMap(Map<?, ?> map) {
		this();
		for (Map.Entry<?, ?> e : map.entrySet()) {
			put(e.getKey().toString(), e.getValue());
		}
	}
}
