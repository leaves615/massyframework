/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月14日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle.support;

import org.massyframework.assembly.base.handle.DependencyServiceResourceHandler;
import org.massyframework.assembly.base.handle.ExportServiceResourceHandler;
import org.massyframework.assembly.base.handle.LifecycleProcessHandler;
import org.massyframework.assembly.base.handle.ServiceInjectHandler;

/**
 * 缺省的处理器工厂
 */
abstract class DefaultHandlerFactory {

	/**
	 * 创建处理器
	 * @param handlerType {@link Class}
	 * @return {@link H}, 可能返回null.
	 */
	public static <H> H createHandler(Class<H> handlerType){
		if (handlerType == LifecycleProcessHandler.class ){
			return handlerType.cast(
					new LifecycleManagement());
		}
		
		if (handlerType == DependencyServiceResourceHandler.class){
			return handlerType.cast(
					new DependencyServiceResourceMatcher());
		}
		
		if (handlerType == ExportServiceResourceHandler.class){
			return handlerType.cast(
					new ExportServiceResourceRepository());
		}
		
		if (handlerType == ServiceInjectHandler.class){
			return handlerType.cast(new ServiceInjector());
		}
		
		return null;
	}
}
