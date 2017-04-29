/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
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
* @日   期:  2017年4月9日
*/
package org.massyframework.assembly.base.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.base.support.InitParams;

/**
 * 装配件工具类，提供各种适用方法的封装
 *
 */
public abstract class ServletUtils {
	
		
	/**
	 * 从初始化参数中，获取ServletContextLisener的类名
	 * @param initParams {@link InitParams},装配件的初始化参数
	 * @return {@link String}
	 */
	public static String getServletContextListenerClassName(InitParams initParams){
		return initParams.getParameter(Constants.LISTENER_CLASSNAME);
	}
	
	/**
	 * 从初始化参数中，获取ServletContextListener的初始参数
	 * <br>以"servletContextListener.init."为开头，必须截取前缀后，才能作为Servlet的初始化参数键
	 * @param initParams {@link InitParams},装配件的初始化参数
	 * @return {@link Map}
	 */
	public static Map<String, String> getServletContextParameters(InitParams initParams){
		Map<String, String> result =
				new HashMap<String, String>();
		
		List<String> keys = initParams.getParameterKeys();
		for (String key: keys){
			if (key.startsWith(Constants.LISTENER_INIT_PREFIX)){
				String name = key.substring(28);
				if ((name != null) && (name.length() != 0)){
					result.put(name, initParams.getParameter(key));
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 从初始化参数中，获取Filter名称
	 * @param initParams {@link InitParams},装配件的初始化参数
	 * @return {@link String}, 不存在则返回null.
	 */
	public static String getFilterName(InitParams initParams){
		return initParams.getParameter(Constants.FILTER_NAME);
	}
	
	/**
	 * 从初始化参数中，获取Filter类型
	 * @param initParams {@link InitParams},装配件的初始化参数
	 * @return {@link String}, 不存在则返回null.
	 */
	public static String getFilterClassName(InitParams initParams){
		return initParams.getParameter(Constants.FILTER_CLASSNAME);
	}
	
	/**
	 * 从初始化参数中，获取filter是否支持异步
	 * <br>默认情况下为支持
	 * @param initParams  {@link InitParams},装配件的初始化参数
	 * @return <code>true</code>支持,<code>false</code>不支持。
	 */
	public static boolean getFilterAsyncSupported(InitParams initParams){
		String text = initParams.getParameter(Constants.FILTER_ASYNCSUPPORTED);
		if (text != null){
			return Boolean.parseBoolean(text);
		}
		return true;
	}
	
	/**
	 * 获取Filer过滤的调度类型
	 * @param initParams {@link InitParams},装配件的初始化参数
	 * @return {@link EnumSet}
	 */
	public static EnumSet<DispatcherType> getFilterDispatcherTypes(InitParams initParams){
		String text = initParams.getParameter(Constants.FILTER_DISPATCHERTYPE);
		if (text != null){
			EnumSet<DispatcherType> result =
					EnumSet.noneOf(DispatcherType.class);
			String[] types = text.split(",");
			for (String type: types){
				if ("forward".equalsIgnoreCase(type)){
					result.add(DispatcherType.FORWARD);
					continue;
				}
				if ("include".equalsIgnoreCase(type)){
					result.add(DispatcherType.INCLUDE);
					continue;
				}
				if ("request".equalsIgnoreCase(type)){
					result.add(DispatcherType.REQUEST);
					continue;
				}
				if ("async".equalsIgnoreCase(type)){
					result.add(DispatcherType.ASYNC);
					continue;
				}
				if ("error".equalsIgnoreCase(type)){
					result.add(DispatcherType.ERROR);
					continue;
				}	
			}
			
			return result;
		}
		
		return EnumSet.of(DispatcherType.REQUEST);
	}
	
	/**
	 * 从初始化参数中，获取Filter的初始化参数
	 * <br>该参数以"filter.init."为开头，必须截取前缀后，才能作为Filter的初始化参数键
	 * @param initParams  {@link InitParams},装配件的初始化参数
	 * @return {@link Map}
	 */
	public static Map<String, String> getFilterInitParameter(InitParams initParams){
		Map<String, String> result =
				new HashMap<String, String>();
		
		List<String> keys = initParams.getParameterKeys();
		for (String key: keys){
			if (key.startsWith(Constants.SERVLET_INIT_PREFIX)){
				String name = key.substring(13);
				if ((name != null) && (name.length() != 0)){
					result.put(name, initParams.getParameter(key));
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 从初始化参数中，获取Filter过滤的路径匹配
	 * @param initParams  {@link InitParams},装配件的初始化参数
	 * @return {@link String}数组，如果路径匹配参数不存在则返回null.
	 */
	public static String[] getFilterUrlPatterns(InitParams initParams){
		String text = initParams.getParameter(Constants.FILTER_URLPATTERNS);
		if (text != null){
			return text.split(",");
		}
		return null;
	}
	
	/**
	 * 从初始化参数中，获取Servlet名称
	 * @param initParams  {@link InitParams},装配件的初始化参数
	 * @return {@link String}, 不存在则返回null.
	 */
	public static String getServletName(InitParams initParams){
		return initParams.getParameter(Constants.SERVLET_NAME);
	}
	
	/**
	 * 从初始化参数中，获取Servlet类名
	 * @param initParams  {@link InitParams},装配件的初始化参数
	 * @return {@link String}
	 */
	public static String getServletClassName(InitParams initParams){
		return initParams.getParameter(Constants.SERVLET_CLASSNAME);
	}
	
	/**
	 * 从初始化参数中，获取Servlet的路径匹配
	 * @param initParams  {@link InitParams},装配件的初始化参数
	 * @return {@link String}数组，如果路径匹配参数不存在则返回null.
	 */
	public static String[] getServletUrlPatterns(InitParams initParams){
		String text = initParams.getParameter(Constants.SERVLET_URLPATTERNS);
		if (text != null){
			return text.split(",");
		}
		return null;
	}
	
	/**
	 * 从初始化参数中，获取加载Servlet加载和启动次序
	 * @param initParams  {@link InitParams},装配件的初始化参数
	 * @return {@link int}
	 */
	public static int getServletLoadOnStartup(InitParams initParams){
		String text = initParams.getParameter(Constants.SERVLET_LOADONSTARTUP);
		if (text != null){
			return Integer.parseInt(text);
		}else{
			return -1;
		}
	}
	
	/**
	 * 从初始化参数中，获取Servlet是否支持异步
	 * <br>默认情况下为支持
	 * @param initParams  {@link InitParams},装配件的初始化参数
	 * @return <code>true</code>支持,<code>false</code>不支持。
	 */
	public static boolean getServeltAsyncSupported(InitParams initParams){
		String text = initParams.getParameter(Constants.SERVLET_LOADONSTARTUP);
		if (text != null){
			return Boolean.parseBoolean(text);
		}
		return true;
	}

	/**
	 * 从初始化参数中，获取Servlet的初始化参数
	 * <br>该参数以"servlet.init."为开头，必须截取前缀后，才能作为Servlet的初始化参数键
	 * @param initParams  {@link InitParams},装配件的初始化参数
	 * @return {@link Map}
	 */
	public static Map<String, String> getServletInitParameter(InitParams initParams){
		Map<String, String> result =
				new HashMap<String, String>();
		
		List<String> keys = initParams.getParameterKeys();
		for (String key: keys){
			if (key.startsWith(Constants.SERVLET_INIT_PREFIX)){
				String name = key.substring(13);
				if ((name != null) && (name.length() != 0)){
					result.put(name, initParams.getParameter(key));
				}
			}
		}
		
		return result;
	}
}
