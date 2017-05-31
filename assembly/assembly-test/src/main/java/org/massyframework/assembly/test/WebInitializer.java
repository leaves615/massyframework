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
* @日   期:  2017年5月10日
*/
package org.massyframework.assembly.test;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.FrameworkFactory;
import org.massyframework.assembly.FrameworkInitializeLoader;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.util.ServiceLoaderUtils;

/**
 * 在Web Application自动启动Massy Framework
 */
public class WebInitializer implements ServletContainerInitializer {

	private static final String CONFIG_FILE = "/WEB-INF/massy/etc/conf.properties";
	
	/**
	 * 
	 */
	public WebInitializer() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContainerInitializer#onStartup(java.util.Set, javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		Map<String, String> configuration = this.getConfiguration(ctx);
		
		FrameworkFactory factory =
				ServiceLoaderUtils.loadFirstService(FrameworkFactory.class);
		
		FrameworkInitializeLoader loader =
				this.createInitializeLoader(configuration, ctx);
		try {
			factory.createFramework(configuration, loader);
			ctx.addListener(new ContextDestroyedListener());
		} catch (Exception e) {
			throw new ServletException("launch massy framework failed.", e);
		}
	}
	
	/**
	 * 创建运行框架初始化器加载器
	 * @param configuration
	 * @param servletContext
	 * @return
	 */
	protected FrameworkInitializeLoader createInitializeLoader(
			Map<String, String> configuration,
			ServletContext servletContext){
		List<FrameworkInitializer>  initializers =
				new ArrayList<FrameworkInitializer>();
		initializers.add(new ServletContextInitializer(servletContext));
		
		SingleableClassLoaderInitializeLoader result =
				new SingleableClassLoaderInitializeLoader(
						initializers, 
						Thread.currentThread().getContextClassLoader(),
						configuration,
						servletContext);
		return result;
	}

	/**
	 * 获取配置参数
	 * @param servletContext {@link ServletContext}
	 * @return {@link Map}
	 */
	protected Map<String, String> getConfiguration(ServletContext servletContext){
		Map<String, String> result = new HashMap<String, String>();
		result.put("environment","j2ee");
		
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
			
		result.put(Constants.ENVIRONMENT, Constants.ENVIRONMENT_J2EE);
		return result;
	}
}
