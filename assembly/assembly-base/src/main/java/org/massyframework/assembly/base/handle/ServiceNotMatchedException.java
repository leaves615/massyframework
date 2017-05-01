/**
* @Copyright: 2017 smarabbit studio. 
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*   
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
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
