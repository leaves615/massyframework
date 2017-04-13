/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

/**
 * 装配件信息处理器,提供设置装配件基本信息的方法
 */
public interface AssemblyInformationHandler {
	
	/**
	 * 设置友好名称
	 * @param name 人性化的友好名称
	 */
	void setName(String name);

	/**
	 * 设置说明
	 * @param description 说明
	 */
	void setDescription(String description);
	
	/**
	 * 设置装配件的符号名称
	 * @param symbolicName 符号名称
	 * @return <code>true</code>设置成功,<code>false</code>符号名称已存在，设置失败
	 */
	boolean setSymbolicName(String symbolicName);
	
	/**
	 * 设置生产商
	 * @param vendor 生产商
	 */
	void setVendor(String vendor);
}
