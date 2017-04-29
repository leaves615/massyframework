/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

/**
 * 常量
 */
public interface Constants {
	
	/**
	 * 装配件符号名称，其值为字符串
	 */
	static final String ASSEMBLY_SYMBOLICNAME = "assembly.symbolicName";
	
	/**
	 * 装配件上下文的类型
	 */
	static final String ASSEMBLYCONTEXT_CLASSNAME = "assemblyContext.className";
	
	/**
	 * 环境属性，取值仅为{@link #ENVIRONMENT_J2SE}和{@link #ENVIRONMENT_J2EE}
	 */
	static final String ENVIRONMENT = "environment";
	
	/**
	 * J2SE运行环境
	 */
	static final String ENVIRONMENT_J2SE = "j2se";
	
	/**
	 * J2EE运行环境
	 */
	static final String ENVIRONMENT_J2EE = "j2ee";
	
	/**
	 * Filter是否支持异步模式
	 */
	static final String FILTER_ASYNCSUPPORTED = "filter.asyncSupported";
	
	/**
	 * 过滤器类型,其值为字符串
	 */
	static final String FILTER_CLASSNAME = "filter.className";
	
	/**
	 * 过滤器的调度模型,其值为字符串
	 */
	static final String FILTER_DISPATCHERTYPE = "filter.dispatcherType";
	
	/**
	 * Filter初始化参数前缀，以"filter.init."开头，放置在装配件的初始化参数中。
	 */
	static final String FILTER_INIT_PREFIX = "filter.init.";
	
	/**
	 * 过滤器名称,其值为字符串
	 */
	static final String FILTER_NAME = "filter.name";
		
	/**
	 * filter过滤的匹配路径,其值为字符串，用","分隔多个urlPattern
	 */
	static final String FILTER_URLPATTERNS = "filter.urlPatterns";
		
	/**
	 * ServletContextListener类型
	 */
	static final String LISTENER_CLASSNAME = "servletContextListener.className";
	
	/**
	 * SerlvetContextListener的初始化参数前缀,以"servletContextListener.init."开头，放置在装配件的初始化参数中。
	 */
	static final String LISTENER_INIT_PREFIX = "servletContextListener.init.";
	
	/**
	 * 服务类型，其值为Class数组<br>
	 * 服务注册的必须项目
	 */
	static final String OBJECT_CLASS = "service.objectClass";
	
	/**
	 * 服务在装配件上下文中的名称， 其值为字符串<br>
	 * 该名称在装配件上下文中必须唯一，不能重复
	 */
	static final String SERVICE_CNAME = "service.cName";
	
	/**
	 * 服务说明，其值为字符串
	 */
	static final String SERVICE_DESCRIPTION = "service.description";
	
	/**
	 * 服务编号，由系统统一生成，值为long
	 */
	static final String SERVICE_ID = "service.id";
	
	/**
	 * 服务输出的名称，允许输出多个名称，值为字符串数组<br>
	 */
	static final String SERVICE_NAME = "service.name";
	
	/**
	 * 服务排名,其值为int。<br>
	 * 默认该值为0
	 */
	static final String SERVICE_RANKING = "service.rank";
	
	/**
	 * Servlet是否支持异步模式
	 */
	static final String SERVLET_ASYNCSUPPORTED = "servlet.asyncSupported";
	
	/**
	 * Servlet的类型
	 */
	static final String SERVLET_CLASSNAME = "servlet.className";
	
	/**
	 * Servlet初始化参数前缀，以"servlet.init."开头，放置在装配件的初始化参数中。
	 */
	static final String SERVLET_INIT_PREFIX = "servlet.init.";
	
	/**
	 * Servlet加载和启动次序，其值为int
	 */
	static final String SERVLET_LOADONSTARTUP = "servlet.loadOnStartup";
	
	/**
	 * Servlet名称
	 */
	static final String SERVLET_NAME = "servlet.name";
		
	/**
	 * Servlet的路径匹配
	 */
	static final String SERVLET_URLPATTERNS = "servlet.urlPatterns";
	
		
}
