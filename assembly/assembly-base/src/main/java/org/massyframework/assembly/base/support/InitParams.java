/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.support;

import java.util.List;

/**
 * 初始化参数
 */
public interface InitParams {

	/**
	 * 初始化参数是否存在
	 * @param key 参数键
	 * @return <code>true</code>参数存在, <code>false</code>参数不存在
	 */
	boolean containsParameter(String key);

	/**
	 * 按键获取初始化参数值
	 * @param key 键
	 * @return {@link String}
	 */
	String getParameter(String key);
	
	/**
	 * 获取所有的初始化参数键
	 * @return {@link List}
	 */
	List<String> getParameterKeys();
}
