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
 * 输出服务仓储引用
 */
public interface ExportServiceRepositoryReference extends ObjectReference<ExportServiceRepository> {

	/**
	 * 从装配件中适配{@link ExportServiceRepository}
	 * @param assembly 装配件
	 * @return {@link ExportServiceRepository},可能返回null.
	 */
	static ExportServiceRepository adaptFrom(Assembly assembly){
		if (assembly == null){
			return null;
		}
		
		ExportServiceRepositoryReference reference =
				assembly.adapt(ExportServiceRepositoryReference.class);
		return reference != null?
				reference.getReference():
					null;
	}
}
