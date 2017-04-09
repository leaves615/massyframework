/**
* @Copyright: 2017 smarabbit studio Inc. All rights reserved.
*  
* @作   者：  huangkaihui
* @日   期:  2017年2月28日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spec;

import java.util.Arrays;
import java.util.List;

import org.massyframework.assembly.util.Asserts;

/**
 * 支持Or运算的组合规则检查器
 * <br>只要内置的规则检查器一项满足，则终止后续检查，直接返回true.
 *
 */
public class Or<T> implements Specification<T> {

	private final List<Specification<T>> specList;
	
	/**
	 * 构造方法
	 * @param specs 规则集合
	 */
	@SuppressWarnings("unchecked")
	public Or(Specification<T> ... specs) {
		this(Arrays.asList(specs));
	}
	
	/**
	 * 构造方法
	 * @param specs 规则集合
	 */
	public Or(List<Specification<T>> specs){
		Asserts.notEmpty(specs, "specs cannot be empty.");
		this.specList = specs;
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.spec.Specification#isSatisfyBy(java.lang.Object)
	 */
	@Override
	public boolean isSatisfyBy(T target) {
		for (Specification<T> spec : this.specList){
			if (spec.isSatisfyBy(target)){
				return true;
			}
		}
		return false;
	}
}
