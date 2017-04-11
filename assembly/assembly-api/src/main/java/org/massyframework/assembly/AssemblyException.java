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
 * 装配件例外的基础类
 */
public abstract class AssemblyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5479383335722468918L;

	public AssemblyException() {
		super();
	}

	public AssemblyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AssemblyException(String message, Throwable cause) {
		super(message, cause);
	}

	public AssemblyException(String message) {
		super(message);

	}

	public AssemblyException(Throwable cause) {
		super(cause);

	}


}