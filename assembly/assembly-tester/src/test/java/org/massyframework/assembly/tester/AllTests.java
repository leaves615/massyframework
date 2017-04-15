package org.massyframework.assembly.tester;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.massyframework.assembly.Framework;

@RunWith(Suite.class)
@SuiteClasses({
	FrameworkTest.class
})
public class AllTests {
	
	/**
	 * 初始化
	 * @throws Exception
	 */
	public static void init() throws Exception{
		if (!FrameworkFactoryBuilder.exists()){
			FrameworkFactoryBuilder.build(null);
		}
	}
	
	/**
	 * 获取运行框架
	 * @return {@link Framework}
	 */
	public static Framework getFramework(){
		return FrameworkFactoryBuilder.getDefault();
	}

}
