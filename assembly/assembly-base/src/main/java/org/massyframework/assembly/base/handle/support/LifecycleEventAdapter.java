/**
* @Copyright: 2017 smarabbit studio.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*   
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
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
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onReadied()
	 */
	@Override
	public void onReadied() {
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
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onUnreadying()
	 */
	@Override
	public void onUnreadying() {

	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleListener#onUninstalling()
	 */
	@Override
	public void onUninstalling() {

	}

}
