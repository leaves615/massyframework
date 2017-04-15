/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.LoggerReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缺省的日志记录引用
 */
public final class DefaultLoggerReference implements LoggerReference {
	
	private final Logger logger;

	/**
	 * 
	 */
	public DefaultLoggerReference(Assembly assembly) {
		this.logger =
			LoggerFactory.getLogger("[" + assembly.getSymbolicName() + "]");
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ObjectReference#getReference()
	 */
	@Override
	public Logger getReference() {
		return this.logger;
	}

}
