/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

/**
 * 服务注入处理器
 */
public interface ServiceInjectHandler {

	/**
	 * 添加注入回调接口
	 * <br>服务注入器在收到回调方法时，会协助获取所由依赖服务，并使用{@link ServiceInjectCallback#doInject(java.util.Map)}
	 * 注入服务实例。方法执行完成后，callback会自动丢弃
	 * @param callback 服务注入回调接口
	 * @throws Exception 发生非预期的异常
	 */
	void addInjectCallback(ServiceInjectCallback callback) throws Exception;
}
