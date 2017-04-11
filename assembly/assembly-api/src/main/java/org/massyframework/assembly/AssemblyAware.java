/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

/**
 * 提供设置装配件的方法的感知接口
 *
 */
public interface AssemblyAware {

	/**
	 * 设置装配件
	 * @param assembly {@link Assembly}
	 */
	void setAssembly(Assembly assembly);
	
	/**
	 * 尝试在目标对象上绑定装配件
	 * <br>当目标对象实现{@link AssemblyAware}接口时，通过{@link #setAssembly(Assembly)}进行绑定。
	 * @param target 目标对象, 可以为null.
	 * @param assembly 装配件, 可以为null.
	 */
	public static <T> void maybeToBind(T target, Assembly assembly){
		if (target == null) return;
		if (target instanceof AssemblyAware){
			((AssemblyAware)target).setAssembly(assembly);
		}
	}
}
