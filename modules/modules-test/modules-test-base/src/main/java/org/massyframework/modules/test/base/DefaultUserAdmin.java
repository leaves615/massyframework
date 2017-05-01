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
package org.massyframework.modules.test.base;

import javax.annotation.Resource;

import org.massyframework.assembly.util.Asserts;
import org.massyframework.modules.test.NamedExistsException;
import org.massyframework.modules.test.User;
import org.massyframework.modules.test.UserAdmin;
import org.massyframework.modules.test.base.persistence.UserDAO;

/**
 * 用户管理服务
 */
public class DefaultUserAdmin implements UserAdmin {
	
	private UserDAO userDAO;

	/**
	 * 
	 */
	public DefaultUserAdmin() {
		
	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.test.UserAdmin#createUser(java.lang.String, java.lang.String)
	 */
	@Override
	public User createUser(String name, String nickname) throws NamedExistsException {
		Asserts.notNull(name, "name cannot be null.");
		Asserts.notNull(this.userDAO, "userDAO cannot be null, please set it.");
		
		if (this.findUser(name)!= null){
			throw new NamedExistsException(name);
		};
		return new DefaultUser(name, nickname);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.test.UserAdmin#findUser(java.lang.String)
	 */
	@Override
	public User findUser(String name) {
		Asserts.notNull(this.userDAO, "userDAO cannot be null, please set it.");
		if (name == null) return null;
		
		return this.userDAO.findUser(name);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.test.UserAdmin#save(org.massyframework.modules.test.User, java.lang.String)
	 */
	@Override
	public void save(User newUser, String password) {
		Asserts.notNull(newUser, "newUser cannot be null.");
		Asserts.notNull(password, "password cannot be null.");
		
		Asserts.notNull(this.userDAO, "userDAO cannot be null, please set it.");
		
		if (!(newUser instanceof DefaultUser)){
			throw new IllegalStateException("canot support user: type=" + newUser.getClass() + ".");
		}
		
		this.userDAO.addUser((DefaultUser)newUser, password);

	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.test.UserAdmin#update(org.massyframework.modules.test.User)
	 */
	@Override
	public void update(User user) {
		Asserts.notNull(this.userDAO, "userDAO cannot be null, please set it.");
		
		if (!(user instanceof DefaultUser)){
			throw new IllegalStateException("canot support user: type=" + user.getClass() + ".");
		}

		this.userDAO.updateUser((DefaultUser)user);
	}

	/**
	 * @return the userDAO
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * @param userDAO the userDAO to set
	 */
	@Resource
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	

}
