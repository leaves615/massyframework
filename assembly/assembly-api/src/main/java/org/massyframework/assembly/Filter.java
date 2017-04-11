/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import java.util.Map;

/**
 * RFC 1960-based 筛选器<br>
 * Filter对象实例可以采用{@link ExportServiceRepository#createFilter(String)}方法创建。<br>
 * 筛选器可以用于通过对服务属性的筛选来选择服务。<br>
 * 一些LDAP筛选例子如下：
 * <pre>
 *  "(cn=Babs Jensen)"
 *  "(!(cn=Tim Howes))"
 *  "(&(" + Constants.OBJECT_CLASS + "=Person)(|(sn=Jensen)(cn=Babs J*)))"
 *  "(o=univ*of*mich*)"
 * </pre>
 */
public interface Filter {

	/**
	 * 对map进行过滤，对map的key和value进行不区分大小写匹配。
	 * @param props {@link Map}属性
	 * @return {@code true}匹配，{@code false}不匹配
	 */
	boolean match(Map<String, Object> props);
	
	/**
	 * 对服务引用进行过滤，对reference的属性名和属性值进行不区分大小写进行匹配
	 * @param reference {@link ExportServiceReference}
	 * @return {@code true}匹配，{@code false}不匹配
	 */
	boolean match(ExportServiceReference<?> reference);
}