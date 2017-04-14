/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.modules;

import java.util.Map;

import javax.servlet.ServletContext;

/**
 * 启动器
 */
public interface Launcher {

	/**
	 * 启动
	 * @param configuration 配置项
	 * @param servletContext Servlet上下文
	 * @throws Exception 发生非预期的异常
	 */
	void launch(Map<String, String> configuration, ServletContext servletContext) throws Exception;
}
