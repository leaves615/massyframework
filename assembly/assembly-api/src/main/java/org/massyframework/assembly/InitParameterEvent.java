/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月13日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

import org.massyframework.assembly.util.Asserts;

/**
 * 初始化参数事件
 */
public final class InitParameterEvent extends FrameworkEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5163469246617567528L;

	private String key;
	private String value;
	
	/**
	 * 构造方法
	 * @param framework
	 */
	public InitParameterEvent(Framework framework, String key, String value) {
		super(framework);
		Asserts.notNull(key, "key cannot be null.");
		Asserts.notNull(value, "value cannot be null.");
		this.key = key;
		this.value = value;
	}

	/**
	 * 属性键
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 属性值
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
