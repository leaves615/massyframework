/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle.support;

import java.net.URL;

import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.base.handle.ConfigFileHandler;
import org.massyframework.assembly.util.Asserts;

/**
 * 缺省的配置文件
 */
public final class DefaultConfigFile implements ConfigFileHandler {

	private URL url;
	
	/**
	 * 
	 */
	public DefaultConfigFile(AssemblyResource resource) {
		Asserts.notNull(resource, "resource cannot be null.");
		this.url = resource.getConfigFile();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ConfigFileHandler#getConfigFile()
	 */
	@Override
	public URL getConfigFile() {
		return this.url;
	}

}
