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
* @日   期:  2017年4月9日
*/
package org.massyframework.assembly;

/**
 * 服务未找到时抛出的例外
 */
public class ServiceNotFoundException extends AssemblyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8558339543321329085L;
	
	/**
	 * @param message
	 */
	public ServiceNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
