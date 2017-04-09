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
