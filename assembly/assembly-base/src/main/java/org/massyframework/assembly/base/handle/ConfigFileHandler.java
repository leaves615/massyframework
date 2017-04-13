/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import java.net.URL;

/**
 * 配置文件处理器
 */
public interface ConfigFileHandler {

	/**
	 * 配置文件的URL
	 * @return {@link URL}
	 */
	URL getConfigFile();
}
