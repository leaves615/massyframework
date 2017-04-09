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
 * 服务工厂，提供创建服务实例的方法
 */
public interface ServiceFactory<S> {

	/**
	 * 获取装配件对应的服务实例
	 * @param assembly {@link Assembly}装配件
	 * @return {@link S}
	 */
	S getService(Assembly assembly);
}
