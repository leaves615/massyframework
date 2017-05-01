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
* @日   期:  2017年4月9日
*/
package org.massyframework.assembly;

import java.util.Collection;
import java.util.List;

/**
 * 输出服务仓储，提供所有输出服务的查找、获取以及注册服务监听事件。
 */
public interface ExportServiceRepository {

	/**
	 * 添加服务事件监听器
	 * @param listener 服务事件监听器
	 * @param filter 服务事件筛选器
	 * @see Filter 
	 */
	void addListener(ExportServiceListener listener, Filter filter);
	
	/**
	 * 添加服务事件监听器
	 * @param listener 服务事件监听器
	 * @param filterString 服务事件筛选字符串，用于筛选关注的服务.<br>
	 * 			可以为null，表示监听所有服务事件
	 */
	void addListener(ExportServiceListener listener, String filterString);
	
	/**
	 * 判断是否包含特定类型的服务
	 * @param serviceType 服务类型
	 * @return <code>true</code>包含，<code>false</code>不包含 
	 */
	boolean containsService(Class<?> serviceType);
	
	/**
	 * 判断是否包含特定类型的服务
	 * @param className 类型名称
	 * @return <code>true</code>包含，<code>false</code>不包含
	 */
	boolean containsService(String className);
	
		
	/**
	 * 直接查找服务实例<br>
	 * 本方法简化了服务查找方法，合并{@link #findService(Class)}和{@link #getService(ExportServiceReference)}两个方法的调用过程.
	 * @param serviceType 服务类型
	 * @return {@link S}, 无对应服务可以返回null.
	 */
	<S> S findService(Class<S> serviceType);
	
	/**
	 * 直接查找服务实例<br>
	 * 本方法简化了服务查找方法，合并{@link #findService(Class, Filter)}和{@link #getService(ExportServiceReference)}两个方法的调用过程.
	 * @param serviceType 服务类型
	 * @param filter 筛选器
	 * @return {@link S}, 无对应服务可以返回null.
	 */
	<S> S findService(Class<S> serviceType, Filter filter);
	
	/**
	 * 直接查找服务实例<br>
	 * 本方法简化了服务查找方法，合并{@link #findService(Class, String)}和{@link #getService(ExportServiceReference)}两个方法的调用过程.
	 * @param serviceType 服务类型
	 * @param filterString 筛选字符串
	 * @return {@link S}, 无对应服务可以返回null.
	 */
	<S> S findService(Class<S> serviceType, String filterString);
	
	/**
	 * 按服务类型查找服务引用<br>
	 * 存在多个同类型服务时，根据{@link Constants#SERVICE_RANKING}（倒序)，和
	 * {@link Constants#SERVICE_ID}(正序) 排序，并返回最优的服务引用
	 * @param serviceType 服务类型
	 * @return {@link ExportServiceReference}，可能返回null.
	 */
	<S> ExportServiceReference<S> findServiceReference(Class<S> serviceType);
	
	/**
	 * 按服务筛选要求查找服务引用<br>
	 * 存在多个同类型服务时，根据{@link Constants#SERVICE_RANKING}（倒序)，和
	 * {@link Constants#SERVICE_ID}(正序) 排序，并返回最优的服务引用
	 * 
	 * <p>
	 * 在已经返回服务类型的情况下，应尽可能使用效率更高的{@link #findServiceRefernece(Class, String)}方法进行查找。
	 * @param filter 筛选器
	 * @return {@link ExportServiceReference}，可能返回null.
	 */
	ExportServiceReference<?> findServiceReference(Filter filter);
	
	/**
	 * 按服务筛选要求查找服务引用<br>
	 * 存在多个同类型服务时，根据{@link Constants#SERVICE_RANKING}（倒序)，和
	 * {@link Constants#SERVICE_ID}(正序) 排序，并返回最优的服务引用
	 * 
	 * <p>
	 * 在已经返回服务类型的情况下，应尽可能使用效率更高的{@link #findServiceRefernece(Class, String)}方法进行查找。
	 * @param filterString 服务筛选字符串
	 * @return {@link ExportServiceReference}，可能返回null.
	 */
	ExportServiceReference<?> findServiceReference(String filterString);
	
	/**
	 * 按服务类型和筛选要求，查找服务引用<br>
	 * 存在多个同类型服务时，根据{@link Constants#SERVICE_RANKING}（倒序)，和
	 * {@link Constants#SERVICE_ID}(正序) 排序，并返回最优的服务引用
	 * @param serviceType 服务类型
	 * @param filter 筛选器
	 * @return {@link ExportServiceReference}
	 */
	<S> ExportServiceReference<S> findServiceRefernece(Class<S> serviceType, Filter filter);
	
	/**
	 * 按服务类型和筛选要求，查找服务引用<br>
	 * 存在多个同类型服务时，根据{@link Constants#SERVICE_RANKING}（倒序)，和
	 * {@link Constants#SERVICE_ID}(正序) 排序，并返回最优的服务引用
	 * @param serviceType 服务类型
	 * @param filterString 筛选字符串
	 * @return {@link ExportServiceReference}
	 */
	<S> ExportServiceReference<S> findServiceRefernece(Class<S> serviceType, String filterString);
	
	/**
	 * 获取所有输出指定服务类型的服务引用
	 * @param serviceType 服务类型
	 * @return {@link List}
	 */
	<S> List<ExportServiceReference<S>> getServiceReferences(Class<S> serviceType);
	
	/**
	 * 获取满足服务筛选要求的所有服务引用<br>
	 * 在已经返回服务类型的情况下，应尽可能使用效率更高的{@link #getServiceReferences(Class, String)}方法。
	 * @param serviceType 服务类型
	 * @param filter 筛选器
	 * @return {@link List}
	 */
	List<ExportServiceReference<?>> getServiceReferences(Filter filter);
	
	/**
	 * 获取满足服务筛选要求的所有服务引用<br>
	 * 在已经返回服务类型的情况下，应尽可能使用效率更高的{@link #getServiceReferences(Class, String)}方法。
	 * @param serviceType 服务类型
	 * @param filterString 筛选字符串
	 * @return {@link List}
	 */
	List<ExportServiceReference<?>> getServiceReferences(String filterString);
	
	/**
	 * 获取所有输出指定服务类型并满足筛选要求的服务引用
	 * @param serviceType 服务类型
	 * @param filter 筛选器
	 * @return {@link List}
	 * @see Filter
	 */
	<S> List<ExportServiceReference<S>> getServiceReferences(Class<S> serviceType, Filter filter);
	
	/**
	 * 获取所有输出指定服务类型并满足筛选要求的服务引用
	 * @param serviceType 服务类型
	 * @param filterString 筛选字符串
	 * @return {@link List}
	 */
	<S> List<ExportServiceReference<S>> getServiceReferences(Class<S> serviceType, String filterString);
	
	/**
	 * 根据服务引用获取服务实例
	 * @param reference 服务引用
	 * @return {@link S},服务实例
	 * @throws ServiceNotFoundException 服务未找到则抛出例外
	 */
	<S> S getService(ExportServiceReference<S> reference) throws ServiceNotFoundException;
	
	/**
	 * 按服务类型获取所有输出服务实例
	 * @return {@link List},服务实例集合
	 */
	<S> List<S> getServices(Class<S> serviceType);
	
	/**
	 * 根据服务引用获取服务实例
	 * @param references 服务引用集合
	 * @return {@link List}服务实例集合
	 * @throws ServiceNotFoundException 服务未找到则抛出例外
	 */
	List<Object> getServices(Collection<ExportServiceReference<?>> references) throws ServiceNotFoundException;
	
	/**
	 * 根据服务引用获取同类型的服务实例
	 * @param references 服务引用集合
	 * @return {@link List}服务实例集合
	 * @throws ServiceNotFoundException 服务未找到则抛出例外
	 */
	<S> List<S> getSameServices(Collection<ExportServiceReference<S>> references) throws ServiceNotFoundException;
	
	/**
	 * 使用筛选字符串创建筛选器<br>
	 * 筛选字符串用于筛选关心的服务
	 * @param filterString 筛选字符串
	 * @return {@link Filter}
	 */
	Filter createFilter(String filterString);
	
	/**
	 * 移除服务事件监听器
	 * @param listener
	 */
	void removeListener(ExportServiceListener listener);
}
