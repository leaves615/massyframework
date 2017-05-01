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
* @日   期:  2017年4月15日
*/
package org.massyframework.modules.launching;

import org.apache.commons.lang3.StringUtils;
import org.jboss.modules.ModuleIdentifier;

/**
 * @author huangkaihui
 *
 */
abstract class ModuleIdentifierUtils {
	
	private static final String DELIMITER = ":";

	/**
	 * 使用模块名称分解成为ModuleIdentifier
	 * @param moduleName 模块名称
	 * @return {@link ModuleIdentifier}
	 */
	public static ModuleIdentifier parse(String moduleName){
		if (moduleName == null) return null;
		
		int index = StringUtils.indexOf(moduleName, DELIMITER);
		if (index == -1){
			return ModuleIdentifier.create(moduleName);
		}else{
			String name = StringUtils.substring(moduleName, 0, index);
			String slot = StringUtils.substring(moduleName, index +1, moduleName.length());
			return ModuleIdentifier.create(name, slot);
		}
		
	}
}
