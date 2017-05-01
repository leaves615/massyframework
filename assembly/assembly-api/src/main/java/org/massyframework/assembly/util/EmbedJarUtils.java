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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.massyframework.assembly.spec.JarEntrySpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供嵌在META-INF/lib目录下的jar包工具方法类
 */
public final class EmbedJarUtils {

	private static final Logger logger = LoggerFactory.getLogger(EmbedJarUtils.class);
	private static final String LIB = "META-INF/lib";
	private static final String JAR = "jar";
	
	/**
	 * 以锚定Class为定位，加载锚定Class所在jar中，打包在META-INF/lib目录下的所有jar包
	 * @param anchorClass 锚定的Class。
	 * @return
	 * @throws IOException
	 */
	public static URL[] loadEmbedJars(Class<?> anchorClass) throws IOException{
		URL anchorUrl = getFilePath(anchorClass);
		List<URL> result = null;
		
		JarFile jarFile = JarUtils.extractJarFile(anchorUrl);
		if (jarFile != null){
			result = extractJarEntryWithJar(jarFile);
		}else{
			Path path = IOUtils.toPath(anchorUrl);
			result = extractJarWithPath(path);
		}
		
		return result.toArray(new URL[result.size()]);
	}
	
	/**
	 * 从jarFile中发现META-INF/lib下的JarEntry，并提取JarEntry到临时文件
	 * @param jarFile jar文件
	 * @return {@link List}
	 * @throws IOException 
	 */
	private static List<URL> extractJarEntryWithJar(JarFile jarFile) throws IOException{
        List<JarEntry> entries = JarUtils.findJarEntries(jarFile, new JarEntrySpecification(LIB, JAR)); 
        List<URL> result = new ArrayList<URL>();
		for (JarEntry entry: entries){
			InputStream input = null;
			try{
				input= jarFile.getInputStream(entry);
			
				File temp = File.createTempFile("assembly-", ".jar");
				temp.deleteOnExit();
				
				IOUtils.copy(input, temp);
				result.add(temp.toURI().toURL());
			}finally{
				IOUtils.closeStream(input);
			}
		}
		
        return result;
	}
	
	/**
	 * 定位到测试目录，发现并加载特定的文件，以便加载指定开发项目下的classes
	 * @param path 本地路径
	 * @return {@link List}数组
	 * @throws IOException 
	 */
	private static List<URL> extractJarWithPath(Path path) throws IOException{		
		path = path.normalize();
		//提取项目根目录
		int count = 2;
		for (int i=0; i<count; i++){
			path = path.getParent();
		}
		//定位到test的resources目录下
		Path config = path.resolve("src/test/resources/embed.classes.dirs");
		return loadEmbedClassesDirs(config);
	}
	
	/**
	 * 返回锚定Class的路径
	 * @return {@link URL}
	 */
	private static URL getFilePath(Class<?> anchorClass){
		return anchorClass.getProtectionDomain().getCodeSource().getLocation();
	}
	
	/**
	 * 加载嵌入项目的Classes目录
	 * @param file embed.classes.dirs配置文件路径
	 * @return {@link URL}
	 */
	@SuppressWarnings("resource")
	private static List<URL> loadEmbedClassesDirs(Path path) throws IOException{
		List<URL>  result = new ArrayList<URL>();
		File file = path.toFile();
		if (file.exists()){
			InputStream input = null;
			try {
				input = new FileInputStream(path.toFile());
				BufferedReader reader = 
						new BufferedReader(new InputStreamReader(input));
				String line = null;
				do {
					line = reader.readLine();
					if (line != null) {
						line = line.trim();
						if (line.length() == 0) {
							continue;
						}
	
						// 跳过注释行
						if (line.startsWith("#")) {
							continue;
						}
						
						//拼接项目目录
						try{
							
							Path classesDir = path.resolve(line);
							classesDir = classesDir.normalize();
							File dir = classesDir.toFile();
							if (dir.isDirectory()){
								result.add(dir.toURI().toURL());
							}else{
								if (logger.isWarnEnabled()){
									logger.warn("classes dir not exist:" + dir.getPath() + ".");
								}
							}
						}catch(Exception e){
							
						}
					
					}
				} while (line != null);
			} finally {
				IOUtils.closeStream(input);
			}
		}else{
			System.err.println("cannot found src/test/resources/embed.classes.dirs file.");
		}
		
		return result;
		
	}

}
