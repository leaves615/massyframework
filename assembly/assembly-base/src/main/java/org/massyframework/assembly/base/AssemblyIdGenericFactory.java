/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 装配件编号生成器工厂,生成唯一顺序的装配件编号
 *
 */
abstract class AssemblyIdGenericFactory {

	private static AtomicLong id = new AtomicLong(-1);
	
	/**
	 * 生成装配件的编号
	 * @return {@link long}
	 */
	public static long genericAssemblyId(){
		return id.incrementAndGet();
	}

}
