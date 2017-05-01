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

import java.util.Collection;
import java.util.List;

/**
 * 管理装配件的输出服务资源<br>
 * 在装配件进入工作状态后，必须向运行框架输出的服务资源<br>
 * 如果装配件无输出服务资源，则使用{@link HandlerRegistry#findHandler(ExportServiceResourceHandler.class)}方法返回null.
 */
public interface ExportServiceResourceHandler extends Handler {

	/**
	 * 注册输出服务资源
	 * @param resource {@link ExportServiceResource} 输出服务资源
	 */
	void add(ExportServiceResource resource);
	
	/**
	 * 注册输出服务资源集合
	 * @param resources {@link Collection}输出服务资源集合
	 */
	void addAll(Collection<ExportServiceResource> resources);
	
	/**
	 * 所有的输出服务资源
	 * @return {@link List}输出服务资源集合
	 */
	List<ExportServiceResource> getExportServiceResources(); 
}
