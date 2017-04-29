/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月15日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.FrameworkEvent;
import org.massyframework.assembly.FrameworkInitializeLoader;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.FrameworkListener;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.base.handle.LifecycleProcessHandler;
import org.massyframework.assembly.util.Asserts;
import org.massyframework.assembly.util.MapUtils;
import org.massyframework.assembly.util.ServiceLoaderUtils;
import org.slf4j.Logger;

/**
 * @author huangkaihui
 *
 */
class DefaultFrameworkLauncher implements FrameworkLauncher {
	
	private AbstractFramework framework;
	private Map<String, String> configuration;
	private FrameworkInitializeLoader initializeLoader;
	private Logger logger;

	private List<FrameworkListener> listeners =
			new CopyOnWriteArrayList<FrameworkListener>();
	/**
	 * 
	 */
	public DefaultFrameworkLauncher(AbstractFramework framework){
		Asserts.notNull(framework, "framework cannot be null.");
		this.framework = framework;
	}
	
	/**
	 * 初始化
	 * @param configuration
	 * @param initializeLoader
	 */
	public void init(Map<String, String> configuration,
			FrameworkInitializeLoader initializeLoader){
		if (this.initializeLoader == null){
			Asserts.notNull(initializeLoader, "initializeLoader cannot be null.");
			this.configuration = configuration;
			this.initializeLoader = initializeLoader;
			this.logger = LoggerReference.adaptFrom(this.framework);
			
			this.setInitParameters(this.configuration);
		}
	}
	
	/**
	 * 添加事件监听器
	 * @param listener
	 */
	public void addListener(FrameworkListener listener){
		if (listener != null){
			this.listeners.add(listener);
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.runtime.FrameworkLauncher#start()
	 */
	@Override
	public void start() throws Exception {
		try{
			LifecycleProcessHandler handler =
					this.framework.getHandlerRegistry().getHandler(LifecycleProcessHandler.class);			
			this.doInitialize(initializeLoader);
			this.publishInitializedEvent();
		
			handler.start();
			this.installAssemblies();
		}catch(Exception e){
			if (logger.isErrorEnabled()){
				logger.error(e.getMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.runtime.FrameworkLauncher#stop()
	 */
	@Override
	public void stop() throws Exception {
		try{
			LifecycleProcessHandler handler =
					this.framework.getHandlerRegistry().getHandler(LifecycleProcessHandler.class);
			this.publishDestoryedEvent();
			handler.stop();
		}catch(Exception e){
			if (logger.isErrorEnabled()){
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 移除事件监听器
	 * @param listener
	 */
	public void removeListener(FrameworkListener listener){
		if (listener != null){
			this.listeners.remove(listener);
		}
	}
	
	/**
	 * 设置初始化参数
	 * @param params 参数
	 */
	protected void setInitParameters(Map<String, String> params){
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
	 * @param initializeLoader 初始化加载器
	 */
	protected void doInitialize(FrameworkInitializeLoader initializeLoader) throws Exception{
		List<FrameworkInitializer> initializers = 
				this.loadFrameworkInitialziers();
		initializers.add(0, new Initailizer());
		
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
				Collections.sort(keys);
				if (!keys.isEmpty()){
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
	}
	
	/**
	 * 安装装配件
	 * @throws Exception
	 */
	protected void installAssemblies() throws Exception{
		List<AssemblyResource> resources = this.initializeLoader.getAssemblyResources();
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
	 * 发布初始化完成事件
	 */
	protected void publishInitializedEvent(){
		FrameworkEvent event = new FrameworkEvent(this.framework);
		for (FrameworkListener listener : this.listeners){
			try{
				listener.frameworkInitialized(event);
				if (logger.isDebugEnabled()){
					logger.debug(listener + " frameworkInitialized(event) invoked.");
				}
			}catch(Exception e){
				if (logger.isErrorEnabled()){
					logger.error(listener + " frameworkInitialized(event) falied.", e);
				}
			}
		}
	}
	
	/**
	 * 发布析钩事件
	 */
	protected void publishDestoryedEvent(){
		FrameworkEvent event = new FrameworkEvent(this.framework);
		for (FrameworkListener listener : this.listeners){
			try{
				listener.frameworkDestroyed(event);
				if (logger.isDebugEnabled()){
					logger.debug(listener + " frameworkDestroyed(event) invoked.");
				}
			}catch(Exception e){
				if (logger.isErrorEnabled()){
					logger.error(listener + " frameworkDestroyed(event) falied.", e);
				}
			}
		}
	}
	
	/**
	 * 加载运行框架初始化
	 * @param initializeHandler 初始化加载器
	 * @return {@link List}
	 */
	protected List<FrameworkInitializer> loadFrameworkInitialziers() throws Exception{
		String text = null;
		if (this.configuration != null){
			text = this.configuration.get(Constants.ENVIRONMENT);
		}
		boolean isJ2EE = Constants.ENVIRONMENT_J2EE.equals(text);
		
		List<FrameworkInitializer> result =
				initializeLoader.getFrameworkInitializer();
		
		List<FrameworkInitializer> list =
				ServiceLoaderUtils.loadServicesAtClassLoader(
						FrameworkInitializer.class, this.getClass().getClassLoader());
		int index = isJ2EE ? 1 : 0; //J2EE模式下，让ServletContext先注册，所以让1
		result.addAll(index, list);
		return result;
	}
}
