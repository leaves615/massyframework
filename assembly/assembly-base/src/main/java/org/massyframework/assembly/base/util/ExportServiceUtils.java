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
* @日   期:  2017年4月14日
*/
package org.massyframework.assembly.base.util;

import java.util.Map;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceReference;

/**
 * @author huangkaihui
 *
 */
public abstract class ExportServiceUtils {
	
	private static String[] EMPTY = new String[0];
	
	/**
	 * 从输出服务引用中取出服务注册类型
	 * @param reference 输出服务引用
	 * @return {@link Class}数组
	 */
	public static Class<?>[] getObjectClass(ExportServiceReference<?> reference){
		if (reference == null){
			return new Class<?>[0];
		}
		
		Object value = reference.getProperty(Constants.OBJECT_CLASS);
		if (value == null){
			return new Class<?>[0];
		}
		
		return (Class<?>[])value;
	}
	
	/**
	 * 从Map中取出服务注册的类型
	 * @param props 服务属性
	 * @return {@link Class}数组
	 */
	public static Class<?>[] getObjectClass(Map<String, Object> props){
		if (props == null){
			return new Class<?>[0];
		}
		
		Object value = props.get(Constants.OBJECT_CLASS);
		if (value == null){
			return new Class<?>[0];
		}
		
		return (Class<?>[])value;
	}

	/**
	 * 从输出服务引用中取出服务名称
	 * @param reference 输出服务引用对象
	 * @return {@link String}数组
	 */
	public static String[] getServiceName(ExportServiceReference<?> reference){
		if (reference == null){
			return EMPTY;
		}
		
		Object value = reference.getProperty(Constants.SERVICE_NAME);
		if (value == null){
			return EMPTY;
		}
		
		if (value instanceof String){
			return new String[]{(String)value};
		}else{
			return (String[])value;
		}
	}
	
	/**
	 * 从Map中取出服务名称
	 * @param props 服务属性Map
	 * @return {@link String}数组
	 */
	public static String[] getServiceName(Map<String, Object> props){
		if (props == null){
			return EMPTY;
		}
		
		Object value = props.get(Constants.SERVICE_NAME);
		if (value == null){
			return EMPTY;
		}
		
		if (value instanceof String){
			return new String[]{(String)value};
		}else{
			return (String[])value;
		}
	}
}
