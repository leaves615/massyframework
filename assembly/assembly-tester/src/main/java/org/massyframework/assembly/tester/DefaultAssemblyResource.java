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
* @日   期:  2017年4月12日
*/
package org.massyframework.assembly.tester;

import java.net.URL;

import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.util.Asserts;

/**
 * 
 * 缺省的装配件资源
 *
 */
public final class DefaultAssemblyResource implements AssemblyResource {

	private final ClassLoader loader;
	private final URL configFile;
	/**
	 * 
	 */
	public DefaultAssemblyResource(ClassLoader loader, URL configFile) {
		Asserts.notNull(loader, "loader cannot be null.");
		Asserts.notNull(configFile, "configFile cannot be null.");
		this.loader = loader;
		this.configFile = configFile;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyResource#getClassLoader()
	 */
	@Override
	public ClassLoader getClassLoader() {
		return this.loader;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyResource#getConfigFile()
	 */
	@Override
	public URL getConfigFile() {
		return this.configFile;
	}

}
