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
