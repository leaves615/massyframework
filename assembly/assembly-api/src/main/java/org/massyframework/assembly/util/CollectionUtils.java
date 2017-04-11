/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.util;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 集合工具类
 *
 */
public abstract class CollectionUtils {

	/**
	 * 判断{@link Collection}是否为null或empty.
	 * @param c {@link Collection}集合
	 * @return 返回true表示为null或者empty,否则返回false.
	 */
	public static <T> boolean isEmpty(Collection<T> c){
		return c == null || c.isEmpty();
	}
	
	/**
	 * 按排序次序添加条目
	 * @param list {@link CopyOnWriteArrayList}实例
	 * @param item 待加入的条目
	 * @return 排训后的位置
	 */
	public static <T extends Comparable<T>> int addInOrder(final CopyOnWriteArrayList<T> list, 
			final T item) {
	    final int insertAt;
	    // The index of the search key, if it is contained in the list; otherwise, (-(insertion point) - 1)
	    final int index = Collections.binarySearch(list, item);
	    if (index < 0) {
	      insertAt = -(index + 1);
	    } else {
	      insertAt = index + 1;
	    }

	    list.add(insertAt, item);
	    return insertAt;
	  }

}
