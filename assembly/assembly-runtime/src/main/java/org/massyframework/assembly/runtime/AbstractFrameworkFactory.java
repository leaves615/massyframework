/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import java.util.Map;

import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkFactory;
import org.massyframework.assembly.FrameworkInitializeLoader;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.util.Asserts;
import org.slf4j.Logger;

/**
 * 实现{@link FrameworkFactory}
 */
abstract class AbstractFrameworkFactory implements FrameworkFactory {

	/**
	 * 
	 */
	public AbstractFrameworkFactory() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkFactory#createFramework(java.util.Map, org.massyframework.assembly.FrameworkInitializeLoader)
	 */
	@Override
	public Framework createFramework(Map<String, String> configuration, FrameworkInitializeLoader initializeLoader)
			throws Exception {
		Asserts.notNull(initializeLoader, "initializeLoader cannot be null.");
		
		//初始化Framework
		AbstractFramework result = this.doCreateFramework();
		this.customizeFramework(result);
		
		Logger logger = LoggerReference.adaptFrom(result);
		if (logger != null){
			logger.debug("Massy Framework created.");
			logger.debug("Massy Framework do launching...");
		}
		
		result.init(configuration, initializeLoader);
		result.getFrameworkLauncher().start();
		
		if (logger != null){
			logger.info("Massy Framework started.");
		}
		
		return result;
	}
	
	/**
	 * 对Framework进行自定义
	 * @param framework {@link AbstractFramework}
	 */
	protected void customizeFramework(AbstractFramework framework){
	}
	
	/**
	 * 创建Framework
	 * @param assemblyLocations 装配件资源路径集合
	 * @return {@link AbstractFramework}
	 */
	protected abstract AbstractFramework doCreateFramework();
}
