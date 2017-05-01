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
package org.massyframework.assembly.util;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.massyframework.assembly.spec.Specification;

/**
 * Jar工具类，提供Jar包内资源的查找
 * @author huangkaihui
 *
 */
public abstract class JarUtils {

	/**
	 * 在jar内查找符合要求的所有JarEntry
	 * @param jar {@link JarFile}
	 * @param spec {@link Specification}
	 * @return {@link List}
	 * @throws IOException 抛出的IO异常
	 */
	public static List<JarEntry> findJarEntries(JarFile jar, 
			Specification<JarEntry> spec) throws IOException{
		Asserts.notNull(jar, "jar cannot be null.");
		Asserts.notNull(spec, "spec cannot be null.");
		List<JarEntry> result = new ArrayList<JarEntry>();
		
		Enumeration<JarEntry> em = jar.entries();
		while (em.hasMoreElements()){
			JarEntry entry = em.nextElement();
			if (spec.isSatisfyBy(entry)){
				result.add(entry);
			}
		}
		return result;
	}
	
	/**
	 * 从资源中提取JarFile
	 * @param url 资源定位
	 * @return {@link JarFile}，如果不是jar资源，则返回null.
	 * @throws IOException 发生IO读写例外
	 */
	public static JarFile extractJarFile(URL resource) throws IOException{
		if (resource.getProtocol().equals("jar")){
			return ((JarURLConnection) resource.openConnection()).getJarFile();  
		}
		return null;
	}
	
}
