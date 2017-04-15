/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spring4;

import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * 依赖服务工厂Bean
 */
final class DependencyServiceFactoryBean<T> extends AbstractFactoryBean<T> {
	
	private Class<T> objectType;
	private T object;

	public DependencyServiceFactoryBean(){
		
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return this.objectType;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
	 */
	@Override
	protected T createInstance() throws Exception {
		return this.object;
	}

	/**
	 * 设置服务类型
	 * @param objectType the objectType to set
	 */
	public void setObjectType(Class<T> objectType) {
		this.objectType = objectType;
	}
	
	public void setObject(T service){
		this.object = service;
	}
}
