/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import org.massyframework.assembly.base.Registration;

/**
 * 处理器的注册凭据
 */
public interface HandlerRegistration<T> extends Registration {

	/**
	 * 已注册的处理器实例
	 * @return {@link T}
	 */
	T getHandler();
}
