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
* @日   期:  2017年4月9日
*/
package org.massyframework.assembly.protocol.servletpath;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.servlet.ServletContext;

import org.massyframework.assembly.util.Asserts;

/**
 * 用于解析"servletpath:"开头的url协议
 */
public class Handler extends URLStreamHandler {

	private final ServletContext servletContext;
		
	/**
	 * 用于解析servletpath的ServletContext		
	 * @param servletContext {@link ServletContext}
	 */
	public Handler(ServletContext servletContext){
		Asserts.notNull(servletContext, "servletContext cannot be null.");
		this.servletContext = servletContext;
	}

	/* (non-Javadoc)
	 * @see java.net.URLStreamHandler#openConnection(java.net.URL)
	 */
	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		String path = u.getPath();
		if (!path.startsWith("/")){
			path = "/" + path;
		}
		
		final URL resourceUrl = servletContext.getResource(path);
        return resourceUrl.openConnection();
	}

	public ServletContext getServletContext(){
		return this.servletContext;
	}
}
