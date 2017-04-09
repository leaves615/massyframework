/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import java.util.EventListener;

/**
 * 装配件事件监听器，监听装配件的注册、激活事件<br>
 * {@link Framework#addListener(AssemblyListener)}和{@link Framework#removeListener(AssemblyListener)}提供了<br>
 * 装配件事件监听的添加和移除方法。
 *
 */
public interface AssemblyListener extends EventListener {

	/**
	 * 当装配件发生改变时，触发本方法
	 * @param event {@link AssemblyEvent}装配件事件。
	 */
	void onChanged(AssemblyEvent event);
}
