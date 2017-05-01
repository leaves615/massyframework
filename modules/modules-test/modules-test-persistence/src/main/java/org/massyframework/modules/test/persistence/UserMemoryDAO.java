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
package org.massyframework.modules.test.persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.massyframework.modules.test.NamedExistsException;
import org.massyframework.modules.test.base.DefaultUser;
import org.massyframework.modules.test.base.persistence.UserDAO;

/**
 * 使用内存存储用户
 */
public class UserMemoryDAO implements UserDAO {
	
	private Map<String, DefaultUser> userMap =
			new ConcurrentHashMap<String, DefaultUser>();

	/**
	 * 
	 */
	public UserMemoryDAO() {
		DefaultUser user = new DefaultUser("test", "你猜你猜你猜猜猜.");
		this.userMap.put(user.getIdentifier(), user);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.test.base.persistence.UserDAO#addUser(org.massyframework.modules.test.base.DefaultUser, java.lang.String)
	 */
	@Override
	public void addUser(DefaultUser newUser, String password) {
		String name = newUser.getIdentifier();
		if (this.userMap.putIfAbsent(name, newUser) != null){
			throw new NamedExistsException(name);
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.test.base.persistence.UserDAO#findUser(java.lang.String)
	 */
	@Override
	public DefaultUser findUser(String name) {
		return this.userMap.get(name);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.test.base.persistence.UserDAO#updateUser(org.massyframework.modules.test.base.DefaultUser)
	 */
	@Override
	public void updateUser(DefaultUser user) {
		String name = user.getIdentifier();
		if (this.userMap.containsKey(name)){
			this.userMap.put(name, user);
		}else{
			throw new RuntimeException("cannot found User: name=" + name + ".");
		}

	}

}
