/**
 * 
 */
package org.massyframework.assembly.servlet.jasper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.descriptor.XmlErrorHandler;
import org.apache.tomcat.util.descriptor.tld.TaglibXml;
import org.apache.tomcat.util.descriptor.tld.TldParser;
import org.apache.tomcat.util.descriptor.tld.TldResourcePath;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSet;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 扩展TldParser类，避免发生Xml解析时找不到Jboss Module中的类
 * @author huangkaihui
 *
 */
final class TldParserEx extends TldParser {

	private static final Log log = LogFactory.getLog(TldParserEx.class);
	/**
	 * @param namespaceAware
	 * @param validation
	 * @param blockExternal
	 */
	public TldParserEx(boolean namespaceAware, boolean validation, boolean blockExternal) {
		super(namespaceAware, validation, blockExternal);
	}

	/**
	 * @param namespaceAware
	 * @param validation
	 * @param ruleSet
	 * @param blockExternal
	 */
	public TldParserEx(boolean namespaceAware, boolean validation, RuleSet ruleSet, boolean blockExternal) {
		super(namespaceAware, validation, ruleSet, blockExternal);
	}

	@Override
	public TaglibXml parse(TldResourcePath path) throws IOException, SAXException {
		Digester digester = this.getDigester();
		if (digester != null){
			try (InputStream is = path.openStream()) {
	            XmlErrorHandler handler = new XmlErrorHandler();
	            digester.setErrorHandler(handler);

	            TaglibXml taglibXml = new TaglibXml();
	            digester.push(taglibXml);

	            InputSource source = new InputSource(path.toExternalForm());
	            source.setByteStream(is);
	            digester.parse(source);
	            if (!handler.getWarnings().isEmpty() || !handler.getErrors().isEmpty()) {
	                handler.logFindings(log, source.getSystemId());
	                if (!handler.getErrors().isEmpty()) {
	                    // throw the first to indicate there was a error during processing
	                    throw handler.getErrors().iterator().next();
	                }
	            }
	            return taglibXml;
	        } finally {
	            digester.reset();
	        }
		}
		return new TaglibXml();
	}

	private Digester getDigester(){
		try{
			Field field = TldParser.class.getDeclaredField("digester");
			if (field != null){
				field.setAccessible(true);
				return (Digester)field.get(this);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
