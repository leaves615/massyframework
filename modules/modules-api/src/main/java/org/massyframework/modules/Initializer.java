/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.modules;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.jboss.modules.LocalModuleLoader;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

/**
 * 初始化Jboss module
 */
public class Initializer implements ServletContainerInitializer {

	private static final String CONFIG_FILE = "/WEB-INF/massy/etc/conf.properties";
	private static final String DEFAULT_MODULES_PATH = "/WEB-INF/massy/modules";
	
	/**
	 * 
	 */
	public Initializer() {
	
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContainerInitializer#onStartup(java.util.Set, javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		
		Map<String, String> configuration = this.getConfiguration(ctx);
		try{
			ModuleLoader moduleLoader = prepareModuleLoader(configuration);
			ctx.setAttribute(ModuleLoader.class.getName(), moduleLoader);
						
			ModuleIdentifier identifier = ModuleIdentifier.create("org.massyframework.assembly");
			Module module = moduleLoader.loadModule(identifier);
			
			ServiceLoader<Launcher> sl = ServiceLoader.load(Launcher.class, module.getClassLoader());
			Iterator<Launcher> it = sl.iterator();
			if (it.hasNext()){
				Launcher launcher = it.next();
				launcher.launch(configuration, ctx);
			}else{
				throw new ServletException("cannot found Launcher with ServiceLoader.");
			}
		}catch(ServletException e){
			throw e;
		}catch(Exception e){
			throw new ServletException("jboss module launch failed.", e);
		}
	}
	
	/**
	 * 准备ModuleLoader
	 * @param configuration 配置项
	 * @return {@link ModuleLoader}
	 */
	protected ModuleLoader prepareModuleLoader(Map<String, String> configuration) throws Exception{
		ModuleLoader result = null;
		String rootPaths = (String)configuration.get(Constants.MODULES_ROOTPATH);
		if (rootPaths != null){
			String[] paths = rootPaths.split(",");
			
			int size = paths.length;
			File[] dirs = new File[size];
			for (int i=0; i<size; i++){
				dirs[i] = new File(paths[i]);
			}
			
			Module.setModuleLogger(new Slf4jModuleLogger());
						
			result = new LocalModuleLoader(dirs);
			String classesPath =(String)configuration.get(Constants.CLASSES_PATH);
			if (classesPath != null){
				result = createClassPathModuleLoader(result, "", classesPath, null);
			}
		}
		
		return result;
	}
	
	/**
	 * 创建ClassPathModuleLoader
	 * @param loader 模块加载器
	 * @param mainClass 主方法
	 * @param classPath 类路径
	 * @param dependencies 依赖
	 * @return
	 * @throws Exception
	 */
	private ModuleLoader createClassPathModuleLoader(ModuleLoader loader, String mainClass, 
			String classPath, String dependencies) throws Exception{
		 try {
	            Class<?> clazz = Class.forName("org.jboss.modules.ClassPathModuleLoader");
	            Constructor<?> defaultConstructor = clazz.getDeclaredConstructor(ModuleLoader.class, String.class, String.class, String.class);
	            defaultConstructor.setAccessible(true);
	            ModuleLoader result = (ModuleLoader) defaultConstructor.newInstance(loader, mainClass, classPath, dependencies);
	            return result;
	        } catch (Exception e) {
	            throw e;
	        }
	}
	
	/**
	 * 获取配置参数
	 * @param servletContext
	 * @return
	 */
	protected Map<String, String> getConfiguration(ServletContext servletContext){
		Map<String, String> result = new HashMap<String, String>();
		result.put("environment","j2ee");
		String absoluteClassesPath = servletContext.getRealPath("/WEB-INF/classes");
		result.put(Constants.CLASSES_PATH, absoluteClassesPath);
		
		try{
			URL url = servletContext.getResource(CONFIG_FILE);
			Properties props = new Properties();
			
			if (url != null){
				InputStream is = null;
				try{
					is = url.openStream();
					props.load(is);
					
					Set<Object> keys = props.keySet();
					for (Object key: keys){
						String name = key.toString();
						result.put(name, props.getProperty(name));
					}
				}catch(Exception e){
					if (is != null){
						try{
							is.close();
						}catch(Exception ex){
							
						}
					}
				}
			}else{
				servletContext.log("cannot found properties file: " + CONFIG_FILE + ".");
			}
			
		}catch(Exception e){
			servletContext.log(e.getMessage(), e);
		}
		
		if (!result.containsKey(Constants.MODULES_ROOTPATH)){
			String rootPaths = servletContext.getRealPath(DEFAULT_MODULES_PATH);
			result.put(Constants.MODULES_ROOTPATH, rootPaths);
		}
		
		return result;
	}

}
