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
* @日   期:  2017年4月30日
*/
package org.massyframework.modules.test.webui;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.massyframework.assembly.util.Asserts;
import org.massyframework.modules.test.User;
import org.massyframework.modules.test.UserAdmin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author huangkaihui
 *
 */
@Controller
@RequestMapping
public class UserFormController {
	
	private UserAdmin userAdmin;

	/**
	 * 
	 */
	public UserFormController() {
		
	}
	
	@RequestMapping(value="/welcome.html",method = RequestMethod.GET)
	public ModelAndView getDefaultPage(HttpServletRequest request){
		Asserts.notNull(this.userAdmin, "userAdmin cannot be null, please set it.");
		ModelAndView result = new ModelAndView();
		result.setViewName("hello");
		
		User user = this.userAdmin.findUser("test");
		result.addObject("nickname", user.getFriendlyName());
		
		return result;
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
