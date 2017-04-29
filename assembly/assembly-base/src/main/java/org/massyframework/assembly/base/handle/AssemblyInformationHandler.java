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
* @日   期:  2017年4月10日
*/
package org.massyframework.assembly.base.handle;

/**
 * 装配件信息处理器,提供设置装配件基本信息的方法
 */
public interface AssemblyInformationHandler {
	
	/**
	 * 设置友好名称
	 * @param name 人性化的友好名称
	 */
	void setName(String name);

	/**
	 * 设置说明
	 * @param description 说明
	 */
	void setDescription(String description);
	
	/**
	 * 设置装配件的符号名称
	 * @param symbolicName 符号名称
	 * @return <code>true</code>设置成功,<code>false</code>符号名称已存在，设置失败
	 */
	boolean setSymbolicName(String symbolicName);
	
	/**
	 * 设置生产商
	 * @param vendor 生产商
	 */
	void setVendor(String vendor);
}
