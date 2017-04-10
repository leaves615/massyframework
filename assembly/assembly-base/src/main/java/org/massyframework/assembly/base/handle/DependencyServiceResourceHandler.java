/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import java.util.Collection;
import java.util.List;

/**
 * 管理装配件的依赖服务资源<br>
 * 依赖服务资源是装配件在进入工作状态前必须被满足的外部资源<br>
 * 如果装配件无依赖服务要求，则使用{@link HandlerRegistry#findHandler(DependencyServiceResourceHandler.class)}方法返回null.
 */
public interface DependencyServiceResourceHandler extends Handler {

	/**
	 * 添加依赖服务资源
	 * @param resource 依赖服务资源
	 */
	void add(DependencyServiceResource resource);
	
	/**
	 * 添加依赖服务资源集合
	 * @param resources 依赖服务资源集合
	 */
	void addAll(Collection<DependencyServiceResource> resources);
		
	/**
	 * 所有依赖服务资源
	 * @return {@link List}依赖服务资源集合，无依赖服务返回Empty集合
	 */
	List<DependencyServiceResource> getDependencyServiceResources(); 
}
