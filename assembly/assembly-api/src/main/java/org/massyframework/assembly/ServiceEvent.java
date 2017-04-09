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
 * 服务注册和注销事件
 */
public final class ServiceEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9020282981984430491L;

	public static final int REGISTED = 0;
	public static final int UNREGISTERING = 1;
	
	private final int type;
	/**
	 * @param source
	 */
	public ServiceEvent(ServiceReference<?> reference, int type) {
		super(reference);
		if (type != 0 && type != 1){
			throw new IllegalStateException("Invalid service event type: type=" + type + ".");
		}
		this.type = type;
	}
	
	/**
	 * 服务引用
	 * @return {@link ServiceReference}
	 */
	public ServiceReference<?> getServiceReference(){
		return (ServiceReference<?>)this.source;
	}
	
	/**
	 * 服务事件类型
	 * <ul>
	 * <li>{@link #REGISTED}服务注册完成</li>
	 * <li>{@link #UNREGISTERING}服务正在撤销注册</li>
	 * </ul>
	 * @return {@link int}
	 */
	public int getType(){
		return this.type;
	}

}
