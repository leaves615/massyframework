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
* @日   期:  2017年4月9日
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
