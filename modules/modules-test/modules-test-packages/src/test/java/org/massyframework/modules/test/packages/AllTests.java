package org.massyframework.modules.test.packages;

import java.util.Map;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.test.FrameworkFactoryBuilder;


@RunWith(Suite.class)
@SuiteClasses({ UserAdminTest.class })
public class AllTests {
	
	/**
	 * 初始化
	 * @throws Exception
	 */
	public static void init() throws Exception{
		if (!FrameworkFactoryBuilder.exists()){
			//使用一个null值作为配置参数，如果确实需要配置参数，可以修改本处代码
			Map<String, String> config = null;
			FrameworkFactoryBuilder.build(config);
		}
	}
	
	/**
	 * 获取运行框架
	 * @return {@link Framework}
	 */
	public static Framework getFramework(){
		//使用存储在FrameworkFactoryBuilder内置的Framework实例，该实例在调用init()方法后有效
		return FrameworkFactoryBuilder.getDefault();
	}

}
