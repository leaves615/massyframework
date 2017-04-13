/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
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
