/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import java.util.Map;

/**
 * 服务注入回调接口
 */
public interface ServiceInjectCallback {

	/**
	 * 注入处理，
	 * @param serviceMap 依赖服务和注入的服务实例
	 * @throws Exception 发生非预期的例外
	 */
	void doInject(Map<DependencyServiceResource,Object> serviceMap) throws Exception;
}
