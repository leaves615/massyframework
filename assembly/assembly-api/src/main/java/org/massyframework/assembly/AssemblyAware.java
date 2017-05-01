/**
* @Copyright: 2017 smarabbit studio. 
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
* @日   期:  2017年4月10日
*/
package org.massyframework.assembly;

/**
 * 提供设置装配件的方法的感知接口
 *
 */
public interface AssemblyAware {

	/**
	 * 设置装配件
	 * @param assembly {@link Assembly}
	 */
	void setAssembly(Assembly assembly);
	
	/**
	 * 尝试在目标对象上绑定装配件
	 * <br>当目标对象实现{@link AssemblyAware}接口时，通过{@link #setAssembly(Assembly)}进行绑定。
	 * @param target 目标对象, 可以为null.
	 * @param assembly 装配件, 可以为null.
	 */
	public static <T> void maybeToBind(T target, Assembly assembly){
		if (target == null) return;
		if (target instanceof AssemblyAware){
			((AssemblyAware)target).setAssembly(assembly);
		}
	}
}
