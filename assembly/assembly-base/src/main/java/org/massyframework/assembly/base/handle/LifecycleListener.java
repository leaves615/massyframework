/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import java.util.EventListener;

/**
 * 生命周期事件监听器
 */
public interface LifecycleListener extends EventListener{

	/**
	 * 装配键已安装
	 */
	void onInstalled();

	/**
	 * 配置解析完成
	 */
	void onResolved();
	
	/**
	 * 已准备就绪
	 */
	void onReadied();
	
	/**
	 * 已激活，装配件进入工作状态
	 */
	void onActivated();
	
	/**
	 * 正在钝化，装配件准备退出工作状态
	 */
	void onInactivating();
	
	/**
	 * 退出准备就绪
	 */
	void onUnreadying();
	
	/**
	 * 装配件准备卸载
	 */
	void onUninstalling();
}
