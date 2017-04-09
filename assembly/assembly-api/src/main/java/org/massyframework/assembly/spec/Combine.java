/**
* @Copyright: 2017 smarabbit studio Inc. All rights reserved.
*  
* @作   者：  huangkaihui
* @日   期:  2017年2月28日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spec;

import java.util.ArrayList;
import java.util.List;

import org.massyframework.assembly.util.Asserts;

/**
 * 支持组合运算，即同时存在and和or运算。
 * <br>在执行{@link #isSatisfyBy(Object)}之前，规则检查器已经通过{@link #addSpecification(Specification, boolean)}添加完毕。
 *
 */
public class Combine<T> implements Specification<T> {
	
	private List<Specification<T>> andSpecs;
	private List<Specification<T>> orSpecs;
	
	/**
	 * 
	 */
	public Combine() {

	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.spec.Specification#isSatisfyBy(java.lang.Object)
	 */
	@Override
	public boolean isSatisfyBy(T target) {
		Or<T> or = null;
		And<T> and = null;
		synchronized(this){
			if (this.orSpecs != null){
				or = new Or<T>(this.orSpecs);
			}
			
			if (this.andSpecs != null){
				and = new And<T>(this.andSpecs);
			}
		}
		
		//or预算优先
		if (or != null){
			if (or.isSatisfyBy(target)){
				return true;
			}
		}
		
		if (and != null){
			if (and.isSatisfyBy(target)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 添加规则检查器	
	 * @param spec {@link Specification}规则检查器
	 * @param andOperation {@code true}执行and运算, {@code false}执行or运算
	 */
	public synchronized void addSpecification(Specification<T> spec, boolean andOperation){
		Asserts.notNull(spec, "spec cannot be null.");
		List<Specification<T>> specs = this.getSpecifications(andOperation);
		specs.add(spec);
	}
	
	/**
	 * 获取规则检查器集合
	 * @param andOperation
	 * @return
	 */
	private List<Specification<T>> getSpecifications(boolean andOperation){
		if (andOperation){
			if (this.andSpecs == null){
				this.andSpecs = new ArrayList<Specification<T>>();
			}else{
				if (this.orSpecs == null){
					this.orSpecs = new ArrayList<Specification<T>>();
				}
			}
		}
		
		return andOperation ? this.andSpecs : this.orSpecs;
	}

}
