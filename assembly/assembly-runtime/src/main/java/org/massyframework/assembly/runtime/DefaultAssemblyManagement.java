/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import org.massyframework.assembly.ExportServiceRepository;

/**
 * 装配件管理
 */
class DefaultAssemblyManagement extends AbstractAssemblyRegistry {

	/**
	 * @param framework
	 * @param serviceRepository
	 */
	public DefaultAssemblyManagement(AbstractFramework framework, ExportServiceRepository serviceRepository) {
		super(framework, serviceRepository);
	}

}
