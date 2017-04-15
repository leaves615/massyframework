/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月15日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
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
