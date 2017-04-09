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
import java.util.Map;

/**
 * 装配件上下文<br>
 * 当装配件进入运行状态后，有且只有一个与之关联的上下文,当装配件钝化时，其关联的上下文也同时被回收。<br>
 * 
 * <p>
 * 装配件上下文提供与装配件关联的内置服务
 *
 */
public interface AssemblyContext {

	/**
	 * 获取关联的装配件
	 * @return {@link Assembly}
	 */
	Assembly getAssembly();
	
	/**
	 * 判断指定名称的服务是否存在
	 * <br>内置服务必须有一个不能重复的名称，根据名称可以判断内置服务是否存在。
	 * @param cName 服务在上下文中的名称，不能重复，具有唯一性
	 * @return <code>true</code>存在，<code>false</code>不存在
	 */
	boolean containsService(String cName);
	
	/**
	 * 获取服务实例
	 * @param cName 服务在上下文中的名称，不能重复，具有唯一性
	 * @return {@link Object}, 服务实例
	 * @throws ServiceNotFoundException 服务未找到时，抛出的例外
	 */
	Object getService(String cName) throws ServiceNotFoundException;
	
	/**
	 * 获取服务实例，并返回指定类型<br>
	 * 本方法直接使用resultType的类名作为服务的cName，使用{@link #getService(String, Class)}返回结果
	 * @param resultType 要求返回的类型
	 * @return {@link S}, 服务实例
	 * @throws ServiceNotFoundException 服务未找到时，抛出的例外
	 */
	<S> S getService(Class<S> resultType) throws ServiceNotFoundException;
	
	/**
	 * 获取服务实例,并返回指定类型
	 * @param cName 服务在上下文中的名称，不能重复，具有唯一性
	 * @param resultType 要求返回的类型
	 * @return {@link S}, 服务实例
	 * @throws ServiceNotFoundException 服务未找到时，抛出的例外
	 */
	<S> S getService(String cName, Class<S> resultType) throws ServiceNotFoundException;
	
	/**
	 * 所有上下文中所有的服务名称
	 * @return {@link List}
	 */
	List<String> getServiceNames();
	
	/**
	 * 获取支持返回类型的所有服务。
	 * @param resultType 要求返回的类型
	 * @return {@link Map}, key为服务的cName, value为服务实例
	 */
	<S> Map<String, S> getServicesOfType(Class<S> resultType);
}
