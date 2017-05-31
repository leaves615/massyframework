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

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyAware;
import org.massyframework.assembly.util.Asserts;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 执行AssemblyAware注入Bean的处理器
 *
 */
final class AssemblyAwareBeanPostProcessor implements BeanPostProcessor {

	private final Assembly assembly;
	private final DefaultListableBeanFactory beanFactory;
	
	/**
	 * 
	 */
	public AssemblyAwareBeanPostProcessor(Assembly assembly, DefaultListableBeanFactory beanFactory) {
		Asserts.notNull(assembly, "assembly cannot be null.");
		Asserts.notNull(beanFactory, "beanFactory cannot be null.");
		this.assembly = assembly;
		this.beanFactory = beanFactory;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (beanName != null){
			if (this.beanFactory.containsBeanDefinition(beanName)){
				BeanDefinition definition = this.beanFactory.getBeanDefinition(beanName);
				if (definition != null){
					if (DependencyServiceFactoryBean.class.getName().equals(definition.getBeanClassName())){
						return bean;
					}
				}
			}
		}
		
		AssemblyAware.maybeToBind(bean, assembly);
		return bean;
	}

}
