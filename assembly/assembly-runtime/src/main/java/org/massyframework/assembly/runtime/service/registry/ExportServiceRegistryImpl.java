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
package org.massyframework.assembly.runtime.service.registry;

import java.util.Map;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ServiceFactory;
import org.massyframework.assembly.base.ExportServiceRegistration;
import org.massyframework.assembly.base.ExportServiceRegistry;
import org.massyframework.assembly.util.Asserts;

/**
 * 实现{@link ExportServiceRepository}接口
 */
abstract class ExportServiceRegistryImpl implements ExportServiceRegistry{

	private final Assembly assembly;
	
	/**
	 * 
	 */
	public ExportServiceRegistryImpl(Assembly assembly) {
		Asserts.notNull(assembly, "assembly cannot be null.");
		this.assembly = assembly;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.ExportServiceRegistry#register(java.lang.Class, java.lang.Object, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <S> ExportServiceRegistration<S> register(Class<S> clazz, S service, Map<String, Object> props) {
		ExportServiceRegistration<?> result = this.register(new Class<?>[]{clazz}, service, props);	
		return (ExportServiceRegistration<S>)result;
				
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.ExportServiceRegistry#register(java.lang.Class, org.massyframework.assembly.ServiceFactory, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <S> ExportServiceRegistration<S> register(Class<S> clazz, ServiceFactory<S> factory,
			Map<String, Object> props) {
		ExportServiceRegistration<?> result = this.register(new Class<?>[]{clazz}, factory, props);
		return (ExportServiceRegistration<S>)result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.ExportServiceRegistry#register(java.lang.Class[], java.lang.Object, java.util.Map)
	 */
	@Override
	public ExportServiceRegistration<?> register(Class<?>[] classes, Object service, Map<String, Object> props) {
		Asserts.notEmpty(classes, "classes cannot be empty.");
		Asserts.notNull(service, "service cannot be null.");
		
		//检查类型
		Class<?> objectClass = service.getClass();
		for (Class<?> clazz: classes){
			if (!clazz.isAssignableFrom(objectClass)){
				throw new IllegalArgumentException("service isnot assignable to: " + clazz.getName() + ".");
			}
		}
		
		ExportServiceRegistration<?> result = 
				this.getExportServiceRegistryFactory()
					.doRegister(classes, service, props, assembly);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.ExportServiceRegistry#register(java.lang.Class[], org.massyframework.assembly.ServiceFactory, java.util.Map)
	 */
	@Override
	public ExportServiceRegistration<?> register(Class<?>[] classes, ServiceFactory<?> factory,
			Map<String, Object> props) {
		Asserts.notEmpty(classes, "classes cannot be empty.");
		Asserts.notNull(factory, "factory cannot be null.");
		
		ExportServiceRegistration<?> result = 
				this.getExportServiceRegistryFactory()
					.doRegister(classes, factory, props, assembly);
		return result;
	}
	
	/**
	 * 所属的装配件
	 * @return {@link Assembly}
	 */
	protected Assembly getAssembly(){
		return this.assembly;
	}

	/**
	 * 获取输出服务注册工厂
	 * @return {@link ExportServiceRegistryFactory}
	 */
	protected abstract ExportServiceRegistryFactory getExportServiceRegistryFactory();
}
