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
* @日   期:  2017年4月10日
*/
package org.massyframework.assembly.base.handle.support;

import java.util.Collection;
import java.util.Map;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.ServiceNotFoundException;
import org.massyframework.assembly.base.handle.AssemblyInformationHandler;
import org.massyframework.assembly.base.handle.DependencyServiceResource;
import org.massyframework.assembly.base.handle.DependencyServiceResourceHandler;
import org.massyframework.assembly.base.handle.ExportServiceResource;
import org.massyframework.assembly.base.handle.ExportServiceResourceHandler;
import org.massyframework.assembly.base.handle.ResolveHandler;
import org.massyframework.assembly.base.handle.AssemblyContextHandler;
import org.massyframework.assembly.base.support.ImmutableInitParams;
import org.massyframework.assembly.base.support.InitParams;
import org.massyframework.assembly.util.Asserts;

/**
 * 提供配置解析后，设置处理方法的封装
 *
 */
public abstract class AbstractResolver extends AbstractHandler implements ResolveHandler {
	
	/**
	 * 构造方法
	 * @param serviceRegistry 服务注册器
	 * @param validator xml验证器
	 */
	public AbstractResolver() {
	}
	
	/**
	 * 设置装配件信息
	 * @param symbolicName 序号名称
	 * @param name 人性化的友好名称
	 * @param description 说明
	 * @param vendor 生产商
	 */
	protected final void setAssemblyInformation(String symbolicName, String name, String description, String vendor){
		Asserts.notNull(symbolicName, "symbolicName cannot be null.");
		AssemblyInformationHandler handler =
				this.getHandler(AssemblyInformationHandler.class);
		handler.setSymbolicName(symbolicName);
		if (name != null){
			handler.setName(name);
		}
		
		if (description != null){
			handler.setDescription(description);
		}
		
		if (vendor != null){
			handler.setVendor(vendor);
		}
	}
	
	/**
	 * 设置服务容器
	 * @param containerName 容器名称 
	 */
	protected final void setAssemblyContextHandler(String contextHandlerName) throws ServiceNotFoundException{
		ExportServiceRepository repository =
				ExportServiceRepositoryReference.adaptFrom(this.getAssembly());
		AssemblyContextHandler handler = repository.findService(AssemblyContextHandler.class, 
				"(" + Constants.SERVICE_NAME + "=" + contextHandlerName + ")");
		if (handler != null){
			this.getHandlerRegistry().register(handler);
		}else{
			throw new ServiceNotFoundException("service not found: serviceType=" + AssemblyContextHandler.class 
					+ "," + Constants.SERVICE_CNAME + "=" + contextHandlerName + ".");
		}
	}
	
	/**
	 * 设置初始化参数
	 * @param params 参数
	 */
	protected final void setInitParams(Map<String, String> params){
		if (this.findHandler(InitParams.class) == null){
			ImmutableInitParams initParams = new ImmutableInitParams(params);
			this.getHandlerRegistry().register(initParams);
		}
	}

	/**
	 * 添加依赖服务定义
	 * @param resources 依赖服务资源集合
	 */
	protected final void addDependencyServiceResources(Collection<DependencyServiceResource> resources){
		DependencyServiceResourceHandler handler =
				this.getHandler(DependencyServiceResourceHandler.class);
		handler.addAll(resources);
	}
	
	/**
	 * 添加输出服务定义
	 * @param resources 输出服务资源集合
	 */
	protected final void addExportServiceResources(Collection<ExportServiceResource> resources){
		ExportServiceResourceHandler handler =
				this.getHandler(ExportServiceResourceHandler.class);
		handler.addAll(resources);
	}
	
}
