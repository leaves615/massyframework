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
 * 自定义的输出服务跟踪接口
 */
public interface ExportServiceTrackerCustomizer<T> {

	/**
	 * 添加服务
	 * @param referecne 输出服务引用
	 * @param service 服务实例
	 */
	void addService(ExportServiceReference<T> referecne, T service);
	
	/**
	 * 移除服务
	 * @param reference 输出服务引用
	 * @param service 服务实例
	 */
	void removeService(ExportServiceReference<T> reference, T service);
}
