/**
* @Copyright: 2017 smarabbit studio Inc. All rights reserved.
*  
* @作   者：  huangkaihui
* @日   期:  2017年2月28日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
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
