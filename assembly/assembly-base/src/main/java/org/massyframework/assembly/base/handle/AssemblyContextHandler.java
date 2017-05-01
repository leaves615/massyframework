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

import java.util.List;
import java.util.Map;

/**
 * 装配件上下文处理器，负责决定何时构建装配件上下文，提供输出的服务实例等
 */
public interface AssemblyContextHandler extends Handler {

	/**
	 * 是否可以进入工作状态
	 * <br>类似于Spring DispatcherServlet的容器，只有在DispatcherServlet初始化后，才能进入工作状态
	 * @return <code>true</code>可以进入工作状态, <code>false</code>因某种原因不能进入工作状态
	 */
	boolean canWork();
				
	/**
	 * 获取需要可输出的服务资源
	 * @param resources 输出服务资源集合
	 * @return {@link Map}输出服务资源和对应的服务实例
	 * @throws Exception 获取服务资源时发生非预期异常
	 */
	Map<ExportServiceResource, Object> getExportableServices(List<ExportServiceResource> resources) throws Exception;
}
