/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime.resolve;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.base.handle.DependencyServiceResource;
import org.massyframework.assembly.util.Asserts;

/**
 * 简单实现的依赖服务资源
 */
final class SimpleDependencyServiceResource implements DependencyServiceResource {
	
	private final String cName;
	private final Class<?> requiredType;
	private final String filterString;
	private final Assembly assembly;

	/**
	 * 
	 */
	public SimpleDependencyServiceResource(String cName, Class<?> requiredType, 
			String filterString, Assembly assembly) {
		Asserts.notNull(cName, "cName cannot be null.");
		Asserts.notNull(requiredType, "requiredType cannot be null.");
		Asserts.notNull(assembly, "assembly cannot be null.");
		
		this.cName = cName;
		this.requiredType = requiredType;
		this.filterString = filterString;
		this.assembly = assembly;
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.DependencyServiceResource#getCName()
	 */
	@Override
	public String getCName() {
		return this.cName;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ObjectReference#getReference()
	 */
	@Override
	public Assembly getReference() {
		return this.assembly;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.DependencyServiceResource#getServiceType()
	 */
	@Override
	public Class<?> getRequiredType() {
		return this.requiredType;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.DependencyServiceResource#getFilterString()
	 */
	@Override
	public String getFilterString() {
		return this.filterString;
	}

}
