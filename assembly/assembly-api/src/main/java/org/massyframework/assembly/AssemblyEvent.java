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
 * 装配件事件，包括安装完成，激活和钝化事件。
 */
public final class AssemblyEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3860483171588500675L;
	
	public static final int INSTALLED = 0;
	public static final int ACTIVATED = 1;
	public static final int INACTIVATING = 2;

	private final int type;
	/**
	 * @param source
	 */
	public AssemblyEvent(Assembly assembly, int type) {
		super(assembly);
		if (type <0 || type>2){
			throw new IllegalStateException("Invalid assesmbly event type: type=" + type + ".");
		}
		this.type = type;
	}
	
	/**
	 * 装配件
	 * @return {@link Assembly}
	 */
	public Assembly getAssmebly(){
		return (Assembly)this.source;
	}
	
	/**
	 * 装配件事件类型：
	 * <ul>
	 * <li>{@link #INSTALLED}安装完成</li>
	 * <li>{@link #ACTIVATED}已激活，进入工作状态</li>
	 * <li>{@link #INACTIVATING}正在准备退出工作状态</li>
	 * </ul>
	 * @return {@link int}
	 */
	public int getType(){
		return this.type;
	}

}
