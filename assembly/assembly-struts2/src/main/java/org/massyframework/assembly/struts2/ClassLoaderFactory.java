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
* @日   期:  2017年5月16日
*/
package org.massyframework.assembly.struts2;

import org.massyframework.assembly.util.Asserts;

import com.opensymphony.xwork2.inject.Context;
import com.opensymphony.xwork2.inject.Factory;

/**
 * 类加载器工厂
 */
public class ClassLoaderFactory implements Factory<ClassLoader> {
	
	private final ClassLoader loader;

	/**
	 * 
	 */
	public ClassLoaderFactory(ClassLoader loader) {
		Asserts.notNull(loader, "loader cannot be null.");
		this.loader = loader;
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.inject.Factory#create(com.opensymphony.xwork2.inject.Context)
	 */
	@Override
	public ClassLoader create(Context context) throws Exception {
		return this.loader;
	}
	
	

}
