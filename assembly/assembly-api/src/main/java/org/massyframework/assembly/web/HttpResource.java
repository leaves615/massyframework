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
package org.massyframework.assembly.web;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.ClassLoaderReference;
import org.massyframework.assembly.util.Asserts;

/**
 * Http资源
 */
public final class HttpResource {

	private final String name;
	private final Assembly assembly;
	private volatile ClassLoader loader;
	
	/**
	 * 
	 */
	public HttpResource(String name, Assembly assembly) {
		Asserts.notNull(name, "name cannot be null.");
		Asserts.notNull(assembly, "assembly cannot be null.");
		this.name = name;
		this.assembly = assembly;
	}
	
	/**
	 * 资源名称
	 * @return {@link String}
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * 资源所属装配件
	 * @return {@link Assembly}
	 */
	public Assembly getAssembly(){
		return this.assembly;
	}
	
	/**
	 * 资源所属装配件的类加载器
	 * @return {@link ClassLoader}
	 */
	public ClassLoader getAssemblyClassLoader(){
		if (this.loader == null){
			this.loader = ClassLoaderReference.adaptFrom(this.assembly);
		}
		return this.loader;
	}

}
