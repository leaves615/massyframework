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
