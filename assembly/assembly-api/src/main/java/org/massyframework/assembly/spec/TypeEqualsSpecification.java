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
