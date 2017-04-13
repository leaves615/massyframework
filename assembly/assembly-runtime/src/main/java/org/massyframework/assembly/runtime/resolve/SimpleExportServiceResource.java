/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime.resolve;

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
final class SimpleExportServiceResource implements ExportServiceResource {

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

}
