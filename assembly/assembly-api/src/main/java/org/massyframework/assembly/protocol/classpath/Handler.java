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
package org.massyframework.assembly.protocol.classpath;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.massyframework.assembly.util.Asserts;


/**
 * 用于解析"classpath:"开头的url协议
 */
public class Handler extends URLStreamHandler {

	private final ClassLoader classLoader;
	
	/**
	 * 构造方法
	 * @param loader 解析classpath对应的类加载器
	 */
	public Handler(ClassLoader loader){
		Asserts.notNull(loader, "loader cannot be null.");
		this.classLoader = loader;
	}

	/* (non-Javadoc)
	 * @see java.net.URLStreamHandler#openConnection(java.net.URL)
	 */
	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		final URL resourceUrl = classLoader.getResource(u.getPath());
        return resourceUrl.openConnection();
	}
	
	public ClassLoader getClassLoader(){
		return this.classLoader;
	}

}
