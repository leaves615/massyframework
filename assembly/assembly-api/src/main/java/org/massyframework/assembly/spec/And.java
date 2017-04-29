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

import java.util.Arrays;
import java.util.List;

import org.massyframework.assembly.util.Asserts;

/**
 * 支持And运算的组合规则检查器
 * <br>只要内置的规则检查器有一项不满足，则终止后续检查，直接返回false.
 *
 */
public final class And<T> implements Specification<T> {

	private final List<Specification<T>> specList;
	
	/**
	 * 构造方法
	 * @param specs 规则集合
	 */
	@SuppressWarnings("unchecked")
	public And(Specification<T> ... specs) {
		this(Arrays.asList(specs));
	}
	
	/**
	 * 构造方法
	 * @param specs 规则集合
	 */
	public And(List<Specification<T>> specs){
		Asserts.notEmpty(specs, "specs cannot be empty.");
		this.specList = specs;
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.spec.Specification#isStaisfyBy(java.lang.Object)
	 */
	@Override
	public boolean isSatisfyBy(T target) {
		for (Specification<T> spec : this.specList){
			if (!spec.isSatisfyBy(target)){
				return false;
			}
		}
		return true;
	}

	public Specification<?>[] getSpecifications(){
		return this.specList.toArray(
				new Specification<?>[this.specList.size()]);
	}
	
	/**
	 * 是否有指定类型的规则检查器
	 * @param specType
	 * 			{@link Class}
	 * @return
	 * 			true表示有，false表示没有
	 */
	public boolean hasSpecification(Class<? extends Specification<?>> specType){
		for (Specification<?> spec : this.specList){
			if (And.class.isInstance(spec)){
				if (((And<?>)spec).hasSpecification(specType)){
					return true;
				}
			}else{
				if (specType.isInstance(specType)){
					return true;
				}
			}
		}
		
		return false;
	}
}
