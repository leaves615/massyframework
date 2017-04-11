/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base;

import org.massyframework.assembly.ExportServiceRepository;

/**
 * @author huangkaihui
 *
 */
public class DefaultAssembly extends AbstractAssembly {

	/**
	 * 构造方法
	 * @param serviceRepository
	 */
	public DefaultAssembly(ExportServiceRepository serviceRepository) {
		super(serviceRepository);
	}

	@Override
	public <T> T adapt(Class<T> adaptType) {
		return null;
	}

}
