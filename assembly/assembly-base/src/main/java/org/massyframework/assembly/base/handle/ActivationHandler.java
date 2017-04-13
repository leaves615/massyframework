/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

/**
 * 装配件启动和停止处理器
 */
public interface ActivationHandler extends Handler {

	/**
	 * 开始启动
	 * @throws Exception
	 */
	void doStarting() throws Exception;
	
	/**
	 * 停止处理
	 * @throws Exception
	 */
	void doStopped() throws Exception;
}
