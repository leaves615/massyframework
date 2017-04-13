/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.base.Registration;

/**
 * 装配件注册凭据
 * @author huangkaihui
 *
 */
public interface AssemblyRegistration extends Registration {

	/**
	 * 注册的装配件
	 * @return {@link Assembly}
	 */
	Assembly getAssembly();
}
