/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import java.util.List;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyListener;
import org.massyframework.assembly.AssemblyNotFoundException;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.base.AbstractAssembly;
import org.massyframework.assembly.spec.Specification;

/**
 * 实现{@link Framework}的抽象类
 *
 */
abstract class AbstractFramework extends AbstractAssembly 
	implements Framework{

	/**
	 * 构造方法
	 * @param serviceRepository
	 */
	public AbstractFramework(ExportServiceRepository serviceRepository) {
		super(serviceRepository);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#addListener(org.massyframework.assembly.AssemblyListener)
	 */
	@Override
	public void addListener(AssemblyListener listener) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#containsAssembly(java.lang.String)
	 */
	@Override
	public boolean containsAssembly(String symbolicName) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#getAssembly(long)
	 */
	@Override
	public Assembly getAssembly(long assemblyId) throws AssemblyNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#getAssembly(java.lang.String)
	 */
	@Override
	public Assembly getAssembly(String symbolicName) throws AssemblyNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#getAssemblies()
	 */
	@Override
	public List<Assembly> getAssemblies() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#getAssemblies(org.massyframework.assembly.spec.Specification)
	 */
	@Override
	public List<Assembly> getAssemblies(Specification<Assembly> spec) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#setInitParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setInitParameter(String key, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#removeListener(org.massyframework.assembly.AssemblyListener)
	 */
	@Override
	public void removeListener(AssemblyListener listener) {
		// TODO Auto-generated method stub
		
	}

}
