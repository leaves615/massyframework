/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

/**
 * 运行在Massy Framework中的装配件.<br>
 * 装配件为程序代码提供一个边界，管理一组聚合的服务，并为聚合服务注入其所依赖的资源。<br>
 * 
 * <P>
 * 装配件必须有符号名称，符号名称必须唯一，用于在Framework中标识装配件，另外按装配件安装次序，<br>
 * Framework会提供一个长整形数作为其编号， 同样该编号在Framework的一次运行周期中保持唯一。<br>
 * 
 * <P>
 * 装配件生命周期包括以下阶段：
 * <ul>
 * <li>解析状态: 正在自省解析配置文件并声明所需的依赖资源和输出资源</li>
 * <li>准备状态: 等待运行所需的资源就绪，对Framework输出的资源进行匹配</li>
 * <li>就绪状态: 所有运行所需的依赖资源匹配成功，随时可进入工作状态</li>
 * <li>工作状态: 构建{@link AssemblyContext}实例, 并对外输出资源； 当所依赖的资源被回收时，装配件将退回准备阶段</li>
 * </ul>
 * 装配件上下文{@link AssemblyContext}在进入工作状态后被创建，通过{@link AssemblyContext}可以获取装配件内置的聚合服务。<br>
 */
public interface Assembly extends Comparable<Assembly>{
	
	/**
	 * 通过适配类型，获得对应的适配实例
	 * @param adaptType 适配类型
	 * @return {@link T}, 如果不支持的适配类型，可以返回null.
	 */
	<T> T adapt(Class<T> adaptType);

	/**
	 * 编号，当装配件被创建时，由Framework统一分配。<br>
	 * 编号在Framework的一次生命周期中保持恒定和唯一，在下次启动前被清零
	 * @return {@link long}
	 */
	long getAssemblyId();
	
	/**
	 * 装配件上下文, 提供内置的服务的查询。<br>
	 * 装配件上下文仅存活在装配件的工作状态。
	 * @return {@link AssemblyContext},可能返回null.
	 * @see AssemblyContext
	 */
	AssemblyContext getAssmeblyContext();
	
	/**
	 * 当前生命周期的所属状态
	 * @return {@link AssemblyStatus}
	 */
	AssemblyStatus getAssemblyStatus();
		
	/**
	 * 功能介绍说明
	 * @return {@link String}字符串，可以返回null.
	 */
	String getDescription();
	
	/**
	 * 获取初始化参数
	 * @param key 参数的键值
	 * @return {@link String}, 键不存在可以返回null.
	 */
	String getInitParameter(String key);
	
	/**
	 * 获取初始化参数,参数为null时返回缺省值
	 * @param key 参数的键值
	 * @param defaultValue 参数为null时，可返回的缺省值
	 * @return {@link String}
	 */
	String getInitParameter(String key, String defaultValue);
	
	/**
	 * 装配件的友好名称
	 * @return {@link String}字符串，可以返回null.
	 */
	String getName();
	
	/**
	 * 符号名称，由生产商定义，采用组织/项目/模块方式命名，具有唯一性。<br>
	 * 例如: org.massyframework.assembly.core
	 * @return {@link String}
	 */
	String getSymbolicName();
	
	/**
	 * 是否准备就绪，随时可以进入工作状态
	 * @return <code>true</code>已就绪, <code>false</code>未就绪
	 */
	boolean isReady();
	
	/**
	 * 是否进入工作状态
	 * @return <code>true</code>已进入工作状态, <code>false</code>未进入工作状态
	 */
	boolean isWorking();
}
