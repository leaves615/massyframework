/**
* @Copyright: 2017 smarabbit studio Inc. All rights reserved.
*  
* @作   者：  huangkaihui
* @日   期:  2017年2月28日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spec;

/**
 * 规则检查器，判断目标对象是否满足约定的规则.
 * <br>查询方可以实现本接口，资源持有管理者提供使用接口查询的方法，例如T[] filter(Specification<T> spec)。
 * <br>通过传入的spec来判断来持有资源是否满足查询要求，从而满足查询多样化的需要。
 *
 */
public interface Specification<T> {

	/**
	 * 目标对象target是否满足方法内的规则要求。
	 * @param target {@link T}
	 * @return
	 * 		true表示满足规则，false表示不满足规则
	 */
	boolean isSatisfyBy(T target);
}
