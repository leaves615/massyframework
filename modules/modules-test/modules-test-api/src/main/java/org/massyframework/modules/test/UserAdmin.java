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
package org.massyframework.modules.test;

/**
 * 用户管理服务
 */
public interface UserAdmin {

	/**
	 * 创建用户
	 * @param name 用户名
	 * @param nickname 昵称
	 * @return {@link User}
	 * @throws NamedExistsException 用户名已经存在时抛出例外
	 */
	User createUser(String name, String nickname) throws NamedExistsException;
	
	/**
	 * 查找用户
	 * @param name 用户名
	 * @return {@link User}
	 */
	User findUser(String name);
	
	/**
	 * 保存新用户
	 * @param newUser 新用户
	 * @param password 密码
	 */
	void save(User newUser, String password);
	
	/**
	 * 更新用户
	 * @param user 用户
	 */
	void update(User user);
}
