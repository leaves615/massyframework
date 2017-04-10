/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.AssemblyStatus;

/**
 * 装配件的基类
 */
public abstract class AbstractAssembly implements Assembly {

	private final long id;
	/**
	 * 
	 */
	public AbstractAssembly() {
		this.id = AssemblyIdGenericFactory.genericAssemblyId();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Assembly o) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getAssemblyId()
	 */
	@Override
	public long getAssemblyId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getAssmeblyContext()
	 */
	@Override
	public AssemblyContext getAssmeblyContext() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getAssemblyStatus()
	 */
	@Override
	public AssemblyStatus getAssemblyStatus() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getCategory()
	 */
	@Override
	public String getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getDescription()
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getInitParameter(java.lang.String)
	 */
	@Override
	public String getInitParameter(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getinitParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public String getinitParameter(String key, String defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getSymbolicName()
	 */
	@Override
	public String getSymbolicName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#isReady()
	 */
	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#isWorking()
	 */
	@Override
	public boolean isWorking() {
		// TODO Auto-generated method stub
		return false;
	}

}
