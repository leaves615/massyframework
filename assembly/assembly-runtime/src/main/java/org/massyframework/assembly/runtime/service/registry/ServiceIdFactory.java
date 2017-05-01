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
* @日   期:  2017年4月10日
*/
package org.massyframework.assembly.runtime.service.registry;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 服务Id工厂
 * @author huangkaihui
 *
 */
abstract class ServiceIdFactory {

	private static final AtomicLong COUNT = new AtomicLong(0);
	
	/**
	 * 生成新的服务编号
	 * @return {@link long}
	 */
	public static long genericId(){
		return COUNT.incrementAndGet();
	}
}
