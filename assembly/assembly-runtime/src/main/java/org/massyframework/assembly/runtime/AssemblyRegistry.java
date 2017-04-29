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
* @日   期:  2017年4月11日
*/
package org.massyframework.assembly.runtime;

import java.util.List;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyListener;
import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.NameExistsException;
import org.massyframework.assembly.spec.Specification;

/**
 * 装配件注册器
 *
 */
public interface AssemblyRegistry {
	
	/**
	 * 添加事件监听器
	 * @param listener 装配件事件监听器
	 */
	void addListener(AssemblyListener listener);
	
	/**
	 * 按规则检查器过滤装配件，并返回首个满足条件的装配件
	 * @param spec 规则检查器
	 * @return {@link Assembly}，无满足条件可返回null.
	 */
	Assembly filter(Specification<Assembly> spec);
	
	/**
	 * 按规则检查器过滤装配件，并返回所有满足条件的装配件
	 * @param spec 规则检查器
	 * @return {@link List}
	 */
	List<Assembly> filterAll(Specification<Assembly> spec);
	
	/**
	 * 按编号获取装配件
	 * @param assemblyId 装配件编号
	 * @return {@link Assembly}, 无对应编号可以返回null.
	 */
	Assembly findAssembly(long assemblyId);
	
	/**
	 * 按符号名称查找装配件
	 * @param symbolicName 符号名称
	 * @return {@link Assembly},无对应符号名称可以返回null.
	 */
	Assembly findAssembly(String symbolicName);
	
	/**
	 * 获取所有装配件
	 * @return {@link List}集合
	 */
	List<Assembly> getAssemblies();
	
	/**
	 * 安装并注册装配件
	 * @param location 装配件定义资源的定位
	 * @return {@link AssemblyRegistration}装配件注册凭据
	 * @throws NameExistsException 当发现存有相同装配件名称时，抛出例外
	 * @throws Exception 其他非预期的错误，抛出例外
	 */
	AssemblyRegistration installAndRegister(AssemblyResource resource) throws NameExistsException, Exception;
	
	/**
	 * 移除装配件事件监听器
	 * @param listener 装配件事件监听器
	 */
	void removeListener(AssemblyListener listener);
}
