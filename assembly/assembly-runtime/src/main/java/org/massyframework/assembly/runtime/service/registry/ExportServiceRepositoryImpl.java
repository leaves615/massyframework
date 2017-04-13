/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime.service.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.ExportServiceListener;
import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.Filter;
import org.massyframework.assembly.ServiceNotFoundException;
import org.massyframework.assembly.util.Asserts;

/**
 * 实现{@link ExportServiceRepository}
 */
public class ExportServiceRepositoryImpl extends ExportServiceRegistryImpl implements ExportServiceRepository {

	private final ExportServiceRepositoryManagement factory;
	
	/**
	 * 构造方法
	 * @param registryFactory
	 * @param assembly
	 */
	public ExportServiceRepositoryImpl(ExportServiceRepositoryManagement factory, Assembly assembly) {
		super(assembly);
		Asserts.notNull(factory, "factory cannot be null.");
		this.factory = factory;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#addListener(org.massyframework.assembly.ExportServiceListener, org.massyframework.assembly.Filter)
	 */
	@Override
	public void addListener(ExportServiceListener listener, Filter filter) {
		this.factory.addListener(listener, filter, this.getAssembly());
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#addListener(org.massyframework.assembly.ExportServiceListener, java.lang.String)
	 */
	@Override
	public void addListener(ExportServiceListener listener, String filterString) {
		Filter filter = this.createFilter(filterString);
		this.addListener(listener, filter);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#findService(java.lang.Class)
	 */
	@Override
	public <S> S findService(Class<S> serviceType) {
		Filter filter = this.factory.createFilter(null);
		return this.findService(serviceType, filter);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#findService(java.lang.Class, org.massyframework.assembly.Filter)
	 */
	@Override
	public <S> S findService(Class<S> serviceType, Filter filter) {
		Asserts.notNull(serviceType, "serviceType cannot be null.");
		ExportServiceReference<S> reference = 
				this.factory.findServiceReference(serviceType, filter);
		if (reference != null){
			return this.factory.getService(reference, this.getAssembly());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#findService(java.lang.Class, java.lang.String)
	 */
	@Override
	public <S> S findService(Class<S> serviceType, String filterString) {
		Filter filter = this.factory.createFilter(filterString);
		return this.findService(serviceType, filter);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#findServiceReference(java.lang.Class)
	 */
	@Override
	public <S> ExportServiceReference<S> findServiceReference(Class<S> serviceType) {
		Filter filter = this.factory.createFilter(null);
		return this.factory.findServiceReference(serviceType, filter);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#findServiceReference(org.massyframework.assembly.Filter)
	 */
	@Override
	public ExportServiceReference<?> findServiceReference(Filter filter) {
		return this.factory.findServiceReference(filter);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#findServiceReference(java.lang.String)
	 */
	@Override
	public ExportServiceReference<?> findServiceReference(String filterString) {
		return this.factory.findServiceReference(
				this.factory.createFilter(filterString));
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#findServiceRefernece(java.lang.Class, org.massyframework.assembly.Filter)
	 */
	@Override
	public <S> ExportServiceReference<S> findServiceRefernece(Class<S> serviceType, Filter filter) {
		return this.factory.findServiceReference(serviceType, filter);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#findServiceRefernece(java.lang.Class, java.lang.String)
	 */
	@Override
	public <S> ExportServiceReference<S> findServiceRefernece(Class<S> serviceType, String filterString) {
		return this.factory.findServiceReference(serviceType, this.factory.createFilter(filterString));
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#getServiceReferences(java.lang.Class)
	 */
	@Override
	public <S> List<ExportServiceReference<S>> getServiceReferences(Class<S> serviceType) {
		Filter filter = this.factory.createFilter(null);
		return this.factory.getServiceReferences(serviceType, filter);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#getServiceReferences(org.massyframework.assembly.Filter)
	 */
	@Override
	public List<ExportServiceReference<?>> getServiceReferences(Filter filter) {
		return this.factory.getServiceReferences(filter);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#getServiceReferences(java.lang.String)
	 */
	@Override
	public List<ExportServiceReference<?>> getServiceReferences(String filterString) {
		Filter filter = this.factory.createFilter(filterString);
		return this.factory.getServiceReferences(filter);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#getServiceReferences(java.lang.Class, org.massyframework.assembly.Filter)
	 */
	@Override
	public <S> List<ExportServiceReference<S>> getServiceReferences(Class<S> serviceType, Filter filter) {
		return this.factory.getServiceReferences(serviceType, filter);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#getServiceReferences(java.lang.Class, java.lang.String)
	 */
	@Override
	public <S> List<ExportServiceReference<S>> getServiceReferences(Class<S> serviceType, String filterString) {
		Filter filter = this.factory.createFilter(filterString);
		return this.factory.getServiceReferences(serviceType, filter);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#getService(org.massyframework.assembly.ExportServiceReference)
	 */
	@Override
	public <S> S getService(ExportServiceReference<S> reference) throws ServiceNotFoundException {
		return this.factory.getService(reference, this.getAssembly());
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#getServices(java.util.Collection)
	 */
	@Override
	public List<Object> getServices(Collection<ExportServiceReference<?>> references) throws ServiceNotFoundException {
		return this.factory.getServices(references, this.getAssembly());
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#getSameServices(java.util.Collection)
	 */
	@Override
	public <S> List<S> getSameServices(Collection<ExportServiceReference<S>> references)
			throws ServiceNotFoundException {
		List<S> result = new ArrayList<S>();
		if (references != null){
			for (ExportServiceReference<S> reference: references){
				result.add(this.factory.getService(reference, this.getAssembly()));
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#createFilter(java.lang.String)
	 */
	@Override
	public Filter createFilter(String filterString) {
		return this.factory.createFilter(filterString);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceRepository#removeListener(org.massyframework.assembly.ExportServiceListener)
	 */
	@Override
	public void removeListener(ExportServiceListener listener) {
		this.factory.getServiceListenerManagement()
			.removeListener(listener, this.getAssembly());
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.runtime.service.registry.ExportServiceRegistryImpl#getExportServiceRegistryFactory()
	 */
	@Override
	protected ExportServiceRegistryFactory getExportServiceRegistryFactory() {
		return this.factory;
	}

	
}
