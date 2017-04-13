/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import java.util.EventObject;

/**
 * 运行框架事件
 *
 */
public class FrameworkEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3785503650481077481L;
	
	/**
	 * 构造方法
	 * @param source
	 */
	public FrameworkEvent(Framework framework) {
		super(framework);
	}
	
	/**
	 * 运行框架
	 * @return
	 */
	public Framework getFramework(){
		return (Framework)this.source;
	}

}
