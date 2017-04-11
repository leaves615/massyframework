/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

/**
 * 装配件内部通过一系列的处理器，每个处理器为装配件提供某种特性的能力，从而完成装配件的全部职责和能力。<br>
 * 本接口为处理器提供初始化和销毁方法
 */
public interface Handler {

	/**
	 * 初始化处理<br>
	 * 在{@link HandlerRegistry#register(Object)}处理过程中，由{@link HandlerRegistry}调用本方法，执行初始化。
	 * @param handlerRegistry 处理注册器
	 */
	void init(HandlerRegistry handlerRegistry);
	
	/**
	 * 销毁处理<br>
	 * 当{@link HandlerRegistration#unregister()}时，由{@link HandlerRegistry}调用本方法。<br>
	 * 在本方法中应该释放持有的资源。
	 */
	void destroy();
}
