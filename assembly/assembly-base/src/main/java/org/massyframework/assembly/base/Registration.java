/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base;

/**
 * 当注册方法调用后，返回注册凭据，使得仅有注册凭据的持有者，具有取消注册的能力
 */
public interface Registration {

	/**
	 * 取消注册
	 */
	void unregister();
}
