/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.tester;

import java.util.ArrayList;
import java.util.List;

import org.massyframework.assembly.FrameworkInitializer;

/**
 * 独立模式下的运行框架初始化处理器
 */
public class StandaloneFrameworkInitializeLoader extends AbstractFrameworkInitializeLoader {

	/**
	 * @param initializers
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

}
