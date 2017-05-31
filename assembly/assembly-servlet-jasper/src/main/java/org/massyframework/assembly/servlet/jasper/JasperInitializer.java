/**
 * 
 */
package org.massyframework.assembly.servlet.jasper;

import java.util.HashMap;
import java.util.Map;

import org.massyframework.assembly.Constants;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.FrameworkInitializer;
import org.massyframework.assembly.web.HttpResourceProcessor;

/**
 * Jasper初始化器
 */
public class JasperInitializer implements FrameworkInitializer {
	
	public JasperInitializer(){
		
	}

	@Override
	public void onStartup(Framework framework) throws Exception {
		this.initJasperResourceProcessor(framework);	
	}

	/**
	 * 初始化Jsp资源处理器
	 * @param framework
	 */
	protected void initJasperResourceProcessor(Framework framework){
		if (Constants.ENVIRONMENT_J2EE.equals(framework.getInitParameter(Constants.ENVIRONMENT))){
			JasparResourceProessor processor =
					new JasparResourceProessor();
			Map<String, Object> props = new HashMap<String, Object>();
			props.put(Constants.SERVICE_DESCRIPTION, "对动态类加载器中的jsp文件提供编译支持.");
			framework.addExportService(HttpResourceProcessor.class, processor, props);
		}
	}
}
