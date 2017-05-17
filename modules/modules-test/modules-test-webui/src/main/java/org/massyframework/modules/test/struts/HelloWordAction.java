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
* @日   期:  2017年5月15日
*/
package org.massyframework.modules.test.struts;

import javax.annotation.Resource;

import org.massyframework.assembly.util.Asserts;
import org.massyframework.modules.test.User;
import org.massyframework.modules.test.UserAdmin;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author huangkaihui
 *
 */
public class HelloWordAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5626098902361383377L;
	
	private UserAdmin userAdmin;
	private String nickname;
	
	/**
	 * 
	 */
	public HelloWordAction() {
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {
		Asserts.notNull(this.userAdmin, "userAdmin cannot be null.");
		User user = this.userAdmin.findUser("test");
		this.nickname = user != null ?
				user.getFriendlyName() :
					"[Unknown]";
				
		return SUCCESS;
	}
	
	public String getNickname(){
		return this.nickname;
	}

	/**
	 * @return the userAdmin
	 */
	public UserAdmin getUserAdmin() {
		return userAdmin;
	}

	/**
	 * @param userAdmin the userAdmin to set
	 */
	@Resource
	public void setUserAdmin(UserAdmin userAdmin) {
		this.userAdmin = userAdmin;
	}
	
	

}
