/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.handle.support;

import org.massyframework.assembly.base.handle.LifecycleListener;

/**
 * 生命周期事件适配器，提供对{@link LifecycleListener}的简单封装
 */
public abstract class LifecycleEventAdapter implements LifecycleListener {

	/**
	 * 
	 */
	public LifecycleEventAdapter() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onInstalled()
	 */
	@Override
	public void onInstalled() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onResolved()
	 */
	@Override
	public void onResolved() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onRedied()
	 */
	@Override
	public void onRedied() {

	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onActivated()
	 */
	@Override
	public void onActivated() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onInactivating()
	 */
	@Override
	public void onInactivating() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onUninstalling()
	 */
	@Override
	public void onUninstalling() {

	}

}
