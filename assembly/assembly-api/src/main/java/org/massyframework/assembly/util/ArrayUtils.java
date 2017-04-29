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
package org.massyframework.assembly.util;

import java.lang.reflect.Array;

/**
 * 数组工具类
 *
 */
public abstract class ArrayUtils {

	/**
	 * 判断arr是否为Empty
	 * @param arr 数组
	 * @return 
	 * 		true表示为Empty, false表示不为Empty
	 */	
	public static <T> boolean isEmpty(T[] arr){
		return (arr == null) || (arr.length == 0);
	}
	
	/**
	 * 根据数组型类型创建对应的二维数组
	 * @param <T> 目标类型
	 * @param clazz 数组型类型
	 * @param length 数组长度
	 * @return
	 * 		{@link T}数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArrayByArrayClass(Class<T[]> clazz, int length) {
	    return (T[]) Array.newInstance(clazz.getComponentType(), length);
	}
	    
	/**
	 * 根据创建一维数组
	 * @param <T> 泛型
	 * @param clazz 类型
	 * @param length 数组长度
	 * @return
	 * 		{@link T}数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArrayByClass(Class<T> clazz, int length) {
		return (T[]) Array.newInstance(clazz, length);
	}
}
