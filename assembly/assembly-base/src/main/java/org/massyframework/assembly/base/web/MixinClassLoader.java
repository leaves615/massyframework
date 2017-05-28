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
* @日   期:  2017年5月18日
*/
package org.massyframework.assembly.base.web;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.massyframework.assembly.util.Asserts;

/**
 * 混合的类加载器
 */
public class MixinClassLoader extends URLClassLoader {
	
	private final List<ClassLoader> loaders;

	/**
	 * @param urls
	 * @param parent
	 */
	public MixinClassLoader(List<ClassLoader> loaders, ClassLoader parent) {
		super(null, parent);
		Asserts.notEmpty(loaders, "loaders cannot be empty.");
		this.loaders = loaders;
	}
	
	public MixinClassLoader(ClassLoader loader, ClassLoader parent){
		super(new URL[0], parent);
		this.loaders = new ArrayList<ClassLoader>();
		this.loaders.add(loader);
	}

	/* (non-Javadoc)
	 * @see java.net.URLClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> result = null;
		for (ClassLoader loader: this.loaders){
			try{
				result = loader.loadClass(name);
				if (result != null){
					return result;
				}
			}catch(ClassNotFoundException e){
				
			}
		}
		
		if (result == null){
			result = getParent().loadClass(name);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.net.URLClassLoader#findResource(java.lang.String)
	 */
	@Override
	public URL findResource(String name) {
		URL result = null;
		for (ClassLoader loader: this.loaders){
			try{
				result = loader.getResource(name);
				if (result != null){
					return result;
				}
			}catch(Exception e){
				
			}
		}
		
		if (result == null){
			result = this.getParent().getResource(name);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.net.URLClassLoader#findResources(java.lang.String)
	 */
	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		Vector<URL> result = new Vector<URL>();
		for (ClassLoader loader: this.loaders){
			Enumeration<URL> em = loader.getResources(name);
			while (em.hasMoreElements()){
				result.add(em.nextElement());
			}
		}
		
		Enumeration<URL> em = getParent().getResources(name);
		while (em.hasMoreElements()){
			result.add(em.nextElement());
		}
		return result.elements();
	}
}
