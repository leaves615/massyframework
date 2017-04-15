/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base;

import java.util.ArrayList;
import java.util.List;

import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.ObjectReference;
import org.massyframework.assembly.util.Asserts;

/**
 * 适配管理器
 */
final class AdaptManagement implements ExportServiceRepositoryReference{
	
	private volatile ExportServiceRepository serviceRepository;
	private List<Object> objects =
			new ArrayList<Object>();

	/**
	 * 构造方法
	 * @param serviceRepository
	 */
	public AdaptManagement(ExportServiceRepository serviceRepository) {
		Asserts.notNull(serviceRepository, "serviceRepository cannot be null.");
		this.serviceRepository = serviceRepository;
	}
	
	/**
	 * 添加适配对象
	 * @param adaptObject 适配对象
	 */
	public synchronized void addAdaptObject(Object adaptObject){
		if (adaptObject != null){
			this.objects.add(adaptObject);
		}
	}
	
	/**
	 * 获取适配对象
	 * @param adaptType 适配类型
	 * @return {@link T},无满足适配的返回null.
	 */
	public synchronized <T> T adapt(Class<T> adaptType){
		Object result = null;
		if (adaptType == ExportServiceRepositoryReference.class){
			return adaptType.cast(this);
		}
		
		for (Object obj: this.objects){
			if (adaptType.isAssignableFrom(obj.getClass())){
				result = obj;
			}
		}
		
		if (result == null){
			if (ObjectReference.class.isAssignableFrom(adaptType)){
				result = this.serviceRepository.findService(adaptType);
			}
		}
		
		return result == null ? null : adaptType.cast(result);
	}

	@Override
	public ExportServiceRepository getReference() {
		return this.serviceRepository;
	}
}
