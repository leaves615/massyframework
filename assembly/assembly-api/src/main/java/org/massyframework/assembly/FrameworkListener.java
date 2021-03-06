/**
* @Copyright: 2017 smarabbit studio. 
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
 * 运行框架事件监听器
 */
public interface FrameworkListener extends EventListener{
	
	/**
	 * 运行框架初始化完成后触发事件。<br>
	 * 原则上手工安装装配件逻辑应置于此
	 * @param event {@link FrameworkEvent}
	 */
	void frameworkInitialized(FrameworkEvent event);
		
	/**
	 * 运行框架释放前触发本事件
	 * @param event {@link FrameworkEvent}
	 */
	void frameworkDestroyed(FrameworkEvent event);
}
