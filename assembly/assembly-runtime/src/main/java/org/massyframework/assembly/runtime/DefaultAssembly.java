/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.base.AbstractAssembly;
import org.massyframework.assembly.base.handle.HandlerRegistry;
import org.massyframework.assembly.runtime.service.registry.ExportServiceRepositoryBuilder;

/**
 * 缺省的装配件
 */
public class DefaultAssembly extends AbstractAssembly {

	/**
	 * 构造方法
	 */
	public DefaultAssembly() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.AbstractAssembly#getExportServiceRepository()
	 */
	@Override
	protected ExportServiceRepository getExportServiceRepository() {
		return ExportServiceRepositoryBuilder.getExportServiceRepository(this);
	}

	@Override
	protected HandlerRegistry getHandlerRegistry(){
		return super.getHandlerRegistry();
	}
}
