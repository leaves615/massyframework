/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime.service.registry;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 服务Id工厂
 * @author huangkaihui
 *
 */
abstract class ServiceIdFactory {

	private static final AtomicLong COUNT = new AtomicLong(0);
	
	/**
	 * 生成新的服务编号
	 * @return {@link long}
	 */
	public static long genericId(){
		return COUNT.incrementAndGet();
	}
}
