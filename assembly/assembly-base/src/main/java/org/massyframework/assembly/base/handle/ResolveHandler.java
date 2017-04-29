/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*   
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
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
