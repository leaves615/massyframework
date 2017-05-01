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
package org.massyframework.modules.test.base.persistence;

import org.massyframework.modules.test.base.DefaultUser;

/**
 * 用户数据存取对象接口, SPI接口定义
 */
public interface UserDAO {

	/**
	 * 添加用户
	 * @param newUser 新用户
	 * @param password 密码
	 */
	void addUser(DefaultUser newUser, String password) ;
	
	/**
	 * 查找用户
	 * @param name 用户名
	 * @return {@link DefaultUser},如果用户不存在，可以返回null.
	 */
	DefaultUser findUser(String name);
	
	/**
	 * 更新用户信息
	 * @param user 用户
	 */
	void updateUser(DefaultUser user);
}
