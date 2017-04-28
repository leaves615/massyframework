/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月14日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spring4.init;

import java.util.HashMap;
import java.util.Map;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.base.handle.AssemblyContextHandler;

/**
 * 初始化方法
 */
public class Initializer implements FrameworkInitializer {

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
		Map<String, Object> props = new HashMap<String, Object>();
		
		props.put(Constants.SERVICE_NAME, new String[]{"spring"});
		props.put(Constants.SERVICE_DESCRIPTION, "提供整合Spring的AssemblyContext.");
		
		framework.addExportService(AssemblyContextHandler.class, 
				new SpringAssemblyContextHanderFactory(), props);
	}

}
