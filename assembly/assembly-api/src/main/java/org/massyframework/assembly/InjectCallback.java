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
* @日   期:  2017年4月13日
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
