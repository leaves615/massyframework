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
 * 名称已经存在时抛出的例外
 * @author huangkaihui
 *
 */
public class NameExistsException extends AssemblyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2315293642738614312L;

	private final String name;
	/**
	 * 
	 */
	public NameExistsException(String name) {
		super("name has exists: name=" + name + ".");
		this.name = name;
	}
	
	/**
	 * 名称
	 * @return
	 */
	public String getName(){
		return this.name;
	}

	
}
