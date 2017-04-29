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
* @日   期:  2017年4月11日
*/
package org.massyframework.assembly.runtime;

import org.massyframework.assembly.ClassLoaderReference;
import org.massyframework.assembly.util.Asserts;

/**
 * 缺省的类加载器引用
 */
final class DefaultClassLoaderReference implements ClassLoaderReference {
	
	private final ClassLoader classLoader;

	/**
	 * 
	 */
	public DefaultClassLoaderReference(ClassLoader classLoader) {
		Asserts.notNull(classLoader, "classLoader cannot be null.");
		this.classLoader = classLoader;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ObjectReference#getReference()
	 */
	@Override
	public ClassLoader getReference() {
		return this.classLoader;
	}

}
