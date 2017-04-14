/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spring4;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyAware;
import org.massyframework.assembly.util.Asserts;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 执行AssemblyAware注入Bean的处理器
 *
 */
final class AssemblyAwareBeanPostProcessor implements BeanPostProcessor {

	private final Assembly assembly;
	
	/**
	 * 
	 */
	public AssemblyAwareBeanPostProcessor(Assembly assembly) {
		Asserts.notNull(assembly, "assembly cannot be null.");
		this.assembly = assembly;
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
		AssemblyAware.maybeToBind(bean, assembly);
		return bean;
	}

}
