/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import org.massyframework.assembly.ClassLoaderReference;
import org.massyframework.assembly.util.Asserts;

/**
 * 缺省的类加载器引用
 */
final class DefaultClassLoaderReference implements ClassLoaderReference {
	
	private final ClassLoader classLoader;

	/**
	 * 
	 */
	public DefaultClassLoaderReference(ClassLoader classLoader) {
		Asserts.notNull(classLoader, "classLoader cannot be null.");
		this.classLoader = classLoader;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ObjectReference#getReference()
	 */
	@Override
	public ClassLoader getReference() {
		return this.classLoader;
	}

}
