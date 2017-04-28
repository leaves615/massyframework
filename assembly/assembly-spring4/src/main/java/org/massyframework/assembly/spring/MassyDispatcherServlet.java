/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spring4;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author huangkaihui
 *
 */
public class MassyDispatcherServlet extends DispatcherServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2823237294274000031L;


	/**
	 * 
	 */
	public MassyDispatcherServlet() {
	}
	
	
	public MassyDispatcherServlet(WebApplicationContext webApplicationContext){
		super(webApplicationContext);
	}

}
