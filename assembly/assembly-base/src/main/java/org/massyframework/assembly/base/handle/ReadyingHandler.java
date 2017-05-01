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
* @日   期:  2017年4月11日
*/
package org.massyframework.assembly.base.handle;

/**
 * 准备就绪处理器，在准备就绪和退出就绪时进行处理
 * @author huangkaihui
 *
 */
public interface ReadyingHandler {

	/**
	 * 执行就绪
	 * @throws ReadyingException
	 */
	void doReadying() throws ReadyingException;
	
	/**
	 * 已退出就绪
	 */
	void doUnreadied();
}
