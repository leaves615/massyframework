/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import org.massyframework.assembly.util.Asserts;

/**
 * 运行框架初始化事件
 */
public class FrameworkInitializeEvent extends FrameworkEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683345429399133756L;
	
	private FrameworkServiceRegistry serviceRegistry;

	/**
	 * @param framework
	 */
	public FrameworkInitializeEvent(Framework framework, 
			FrameworkServiceRegistry serviceRegistry) {
		super(framework);
		Asserts.notNull(serviceRegistry, "serviceRegistry cannot be null.");
		this.serviceRegistry = serviceRegistry;
	}
	
	/**
	 * 框架服务注册器
	 * @return {@link FrameworkServiceRegistry}
	 */
	public FrameworkServiceRegistry getFrameworkServiceRegistry(){
		return this.serviceRegistry;
	}
	
}
