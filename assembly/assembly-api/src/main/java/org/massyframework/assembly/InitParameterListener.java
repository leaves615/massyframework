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
* @日   期:  2017年4月13日
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
