/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import java.util.List;

import org.massyframework.assembly.spec.Specification;

/**
 * 装配件的运行框架<br>
 * 运行框架管理所有装配件，并提供装配件的安装方法。<br>
 * 
 *
 */
public interface Framework extends Assembly{
	
	/**
	 * 添加装配件事件监听器
	 * @param listener {@link AssemblyListener}事件监听器
	 */
	void addListener(AssemblyListener listener);
	
	/**
	 * 判断指定符号名称的装配件是否存在
	 * @param symbolicName 符号名称
	 * @return <code>true</code>存在，<code>false</code>不存在
	 */
	boolean containsAssembly(String symbolicName);
	
	/**
	 * 使用装配件编号查找装配件
	 * @param assemblyId 装配件编号
	 * @return {@link Assembly},装配件
	 * @throws AssemblyNotFoundException 装配件未找到时抛出异常
	 */
	Assembly getAssembly(long assemblyId) throws AssemblyNotFoundException;
	
	/**
	 * 按符号名称查找装配件
	 * @param symbolicName 符号名称
	 * @return {@link Assembly},装配件
	 * @throws AssemblyNotFoundException 装配件未找到时抛出异常
	 */
	Assembly getAssembly(String symbolicName) throws AssemblyNotFoundException;
	
	/**
	 * 显示所有安装的装配件
	 * @return {@link Assembly}
	 */
	List<Assembly> getAssemblies();
	
	/**
	 * 显示所有满足规则要求的装配件<br>
	 * 使用规则检查器对所有装配件进行遍历，当{@link Specification#isSatisfyBy(Object)}返回true时，将装配件作为返回列表集合元素之一。
	 * @param spec {@link Specification}规则检查器
	 * @return {@link List}
	 */
	List<Assembly> getAssemblies(Specification<Assembly> spec);
	
	/**
	 * 设置初始化参数<br>
	 * 如果初始化参数已经存在，则设置将失败
	 * @param key 初始化参数的键
	 * @param value 初始化参数的值
	 * @return <code>true</code>设置成功, <code>false</code>设置失败
	 */
	boolean setInitParameter(String key, String value);
	
	/**
	 * 移除装配件事件监听器
	 * @param listener
	 */
	void removeListener(AssemblyListener listener);
}