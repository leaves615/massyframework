/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.packages;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.FrameworkInitializeHandler;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.spec.JarEntrySpecification;
import org.massyframework.assembly.util.ClassLoaderUtils;
import org.massyframework.assembly.util.JarUtils;
import org.massyframework.assembly.util.ServiceLoaderUtils;

/**
 * 
 * @author huangkaihui
 *
 */
public abstract class AbstractFrameworkInitializeHandler implements FrameworkInitializeHandler {

	private static final String PATH = "META-INF/assembly";
	private static final String EXTENSION = ".xml";
	
	private List<FrameworkInitializer> initializers;
	
	/**
	 * 
	 * @param initializers
	 */
	public AbstractFrameworkInitializeHandler(List<FrameworkInitializer> initializers) {
		this.initializers = initializers;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializeHandler#getFrameworkInitializer()
	 */
	@Override
	public List<FrameworkInitializer> getFrameworkInitializer() {
		List<ClassLoader> loaders = this.getClassLoaderes();
		List<FrameworkInitializer> result = new ArrayList<FrameworkInitializer>();
		for (ClassLoader loader: loaders){
			List<FrameworkInitializer> list = null;
			if (loader instanceof URLClassLoader){
				list = ServiceLoaderUtils.loadServicesAtClassLoader(
						FrameworkInitializer.class, (URLClassLoader)loader);
			}else{
				list = ServiceLoaderUtils.loadServices(FrameworkInitializer.class, loader);
			}
			result.addAll(list);
		}
		
		if (this.initializers != null){
			result.addAll(0, this.initializers);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializeHandler#getAssemblyResources()
	 */
	@Override
	public List<AssemblyResource> getAssemblyResources() {
		List<ClassLoader> loaders = this.getClassLoaderes();
		List<AssemblyResource> result = new ArrayList<AssemblyResource>();
		
		for (ClassLoader loader: loaders){
			try{
				List<AssemblyResource> list =
						this.loadAssemblyResourcesFromClassLoader(loader);
				result.addAll(list);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
	 * 从ClassLoader中加载AssemblyLocation集合
	 * @param loader 类加载器
	 * @return {@link List}
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	protected List<AssemblyResource> loadAssemblyResourcesFromClassLoader(ClassLoader loader)
			throws IOException, URISyntaxException{				
		List<AssemblyResource> result = new ArrayList<AssemblyResource>();

		if (loader instanceof URLClassLoader){
			List<URL> resources = ClassLoaderUtils.getResources(PATH, ".xml", (URLClassLoader)loader);
			for (URL resource: resources){
				result.add(new DefaultAssemblyResource(loader, resource));
			}
		}else{
			Enumeration<URL> em = loader.getResources(PATH);
			while (em.hasMoreElements()){
				URL url = em.nextElement();
				String protocol = url.getProtocol();
				switch (protocol){
					case "jar": {
						JarURLConnection connect = (JarURLConnection)url.openConnection();
						JarFile jarFile = connect.getJarFile();
						this.loadAssemblyResourcesFromJarFile(jarFile, result, loader);
						break;
					}
					case "file":{
						File file = new File(url.toURI());
						this.loadAssemblyResourcesFromDirectory(file, result, loader);
						break;
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 从jarFile文件中加载配置资源
	 * @param jarFile jar文件
	 * @param list 装配件定位集合
	 * @param loader 类加载器
	 * @throws IOException 发生非预期的读写异常
	 */
	protected void loadAssemblyResourcesFromJarFile(JarFile jarFile, List<AssemblyResource> list, ClassLoader loader)
		throws IOException{
		List<JarEntry> entries = JarUtils.findJarEntries(jarFile, 
				new JarEntrySpecification(PATH, EXTENSION));
		if (!entries.isEmpty()){
			for (JarEntry entry: entries){
				URL url = new URL("jar:file:" + jarFile.getName() + "!/" + entry.getName());
				AssemblyResource resource =
						new DefaultAssemblyResource(loader, url);
				list.add(resource);
			}
		}
	}
	
	/**
	 * 从文件目录中加载配置资源
	 * @param directory 目录
	 * @param list 装配件定位集合
	 * @param loader 类加载器
	 * @throws IOException 发生非预期的读写异常
	 */
	protected void loadAssemblyResourcesFromDirectory(File directory, List<AssemblyResource> list, ClassLoader loader)
		throws IOException{
		if (directory.exists()){
			File[] files = directory.listFiles(new FileFilter(){

				@Override
				public boolean accept(File file) {
					if (file.getName().endsWith(EXTENSION)){
						return true;
					}
					return false;
				}
				
			});
			
			for (File file: files){
				URL url = file.toURI().toURL();
				AssemblyResource resource =
						new DefaultAssemblyResource(loader, url);
				list.add(resource);
			}
		}
	}

	/**
	 * 获取类加载集合
	 * @return {@link List}
	 */
	protected abstract List<ClassLoader> getClassLoaderes();
}
