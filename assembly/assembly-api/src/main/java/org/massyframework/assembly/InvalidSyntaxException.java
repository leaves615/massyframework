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
 * 无效的语法，当筛选字符串不满足语法要求时，抛出例外
 */
public class InvalidSyntaxException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7280454903203396108L;

	
	private final String message;
	private final String filter;
	
	/**
	 * 构造方法
	 * @param msg 错误消息
	 * @param filter 过滤串
	 */
	public InvalidSyntaxException(String msg, String filter) {
		super(msg);
		this.message = msg;
		this.filter = filter;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

}
