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
package org.massyframework.assembly.base;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 装配件编号生成器工厂,生成唯一顺序的装配件编号
 *
 */
abstract class AssemblyIdFactory {

	private static AtomicLong id = new AtomicLong(-1);
	
	/**
	 * 生成装配件的编号
	 * @return {@link long}
	 */
	public static long genericAssemblyId(){
		return id.incrementAndGet();
	}

}
