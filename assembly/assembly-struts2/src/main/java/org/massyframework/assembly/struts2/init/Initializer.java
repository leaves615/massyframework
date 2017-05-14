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
* @日   期:  2017年5月13日
*/
package org.massyframework.assembly.struts2.init;

import java.util.HashMap;
import java.util.Map;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.base.handle.AssemblyContextHandler;

/**
 * Struts2初始化器
 */
public final class Initializer implements FrameworkInitializer {

	/**
	 * 
	 */
	public Initializer() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializer#onStartup(org.massyframework.assembly.Framework)
	 */
	@Override
	public void onStartup(Framework framework) throws Exception {
		if (this.isJ2EE(framework)){
			//注册Struts2的过滤器
			Map<String, Object> props = new HashMap<String, Object>();
			
			props.put(Constants.SERVICE_NAME, new String[]{"struts"});
			props.put(Constants.SERVICE_DESCRIPTION, "提供整合Struts2的AssemblyContext.");
			
			framework.addExportService(AssemblyContextHandler.class, 
					new Struts2AssemblyContextHandlerFactory(), props);
		}
	}

	/**
	 * 判断是否运行在J2EE环境下
	 * @param framework 运行框架
	 * @return <code>true</code>是, <code>false</code>否
	 */
	protected boolean isJ2EE(Framework framework){
		String text = framework.getInitParameter(Constants.ENVIRONMENT);
		return Constants.ENVIRONMENT_J2EE.equals(text);
	}
}
