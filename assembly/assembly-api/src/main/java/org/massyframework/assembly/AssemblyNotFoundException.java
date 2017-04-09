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
 * @author huangkaihui
 *
 */
public class AssemblyNotFoundException extends AssemblyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3214871729074511876L;

	/**
	 * 
	 */
	public AssemblyNotFoundException() {
		
	}

	/**
	 * @param message
	 */
	public AssemblyNotFoundException(String symbolicName) {
		super("Assembly cannot be found: symbolicName=" + symbolicName + ".");
	}

	/**
	 * @param cause
	 */
	public AssemblyNotFoundException(long assemblyId) {
		super("Assembly cannot be found: assemblyId=" + assemblyId + ".");
	}

	
}
