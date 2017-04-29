/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
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
* @日   期:  2017年4月14日
*/
package org.massyframework.assembly.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.base.handle.AssemblyContextHandler;
import org.massyframework.assembly.base.handle.support.CustomizeAssemblyContextManagement;

/**
 * 初始化
 */
class Initailizer implements FrameworkInitializer {

	/**
	 * 
	 */
	public Initailizer() {

	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializer#onStartup(org.massyframework.assembly.Framework)
	 */
	@Override
	public void onStartup(Framework framework) throws Exception {
		this.initExecutorService(framework);
		this.initCustomizeAssemblyContextManagement(framework);
	}
	
	/**
	 * 初始化执行服务线程数量
	 * @param launchContext
	 */
	protected void initExecutorService(Framework framework){
		String text = framework.getInitParameter(ExecutorService.class.getName());
		int size = text == null ?
				Runtime.getRuntime().availableProcessors() * 2 :
					Integer.parseInt(text);
		ExecutorService service = Executors.newFixedThreadPool(size);
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(Constants.SERVICE_DESCRIPTION, "框架内核ExecutorService线程池，最大线程数量：" + size + ".");
		framework.addExportService(ExecutorService.class, service, props);
	}
	
	/**
	 * 初始化自定义装配件上下文处理器
	 * @param launchContext 启动上下文
	 */
	protected void initCustomizeAssemblyContextManagement(Framework framework){
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(Constants.SERVICE_NAME, new String[]{"customize"});
		framework.addExportService(
				AssemblyContextHandler.class, 
				new CustomizeAssemblyContextManagement(), props);
	}

}
