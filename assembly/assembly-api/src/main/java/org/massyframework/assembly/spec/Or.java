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
