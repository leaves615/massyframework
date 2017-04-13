/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.util;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Properties;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.massyframework.assembly.Framework;
import org.xml.sax.SAXException;

/**
 * SchemaValidator 验证器
 */
public abstract class SchemaValidatorUtils {
	
	private static SoftReference<Validator> REFERENCE;
	
	/**
	 * 获取配置文件的验证器
	 * @return {@link Validator}
	 * @throws SAXException
	 */
	public static synchronized Validator getValidator() throws SAXException{
		Validator result = REFERENCE == null ? null : REFERENCE.get();
		if (result == null){
				
			URL url =  loadXmlSchema();
			SchemaFactory schemaFactory =   
			    SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");  

			Schema schema = schemaFactory.newSchema(url);
			result = schema.newValidator();
			
			REFERENCE = new SoftReference<Validator>(result);
		}
		
		return result;
	}
	
	/**
	 * 加载所有的Schema配置
	 * @return {@link Properties}
	 * @throws IOException
	 */
	private static URL loadXmlSchema(){
		ClassLoader loader = Framework.class.getClassLoader();
		return loader.getResource("org/massyframework/assembly/xml/assembly-1.0.xsd");
	}
}
