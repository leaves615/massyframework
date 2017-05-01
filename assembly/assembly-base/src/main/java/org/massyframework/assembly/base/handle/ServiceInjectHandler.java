/**
* @Copyright: 2017 smarabbit studio. 
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

import org.massyframework.assembly.InjectCallback;

/**
 * 服务注入处理器
 */
public interface ServiceInjectHandler {

	/**
	 * 添加注入回调接口
	 * <br>服务注入器在收到回调方法时，会协助获取所由依赖服务，并使用{@link InjectCallback#doInject(java.util.Map)}
	 * 注入服务实例。方法执行完成后，callback会自动丢弃
	 * @param callback 服务注入回调接口
	 * @throws Exception 发生非预期的异常
	 */
	void addInjectCallback(InjectCallback callback) throws Exception;
}
