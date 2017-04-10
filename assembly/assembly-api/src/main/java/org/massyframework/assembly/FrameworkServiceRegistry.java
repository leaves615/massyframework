/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import java.util.Map;

/**
 * 提供框架核心服务的注册方法
 */
public interface FrameworkServiceRegistry{

	/**
	 * 添加服务，服务自动作为输出服务供其他装配件使用<br>
	 * @param exportTypes 输出服务的类型
	 * @param service 服务实例
	 * @param props 服务属性, 可以为null
	 */
	void addService(Class<?>[] exportTypes, Object service, 
			Map<String, Object> props);
	
	/**
	 * 添加服务，服务自动作为输出服务供其他装配件使用<br>
	 * @param exportType 输出服务的类型
	 * @param service 服务实例
	 * @param props 服务属性, 可以为null
	 */
	<S> void addService(Class<?> exportType, S service,
			Map<String, Object> props);
	
	/**
	 * 添加服务，服务自动作为输出服务供其他装配件使用<br>
	 * @param exportType 输出服务的类型
	 * @param factory 服务工厂
	 * @param props 服务属性，可以为null
	 */
	<S> void addService(Class<?> exportType, ServiceFactory<S> factory,
			Map<String, Object> props);
	
	/**
	 * 添加服务，服务自动作为输出服务供其他装配件使用<br>
	 * @param exportTypes 输出服务的类型
	 * @param factory 服务工厂
	 * @param props 服务属性，可以为null
	 */
	<S> void addService(Class<?>[] exportTypes, ServiceFactory<S> factory, 
			Map<String, Object> props) throws NameExistsException;
}
