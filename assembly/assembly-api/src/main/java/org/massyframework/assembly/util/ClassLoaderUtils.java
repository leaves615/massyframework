/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.massyframework.assembly.spec.JarEntrySpecification;

/**
 * 类加载工具类
 */
public abstract class ClassLoaderUtils {
	
	/**
	 * 使用缺省的构造方法，构造类实例
	 * @param clazz
	 * @return {@link T} 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <T> T newInstance(Class<T> clazz) 
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Asserts.notNull(clazz, "clazz cannot be null.");
		Constructor<T> cstr = clazz.getConstructor(new Class<?>[0]);
		if (!cstr.isAccessible()){
			cstr.setAccessible(true);
		}
		return cstr.newInstance(new Object[0]);
	}
	
	/**
	 * 设置线程上下文的类加载器，并返回设置前的类加载器
	 * @param loader {@link ClassLoader}
	 * @return {@link ClassLoader},线程上下文设置前的类加载器
	 */
	public static ClassLoader setThreadContextClassLoader(ClassLoader loader){
		Asserts.notNull(loader, "loader cannot be null.");
		ClassLoader result = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(loader);
		return result;
	}
	
	/**
	 * 获取指定URLClassLoader内的内置资源.<br>
	 * 内置资源仅仅存放在{@link URLClassLoader#getURLs()}内，通过对Urls的解析来完成.<br>
	 * 本方法不返回存放在{@link URLClassLoader#getURLs()}外的其他内置资源
	 * @param classLoader 类加载器
	 * @return {@link List}集合
	 */
	public static List<URL> getResources(String resourcePath, URLClassLoader classLoader){
		Asserts.notNull(classLoader, "classLoader cannot be null.");
		Asserts.notNull(resourcePath, "resourcePath cannot be null.");
		List<URL> result = new ArrayList<URL>();
		
		URL[] urls = classLoader.getURLs();
		if (urls != null){
			for (URL url: urls){
				URL found = null;
				try{
					JarFile jarFile = JarUtils.extractJarFile(url);
					if (jarFile != null){
						found = findResourceWithJarFile(resourcePath, jarFile);
					}else{
						Path path = IOUtils.toPath(url);
						found = findResourceWithPath(resourcePath, path);
					}					
				}catch(Exception e){
					
				}
				if (found != null){
					result.add(found);
				}
			}
		}
		return result;
	}
	
	/**
	 * 从JarFile中查找资源
	 * @param resourcePath 资源路径
	 * @param jarFile JarFile
	 * @return {@link URL}, 如果资源不存在，则返回null.
	 * @throws IOException 发生非预期的读写例外
	 */
	private static URL findResourceWithJarFile(String resourcePath, JarFile jarFile)
	 	throws IOException{
		JarEntry entry = jarFile.getJarEntry(resourcePath);
		if (entry != null){
			return  new URL("jar:file:" + jarFile.getName() + "!/" + entry.getName());
		}
		return null;
	}
	
	/**
	 * 在本地路径中查找资源
	 * @param resourcePath 资源路径
	 * @param path 本地文件路径
	 * @return {@link URL},如果资源不存在，则返回null.
	 * @throws IOException 发生非预期的读写异常
	 */
	private static URL findResourceWithPath(String resourcePath, Path path)
		throws IOException{
		Path result = path.resolve(resourcePath);
		result = result.normalize();
		File file = result.toFile();
		if (file.exists()){
			return file.toURI().toURL();
		}
		return null;
	}
	
	/**
	 * 从URLClassLoader中获取内置资源， 内置资源必须满足在指定的目录路径下，并满足扩展名要求.
	 * @param directoryPath 目录路径
	 * @param extension 文件扩展名
	 * @return {@link List}
	 */
	public static List<URL> getResources(String directoryPath, String extension, URLClassLoader classLoader){
		Asserts.notNull(directoryPath, "directoryPath cannot be null.");
		Asserts.notNull(extension, "extension cannot be null.");
		Asserts.notNull(classLoader, "classLoader cannot be null.");
		
		if (!extension.startsWith(".")){
			extension = "." + extension;
		}
		
		List<URL> result = new ArrayList<URL>();
		
		URL[] urls = classLoader.getURLs();
		if (urls != null){
			for (URL url: urls){
				List<URL> founds = null;
				try{
					JarFile jarFile = JarUtils.extractJarFile(url);
					if (jarFile != null){	
						founds = findResourcesWithJarFile(directoryPath, extension, jarFile);
					}else{
						Path path = IOUtils.toPath(url);
						founds = findResourcesWithPath(directoryPath, extension, path);
					}
				}catch(Exception e){
					
				}
				
				if (founds != null){
					result.addAll(founds);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 从JarFile中查找指定目录下，特定扩展名的内置资源
	 * @param directoryPath 目录路径
	 * @param extension 扩展名
	 * @param jarFile jar文件
	 * @return {@link List}
	 */
	private static List<URL> findResourcesWithJarFile(String directoryPath, String extension, 
			JarFile jarFile) throws IOException{
		List<JarEntry> entries = 
				JarUtils.findJarEntries(jarFile, new JarEntrySpecification(directoryPath, extension));
		List<URL> result = new ArrayList<URL>();
		for (JarEntry entry: entries){
			URL url = new URL("jar:file:" + jarFile.getName() + "!/" + entry.getName());
			result.add(url);
		}
		return result;
	}
	
	/**
	 * 从Path的directoryPath目录下，或者指定扩展名的文件
	 * @param directoryPath 路径目录
	 * @param extension 扩展名
	 * @param path {@link Path}对象
	 * @return {@link List}
	 * @throws IOException 发生非预期的读写例外
	 */
	private static List<URL> findResourcesWithPath(String directoryPath, final String extension,
			Path path) throws IOException {
		List<URL> result = null;
		Path dirsPath = path.resolve(directoryPath);
		File dirs = dirsPath.toFile();
		if (dirs.isDirectory()){
			File[] files = dirs.listFiles(new FileFilter(){

				/* (non-Javadoc)
				 * @see java.io.FileFilter#accept(java.io.File)
				 */
				@Override
				public boolean accept(File file) {
					return file.getName().endsWith(extension);
				}
				
			});
			
			int size = files.length;
			if (size > 0){
				result = new ArrayList<URL>();
				for (int i=0; i<size; i++){
					result.add(files[i].toURI().toURL());
				}
			}
		}
		
		return result;
	}
}
