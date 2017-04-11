/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import java.util.List;

/**
 * 装配键输出服务的引用
 */
public interface ExportServiceReference<S> {
	
	/**
	 * 提供服务实例的装配件
	 * @return {@link Assembly}
	 */
	Assembly getAssembly();
	
	/**
	 * 服务的编号，由运行框架生成<br>
	 * 存放在服务属性中，键为{@link Constants#SERVICE_ID}
	 * @return {@link long}
	 */
	long getServiceId();
		
	/**
	 * 获取服务属性
	 * @param key 属性的键
	 * @return {@link Object}, 属性不存在返回null.
	 */
	Object getProperty(String key);
	
	/**
	 * 获取服务属性
	 * @param key 属性的键
	 * @param propType 属性的类型
	 * @return {@link P},属性不存在返回null.
	 */
	<P> P getProperty(String key, Class<P> propType);
	
	/**
	 * 获取所有服务属性的键值
	 * @return {@link List}
	 */
	List<String> getPropertyKeys();
}
