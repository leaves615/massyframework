/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle;

import org.massyframework.assembly.AssemblyStatus;

/**
 * 生命周期处理器提供装配件生命周期阶段过渡和转换封装
 */
public interface LifecycleProcessHandler extends Handler {
	
	/**
	 * 添加生命周期事件监听器
	 * @param listener {@link LifecycleListener}事件监听器
	 */
	void addListener(LifecycleListener listener);

	/**
	 * 装配件的状态
	 * @return {@link AssemblyStatus}
	 */
	AssemblyStatus getAssemblyStatus();
	
	/**
	 * 启动,进入工作状态
	 * <br>启动时，可能因为各种原因无法进入工作状态。
	 * <br>具体状态在方法执行完后，可用{@link #getAssemblyStatus()} == {@link AssemblyStatus#WORKING}进行检查。
	 * @throws Exception 启动时发生的非预期例外
	 */
	void start() throws Exception;
	
	/**
	 * 停止，退出工作状态
	 * @throws Exception 停止时发生的非预期例外
	 */
	void stop() throws Exception;
	
	/**
	 * 移除生命周期事件监听器
	 * @param listener {@link LifecycleListener}事件监听器
	 */
	void removeListener(LifecycleListener listener);
}
