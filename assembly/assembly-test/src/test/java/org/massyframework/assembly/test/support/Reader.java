/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月25日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.test.support;

import javax.annotation.Resource;

import org.massyframework.assembly.util.Asserts;

/**
 * @author huangkaihui
 *
 */
public class Reader {
	
	private Speak speak;

	/**
	 * 
	 */
	public Reader() {

	}
	
	public void read(String text){
		Asserts.notNull(speak, "speak cannot be null.please set it.");
		this.speak.say(text);
	}

	/**
	 * @return the speak
	 */
	public Speak getSpeak() {
		return speak;
	}

	/**
	 * @param speak the speak to set
	 */
	@Resource
	public void setSpeak(Speak speak) {
		this.speak = speak;
	}

}
