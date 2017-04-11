/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime.service.registry;

import java.util.Map;

import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.Filter;


/**
 * 空的过滤器, 不对服务进行筛选
 */
class EmptyFilter implements Filter {

	/**
	 * 
	 */
	private EmptyFilter() {
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.service.Filter#match(java.util.Map)
	 */
	@Override
	public boolean match(Map<String, Object> props) {
		return props != null;
	}

	/* (non-Javadoc)
	 * @see org.smarabbit.massy.service.Filter#match(org.smarabbit.massy.service.ServiceReference)
	 */
	@Override
	public boolean match(ExportServiceReference<?> reference) {
		return reference != null;
	}
	
	private static EmptyFilter EMPTY = new EmptyFilter();
	
	public static Filter getDefault(){
		return EMPTY;
	}

}
