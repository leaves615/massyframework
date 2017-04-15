/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月15日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

/**
 * 运行框架启动器, 提供运行框架的启动和停止能力
 */
public interface FrameworkLauncher {

	/**
	 * 启动
	 * @throws Exception 发生启动的异常
	 */
	void start() throws Exception;
	
	/**
	 * 停止
	 */
	void stop() throws Exception;
}
