package org.massyframework.modules.test.packages;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.test.AwaitAssemblyWorking;
import org.massyframework.modules.test.User;
import org.massyframework.modules.test.UserAdmin;

public class UserAdminTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		AllTests.init();
	}

	@Test
	public void test() {
		Framework framework = AllTests.getFramework();
		assertNotNull("framework未能创建.", framework);
		
		String symbolicName="modules.test.business";
		AwaitAssemblyWorking await =
				new AwaitAssemblyWorking(framework, symbolicName);
		await.ensureWorking();
		
		
		Assembly assembly = framework.getAssembly(symbolicName);
		assertNotNull("assembly[" + symbolicName + "] 不能为null", assembly);
		
		if (assembly.isWorking()){
			UserAdmin userAdmin = assembly.getAssmeblyContext().getService("userAdmin", UserAdmin.class);
			assertNotNull("bean[userAdmin] cannot be null.", userAdmin);
			
			User user = userAdmin.findUser("test");
			assertNotNull("user[identifier=test] cannot be null.", user);
		}
	}

}
