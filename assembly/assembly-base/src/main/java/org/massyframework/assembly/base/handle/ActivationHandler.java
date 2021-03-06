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
* @日   期:  2017年4月10日
*/
package org.massyframework.assembly.base.handle;

/**
 * 装配件启动和停止处理器
 */
public interface ActivationHandler extends Handler {

	/**
	 * 开始启动
	 * @throws Exception
	 */
	void doStarting() throws Exception;
	
	/**
	 * 停止处理
	 * @throws Exception
	 */
	void doStopped() throws Exception;
}
