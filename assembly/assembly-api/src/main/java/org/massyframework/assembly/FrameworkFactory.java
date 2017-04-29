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

import java.util.Map;

/**
 * 装配件运行框架的工厂，负责启动和加载装配件运行框架
 */
public interface FrameworkFactory {
	
	/**
	 * 创建运行框架
	 * @param configuration 运行框架的初始化配置参数
	 * @param initializeLoader 初始化加载器
	 * @return {@link Framework}
	 * @throws Exception 创建时发生非预期的例外
	 */
	Framework createFramework(Map<String, String> configuration, 
			FrameworkInitializeLoader initializeLoader) throws Exception;
}
