package org.massyframework.assembly.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.concurrent.ExecutorService;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.DefaultAssemblyResource;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceRepositoryReference;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.test.support.Reader;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FrameworkTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		AllTests.init();
	}

	/**
	 * 测试验证运行框架有效性
	 */
	@Test
	public void test01_ValidateFramework() {
		Framework framework = AllTests.getFramework();
		assertNotNull("framework未能创建.", framework);
		
		assertTrue("framework没有进入工作状态", framework.isWorking());
		
		assertNotNull("framework的编号不等于0", framework.getAssemblyId());
		assertNotNull("framework的符号名称不能为null.", framework.getSymbolicName());
		
		ExportServiceRepository serviceRepository =
				ExportServiceRepositoryReference.adaptFrom(framework);
		assertNotNull("ExportServiceRepository不能为null.", serviceRepository);
		
		
		ExecutorService executor = serviceRepository.findService(ExecutorService.class);
		assertNotNull("执行线程池不能为null.", executor);
	}

	/**
	 * 测试自定义的装配件上下文
	 */
	@Test
	public void test02_installCustomizeAssemblyContext() throws Exception{
		String path = "test/customizeAssembly.xml";
		URL url =
				this.getClass().getClassLoader().getResource(path);
		assertNotNull("装配件配置文件不存在: url=" + path + ".");
		DefaultAssemblyResource resource=
				new DefaultAssemblyResource(this.getClass().getClassLoader(), url);
		
		Framework framework = AllTests.getFramework();
		Assembly assembly = framework.installAssembly(resource);
		
		assertNotNull("安装返回装配件不能为null.", assembly);
		assertTrue("安装返回装配件的编号不能为0", assembly.getAssemblyId()!=0);
		
		String symbolicName = "test.customize.assemblyContext";
		assertTrue("装配件符号名称不等于[" + symbolicName + "]", symbolicName.equals(assembly.getSymbolicName()));
		
		String name = "测试-自定义装配件上下文";
		assertTrue("装配件名称不等于[" + name + "]", name.equals(assembly.getName()));
		
		String description="为系统提供装配件所需的各种基础设施，包括初始化参数、服务、依赖管理等";
		assertTrue("装配件说明不等于[" + description + "]", description.equals(assembly.getDescription()));
		
		AwaitAssemblyWorking await = 
				new AwaitAssemblyWorking(framework, symbolicName);
		await.ensureWorking();
		
		assertTrue("装配件未进入工作状态", assembly.isWorking());
		ExecutorService executor = 
				assembly.getAssmeblyContext().getService("executor", ExecutorService.class);
		assertNotNull("未注入ExecutorService服务", executor);
		
		ExportServiceRepository serviceRepository =
				assembly.getAssmeblyContext().getService(ExportServiceRepository.class);
		assertNotNull("未注入ExportServiceRepository服务", serviceRepository);
	}
	
	/**
	 * 测试独立的Spring装配件上下文
	 */
	@Test
	public void test03_installStandaloneSpringAssembly() throws Exception {
		String path = "test/springAssembly.xml";
		URL url =
				this.getClass().getClassLoader().getResource(path);
		assertNotNull("装配件配置文件不存在: url=" + path + ".");
		DefaultAssemblyResource resource=
				new DefaultAssemblyResource(this.getClass().getClassLoader(), url);
		
		Framework framework = AllTests.getFramework();
		Assembly assembly = framework.installAssembly(resource);
		
		String symbolicName = "test.spring.standalone";
		AwaitAssemblyWorking await = 
				new AwaitAssemblyWorking(framework, symbolicName);
		await.ensureWorking();
		

		
		assertNotNull("安装返回装配件不能为null.", assembly);
		assertTrue("安装返回装配件的编号不能为0", assembly.getAssemblyId()!=0);
		
		assertTrue("装配件符号名称不等于[" + symbolicName + "]", symbolicName.equals(assembly.getSymbolicName()));
		
		String name = "测试-Spring装配件上下文";
		assertTrue("装配件名称不等于[" + name + "]", name.equals(assembly.getName()));
		
		String description="为系统提供装配件所需的各种基础设施，包括初始化参数、服务、依赖管理等";
		assertTrue("装配件说明不等于[" + description + "]", description.equals(assembly.getDescription()));
		
		ExecutorService executor = 
				assembly.getAssmeblyContext().getService("executor", ExecutorService.class);
		assertNotNull("未注入ExecutorService服务", executor);
		
		Reader reader =
				assembly.getAssmeblyContext().getService("reader", Reader.class);
		reader.read("Hello Every Body.");
		
		ExportServiceRepository serviceRepository =
				assembly.getAssmeblyContext().getService(ExportServiceRepository.class);
		assertNotNull("未注入ExportServiceRepository服务", serviceRepository);
	}
	
}
