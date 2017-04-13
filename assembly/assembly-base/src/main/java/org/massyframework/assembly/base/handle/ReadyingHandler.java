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
 * 准备就绪处理器，在准备就绪和退出就绪时进行处理
 * @author huangkaihui
 *
 */
public interface ReadyingHandler {

	/**
	 * 执行就绪
	 * @throws ReadyingException
	 */
	void doReadying() throws ReadyingException;
	
	/**
	 * 执行退出就绪
	 */
	void doUnreadying();
}
