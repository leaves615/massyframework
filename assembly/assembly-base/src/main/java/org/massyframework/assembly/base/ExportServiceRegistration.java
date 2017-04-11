/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base;

import org.massyframework.assembly.ExportServiceReference;

/**
 * 提供输出服务注销的方法，仅持有服务注册凭据的对象方可注销输出服务
 */
public interface ExportServiceRegistration<S> extends Registration{

	/**
	 * 获取服务引用
	 * @return {@link ExportServiceReference}
	 */
	ExportServiceReference<S> getReference();
}
