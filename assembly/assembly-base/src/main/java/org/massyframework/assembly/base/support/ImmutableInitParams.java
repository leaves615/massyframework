/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
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
