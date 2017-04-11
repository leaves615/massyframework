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
	
}
