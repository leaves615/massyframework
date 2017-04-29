/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import java.util.EventListener;
import java.util.List;
import java.util.Map;

import org.massyframework.assembly.spec.Specification;

/**
 * 装配件的运行框架<br>
 * 运行框架管理所有装配件，并提供装配件的安装方法。<br>
 */
public interface Framework extends Assembly{
	
	/**
	 * 添加事件监听器， 目前本方法支持{@link FrameworkListener}，<br>
	 * {@link AssemblyListener}和{@link InitParameterListener}三种事件监听器<br>
	 * 其中注册的{@link InitParameterListener}在运行框架启动时被自动丢弃。
	 * 
	 * @param listener {@link EventListener}事件监听器
	 */
	void addListener(EventListener listener);
			
	/**
	 * 添加输出服务，供其他装配件使用<br>
	 * @param exportTypes 输出服务的类型
	 * @param service 服务实例
	 * @param props 服务属性, 可以为null
	 */
	void addExportService(Class<?>[] exportTypes, Object service, 
			Map<String, Object> props);
	
	/**
	 * 添加输出服务，供其他装配件使用<br>
	 * @param exportType 输出服务的类型
	 * @param service 服务实例
	 * @param props 服务属性, 可以为null
	 */
	<S> void addExportService(Class<S> exportType, S service,
			Map<String, Object> props);
	
	/**
	 * 添加输出服务，供其他装配件使用<br>
	 * @param exportType 输出服务的类型
	 * @param factory 服务工厂
	 * @param props 服务属性，可以为null
	 */
	<S> void addExportService(Class<S> exportType, ServiceFactory<S> factory,
			Map<String, Object> props);
	
	/**
	 * 添加输出服务，供其他装配件使用<br>
	 * @param exportTypes 输出服务的类型
	 * @param factory 服务工厂
	 * @param props 服务属性，可以为null
	 */
	<S> void addExportService(Class<?>[] exportTypes, ServiceFactory<S> factory, 
			Map<String, Object> props);
	
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
	 * 安装装配件
	 * @param resource 装配件资源
	 * @return {@link Assembly},已安装的装配件
	 * @throws Exception 安装失败时抛出的例外
	 */
	Assembly installAssembly(AssemblyResource resource) throws Exception;
	
	/**
	 * 设置初始化参数<br>
	 * 如果初始化参数已经存在，则设置将失败
	 * @param key 初始化参数的键
	 * @param value 初始化参数的值
	 * @return <code>true</code>设置成功, <code>false</code>设置失败
	 */
	boolean setInitParameter(String key, String value);
	
	/**
	 * 移除事件监听器
	 * @param listener {@link EventListener} 运行框架监听器
	 */
	void removeListener(EventListener listener);
	
}
