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
* @日   期:  2017年4月11日
*/
package org.massyframework.assembly.base.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 不可变的初始化参数
 * @author huangkaihui
 *
 */
public final class ImmutableInitParams implements InitParams {

	private ImmutableMap<String, String> params;
	
	/**
	 * 
	 */
	public ImmutableInitParams(Map<String, String> params) {
		this.params = ImmutableMap.newInstance(params);
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.assembly.InitParams#containsParameter(java.lang.String)
	 */
	@Override
	public boolean containsParameter(String key) {
		if (key == null) return false;
		return this.params.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.assembly.InitParams#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String key) {
		return this.params.get(key);
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.assembly.InitParams#getParameterKeys()
	 */
	@Override
	public List<String> getParameterKeys() {
		return new ArrayList<String>(
				this.params.keySet());
	}

}
