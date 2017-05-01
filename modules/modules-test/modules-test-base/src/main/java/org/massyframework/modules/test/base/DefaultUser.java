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

import org.massyframework.modules.test.User;

/**
 * 缺省的用户
 */
public class DefaultUser implements User {
	
	private String name;
	private String nickname;


	/**
	 * 构造方法
	 * @param name 用户名
	 * @param nickname 昵称
	 */
	public DefaultUser(String name, String nickname) {
		this.name = name;
		this.nickname =  nickname;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.test.User#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.modules.test.User#getFriendlyName()
	 */
	@Override
	public String getFriendlyName() {
		return this.nickname;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultUser other = (DefaultUser) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultUser [name=" + name + ", nickname=" + nickname + "]";
	}
}
