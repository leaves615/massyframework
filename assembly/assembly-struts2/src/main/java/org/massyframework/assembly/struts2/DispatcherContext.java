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
* @日   期:  2017年5月19日
*/
package org.massyframework.assembly.struts2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.struts2.dispatcher.Dispatcher;
import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.InjectCallback;
import org.massyframework.assembly.ServiceNotFoundException;
import org.massyframework.assembly.base.handle.AssemblyContextHandler;
import org.massyframework.assembly.base.handle.DependencyServiceResource;
import org.massyframework.assembly.base.handle.DependencyServiceResourceHandler;
import org.massyframework.assembly.base.handle.ExportServiceResource;
import org.massyframework.assembly.base.handle.ExportServiceResourceHandler;
import org.massyframework.assembly.base.handle.HandlerRegistry;
import org.massyframework.assembly.base.handle.ServiceInjectHandler;
import org.massyframework.assembly.util.MapUtils;
import org.springframework.util.CollectionUtils;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.ContainerProvider;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.inject.Factory;
import com.opensymphony.xwork2.inject.Scope;
import com.opensymphony.xwork2.util.location.LocatableProperties;

/**
 * 使用Dispatcher作为Struts的服务容器管理器
 * @author huangkaihui
 *
 */
public class DispatcherContext extends Dispatcher 
	implements AssemblyContextHandler, AssemblyContext {
	
	private volatile HandlerRegistry handlerRegistry;
	private volatile Map<String, ExportServiceResource> exportes;

	/**
	 * @param servletContext
	 * @param initParams
	 */
	public DispatcherContext(ServletContext servletContext, Map<String, String> initParams) {
		super(servletContext, initParams);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.Handler#init(org.massyframework.assembly.base.handle.HandlerRegistry)
	 */
	@Override
	public void init(HandlerRegistry handlerRegistry) {
		this.handlerRegistry = handlerRegistry;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.Handler#destroy()
	 */
	@Override
	public void destroy() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.AssemblyContextHandler#canWork()
	 */
	@Override
	public boolean canWork() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts2.dispatcher.Dispatcher#createConfigurationManager(java.lang.String)
	 */
	@Override
	protected ConfigurationManager createConfigurationManager(String name) {
		ConfigurationManager result = super.createConfigurationManager(name);
		result.addContainerProvider(new Provider());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.AssemblyContextHandler#getExportableServices(java.util.List)
	 */
	@Override
	public Map<ExportServiceResource, Object> getExportableServices(List<ExportServiceResource> resources)
			throws Exception {
		Map<ExportServiceResource, Object> result =
				new HashMap<ExportServiceResource, Object>();
		Container container = this.getContainer();
		
		for (ExportServiceResource resource: resources){
			String name = resource.getCName();
			Class<?> type = resource.getExportTypes()[0];
			Object service = container.getInstance(type, name);
			result.put(resource, service);
		}
		
		return result;
	}
	

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getAssembly()
	 */
	@Override
	public Assembly getAssembly() {
		HandlerRegistry registry = this.handlerRegistry;
		return registry == null ?
				null :
					registry.getReference();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#containsService(java.lang.String)
	 */
	@Override
	public boolean containsService(String cName) {
		return this.exportes.containsKey(cName);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.String)
	 */
	@Override
	public Object getService(String cName) throws ServiceNotFoundException {
		ExportServiceResource resource =
				this.exportes.get(cName);
		return resource == null ?
				null :
					this.getContainer().getInstance(resource.getExportTypes()[0], cName);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.Class)
	 */
	@Override
	public <S> S getService(Class<S> resultType) throws ServiceNotFoundException {
		return this.getContainer().getInstance(resultType);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.String, java.lang.Class)
	 */
	@Override
	public <S> S getService(String cName, Class<S> resultType) throws ServiceNotFoundException {
		return this.getContainer().getInstance(resultType, cName);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getServiceNames()
	 */
	@Override
	public List<String> getServiceNames() {
		List<String> result = new ArrayList<String>(this.exportes.keySet());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getServicesOfType(java.lang.Class)
	 */
	@Override
	public <S> Map<String, S> getServicesOfType(Class<S> resultType) {
		Map<String, S> result = new HashMap<String, S>();
		
		Set<String> names = this.getContainer().getInstanceNames(resultType);
		if (!CollectionUtils.isEmpty(names)){
			for (String name: names){
				result.put(name, this.getContainer().getInstance(resultType, name));
			}
		}
		return result;
	}

	private class Provider implements ContainerProvider {
		
		private volatile Map<DependencyServiceResource, Object> depdendencies ;
		

		@Override
		public void destroy() {
						
		}

		@Override
		public void init(Configuration configuration) throws ConfigurationException {
			try{
				ExportServiceResourceHandler resourceHandler =
						handlerRegistry.getHandler(ExportServiceResourceHandler.class);
				List<ExportServiceResource> resources = resourceHandler.getExportServiceResources();
				Map<String, ExportServiceResource> tmp = new HashMap<String, ExportServiceResource>();
				for (ExportServiceResource resource: resources){
					tmp.put(resource.getCName(), resource);
				}
				exportes = tmp;
				
				ServiceInjectHandler handler =
					handlerRegistry.getHandler(ServiceInjectHandler.class);
			
				handler.addInjectCallback(new Callback());
			}catch(Exception e){
				throw new ConfigurationException(e.getMessage(), e);
			}
		}

		@Override
		public boolean needsReload() {
			return false;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void register(ContainerBuilder builder, LocatableProperties props) throws ConfigurationException {
			if (!MapUtils.isEmpty(depdendencies)){
				for (Entry<DependencyServiceResource, Object> entry: depdendencies.entrySet()){
					DependencyServiceResource resource = entry.getKey();
					this.addService(builder, resource.getCName(), resource.getRequiredType(), 
							new DependencyServiceFactory(entry.getValue()));
				}
			}
		}
		
		/**
		 * 添加服务
		 * @param builder
		 * @param name
		 * @param clazz
		 * @param factory
		 */
		private <T> void addService(ContainerBuilder builder, String name, Class<T> clazz, Factory<T> factory){
			builder.factory(clazz, name, factory, Scope.SINGLETON);
		}
		
		private class Callback implements InjectCallback {

			@Override
			public void doInject(Map<String, Object> serviceMap) throws Exception {
				if (MapUtils.isEmpty(serviceMap)){
					return;
				}
				
				DependencyServiceResourceHandler handler =
						handlerRegistry.getHandler(DependencyServiceResourceHandler.class);
				List<DependencyServiceResource> resources=
						handler.getDependencyServiceResources();
				
				Map<DependencyServiceResource, Object> services =
						new HashMap<DependencyServiceResource, Object>();
				for (DependencyServiceResource resource: resources){
					String cName = resource.getCName();
					Object service = serviceMap.get(cName);
					services.put(resource, service);
				}
				
				depdendencies = services;
			}
			
		}
	}
}
