/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import org.massyframework.assembly.AssemblyException;

/**
 * 当指定类型的处理器不存在时，抛出的例外
 */
public final class HandlerNotFoundException extends AssemblyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4474789388612801988L;

	private final Class<?> handlerType;
	
	/**
	 * 构造方法
	 * @param handlerType
	 */
	public HandlerNotFoundException(Class<?> handlerType) {
		super("handler cannot be found: type=" + handlerType + ".");
		this.handlerType = handlerType;
	}

	/**
	 * 处理器类型
	 * @return {@link Class}
	 */
	public Class<?> getHandlerType(){
		return this.handlerType;
	}
}
