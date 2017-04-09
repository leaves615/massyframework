/**
* @Copyright: 2017 smarabbit studio Inc. All rights reserved.
*  
* @作   者：  huangkaihui
* @日   期:  2017年3月3日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/

package org.massyframework.assembly;

/**
 * 装配件的生命周期状态
 * <ul>
 * <li>解析状态: 正在自省解析配置文件并声明所需的依赖资源和输出资源</li>
 * <li>准备状态: 等待运行所需的资源就绪，对Framework输出的资源进行匹配</li>
 * <li>就绪状态: 所有运行所需的依赖资源匹配成功，随时可进入工作状态</li>
 * <li>工作状态: 构建{@link AssemblyContext}实例, 并对外输出资源； 当所依赖的资源被回收时，装配件将退回准备阶段</li>
 * </ul>
 *
 */
public enum AssemblyStatus {
	
	/**
	 * 解析状态
	 * <br>装配件正在解析配置文件
	 */
	RESOLVE,
	
	/**
	 * 准备状态
	 * <br>还存有未满足的依赖资源，等待资源准备就绪
	 */
	PREPARE,
	
	/**
	 * 就绪状态
	 * <br>所有依赖资源都已经满足，随时可进入工作状态。
	 * <br>提供Servlet的装配件，应Servlet的延迟加载，可暂时保留在就绪状态。一旦http请求到达，可立刻进入工作状态。
	 */
	READY,
	
	/**
	 * 工作状态
	 * <br>进入工作状态，同时向外部输出资源。
	 * <br>当依赖资源不可用时，会退出工作状态到准备状态
	 */
	WORKING
}
