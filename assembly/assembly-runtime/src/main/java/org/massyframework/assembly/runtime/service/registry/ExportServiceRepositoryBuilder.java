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
package org.massyframework.assembly.runtime.service.registry;

import java.util.HashMap;
import java.util.Map;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.base.ExportServiceRegistry;
import org.massyframework.assembly.util.Asserts;

/**
 * 输出服务仓储构建工具
 */
public abstract class ExportServiceRepositoryBuilder {

	private static ExportServiceRepositoryManagement MANAGEMENT;
	
	/**
	 * 创建输出服务仓储
	 * @param assembly 对应的装配件
	 * @return {@link ExportServiceRepository}
	 */
	public static synchronized ExportServiceRepository getExportServiceRepository(Assembly assembly){
		Asserts.notNull(assembly, "assembly cannot be null.");
		if (MANAGEMENT == null){
			MANAGEMENT = new ExportServiceRepositoryManagement();
			
			//注册输出服务注册器服务
			Map<String, Object> props = new HashMap<String, Object>();
			props.put(Constants.SERVICE_DESCRIPTION, "系统缺省的输出服务注册器");
			props.put(Constants.SERVICE_RANKING, Integer.MAX_VALUE);
			MANAGEMENT.doRegister(new Class<?>[]{ExportServiceRegistry.class}, MANAGEMENT, props, assembly);
		}
		
		return MANAGEMENT.getExportServiceRepository(assembly);
	}

}
