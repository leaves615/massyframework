/**
* @Copyright: 2017 smarabbit studio Inc. All rights reserved.
*  
* @作   者：  huangkaihui
* @日   期:  2017年2月28日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.util;

import java.util.Map;

/**
 * Map工具类
 *
 */
public abstract class MapUtils {

	/**
	 * 判断{@link Map}是否为null或empty.
	 * @param c {@link Map}
	 * @return 返回true表示为null或者empty,否则返回false.
	 */
	public static <K, V> boolean isEmpty(Map<K, V> map){
		return map == null || map.isEmpty();
	}
}
