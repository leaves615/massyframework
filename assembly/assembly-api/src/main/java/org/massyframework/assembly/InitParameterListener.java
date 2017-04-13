/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月13日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import java.util.EventListener;

/**
 * 初始化参数监听器,监听运行框架初始化参数的变化事件
 */
public interface InitParameterListener extends EventListener{
	
	/**
	 * 初始化参数更改后，触发本事件
	 * @param event {@link InitParameterEvent}, 初始化参数更改事件
	 */
	void onParameterChange(InitParameterEvent event);
}
