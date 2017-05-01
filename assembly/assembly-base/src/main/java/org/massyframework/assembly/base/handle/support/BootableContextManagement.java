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
* @日   期:  2017年4月12日
*/
package org.massyframework.assembly.base.handle.support;

import org.massyframework.assembly.base.support.InitParams;

/**
 * 包含在Servlet、Filter和ContextListener对象内的，必须首先使得Servlet、Filter或者COntextListener可用后，
 * 装配件方能进入工作状态的装配件上下文管理器。
 */
public abstract class BootableContextManagement<B>
	extends AssemblyContextManagement{

	/**
	 * 
	 */
	public BootableContextManagement() {
	}
	
	/**
	 * 初始化引导组件
	 * @param initParams 装配件的初始化参数
	 */
	protected abstract B createBootable(InitParams initParams) throws Exception;
	
	/**
	 * 获取可引导组件
	 * @return {@link B}, 可能返回null.
	 */
	protected abstract B getBootable();
	
	/**
	 * 释放引导组件
	 */
	protected abstract void destroyBootable();
}
