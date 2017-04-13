/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import java.io.IOException;

import org.massyframework.assembly.Assembly;

/**
 * 解析配置文件，生成装配件基本信息、初始化参数、依赖资源和输出资源等。
 */
public interface ResolveHandler {
	
	/**
	 * 从{@link Assembly#getLocation()}处加载配置文件，并进行解析
	 * @throws IOException 发生非预期的读写异常
	 * @throws Exception 其他非预期的异常
	 */
	void doResolve() throws IOException, Exception;
}
