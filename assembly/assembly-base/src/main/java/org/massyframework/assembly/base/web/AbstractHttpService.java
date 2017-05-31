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
* @日   期:  2017年5月17日
*/
package org.massyframework.assembly.base.web;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.util.Asserts;
import org.massyframework.assembly.web.HttpResource;
import org.massyframework.assembly.web.HttpResourceProcessor;
import org.massyframework.assembly.web.HttpService;

/**
 * 抽象的HttpService
 */
abstract class AbstractHttpService implements HttpService {

	public static final String PATH_SEPARATOR = "/";
	
	private AtomicInteger count = new AtomicInteger(0);
	private Map<String, HttpResourceServlet> servletMap =
			new ConcurrentHashMap<String, HttpResourceServlet>();
	
	/**
	 * 
	 */
	public AbstractHttpService() {
	}

	

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.web.HttpService#bindMapping(java.lang.String, java.lang.String, org.massyframework.assembly.Assembly)
	 */
	@Override
	public boolean bindMapping(String alias, String page, Assembly assembly) {
		alias = this.cheakPath(alias);
		page = this.cheakPath(page);
		HttpResource resource = new HttpResource(page, assembly);
		return this.getPageMappingFilter().addPageMappging(alias, resource);
	}


	/* (non-Javadoc)
	 * @see org.massyframework.assembly.web.HttpService#registerResources(java.lang.String, java.lang.String, org.massyframework.assembly.Assembly)
	 */
	@Override
	public boolean registerResources(String alias, String name, Assembly assembly) {
		alias = this.cheakPath(alias);
		name = this.cheakPath(name);
		
		HttpResource resource = new HttpResource(name, assembly);
		HttpResourceServlet servlet = this.createServlet(alias, resource);
		if (servlet != null){
			return this.servletMap.putIfAbsent(alias, servlet)!= null;
		}
		
		return false;
	}
	
	/**
	 * 创建HttpResourceServlet,并注册
	 * @param resource {@link HttpResource}
	 * @return {@link HttpResourceServlet} http资源Servlet.
	 */
	protected HttpResourceServlet createServlet(String alias, HttpResource resource){
		String urlPattern = alias;
		if (!alias.endsWith("/*")){
			urlPattern = alias.endsWith("/") ?
					alias + "*" :
						alias + "/*";
		}
		HttpResourceServlet result =
				new HttpResourceServlet(resource, this.getDefault());
		ServletContext sc = this.getServletContext();
		ServletRegistration.Dynamic registration =
				sc.addServlet("httpResourceServlet-" + count.incrementAndGet(), result);
		if (registration != null){
			registration.addMapping(urlPattern);
			registration.setAsyncSupported(true);		
			
			return result;
		}
		return null;
	}
	
	/**
	 * 获取页面映射过滤器
	 * @return {@link PageMappingFilter}
	 */
	protected abstract PageMappingFilter getPageMappingFilter();

	/**
	 * 获取ServletContext
	 * @return {@link ServletContext}
	 */
	protected abstract ServletContext getServletContext();
	
	/**
	 * 缺省的HttpResourceProcessor
	 * @return {@link HttpResourceProcessor}
	 */
	protected abstract HttpResourceProcessor getDefault();
	
	/**
	 * 检查路径
	 * @param path
	 * @return {@link String}
	 */
	protected String cheakPath(String path){
		Asserts.notNull(path, "path cannot be null.");
		return path.startsWith(PATH_SEPARATOR) ? path: PATH_SEPARATOR + path;
	}
}
