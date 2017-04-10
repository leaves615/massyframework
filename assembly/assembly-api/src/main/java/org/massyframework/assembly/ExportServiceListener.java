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
 * 输出服务监听器,监听运行框架的服务注册事件<br>
 * {@link ExportServiceRepository#addListener(ServiceListener, Filter)}和<br>
 * {@link ExportServiceRepository#removeListener(ServiceListener)}提供了<br>
 * 服务事件监听的添加和移除方法
 */
public interface ExportServiceListener extends EventListener{

	/**
	 * 当发生服务注册和撤销注册时，触发本方法
	 * @param event {@link ServiceEvent}服务事件
	 */
	void onChanged(ServiceEvent event);
}
