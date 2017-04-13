/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import org.massyframework.assembly.AssemblyReference;

/**
 * 依赖服务资源
 */
public interface DependencyServiceResource extends AssemblyReference{

	/**
	 * 依赖服务被注入后，在装配件上下文中的名称
	 * @return {@link String}
	 */
	String getCName();
	
	/**
	 * 依赖服务的类型
	 * @return {@link Class}
	 */
	Class<?> getRequiredType();
	
	/**
	 * 服务筛选条件
	 * @return {@link String}
	 */
	String getFilterString();
}
