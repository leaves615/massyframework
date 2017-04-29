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
* @日   期:  2017年4月11日
*/
package org.massyframework.assembly;

/**
 * 类加载器引用
 */
public interface ClassLoaderReference extends ObjectReference<ClassLoader> {

	/**
	 * 从装配件适配出类加载器对象
	 * @param assembly 装配件
	 * @return {@link ClassLoader},可能返回null.
	 */
	static ClassLoader adaptFrom(Assembly assembly){
		if (assembly == null){
			return null;
		}
		
		ClassLoaderReference reference =
				assembly.adapt(ClassLoaderReference.class);
		return reference != null ?
					reference.getReference():
						null;
	}
}
