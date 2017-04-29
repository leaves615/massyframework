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
* @日   期:  2017年4月19日

*/
package org.massyframework.assembly.protocol;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.massyframework.assembly.util.Asserts;

/**
 * URL工厂，提供扩展classpath和servletpath两种url协议
 */
public abstract class URLFactory {
	
	private static final String CLASSPATH   = "classpath:";
	private static final String SERVLETPATH = "servletpath:";

	/**
	 * 是否具有{@link #CLASSPATH}前缀
	 * @param location 存取位置
	 * @return true具有，false不具有
	 */
	public static boolean hasClassPathPrefix(String location){
		return location.startsWith(CLASSPATH);
	}
	
	/**
	 *  是否具有{@link #SERVLETPATH}前缀
	 * @param location 存取位置
	 * @return true具有，false不具有
	 */
	public static boolean hasServletPathPrefix(String location){
		return location.startsWith(SERVLETPATH);
	}
	
	/**
	 * 创建ClassPath类型的URL
	 * @param location 资源针对loader的位置
	 * @param loader 对应的类加载器
	 * @return {@link URL}
	 * @throws MalformedURLException 格式不正确抛出例外
	 */
	public static URL createClassPathUrl(String location, ClassLoader loader)
			throws MalformedURLException{
		Asserts.notNull(location, "location cannot be null.");
		Asserts.notNull(loader, "loader cannot be null.");
		return new URL(null, location, 
				new org.massyframework.assembly.protocol.classpath.Handler(loader));
	}
	
	/**
	 * 创建ServletPath类型的URL
	 * @param location 资源针对servletContext的位置
	 * @return {@link URL}
	 * @throws MalformedURLException 格式不正确抛出例外
	 */
	public static URL createServletPathUrl(String location, ServletContext servletContext)
			throws MalformedURLException{
		return new URL(null, location, new org.massyframework.assembly.protocol.servletpath.Handler(servletContext));
	}
}
