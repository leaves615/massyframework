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
* @日   期:  2017年4月12日
*/
package org.massyframework.assembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.massyframework.assembly.util.Asserts;

/**
 * 自定义的装配件上下文
 */
public class CustmizeAssemblyContext implements AssemblyContext, AssemblyAware, InjectCallback {

	private volatile Assembly assembly;
	private Map<String, Object> serviceMap;
	
	/**
	 * 
	 */
	public CustmizeAssemblyContext() {
		this.serviceMap = new HashMap<String, Object>();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyAware#setAssembly(org.massyframework.assembly.Assembly)
	 */
	@Override
	public void setAssembly(Assembly assembly) {
		this.assembly = assembly;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getAssembly()
	 */
	@Override
	public Assembly getAssembly() {
		return this.assembly;
	}
	
	/**
	 * 初始化
	 */
	protected void init(){
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#containsService(java.lang.String)
	 */
	@Override
	public boolean containsService(String cName) {
		if (cName == null) return false;
		return this.serviceMap.containsKey(cName);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.String)
	 */
	@Override
	public Object getService(String cName) throws ServiceNotFoundException {
		Asserts.notNull(cName, "cName cannot be null.");
		Object result = this.serviceMap.get(cName);
		if (result == null){
			throw new ServiceNotFoundException("service not found: cName=" + cName + ".");
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.Class)
	 */
	@Override
	public <S> S getService(Class<S> resultType) throws ServiceNotFoundException {
		Asserts.notNull(resultType, "resultType cannot be null.");
		return this.getService(resultType.getName(), resultType);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.String, java.lang.Class)
	 */
	@Override
	public <S> S getService(String cName, Class<S> resultType) throws ServiceNotFoundException {
		Asserts.notNull(resultType, "resultType cannot be null.");
		Object result = this.getService(cName);
		return resultType.cast(result);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getServiceNames()
	 */
	@Override
	public List<String> getServiceNames() {
		return new ArrayList<String>(this.serviceMap.keySet());
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getServicesOfType(java.lang.Class)
	 */
	@Override
	public <S> Map<String, S> getServicesOfType(Class<S> resultType) {
		Asserts.notNull(resultType, "resultType cannot be null.");
		Map<String, S> result = new HashMap<String, S>();
		for (Entry<String, Object> entry: this.serviceMap.entrySet()){
			if (resultType.isAssignableFrom(entry.getValue().getClass())){
				result.put(entry.getKey(), resultType.cast(entry.getValue()));
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.InjectCallback#doInject(java.util.Map)
	 */
	@Override
	public void doInject(Map<String, Object> serviceMap) throws Exception {
		for (Entry<String, Object> entry: serviceMap.entrySet()){
			this.serviceMap.putIfAbsent(entry.getKey(), entry.getValue());
		}
		this.init();
	}
	
	/**
	 * 添加服务
	 * @param cName 名称
	 * @param service 服务实例
	 */
	protected void addService(String cName, Object service){
		Asserts.notNull(cName, "cName cannot be null.");
		Asserts.notNull(service, "service cannot be null.");
		this.serviceMap.putIfAbsent(cName, service);
	}
}
