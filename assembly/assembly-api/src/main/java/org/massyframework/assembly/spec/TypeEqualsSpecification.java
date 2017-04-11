/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spec;

import org.massyframework.assembly.util.Asserts;

/**
 * 类型相同规则检查器
 * <br>检查目标对象的类型与要求类型一致
 *
 */
public class TypeEqualsSpecification<T> implements Specification<T> {

	private Class<?> requiredType;
	
	/**
	 * 
	 */
	public TypeEqualsSpecification(Class<?> requiredType) {
		Asserts.notNull(requiredType, "requiredType cannot be null.");
		this.requiredType = requiredType;
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.spec.Specification#isStaisfyBy(java.lang.Object)
	 */
	@Override
	public boolean isSatisfyBy(T target) {
		return this.requiredType == target.getClass();
	}

	/**
	 * 要求的类型
	 * @return
	 */
	public Class<?> getRequiredType(){
		return this.requiredType;
	}

}
