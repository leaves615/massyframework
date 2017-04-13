/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

/**
 * 类加载器引用
 */
public interface ClassLoaderReference extends ObjectReference<ClassLoader> {

	/**
	 * 从装配件适配出类加载器对象
	 * @param assembly 装配件
	 * @return {@link ClassLoader},可能返回null.
	 */
	static ClassLoader adaptFrom(Assembly assembly){
		if (assembly == null){
			return null;
		}
		
		ClassLoaderReference reference =
				assembly.adapt(ClassLoaderReference.class);
		return reference != null ?
					reference.getReference():
						null;
	}
}
