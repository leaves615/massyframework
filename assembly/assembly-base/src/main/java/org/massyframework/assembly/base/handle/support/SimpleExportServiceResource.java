/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
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
package org.massyframework.assembly.base.handle.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.base.handle.ExportServiceResource;
import org.massyframework.assembly.util.Asserts;

/**
 * 简单实现的输出服务资源
 */
public final class SimpleExportServiceResource implements ExportServiceResource {

	private Map<String, Object> props;
	
	/**
	 * 
	 */
	public SimpleExportServiceResource(String cName, Class<?>[] exportTypes, Map<String, Object> props) {
		Asserts.notNull(cName, "cName cannot be null.");
		Asserts.notEmpty(exportTypes, "exportTypes cannot be empty.");
		this.props = props == null ?
				new HashMap<String, Object>() :
					new HashMap<String, Object>(props);
		this.props.put(Constants.SERVICE_CNAME, cName);
		this.props.put(Constants.OBJECT_CLASS, exportTypes);
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ExportServiceResource#getCName()
	 */
	@Override
	public String getCName() {
		return (String)this.props.get(Constants.SERVICE_CNAME);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ExportServiceResource#getExportTypes()
	 */
	@Override
	public Class<?>[] getExportTypes() {
		return (Class<?>[])this.props.get(Constants.OBJECT_CLASS);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ExportServiceResource#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String key) {
		return this.props.get(key);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.ExportServiceResource#getPropertyKeys()
	 */
	@Override
	public List<String> getPropertyKeys() {
		return new ArrayList<String>(this.props.keySet());
	}

	Map<String, Object> getServiceProperties(){
		return props;
	}
}
