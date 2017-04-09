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
 * 提供在运行框架初始化阶段的初始化接口<br>
 * 实现本接口的方法，可以在运行阶段重定义运行框架的配置参数、注册系统核心服务。
 */
public interface FrameworkInitializer {

	/**
	 * 启动
	 * @param framework 运行框架
	 * @throws Exception
	 */
	void onStartup(Framework framework) throws Exception;
}
