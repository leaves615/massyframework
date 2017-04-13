package org.massyframework.assembly.packages;

import org.junit.Test;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkFactory;
import org.massyframework.assembly.util.ServiceLoaderUtils;

public class FrameworkTester {

	@Test
	public void test() throws Exception{
		FrameworkFactory factory =
				ServiceLoaderUtils.loadFirstService(FrameworkFactory.class);
		
		Framework framework = factory.createFramework(null, 
				new StandaloneFrameworkInitializeHandler(null));
		
		System.out.println(framework.getAssemblyStatus());
	}

}
