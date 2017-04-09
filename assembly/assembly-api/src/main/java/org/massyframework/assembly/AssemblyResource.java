/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import java.net.URL;

/**
 * 装配件资源
 */
public interface AssemblyResource {

	/**
	 * 装配件资源对应的类加载器
	 * @return {@link ClassLoader}
	 */
	ClassLoader getClassLoader();
	
	/**
	 * 装配件配置文件的URL
	 * @return {@link URL}
	 */
	URL getConfigFile();
}
