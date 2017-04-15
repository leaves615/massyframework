/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly;

/**
 * 输出服务仓储引用
 */
public interface ExportServiceRepositoryReference extends ObjectReference<ExportServiceRepository> {

	/**
	 * 从装配件中适配{@link ExportServiceRepository}
	 * @param assembly 装配件
	 * @return {@link ExportServiceRepository},可能返回null.
	 */
	static ExportServiceRepository adaptFrom(Assembly assembly){
		if (assembly == null){
			return null;
		}
		
		ExportServiceRepositoryReference reference =
				assembly.adapt(ExportServiceRepositoryReference.class);
		return reference != null?
				reference.getReference():
					null;
	}
}
