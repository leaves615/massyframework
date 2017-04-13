/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

/**
 * 撤销注册处理器
 * @author huangkaihui
 *
 */
public interface RegisterableHandler {
	
	/**
	 * 完成注册
	 */
	void registCompleted();
	
	/**
	 * 装配件取消注册前处理
	 */
	void unregistering();
}
