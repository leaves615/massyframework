/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import java.util.List;
import java.util.Map;

import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkFactory;
import org.massyframework.assembly.FrameworkInitializeHandler;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.util.Asserts;
import org.massyframework.assembly.util.MapUtils;
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
	 * @see org.massyframework.assembly.FrameworkFactory#createFramework(java.util.Map, org.massyframework.assembly.FrameworkInitializeHandler)
	 */
	@Override
	public Framework createFramework(Map<String, String> configuration, FrameworkInitializeHandler initializeHandler)
			throws Exception {
		Asserts.notNull(initializeHandler, "initializeHandler cannot be null.");
		
		//初始化Framework
		AbstractFramework result = this.doCreateFramework();
		this.customizeFramework(result);
		
		Logger logger = LoggerReference.adaptFrom(result);
		if (logger != null){
			logger.debug("Massy Framework created.");
			logger.debug("Massy Framework do launching...");
		}
		
		//设置参数
		this.setInitParameters(result, configuration, logger);
		this.doStart(result, initializeHandler, logger);
				
		this.installAssemblies(result, initializeHandler, logger);
		
		result.start();
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
	 * 设置初始化参数
	 * @param framework 运行框架
	 * @param params 参数
	 */
	protected void setInitParameters(AbstractFramework framework, Map<String, String> params, Logger logger){
		if (!MapUtils.isEmpty(params)){
			FrameworkInitParams initParams = framework.getFrameworkInitParams();
			List<String> unsets = initParams.setParameters(params);
			if (!unsets.isEmpty()){
				//显示异常
				if (logger != null){
					if (logger.isWarnEnabled()){
						StringBuilder builder = new StringBuilder();
						builder.append("unset init parameter, because it exits:").append("\r\n");
						for (String unset: unsets){
							builder.append("\t")
								.append(unset).append("= ").append(params.get(unset))
								.append("\r\n");
						}
						
						logger.warn(builder.toString());
					}
				}
			}
		}
	}
	
	/**
	 * 执行初始化
	 * @param framework
	 * @param initializeHandler
	 * @param logger
	 */
	protected void doStart(AbstractFramework framework, FrameworkInitializeHandler initializeHandler, Logger logger){
		List<FrameworkInitializer> initializers = initializeHandler.getFrameworkInitializer();
		for (FrameworkInitializer initializer: initializers){
			try{
				initializer.onStartup(framework);
				if (logger != null){
					if (logger.isDebugEnabled()){
						logger.debug(initializer + " onStartup() invoked.");
					}
				}
			}catch(Exception e){
				if (logger != null){
					if (logger.isErrorEnabled()){
						logger.error( initializer + " onStartup() invoke failed.", e);
					}
				}
			}
		}
		
		//设置为只读模式
		FrameworkInitParams initParams = framework.getFrameworkInitParams();
		initParams.setReadOnly();
		
		//显示所有参数
		if (logger != null){
			if (logger.isInfoEnabled()){
				List<String> keys = initParams.getParameterKeys();
				StringBuilder builder = new StringBuilder();
				builder.append("framework starting with:").append("\r\n");
				for(String key: keys){
					builder.append("\t")
						.append(key).append("= ").append(initParams.getParameter(key))
						.append("\r\n");
				}
				
				logger.info(builder.toString());
			}
		}
	}
	
	/**
	 * 安装装配件
	 * @param framework 运行框架
	 * @param initializeHandler 初始化处理器
	 * @param logger 日志记录器
	 * @throws Exception
	 */
	protected void installAssemblies(AbstractFramework framework, 
			FrameworkInitializeHandler initializeHandler, Logger logger) throws Exception{
		List<AssemblyResource> resources = initializeHandler.getAssemblyResources();
		for (AssemblyResource resource: resources){
			try{
				framework.installAssembly(resource);
			}catch(Exception e){
				if (logger != null){
					if (logger.isErrorEnabled()){
						logger.error("install " + resource + " failed.", e);
					}
				}
			}
		}
	}
	
	/**
	 * 创建Framework
	 * @param assemblyLocations 装配件资源路径集合
	 * @return {@link AbstractFramework}
	 */
	protected abstract AbstractFramework doCreateFramework();
}
