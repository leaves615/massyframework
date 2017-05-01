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

/**
 * 提供在运行框架初始化阶段的初始化接口<br>
 * 实现本接口的方法，可以在运行阶段重定义运行框架的配置参数、注册系统核心服务。
 */
public interface FrameworkInitializer {

	/**
	 * 启动
	 * @param framework 运行框架
	 * @throws Exception
	 */
	void onStartup(Framework framework) throws Exception;
}
