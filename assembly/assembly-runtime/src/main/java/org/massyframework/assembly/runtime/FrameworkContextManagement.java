/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.base.handle.support.AssemblyContextWrapper;
import org.massyframework.assembly.base.handle.support.CustomizeAssemblyContextManagement;

/**
 * 运行框架上下文管理器
 */
final class FrameworkContextManagement extends CustomizeAssemblyContextManagement {

	/**
	 * 
	 */
	public FrameworkContextManagement() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.CustomizeAssemblyContextManagement#createAssemblyContext()
	 */
	@Override
	protected AssemblyContext createAssemblyContext() throws Exception {
		return new AssemblyContextWrapper<FrameworkContext>(
				new FrameworkContext());
	}

	
}
