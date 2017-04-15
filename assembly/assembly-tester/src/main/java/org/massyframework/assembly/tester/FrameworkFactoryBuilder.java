/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月14日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.tester;

import java.util.Map;

import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkFactory;
import org.massyframework.assembly.FrameworkInitializeLoader;
import org.massyframework.assembly.util.ServiceLoaderUtils;

/**
 * 框架构成构建器
 */
public class FrameworkFactoryBuilder {

	private static volatile Framework INSTANCE;
	
	/**
	 * 获取缺省的运行框架
	 * @return {@link Framework}
	 */
	public static Framework getDefault(){
		return INSTANCE;
	}
	
	/**
	 * 是否实例已经构建并存在
	 * @return <code>true</code>存在,<code>false</code>不存在
	 */
	public static boolean exists(){
		return INSTANCE != null;
	}
	
	/**
	 * 构建运行框架
	 * @param configuration 配置项
	 * @throws Exception 发生非预期的例外
	 */
	public static void build(Map<String, String> configuration) throws Exception{
		build(configuration, 
				new StandaloneFrameworkInitializeLoader(null));
	}
	
	/**
	 * 构建运行框架
	 * @param configuratiion 配置项
	 * @param loader 加载器
	 * @throws Exception 发生非预期的例外
	 */
	public static void build(Map<String, String> configuration,
			FrameworkInitializeLoader loader) throws Exception{
		FrameworkFactory factory =
				ServiceLoaderUtils.loadFirstService(FrameworkFactory.class);
		
		INSTANCE = factory.createFramework(null, 
				new StandaloneFrameworkInitializeLoader(null));
	}
	
}
