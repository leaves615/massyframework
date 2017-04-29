/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
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

import java.util.EventObject;

/**
 * 服务注册和注销事件
 */
public final class ServiceEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9020282981984430491L;

	public static final int REGISTED = 0;
	public static final int UNREGISTERING = 1;
	
	private final int type;
	/**
	 * @param source
	 */
	public ServiceEvent(ExportServiceReference<?> reference, int type) {
		super(reference);
		if (type != 0 && type != 1){
			throw new IllegalStateException("Invalid service event type: type=" + type + ".");
		}
		this.type = type;
	}
	
	/**
	 * 服务引用
	 * @return {@link ExportServiceReference}
	 */
	public ExportServiceReference<?> getServiceReference(){
		return (ExportServiceReference<?>)this.source;
	}
	
	/**
	 * 服务事件类型
	 * <ul>
	 * <li>{@link #REGISTED}服务注册完成</li>
	 * <li>{@link #UNREGISTERING}服务正在撤销注册</li>
	 * </ul>
	 * @return {@link int}
	 */
	public int getType(){
		return this.type;
	}

}
