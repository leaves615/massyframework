/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.modules.launching;

import java.net.URL;

import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.util.Asserts;

/**
 * 缺省的装配件资源实现
 */
final class DefaultAssemblyResource implements AssemblyResource {

	private final ClassLoader loader;
	private final URL configFile;
	/**
	 * 
	 */
	public DefaultAssemblyResource(ClassLoader loader, URL configFile) {
		Asserts.notNull(loader, "loader cannot be null.");
		Asserts.notNull(configFile, "configFile cannot be null.");
		this.loader = loader;
		this.configFile = configFile;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyResource#getClassLoader()
	 */
	@Override
	public ClassLoader getClassLoader() {
		return this.loader;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyResource#getConfigFile()
	 */
	@Override
	public URL getConfigFile() {
		return this.configFile;
	}

}
