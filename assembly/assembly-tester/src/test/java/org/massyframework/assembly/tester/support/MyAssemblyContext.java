/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月14日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.tester.support;

import org.massyframework.assembly.CustmizeAssemblyContext;

/**
 * @author huangkaihui
 *
 */
public class MyAssemblyContext extends CustmizeAssemblyContext {
	
	/**
	 * 
	 */
	public MyAssemblyContext() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.CustmizeAssemblyContext#init()
	 */
	@Override
	protected void init() {
		super.init();
		
		//创建Speak实例
		DefaultSpeak speak = new DefaultSpeak();
		this.addService("speak", speak);
	}
	
	

}
