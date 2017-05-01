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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务加载工具类，对ServiceLoader提供的方法进行封装。
 */
public abstract class ServiceLoaderUtils {
	
	private static final Logger logger =
			LoggerFactory.getLogger(ServiceLoaderUtils.class);
	/**
	 * 加载并返回首个服务，使用当前线程的ContextClassLoader加载
	 * 
	 * @param service
	 *            服务类型，非空
	 * @return 返回服务实例，服务未配置返回null.
	 */
	public static <S> S loadFirstService(Class<S> service) {
		return loadFirstService(service, Thread.currentThread().getContextClassLoader());
	}

	/**
	 * 加载并返回首个服务
	 * 
	 * @param service
	 *            服务类型，非空
	 * @param loader
	 *            {@link ClassLoader},非空
	 * @return 返回服务实例，服务未配置则返回null.
	 */
	public static <S> S loadFirstService(Class<S> service, ClassLoader loader) {
		Asserts.notNull(service, "service.");
		Asserts.notNull(loader, "loader.");
		Iterator<S> it = iteratorService(service, loader);
		return it.hasNext() ? it.next() : null;
	}

	/**
	 * 加载并返回所有服务
	 * 
	 * @param service
	 *            服务类型，非空
	 * @return 返回{@link List},不能返回null.
	 */
	public static <S> List<S> loadServices(Class<S> service) {
		return loadServices(service, Thread.currentThread().getContextClassLoader());
	}

	/**
	 * 加载并返回所有服务
	 * 
	 * @param service
	 *            服务类型，非空
	 * @param loader
	 *            {@link ClassLoader},非空
	 * @return 返回{@link List},不能返回null.
	 */
	@SuppressWarnings("unchecked")
	public static <S> List<S> loadServices(Class<S> service, ClassLoader loader) {
		Asserts.notNull(service, "service cannot be null.");
		Asserts.notNull(loader, "loader cannot be null.");
		try {
			Iterator<S> it = iteratorService(service, loader);
			List<S> result = new ArrayList<S>();

			while (it.hasNext()) {
				result.add(it.next());
			}

			return result;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * 加载服务
	 * 
	 * @param service
	 *            服务类型，非空
	 * @param loader
	 *            {@link ClassLoader}
	 * @return 返回{@link Iterator}.不能返回null.
	 */
	protected static <S> Iterator<S> iteratorService(Class<S> service, ClassLoader loader) {
		ServiceLoader<S> sl = ServiceLoader.load(service, loader);
		return sl.iterator();
	}

	/**
	 * 从{@link ClassLoader#getURLs()}中查找META-INF/services下的内置服务配置资源，并加载文件内定义的服务。
	 * 内置资源仅仅存放在{@link URLClassLoader#getURLs()}内，通过对Urls的解析来完成.<br>
	 * 本方法不返回存放在{@link URLClassLoader#getURLs()}外的其他内置资源
	 * @param service 服务类型
	 * @param resources 资源文件，
	 * @param loader 类加载器
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public static <S> List<S> loadServicesAtClassLoader(Class<S> service, ClassLoader classLoader){
		Asserts.notNull(service, "service cannot be null.");
		Asserts.notNull(classLoader, "classLoader cannot be null.");
		
		if (!(classLoader instanceof URLClassLoader)){
			return loadServices(service, classLoader);
		}
		
		List<URL> resources = ClassLoaderUtils.getResources(
				"META-INF/services/" + service.getName(), (URLClassLoader)classLoader);
		
		List<S> result = new ArrayList<S>();
		
		for (URL resource: resources){
			InputStream input = null;
			try{
				input = resource.openStream();
				BufferedReader reader = 
						 new BufferedReader(new InputStreamReader(input));
				 
				String line = null;
				do{
					line = reader.readLine();			
					if (line != null){
						line = line.trim();
						if (line.length() == 0){
							continue;
						}
						
						//跳过注释行
						if (line.startsWith("#")){
							continue;
						}
						
						try{
							Class<?> clazz = classLoader.loadClass(line);
							result.add((S)clazz.newInstance());
						}catch(Exception e){
							if (logger.isErrorEnabled()){
								logger.error("create service instance failed.", e);
							}
						}
					}
				}
				while(line!= null);
			}catch(Exception e){
				
			}finally{
				IOUtils.closeStream(input);
			}
		}
		
		return result;
	}
}
