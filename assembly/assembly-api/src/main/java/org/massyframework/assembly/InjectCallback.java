/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月13日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import java.util.Map;

/**
 * 由框架使用，向AssemblyContext回调注入服务
 */
public interface InjectCallback {

	/**
	 * 执行服务注入
	 * @param serviceMap 服务Map,key为cName, value为服务实例
	 * @throws Exception 发生非预期的例外
	 */
	void doInject(Map<String, Object> serviceMap) throws Exception;
}
