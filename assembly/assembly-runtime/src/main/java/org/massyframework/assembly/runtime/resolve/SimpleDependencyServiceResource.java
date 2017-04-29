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
package org.massyframework.assembly.runtime.resolve;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.base.handle.DependencyServiceResource;
import org.massyframework.assembly.util.Asserts;

/**
 * 简单实现的依赖服务资源
 */
final class SimpleDependencyServiceResource implements DependencyServiceResource {
	
	private final String cName;
	private final Class<?> requiredType;
	private final String filterString;
	private final Assembly assembly;

	/**
	 * 
	 */
	public SimpleDependencyServiceResource(String cName, Class<?> requiredType, 
			String filterString, Assembly assembly) {
		Asserts.notNull(cName, "cName cannot be null.");
		Asserts.notNull(requiredType, "requiredType cannot be null.");
		Asserts.notNull(assembly, "assembly cannot be null.");
		
		this.cName = cName;
		this.requiredType = requiredType;
		this.filterString = filterString;
		this.assembly = assembly;
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.DependencyServiceResource#getCName()
	 */
	@Override
	public String getCName() {
		return this.cName;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ObjectReference#getReference()
	 */
	@Override
	public Assembly getReference() {
		return this.assembly;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.DependencyServiceResource#getServiceType()
	 */
	@Override
	public Class<?> getRequiredType() {
		return this.requiredType;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.DependencyServiceResource#getFilterString()
	 */
	@Override
	public String getFilterString() {
		return this.filterString;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SimpleDependencyServiceResource [cName=" + cName + ", requiredType=" + requiredType + ", filterString="
				+ filterString + ", assembly=" + assembly + "]";
	}

}
