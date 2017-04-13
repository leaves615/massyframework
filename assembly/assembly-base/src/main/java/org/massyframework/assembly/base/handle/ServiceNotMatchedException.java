/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

/**
 * 未找到指定输出服务资源相匹配服务时抛出的例外
 */
public class ServiceNotMatchedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2789198470821057864L;

	private ExportServiceResource resource;
	
	/**
	 * 
	 */
	public ServiceNotMatchedException(ExportServiceResource resource) {
		super("cannot found service with: resource= " + resource + ".");
		this.resource = resource;
	}

	/**
	 * 输出服务资源
	 * @return {@link ExportServiceResource}
	 */
	public ExportServiceResource getExportServiceResource(){
		return this.resource;
	}
	
}
