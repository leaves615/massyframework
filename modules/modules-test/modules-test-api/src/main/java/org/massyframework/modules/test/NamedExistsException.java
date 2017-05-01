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
* @日   期:  2017年4月30日
*/
package org.massyframework.modules.test;

/**
 * 命名已经存在时抛出的例外
 */
public class NamedExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4091887178500945561L;
	
	private String name;

	/**
	 * 
	 */
	public NamedExistsException(String name) {
		super("name is exists: name=" + name + ".");
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	
}
