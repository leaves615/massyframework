# Massy Framework 

## 概述
Massy Framwork提供模块化开发的开发视图，它重点关注软件开发期质量，为软件模块的组织提供有利于可扩展性、可重用性、易测试性等特性。<br>
随着软件规模的扩大，如果您的软件存在以下迫在眉睫需要解决的问题，可以考虑使用Massy Framework作为软件研发管理的辅助工具:
- 测试成本高<br>
回归测试花费代价越来越大，软件体系没有支持模块化设计，导致测试面广，延长交付工期。
- 代码臃肿，维护成本高<br>
赶工图方便，程序代码跳过必要的业务逻辑检查环节，直接使用底层代码，甚至直接写数据库表，故障排查难。
- 需要立刻进行重建，但时间成本代价太大<br>
系统运行多年，在扩展性、效率上越来越底下，想重建，但多年积累不是短时间就能重建完成，另外，源源不断的新需求也会导致新建系统难以追上旧系统的速度，停止新功能的时间成本难以消化和承受。

Massy Framework采用以下方式和策略，缓解您面对以上问题的困扰：
- 模块化<br>
使用装配件(Assembly)提供一个边界，在这个边界内为程序代码提供运行所需的环境。这里环境是指，依据装配件的配置定义，由框架配给装配件自身无法创造，只能依赖外部的服务资源；当装配件所需的服务资源都得到满足后，就可以从等待进入工作状态，并向框架反哺输出本地服务资源，由框架将这些服务资源提供给需要它的其他装配件，从而建立起一个相互支撑的模块链。<br>
当装配件通过配置约定了所需依赖和可输出资源后，装配件就成为一个具备自我描述的模块。模块内的程序代码并不用担心它所需的运行环境，因为所需的服务在程序代码准备运行前就已经就绪到位了。
- 隔离性<br>
每个模块，要求明确的定义API和SPI，API是提供给其他模块使用的接口，SPI是为模块提供多样化，扩展能力的接口。两者面向的对象和适用场景不同。<br>
API定义放在独立项目工程中，生成独立的jar文件，而SPI和具体的模块功能实现代码存放在其他项目工程中。Massy Framework建议使用自定义的类加载器加载SPI和模块功能实现代码，这样能增加其他模块直接访问本模块底层代码的难度，从而有利于程序员将重心放在API的设计上，而不是图方便的乱搭桥。<br>
另外，为了解决Jar Hell(泛指Jar的多版本共存所产生的问题)，Massy Framework引入了JBoss Modules。由JBoss Modules来约定Jar包之间的依赖关系，允许多版本Jar共存，从而缓解Jar Hell造成的问题。
- 测试成本<br>
模块如果没有被修改，基本上是无需进行回归测试，测试人员只需要关注新功能变动的模块即可。
- 分而治理<br>
模块的隔离性，为您提供新旧系统共存提供了解决重建的一种思路，您可以在旧系统中加入Massy Framework框架，将旧系统的所有程序逻辑视为一个庞大的模块。<br>
对于新功能，可以按新模块进行建设，让旧系统定义出新的API为新模块服务，以满足新功能能使用旧系统服务的能力；
同时，可以组织对旧系统按功能进行拆分，先拆分为大模块，再从大模块拆分为中型模块、小模块。您完全有足够的时间，慢慢的将旧系统改造成您想要的模样。<br>
这样您就能赢得新功能按时上线，而旧系统逐步被重建所需要时间成本。

当然，解决以上问题，并不简单的认为使用Massy Framework即可。它需要软件部门规范软件的组织模型，增加对代码审查，以符合预期的规范，只有这样，才能保证Massy Framework能起到作用，并有利于问题向好的方向转换。

## 快速入门
Massy Framework使用JDK8编译，运行在J2EE6(Servlet3)标准之上，MassyFramework不具备类似OSGI的热拔插能力，从而降低代码编写的复制性。
装配件是Massy Framework的核心，框架运行时会默认加载存放在META-INF/assembly目录下的assembly.xml文件，该文件对应一个符号名称唯一的装配件;如果您的配置文件不是这个名子，表明可能需要根据运行框架的初始化配置参数来决定加载那个特定的装配件。无论如何，最终都需要手工安装所需的装配件。
以下使用独立运行模式，和WEB模式来分析Massy Framework的启动过程
### 独立运行模式
打开assembly-tester项目中的org.massyframework.assembly.tester.AllTests.java,可以观察Massy Framework的启动方法：
```
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

```
在org.massyframework.assembly.tester.FrameworkTest.java中，展现了如何使用手工方式安装一个装配件。
```
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
		
		assertTrue("装配件未进入工作状态", assembly.isWorking());
		ExecutorService executor = 
				assembly.getAssmeblyContext().getService("executor", ExecutorService.class);
		assertNotNull("未注入ExecutorService服务", executor);
		
		ExportServiceRepository serviceRepository =
				assembly.getAssmeblyContext().getService(ExportServiceRepository.class);
		assertNotNull("未注入ExportServiceRepository服务", serviceRepository);
	}
```
而test/customizeAssembly.xml是在存放在项目的src/test/reosurces/test/customizeAssembly.xml文件，它约定了装配件的符号名称，依赖服务和输出服务这些装配件运行信息.
```
<?xml version="1.0" encoding="UTF-8"?>
<assembly xmlns="http://www.massyframework.org/schema/assembly"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.massyframework.org/schema/assembly
		http://www.massyframework.org/schema/assembly/assembly.xsd ">
		
	<symbolicName>test.customize.assemblyContext</symbolicName>
	<name>测试-自定义装配件上下文</name>
	<description>为系统提供装配件所需的各种基础设施，包括初始化参数、服务、依赖管理等</description>
	<vendor>smarabbit</vendor>
	<assemblyContext>customize</assemblyContext>
	
	<init-params>
		<parameter key="assemblyContext.className"  
			value="org.massyframework.assembly.tester.support.MyAssemblyContext" />
	</init-params>
	
	<!-- 依赖服务 -->
	<dependency-service cName="executor">
		<class>java.util.concurrent.ExecutorService</class>
	</dependency-service>
	
	<!-- 输出服务 -->
	<export-service cName="speak">
		<class>org.massyframework.assembly.tester.support.Speak</class>
	</export-service>
</assembly>

```
其中<assemblyContext>customize</assemblyContext>指明AssemblyContext是自定义的容器，该容器的类名定义在装配件的初始化参数中：
```
<parameter key="assemblyContext.className"  
			value="org.massyframework.assembly.tester.support.MyAssemblyContext" />
```
test.customize.assemblyContext有1个依赖服务和一个输出服务。更具体的内容可以参考使用指南的说明。
```
    <!-- 依赖服务 -->
	<dependency-service cName="executor">
		<class>java.util.concurrent.ExecutorService</class>
	</dependency-service>
	
	<!-- 输出服务 -->
	<export-service cName="speak">
		<class>org.massyframework.assembly.tester.support.Speak</class>
	</export-service>
```
程序代码可以在装配件激活后，使用executor和speck通过装配件上下文获取到这两个服务。
### WEB模式
Web模式下的样例代码放在modules-webapp中，在这个样例工程里，展现了集成JBoss Modules的能力。如果您还不了解JBoss Modules，建议您先了解以下内容：
- 使用JBoss来模块化Java [http://www.hollischuang.com/archives/854](http://)
- JBoss Modules – Module name [http://www.hollischuang.com/archives/870](http://)
- JBoss 系列八十五： JBoss Modules 简单介绍 [https://my.oschina.net/iwuyang/blog/197231](http://)

modules-webapp项目中，pom.xml以及web.xml，给人一种空荡荡的感觉，但这仅仅是表相，在src/main/webapp/WEB-INF/massy/modules目录下，您可以看到一组模块定义和引用关系，这里就是jboss modules的根目录。
和独立运行模式不同，Web模式不用您亲自编写Massy Framework的启动代码，它被封装在org.massyframework.modules.Initializer.java类中;Servlet3标准中，实现了javax.servlet.ServletContainerInitializer接口的类，并采用ServiceLoader方式定义，会在Servlet容器创建时被自动构建，并被调用onStartup(...)方法.Massy Framework正是通过这种策略，被秘密的在后台启动。
而src/main/webapp/WEB-INF/massy/etc/conf.properties提供了Framework启动时可自定义的配置参数。



## 使用指南
## API帮助

