/**
* @Copyright: 2017 smarabbit studio Inc. All rights reserved.
*  
* @作   者：  huangkaihui
* @日   期:  2017年3月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spec;

import java.util.jar.JarEntry;

import org.massyframework.assembly.util.Asserts;

/**
 * JarEntry规则过滤器, 提供jarEntry的过滤筛选
 */
public class JarEntrySpecification implements Specification<JarEntry> {
		
	/** 目录路径 **/
	private final String directoryPath;	
	private final String extension;
		
	/**
	 * 构造方法
	 * @param directoryPath 目录
	 */
	public JarEntrySpecification(String directoryPath, String extensionName){
		Asserts.notNull(directoryPath, "directoryPath cannot be null.");
		Asserts.notNull(extensionName, "extensionName cannot be null.");
		this.directoryPath = directoryPath.endsWith("/") ?
				directoryPath :
					directoryPath + "/";
		this.extension = extensionName.startsWith(".") ?
				extensionName :
					"." + extensionName;
	}
	
	/* (non-Javadoc)
	 * @see org.smarabbit.massy.spec.Specification#isSatisfyBy(java.lang.Object)
	 */
	@Override
	public boolean isSatisfyBy(JarEntry target) {
		String name = target.getName();
		if (name.startsWith(directoryPath)){
			String text = name.substring(directoryPath.length());
			if ((text.indexOf("/") == -1) && (text.endsWith(this.extension))){
				return true;
			}
		}
		return false;
	}
	
}
