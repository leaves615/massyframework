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
 * 装配件运行框架的工厂，负责启动和加载装配件运行框架
 */
public interface FrameworkFactory {
	
	/**
	 * 创建运行框架
	 * @param configuration 运行框架的初始化配置参数
	 * @param locator 装配件的定位器
	 * @return {@link Framework}
	 * @throws Exception 创建时发生非预期的例外
	 */
	Framework createFramework(Map<String, String> configuration, 
			FrameworkInitializeHandler locator) throws Exception;
}
