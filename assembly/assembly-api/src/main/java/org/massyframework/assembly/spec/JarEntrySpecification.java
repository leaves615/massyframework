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
* @日   期:  2017年4月9日
*/
package org.massyframework.assembly.spec;

import java.util.jar.JarEntry;

import org.massyframework.assembly.util.Asserts;

/**
 * JarEntry规则过滤器, 提供jarEntry的过滤筛选
 */
public class JarEntrySpecification implements Specification<JarEntry> {
		
	/** 目录路径 **/
	private final String directoryPath;	
	private final String extension;
		
	/**
	 * 构造方法
	 * @param directoryPath 目录
	 */
	public JarEntrySpecification(String directoryPath, String extensionName){
		Asserts.notNull(directoryPath, "directoryPath cannot be null.");
		Asserts.notNull(extensionName, "extensionName cannot be null.");
		this.directoryPath = directoryPath.endsWith("/") ?
				directoryPath :
					directoryPath + "/";
		this.extension = extensionName.startsWith(".") ?
				extensionName :
					"." + extensionName;
	}
	
	/* (non-Javadoc)
	 * @see org.smarabbit.massy.spec.Specification#isSatisfyBy(java.lang.Object)
	 */
	@Override
	public boolean isSatisfyBy(JarEntry target) {
		String name = target.getName();
		if (name.startsWith(directoryPath)){
			String text = name.substring(directoryPath.length());
			if ((text.indexOf("/") == -1) && (text.endsWith(this.extension))){
				return true;
			}
		}
		return false;
	}
	
}
