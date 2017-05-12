/**
* @Copyright: 2017 smarabbit studio. 
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*   
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月14日
*/
package org.massyframework.assembly.test;

import java.util.Map;

import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkFactory;
import org.massyframework.assembly.FrameworkInitializeLoader;
import org.massyframework.assembly.util.Asserts;
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
				new SingleableClassLoaderInitializeLoader(null));
	}
	
	/**
	 * 构建运行框架
	 * @param configuratiion 配置项
	 * @param loader 加载器
	 * @throws Exception 发生非预期的例外
	 */
	public static void build(Map<String, String> configuration,
			FrameworkInitializeLoader loader) throws Exception{
		Asserts.notNull(loader, "loader cannot be null.");
		FrameworkFactory factory =
				ServiceLoaderUtils.loadFirstService(FrameworkFactory.class);
		
		INSTANCE = factory.createFramework(null, 
				loader);
	}
	
}
