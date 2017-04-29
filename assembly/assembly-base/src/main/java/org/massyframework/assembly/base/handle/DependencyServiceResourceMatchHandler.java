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

import java.util.Collection;
import java.util.List;

import org.massyframework.assembly.ExportServiceReference;

/**
 * 依赖服务资源匹配处理器
 */
public interface DependencyServiceResourceMatchHandler extends Handler {

	/**
	 * 获取指定依赖服务资源匹配的服务引用
	 * @param resource 依赖服务资源
	 * @return {@link ExportServiceReference}
	 */
	ExportServiceReference<?> getMatchedServiceReference(DependencyServiceResource resource);
	
	/**
	 * 获取未匹配的依赖服务资源
	 * @return {@link List}集合，所有依赖资源都找到匹配项目，返回Empty集合
	 */
	Collection<DependencyServiceResource> getUnmatchDependencyServiceResources();
		
	/**
	 * 判断所有依赖服务找到匹配项
	 * @return <code>true</code>所有依赖服务找到匹配,<code>false</code>还有未匹配的依赖服务
	 */
	boolean isAllMatched();
}
