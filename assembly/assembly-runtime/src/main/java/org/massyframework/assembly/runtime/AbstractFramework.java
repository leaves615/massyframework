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
package org.massyframework.assembly.runtime;

import java.util.EventListener;
import java.util.List;
import java.util.Map;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyListener;
import org.massyframework.assembly.AssemblyNotFoundException;
import org.massyframework.assembly.AssemblyResource;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkInitializeLoader;
import org.massyframework.assembly.FrameworkListener;
import org.massyframework.assembly.InitParameterEvent;
import org.massyframework.assembly.InitParameterListener;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.ServiceFactory;
import org.massyframework.assembly.base.AbstractAssembly;
import org.massyframework.assembly.base.ExportServiceRegistry;
import org.massyframework.assembly.base.handle.AssemblyInformationHandler;
import org.massyframework.assembly.base.handle.HandlerRegistry;
import org.massyframework.assembly.runtime.service.registry.ExportServiceRepositoryBuilder;
import org.massyframework.assembly.spec.Specification;
import org.slf4j.Logger;

/**
 * 实现{@link Framework}的抽象类
 */
abstract class AbstractFramework extends AbstractAssembly 
	implements Framework{

	private final DefaultAssemblyManagement assemblyManagement;
	private final DefaultFrameworkLauncher frameworkLauncher;
		
	/**
	 * 构造方法
	 */
	public AbstractFramework() {
		super("org.massyframework.assembly.core");
		AssemblyInformationHandler handler =
				this.getHandlerRegistry().getHandler(AssemblyInformationHandler.class);
		handler.setName("system");
		handler.setDescription("");
		this.assemblyManagement = 
				new DefaultAssemblyManagement(this, this.getExportServiceRepository());
		this.frameworkLauncher = new DefaultFrameworkLauncher(this);
		
	}

	
		
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#addListener(java.util.EventListener)
	 */
	@Override
	public void addListener(EventListener listener) {
		if (listener instanceof FrameworkListener){
			this.frameworkLauncher.addListener((FrameworkListener)listener);
		}
		if (listener instanceof AssemblyListener){
			this.assemblyManagement.addListener((AssemblyListener)listener);
		}
		if (listener instanceof InitParameterListener){
			getFrameworkInitParams().addListener((InitParameterListener)listener);
		}
	}



	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#addExportService(java.lang.Class[], java.lang.Object, java.util.Map)
	 */
	@Override
	public void addExportService(Class<?>[] exportTypes, Object service, Map<String, Object> props) {
		ExportServiceRegistry serviceRegistry =
				this.getExportServiceRepository().findService(ExportServiceRegistry.class);
		serviceRegistry.register(exportTypes, service, props);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#addExportService(java.lang.Class, java.lang.Object, java.util.Map)
	 */
	@Override
	public <S> void addExportService(Class<S> exportType, S service, Map<String, Object> props) {
		ExportServiceRegistry serviceRegistry =
				this.getExportServiceRepository().findService(ExportServiceRegistry.class);
		serviceRegistry.register(exportType, service, props);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#addExportService(java.lang.Class, org.massyframework.assembly.ServiceFactory, java.util.Map)
	 */
	@Override
	public <S> void addExportService(Class<S> exportType, ServiceFactory<S> factory, Map<String, Object> props) {
		ExportServiceRegistry serviceRegistry =
				this.getExportServiceRepository().findService(ExportServiceRegistry.class);
		serviceRegistry.register(exportType, factory, props);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#addExportService(java.lang.Class[], org.massyframework.assembly.ServiceFactory, java.util.Map)
	 */
	@Override
	public <S> void addExportService(Class<?>[] exportTypes, ServiceFactory<S> factory, Map<String, Object> props) {
		ExportServiceRegistry serviceRegistry =
				this.getExportServiceRepository().findService(ExportServiceRegistry.class);
		serviceRegistry.register(exportTypes, factory, props);
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#containsAssembly(java.lang.String)
	 */
	@Override
	public boolean containsAssembly(String symbolicName) {
		return this.getAssemblyManagement().findAssembly(symbolicName) != null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#getAssembly(long)
	 */
	@Override
	public Assembly getAssembly(long assemblyId) throws AssemblyNotFoundException {
		Assembly result = this.getAssemblyManagement().findAssembly(assemblyId);
		if (result == null){
			throw new AssemblyNotFoundException(assemblyId);
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#getAssembly(java.lang.String)
	 */
	@Override
	public Assembly getAssembly(String symbolicName) throws AssemblyNotFoundException {
		Assembly result = this.getAssemblyManagement().findAssembly(symbolicName);
		if (result == null){
			throw new AssemblyNotFoundException(symbolicName);
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#getAssemblies()
	 */
	@Override
	public List<Assembly> getAssemblies() {
		return this.getAssemblyManagement().getAssemblies();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#getAssemblies(org.massyframework.assembly.spec.Specification)
	 */
	@Override
	public List<Assembly> getAssemblies(Specification<Assembly> spec) {
		return this.getAssemblyManagement().filterAll(spec);
	}
	

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#installAssembly(org.massyframework.assembly.AssemblyResource)
	 */
	@Override
	public Assembly installAssembly(AssemblyResource resource) throws Exception {
		try{
			AssemblyRegistration registration = this.assemblyManagement.installAndRegister(resource);
			return registration.getAssembly();
		}catch(Exception e){
			Logger logger = LoggerReference.adaptFrom(this);
			if (logger != null){
				if (logger.isErrorEnabled()){
					logger.error("install assembly failed: resource=" + resource + ".", e);
				}
			}
			
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#setInitParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setInitParameter(String key, String value) {
		FrameworkInitParams initParams =
				this.getHandlerRegistry().getHandler(FrameworkInitParams.class);
		boolean result = initParams.setParameter(key, value);
		if (result){
			initParams.publishChangeEvent(
					new InitParameterEvent(this, key, value));
		}
		return result;
	}
	
	/**
	 * 运行框架初始化参数
	 * @return {@link FrameworkInitParams}
	 */
	FrameworkInitParams getFrameworkInitParams(){
		return this.getHandlerRegistry().getHandler(FrameworkInitParams.class);
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Framework#removeListener(java.util.EventListener)
	 */
	@Override
	public void removeListener(EventListener listener) {
		if (listener instanceof FrameworkListener){
			this.frameworkLauncher.removeListener((FrameworkListener)listener);
		}
		if (listener instanceof AssemblyListener){
			this.assemblyManagement.removeListener((AssemblyListener)listener);
		}
		if (listener instanceof InitParameterListener){
			getFrameworkInitParams().removeListener((InitParameterListener)listener);
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.AbstractAssembly#getExportServiceRepository()
	 */
	@Override
	protected ExportServiceRepository getExportServiceRepository() {
		return ExportServiceRepositoryBuilder.getExportServiceRepository(this);
	}
	

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.AbstractAssembly#getHandlerRegistry()
	 */
	@Override
	protected HandlerRegistry getHandlerRegistry() {
		return super.getHandlerRegistry();
	}
	
	/**
	 * 初始化
	 * @param configuration 配置项目
	 * @param initializeLoader 初始化加载器
	 */
	void init(Map<String, String> configuration, 
			FrameworkInitializeLoader initializeLoader){
		this.frameworkLauncher.init(configuration, initializeLoader);
	}

	/**
	 * 装配件管理
	 * @return {@link DefaultAssemblyManagement}
	 */
	DefaultAssemblyManagement getAssemblyManagement(){
		return this.assemblyManagement; 
	}
	
	FrameworkLauncher getFrameworkLauncher(){
		return this.frameworkLauncher;
	}
}
