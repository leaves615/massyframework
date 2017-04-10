/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import java.util.List;

import org.massyframework.assembly.AssemblyReference;

/**
 * 输出的服务资源
 */
public interface ExportServiceResource extends AssemblyReference {

	/**
	 * 输出类型
	 * @return {@link Class}数组
	 */
	Class<?>[] getExportTypes();
	
	/**
	 * 获取输出服务的属性
	 * @param key 属性的键
	 * @return {@link Object}, 属性不存在返回null.
	 */
	Object getProperty(String key);
	
	/**
	 * 所有属性名称
	 * @return {@link List}
	 */
	List<String> getPropertyNames();
}
