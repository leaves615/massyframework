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
package org.massyframework.assembly.spring;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.InjectCallback;
import org.massyframework.assembly.ServiceNotFoundException;
import org.massyframework.assembly.base.handle.DependencyServiceResource;
import org.massyframework.assembly.base.handle.DependencyServiceResourceHandler;
import org.massyframework.assembly.base.handle.Handler;
import org.massyframework.assembly.base.handle.HandlerRegistry;
import org.massyframework.assembly.base.handle.ServiceInjectHandler;
import org.massyframework.assembly.util.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;

/**
 * 实现{@link AssemblyContext}的Spring容器
 */
public class SpringAssemblyContext extends AbstractXmlApplicationContext 
	implements AssemblyContext, Handler {

	private volatile HandlerRegistry handlerRegistry;
	
	/**
	 * 
	 */
	public SpringAssemblyContext() {
	}

	/**
	 * @param parent
	 */
	public SpringAssemblyContext(ApplicationContext parent) {
		super(parent);
	}
	
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.handle.Handler#init(org.massyframework.assembly.handle.HandlerRegistry)
	 */
	@Override
	public void init(HandlerRegistry handlerRegistry) {
		this.handlerRegistry = handlerRegistry;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.context.support.AbstractXmlApplicationContext#initBeanDefinitionReader(org.springframework.beans.factory.xml.XmlBeanDefinitionReader)
	 */
	@Override
	protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
		super.initBeanDefinitionReader(reader);
        reader.setBeanClassLoader(this.getClassLoader());
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.support.AbstractXmlApplicationContext#loadBeanDefinitions(org.springframework.beans.factory.support.DefaultListableBeanFactory)
	 */
	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
		this.prapareLoadBeanDefinitions(beanFactory);
		super.loadBeanDefinitions(beanFactory);
		this.postLoadBeanDefintions(beanFactory);
	}
	
	/**
	 * 准备加载Bean定义
	 * @param beanFactory
	 * @throws BeansException
	 * @throws IOException
	 */
	protected final void prapareLoadBeanDefinitions(DefaultListableBeanFactory beanFactory)
			throws BeansException, IOException {
		try{
			AssemblyAwareBeanPostProcessor processor =
					new AssemblyAwareBeanPostProcessor(this.handlerRegistry.getReference(), beanFactory);
			beanFactory.addBeanPostProcessor(processor);
			
			ServiceInjectHandler handler =
				this.handlerRegistry.getHandler(ServiceInjectHandler.class);
			handler.addInjectCallback(new Callback(beanFactory));
		}catch(Exception e){
			throw new BeanDefinitionStoreException("inject dependency service failed.", e);
		}
	}
	
	/**
	 * 注入服务
	 * @param serviceMap 服务Map
	 * @throws Exception 发生非预期的例外
	 */
	private void inject(Map<DependencyServiceResource, Object> serviceMap,
			DefaultListableBeanFactory beanFactory) throws Exception{
		if (MapUtils.isEmpty(serviceMap)){
			return;
		}
				
		for (Entry<DependencyServiceResource, Object> entry: serviceMap.entrySet()){
		
			String beanName = entry.getKey().getCName();
			Class<?> beanType = entry.getKey().getRequiredType();
			
			
			//动态的增加Bean定义
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(DependencyServiceFactoryBean.class);
			MutablePropertyValues propertyValue = beanDefinition
					.getPropertyValues();
			propertyValue.add("objectType", beanType);
			propertyValue.add("object", entry.getValue());

			//使用组件编号注册Bean定义
			beanFactory.registerBeanDefinition(beanName, beanDefinition);
		}
	}
	/**
	 * 执行加载Bean定义
	 * @param beanFactory
	 * @throws BeansException
	 * @throws IOException
	 */
	protected void doLoadBeanDefinitions(DefaultListableBeanFactory beanFactory)
			throws BeansException, IOException {
		super.loadBeanDefinitions(beanFactory);
	}
	
	/**
	 * Bean定义加载后处理
	 * @param beanFactory
	 * @throws BeansException
	 * @throws IOException
	 */
	protected void postLoadBeanDefintions(DefaultListableBeanFactory beanFactory)
			throws BeansException, IOException {
		//增加ExportServiceRepository
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(DependencyServiceFactoryBean.class);
		MutablePropertyValues propertyValue = beanDefinition
				.getPropertyValues();
		propertyValue.add("objectType", ExportServiceRepository.class);
		propertyValue.add("object", 
				ExportServiceRepositoryReference.adaptFrom(this.getAssembly()));
		beanFactory.registerBeanDefinition(ExportServiceRepository.class.getName(), beanDefinition);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getAssembly()
	 */
	@Override
	public Assembly getAssembly() {
		return this.handlerRegistry == null ? null : this.handlerRegistry.getReference();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#containsService(java.lang.String)
	 */
	@Override
	public boolean containsService(String cid) {
		return this.containsBean(cid);
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getServiceNames()
	 */
	@Override
	public List<String> getServiceNames() {
		return Arrays.asList(this.getBeanDefinitionNames());
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.Class)
	 */
	@Override
	public <S> S getService(Class<S> requiredType) throws ServiceNotFoundException {
		try{
			return this.getBean(requiredType);
		}catch(BeansException e){
			throw new ServiceNotFoundException(requiredType.getName(), e);
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.String)
	 */
	@Override
	public Object getService(String cName) throws ServiceNotFoundException {
		try{
			return this.getBean(cName);
		}catch(BeansException e){
			throw new ServiceNotFoundException(cName, e);
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getService(java.lang.String, java.lang.Class)
	 */
	@Override
	public <S> S getService(String cName, Class<S> requiredType) throws ServiceNotFoundException {
		try{
			return this.getBean(cName, requiredType);
		}catch(BeansException e){
			throw new ServiceNotFoundException("service not found: cName=" + cName + ", requiredType=" + requiredType + ".", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyContext#getServicesWithType(java.lang.Class)
	 */
	@Override
	public <S> Map<String, S> getServicesOfType(Class<S> requiredType) {
		return this.getBeansOfType(requiredType);
	}
	
	private class Callback implements InjectCallback {
		
		private DefaultListableBeanFactory beanFactory;
		
		public Callback(DefaultListableBeanFactory beanFactory){
			this.beanFactory = beanFactory;
		}

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
			inject(services, beanFactory);
			doLoadBeanDefinitions(beanFactory);
		}
		
	}
}
