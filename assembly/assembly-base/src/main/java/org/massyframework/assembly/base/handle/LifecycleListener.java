/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*   
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
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
