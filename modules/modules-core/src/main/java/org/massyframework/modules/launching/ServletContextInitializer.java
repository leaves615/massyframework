/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.modules.launching;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.util.Asserts;

/**
 * @author huangkaihui
 *
 */
public class ServletContextInitializer implements FrameworkInitializer {

	private final ServletContext context;
	/**
	 * 
	 */
	public ServletContextInitializer(ServletContext context) {
		Asserts.notNull(context, "context cannot be null.");
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializer#onStartup(org.massyframework.assembly.Framework)
	 */
	@Override
	public void onStartup(Framework framework) throws Exception {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(Constants.SERVICE_DESCRIPTION, "当前Web应用程序的Servlet上下文.");
		props.put(Constants.SERVICE_RANKING, Integer.MAX_VALUE);
		
		framework.addExportService(ServletContext.class, this.context, props);
	}

}
