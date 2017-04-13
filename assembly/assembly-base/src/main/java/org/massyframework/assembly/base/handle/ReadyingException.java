/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import org.massyframework.assembly.AssemblyException;

/**
 * 就绪准备例外
 */
public class ReadyingException extends AssemblyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3285434576827978904L;

	/**
	 * 
	 */
	public ReadyingException() {
	}

	/**
	 * @param message
	 */
	public ReadyingException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ReadyingException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ReadyingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ReadyingException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
