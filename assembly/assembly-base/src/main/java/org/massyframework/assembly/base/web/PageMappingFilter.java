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

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.web.HttpResource;

/**
 * 页面映射过滤器
 * @author huangkaihui
 *
 */
public class PageMappingFilter implements Filter {

	private FilterConfig config;
	private Map<String, HttpResource> mappingMap =
			new ConcurrentHashMap<String, HttpResource>();
	/**
	 * 
	 */
	public PageMappingFilter() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String uri = httpRequest.getRequestURI();
		
		HttpResource resource = this.mappingMap.get(uri);
		if (resource != null){
			RequestDispatcher rd = request.getRequestDispatcher(resource.getName());
			if (rd != null){
				rd.forward(request, response);
				return;
			}
		}
		
		chain.doFilter(httpRequest, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		
	}
	
	/**
	 * 过滤器配置
	 * @return {@link FilterConfig}
	 */
	public FilterConfig getFilterConfig(){
		return this.config;
	}

	/**
	 * 添加页面映射
	 * @param alias 别名
	 * @param resource 映射资源
	 * @return <code>true</code>注册成功，<code>false<code>注册失败，因<code>alias</code>已经被注册
	 */
	boolean addPageMappging(String alias, HttpResource resource){
		return this.mappingMap.putIfAbsent(alias, resource) != null;
	}
	
	/**
	 * 清除页面映射
	 * @param assembly 装配件
	 */
	void clean(Assembly assembly){
		for (Entry<String, HttpResource> entry: this.mappingMap.entrySet()){
			if (entry.getValue().getAssembly() == assembly){
				this.mappingMap.remove(entry.getKey());
			}
		}
	}
	
}
