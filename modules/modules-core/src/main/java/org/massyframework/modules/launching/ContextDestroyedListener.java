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
* @日   期:  2017年5月11日
*/
package org.massyframework.modules.launching;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.Framework;

/**
 * @author huangkaihui
 *
 */
class ContextDestroyedListener implements ServletContextListener {

	/**
	 * 
	 */
	public ContextDestroyedListener() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Framework framework = this.getFramework(sce.getServletContext());
		if (framework != null){
			ExportServiceRepository serviceRepository =
					ExportServiceRepositoryReference.adaptFrom(framework);
			
			List<ExecutorService> executors = serviceRepository.getServices(ExecutorService.class);
			for (ExecutorService executor : executors){
				executor.shutdown();
			}
		}
		
	}
	
	/**
	 * 获取运行框架
	 * @param context
	 * @return
	 */
	protected Framework getFramework(ServletContext context){
		Object result = context.getAttribute(Framework.class.getName());
		if (result != null){
			if (result instanceof Framework){
				return (Framework)result;
			}
		}
		
		return null;
	}

}
