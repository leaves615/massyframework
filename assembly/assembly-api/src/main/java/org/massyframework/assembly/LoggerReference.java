/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import org.slf4j.Logger;

/**
 * Logger引用
 */
public interface LoggerReference extends ObjectReference<Logger> {

	/**
	 * 从装配件适配出登录日志对象
	 * @param assembly
	 * @return {@link Logger}, 可能返回null.
	 */
	static Logger adaptFrom(Assembly assembly){
		if (assembly == null){
			return null;
		}
		
		LoggerReference reference = assembly.adapt(LoggerReference.class);
		return reference != null ?
				reference.getReference():
					null;
	}
}
