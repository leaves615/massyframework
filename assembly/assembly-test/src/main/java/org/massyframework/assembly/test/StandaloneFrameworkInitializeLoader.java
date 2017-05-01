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
* @日   期:  2017年4月12日
*/
package org.massyframework.assembly.test;

import java.util.ArrayList;
import java.util.List;

import org.massyframework.assembly.FrameworkInitializer;

/**
 * 独立模式下的运行框架初始化处理器
 */
public class StandaloneFrameworkInitializeLoader extends AbstractFrameworkInitializeLoader {

	
	/**
	 * 构造方法
	 * @param initializers {@link List}
	 */
	public StandaloneFrameworkInitializeLoader(List<FrameworkInitializer> initializers) {
		super(initializers);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.init.AbstractFrameworkInitializeHandler#getClassLoaderes()
	 */
	@Override
	protected List<ClassLoader> getClassLoaderes() {
		List<ClassLoader> result =
				new ArrayList<ClassLoader>();
		result.add(Thread.currentThread().getContextClassLoader());
		return result;
	}

	/**
	 * 创建独立运行框架初始化加载器
	 * @param initializers 初始化器
	 * @return {@link StandaloneFrameworkInitializeLoader}
	 */
	public static StandaloneFrameworkInitializeLoader create(FrameworkInitializer... initializers){
		List<FrameworkInitializer> list = new ArrayList<FrameworkInitializer>();
		for (FrameworkInitializer initializer: initializers){
			list.add(initializer);
		}
		return new StandaloneFrameworkInitializeLoader(list);
	}
}
