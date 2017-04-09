/**
* @Copyright: 2017 smarabbit studio Inc. All rights reserved.
*  
* @作   者：  huangkaihui
* @日   期:  2017年2月28日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spec;

import org.massyframework.assembly.util.Asserts;

/**
 * 类型是否具有派生关系的规则检查器
 * <br>判断目标对象是否是指定要求类型的子类或者继承类
 *
 */
public class TypeIsAssignableFromSpecification<T> implements Specification<T> {

	private Class<?> requiredType;
	/**
	 * 
	 */
	public TypeIsAssignableFromSpecification(Class<?> requiredType) {
		Asserts.notNull(requiredType, "requiredType cannot be null.");
		this.requiredType = requiredType;
	}
	
	/* (non-Javadoc)
	 * @see org.smarabbit.massy.spec.Specification#isStaisfyBy(java.lang.Object)
	 */
	@Override
	public boolean isSatisfyBy(T target) {
		return this.requiredType.isAssignableFrom(target.getClass());
	}



	/**
	 * 要求的类型
	 * @return
	 */
	public Class<?> getRequiredType(){
		return this.requiredType;
	}

}
