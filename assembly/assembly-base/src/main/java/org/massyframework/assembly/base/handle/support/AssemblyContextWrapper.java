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
package org.massyframework.assembly.base.handle.support;

import java.util.List;
import java.util.Map;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyAware;
import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.ServiceNotFoundException;
import org.massyframework.assembly.util.Asserts;

/**
 * 装配件上下文封装器
 */
public class AssemblyContextWrapper<T extends AssemblyContext> 
	extends AbstractHandler implements AssemblyContext {

	private final T reference;
	/**
	 * 
	 */
	public AssemblyContextWrapper(T context) {
		Asserts.notNull(context, "context cannot be null.");
		this.reference = context;
	}

	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.support.AbstractHandler#init()
	 */
	@Override
	protected void init() {
		super.init();
		AssemblyAware.maybeToBind(this.reference, this.getAssembly());
	}


	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.Handler#destroy()
	 */
	@Override
	public void destroy() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getAssembly()
	 */
	@Override
	public Assembly getAssembly() {
		return super.getAssembly();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#containsService(java.lang.String)
	 */
	@Override
	public boolean containsService(String cName) {
		return this.reference.containsService(cName);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.String)
	 */
	@Override
	public Object getService(String cName) throws ServiceNotFoundException {
		return this.reference.getService(cName);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.Class)
	 */
	@Override
	public <S> S getService(Class<S> resultType) throws ServiceNotFoundException {
		return this.reference.getService(resultType);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.String, java.lang.Class)
	 */
	@Override
	public <S> S getService(String cName, Class<S> resultType) throws ServiceNotFoundException {
		return this.reference.getService(cName, resultType);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getServiceNames()
	 */
	@Override
	public List<String> getServiceNames() {
		return this.reference.getServiceNames();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getServicesOfType(java.lang.Class)
	 */
	@Override
	public <S> Map<String, S> getServicesOfType(Class<S> resultType) {
		return this.reference.getServicesOfType(resultType);
	}
	
	/**
	 * 获取内部引用的装配上下文
	 * @return
	 */
	public T getReference(){
		return this.reference;
	}

}
