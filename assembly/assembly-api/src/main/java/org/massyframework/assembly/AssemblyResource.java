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
package org.massyframework.assembly;

import java.net.URL;

/**
 * 装配件资源
 */
public interface AssemblyResource {

	/**
	 * 装配件资源对应的类加载器
	 * @return {@link ClassLoader}
	 */
	ClassLoader getClassLoader();
	
	/**
	 * 装配件配置文件的URL
	 * @return {@link URL}
	 */
	URL getConfigFile();
}
