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
package org.massyframework.assembly.util;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具类
 *
 */
public abstract class Asserts {

	/**
	 * 断言是否为null
	 * @param object 断言对象
	 * @param message 断言为真时抛出的错误消息
	 */
	public static void notNull(Object object, String message){
		if (object == null){
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * 断言不能为Empty
	 * @param c {@link Collection}集合
	 * @param message 断言为真时抛出的错误消息
	 */
	public static <T> void notEmpty(Collection<T> c, String message){
		if (CollectionUtils.isEmpty(c)){
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * 断言不能为Empty
	 * @param arr {@link T}数组
	 * @param message 断言为真时抛出的错误消息
	 */
	public static <T> void notEmpty(T[] arr, String message){
		if (ArrayUtils.isEmpty(arr)){
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 *  断言不能为Empty
	 * @param map {@link Map}
	 * @param message 断言为真时抛出的错误消息
	 */
	public static <K, V> void notEmpty(Map<K, V> map, String message){
		if (MapUtils.isEmpty(map)){
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * 断言是否不为真
	 * @param bool
	 * @param message
	 */
	public static void notTrue(boolean bool, String message){
		if (!bool){
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * 断言是否不为真
	 * @param bool
	 * @param message
	 */
	public static void notTrue(Boolean bool, String message){
		notTrue(bool.booleanValue(), message);
	}
}
