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
* @日   期:  2017年4月9日
*/
package org.massyframework.assembly;

/**
 * 服务工厂，提供创建服务实例的方法
 */
public interface ServiceFactory<S> {

	/**
	 * 获取装配件对应的服务实例
	 * @param assembly {@link Assembly}装配件
	 * @return {@link S}
	 */
	S getService(Assembly assembly);
}
