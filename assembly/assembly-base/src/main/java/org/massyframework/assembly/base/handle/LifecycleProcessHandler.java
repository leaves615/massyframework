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

import org.massyframework.assembly.AssemblyStatus;

/**
 * 生命周期处理器提供装配件生命周期阶段过渡和转换封装
 */
public interface LifecycleProcessHandler extends Handler {
	
	/**
	 * 添加生命周期事件监听器
	 * @param listener {@link LifecycleListener}事件监听器
	 */
	void addListener(LifecycleListener listener);

	/**
	 * 装配件的状态
	 * @return {@link AssemblyStatus}
	 */
	AssemblyStatus getAssemblyStatus();
	
	/**
	 * 启动,进入工作状态
	 * <br>启动时，可能因为各种原因无法进入工作状态。
	 * <br>具体状态在方法执行完后，可用{@link #getAssemblyStatus()} == {@link AssemblyStatus#WORKING}进行检查。
	 * @throws Exception 启动时发生的非预期例外
	 */
	void start() throws Exception;
	
	/**
	 * 停止，退出工作状态
	 * @throws Exception 停止时发生的非预期例外
	 */
	void stop() throws Exception;
	
	/**
	 * 移除生命周期事件监听器
	 * @param listener {@link LifecycleListener}事件监听器
	 */
	void removeListener(LifecycleListener listener);
}
