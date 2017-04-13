/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
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
