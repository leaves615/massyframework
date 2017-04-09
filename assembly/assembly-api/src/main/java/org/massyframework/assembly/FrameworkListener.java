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
 * 运行框架事件监听器
 */
public interface FrameworkListener extends EventListener{

	/**
	 * 运行框架初始化完成后触发事件。<br>
	 * 在本事件处理过程中，可以注册框架服务。
	 * @param event {@link FrameworkInitializeEvent}
	 */
	void frameworkInitialized(FrameworkInitializeEvent event);
	
	/**
	 * 运行框架释放前触发本事件
	 * @param event {@link FrameworkEvent}
	 */
	void frameworkDestroyed(FrameworkEvent event);
}
