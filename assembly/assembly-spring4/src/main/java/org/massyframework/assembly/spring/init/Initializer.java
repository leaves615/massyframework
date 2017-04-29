/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月14日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.spring.init;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.base.handle.AssemblyContextHandler;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.IntrospectorCleanupListener;

/**
 * 初始化方法
 */
public class Initializer implements FrameworkInitializer {

	/**
	 * 是否自动添加中文编码过滤器
	 */
	public static final String AUTO_ADD_ENCODING_FILTEER = "spring.encodingFilter.autoAdd";
	
	/**
	 * 
	 */
	public Initializer() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializer#onStartup(org.massyframework.assembly.Framework)
	 */
	@Override
	public void onStartup(Framework framework) throws Exception {
		Map<String, Object> props = new HashMap<String, Object>();
		
		props.put(Constants.SERVICE_NAME, new String[]{"spring"});
		props.put(Constants.SERVICE_DESCRIPTION, "提供整合Spring的AssemblyContext.");
		
		framework.addExportService(AssemblyContextHandler.class, 
				new SpringAssemblyContextHanderFactory(), props);
		
		if (this.isJ2EE(framework)){
			String text = framework.getInitParameter(AUTO_ADD_ENCODING_FILTEER);
			if ((text == null) || ("true".equalsIgnoreCase(text))){
				ExportServiceRepository serviceRepository =
					ExportServiceRepositoryReference.adaptFrom(framework);
				this.prepareWebEnvironment(serviceRepository);
			}
		}
	}
	
	/**
	 * 准备WEB运行环境
	 * @param framework {@link Framework}
	 */
	protected void prepareWebEnvironment(ExportServiceRepository serviceRepository){
		ServletContext servletContext = 
				serviceRepository.findService(ServletContext.class);
		
		//防止发生java.beans.Introspector内存泄露,
		//应将它配置在ContextLoaderListener的前面
		servletContext.addListener(IntrospectorCleanupListener.class);
		
		//防止中文乱码
		FilterRegistration.Dynamic registration =
				servletContext.addFilter("SpringEncodingFilter", CharacterEncodingFilter.class);
		registration.setAsyncSupported(true);
		registration.addMappingForUrlPatterns(
				EnumSet.allOf(DispatcherType.class), false, "/*");
		registration.setInitParameter("encoding", "UTF-8");
		registration.setInitParameter("forceEncoding", "true");
	}
		
	/**
	 * 判断是否运行在J2EE环境下
	 * @param framework 运行框架
	 * @return <code>true</code>是, <code>false</code>否
	 */
	protected boolean isJ2EE(Framework framework){
		String text = framework.getInitParameter(Constants.ENVIRONMENT);
		return Constants.ENVIRONMENT_J2EE.equals(text);
	}
}
