/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

/**
 * 提供缺省的FrameworkFactory
 */
public final class DefaultFrameworkFactory extends AbstractFrameworkFactory {

	/**
	 * 
	 */
	public DefaultFrameworkFactory() {

	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.runtime.AbstractFrameworkFactory#doCreateFramework()
	 */
	@Override
	protected AbstractFramework doCreateFramework() {
		DefaultFramework result = new DefaultFramework();
		
		FrameworkInitParams initParams = new FrameworkInitParams();
		result.getHandlerRegistry().register(initParams);
				
		FrameworkContextManagement management =
				new FrameworkContextManagement();
		result.getHandlerRegistry().register(management);
		
		return result;
	}

	
}
