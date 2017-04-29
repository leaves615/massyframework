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
* @日   期:  2017年4月15日
*/
package org.massyframework.assembly.runtime;

/**
 * 运行框架启动器, 提供运行框架的启动和停止能力
 */
public interface FrameworkLauncher {

	/**
	 * 启动
	 * @throws Exception 发生启动的异常
	 */
	void start() throws Exception;
	
	/**
	 * 停止
	 */
	void stop() throws Exception;
}
