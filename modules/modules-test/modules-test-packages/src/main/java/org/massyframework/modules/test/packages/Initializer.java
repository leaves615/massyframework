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
package org.massyframework.modules.test.packages;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.DefaultAssemblyResource;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.util.EmbedJarUtils;

/**
 * 初始化器
 */
public class Initializer implements FrameworkInitializer {

	/**
	 * 
	 */
	public Initializer() {
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.FrameworkInitializer#onStartup(org.massyframework.assembly.Framework)
	 */
	@Override
	public void onStartup(Framework framework) throws Exception {
		URLClassLoader loader =
				this.createdEmbedClassLoader();		
		//找到装配件的配置文件，并手工安装装配件
		URL url =  this.getClass().getResource("/META-INF/assembly/modules-test-business.xml");
		if (url != null){
			DefaultAssemblyResource resource =
					new DefaultAssemblyResource(loader, url);
			framework.installAssembly(resource);
		}
				
		//只有在J2EE运行环境下，才允许注册基于Web的装配件
		if (Constants.ENVIRONMENT_J2EE.equals(framework.getInitParameter(Constants.ENVIRONMENT))){
			url = this.getClass().getResource("/META-INF/assembly/modules-test-webui.xml");
			if (url != null){
				DefaultAssemblyResource resource =
						new DefaultAssemblyResource(loader, url);
				framework.installAssembly(resource);
			}
			
			url = this.getClass().getResource("/META-INF/assembly/modules-test-struts2.xml");
			if (url != null){
				DefaultAssemblyResource resource =
						new DefaultAssemblyResource(loader, url);
				framework.installAssembly(resource);
			}
		}
	}

	/**
	 * 创建嵌入类加载器
	 * @return {@link URLClassLoader}
	 * @throws IOException 发生非预期异常所抛出的例外
	 * @throws URISyntaxException 
	 */
	protected URLClassLoader createdEmbedClassLoader() throws IOException, URISyntaxException{
		//以当前类为锚点，定位到META-INF/lib目录，同时将目录中的jar包复制到临时目录下
		URL[] jars = EmbedJarUtils.loadEmbedJars(Initializer.class);
        //创建自定义的类加载器，用于加载上面三个jar包
		return new URLClassLoader(
				jars,
				this.getClass().getClassLoader());
	}
}
