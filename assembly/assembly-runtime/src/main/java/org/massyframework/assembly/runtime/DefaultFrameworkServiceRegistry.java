/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import java.util.Map;

import org.massyframework.assembly.FrameworkServiceRegistry;
import org.massyframework.assembly.NameExistsException;
import org.massyframework.assembly.ServiceFactory;
import org.massyframework.assembly.base.ExportServiceRegistry;
import org.massyframework.assembly.util.Asserts;

/**
 * 实现{@link FrameworkServiceRegistry}
 */
public final class DefaultFrameworkServiceRegistry implements FrameworkServiceRegistry {
	
	private final ExportServiceRegistry serviceRegistry;

	/**
	 * 
	 */
	public DefaultFrameworkServiceRegistry(ExportServiceRegistry serviceRegistry) {
		Asserts.notNull(serviceRegistry, "serviceRegistry");
		this.serviceRegistry = serviceRegistry;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkServiceRegistry#addService(java.lang.Class[], java.lang.Object, java.util.Map)
	 */
	@Override
	public void addService(Class<?>[] exportTypes, Object service, Map<String, Object> props) {
		this.serviceRegistry.register(exportTypes, service, props);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkServiceRegistry#addService(java.lang.Class, java.lang.Object, java.util.Map)
	 */
	@Override
	public <S> void addService(Class<S> exportType, S service, Map<String, Object> props) {
		this.serviceRegistry.register(exportType, service, props);

	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkServiceRegistry#addService(java.lang.Class, org.massyframework.assembly.ServiceFactory, java.util.Map)
	 */
	@Override
	public <S> void addService(Class<S> exportType, ServiceFactory<S> factory, Map<String, Object> props) {
		this.serviceRegistry.register(exportType, factory, props);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkServiceRegistry#addService(java.lang.Class[], org.massyframework.assembly.ServiceFactory, java.util.Map)
	 */
	@Override
	public <S> void addService(Class<?>[] exportTypes, ServiceFactory<S> factory, Map<String, Object> props)
			throws NameExistsException {
		this.serviceRegistry.register(exportTypes, factory, props);
	}

}
