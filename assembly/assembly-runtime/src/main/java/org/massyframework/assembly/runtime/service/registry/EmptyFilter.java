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
