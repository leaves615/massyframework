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

import java.util.List;

/**
 * 为运行框架的启动运行提供FrameworkInitializer和AssemblyResource，<br>
 * 并能控制FrameworkInitializer和AssemblyResource的次序。
 */
public interface FrameworkInitializeLoader {
	
	/**
	 * 获取运行框架初始化器<br>
	 * 在{@link FrameworkFactory#createFramework(java.util.Map, InitializeHandler)}方法中，<br>
	 * 调用{@link FrameworkInitializer#onStartup(Framework)}方法进行初始化。
	 * @return {@link List}
	 * @throws Exception 发生非预期的例外
	 */
	List<FrameworkInitializer> getFrameworkInitializer() throws Exception;

	/**
	 * 返回装配件资源<br>
	 * 在{@link FrameworkFactory#createFramework(java.util.Map, InitializeHandler)}方法中，<br>
	 * 使用{@link AssemblyResource}创建对应的装配件
	 * 方法将在{@link FrameworkFactory}
	 * @return {@link List}
	 * @throws Exception 发生非预期的例外
	 */
	List<AssemblyResource> getAssemblyResources() throws Exception;
}
