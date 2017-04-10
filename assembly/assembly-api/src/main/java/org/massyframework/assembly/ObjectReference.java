/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

/**
 * 对象引用接口
 */
public interface ObjectReference<T> {

	/**
	 * 获取引用对象
	 * @return {@link T}
	 */
	T getReference();
}
