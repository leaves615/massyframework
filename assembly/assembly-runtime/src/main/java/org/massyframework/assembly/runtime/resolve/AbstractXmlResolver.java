/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;

import org.massyframework.assembly.ClassLoaderReference;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.base.handle.ConfigFileHandler;
import org.massyframework.assembly.base.handle.DependencyServiceResource;
import org.massyframework.assembly.base.handle.ExportServiceResource;
import org.massyframework.assembly.base.handle.support.AbstractResolver;
import org.massyframework.assembly.base.handle.support.SimpleExportServiceResource;
import org.massyframework.assembly.util.CollectionUtils;
import org.massyframework.assembly.util.IOUtils;
import org.massyframework.assembly.util.SchemaValidatorUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 提供主要的xml文件解析方法
 */
abstract class AbstractXmlResolver extends AbstractResolver {
	
	public static final String ASSEMBLY           = "assembly";
	public static final String SYMBOLICNAME       = "symbolicName";
	public static final String NAME               = "name";
	public static final String DESCRIPTION        = "description";
	public static final String VENDOR             = "vendor";
	public static final String ASSEMBLYCONTEXT    = "assemblyContext";
	
	public static final String INIT_PARAMS        = "init-params";
	public static final String PARAMETER          = "parameter";
	public static final String PARAMETER_KEY      = "key";
	public static final String PARAMETER_VALUE    = "value";
	
	public static final String DEPENDENCY_SERVICE = "dependency-service";
	public static final String CNAME              = "cName";
	public static final String CLASS              = "class";
	public static final String FILTERSTRING       = "filterString";
	
	public static final String EXPORT_SERVICE     = "export-service";
	public static final String PROPERTIES         = "properties";
	public static final String PROPERTY           = "property";
	public static final String PROPERTY_NAME      = "name";
	public static final String PROPERTY_TYPE      = "type";
	public static final String PROPERTY_VALUE     = "value";
	
	private final Validator validator;
	
	/**
	 * 构造方法
	 * @param serviceRegistry 服务注册器
	 * @param validator xml格式验证器
	 * @throws SAXException 
	 */
	public AbstractXmlResolver() throws SAXException {
		this.validator = SchemaValidatorUtils.getValidator();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.framework.handle.ResolveHandler#doResolve()
	 */
	@Override
	public final void doResolve() throws IOException, Exception {
		URL url =  this.getXmlConfiguration();
		
		this.validatingXmlConfiguration(url);
			
		InputStream inputStream = null;
		try{
			inputStream = url.openStream();
			Document document = this.createDocument(inputStream);
			//解析
			String container = this.doParser(document);
			this.doParserInitParams(document);
			this.doParserDependencyService(document);
			this.doParserExportService(document);
			this.setAssemblyContextHandler(container);
		}finally{
			IOUtils.closeStream(inputStream);
		}
	}
	
	/*
	 * 解析装配件基本信息
	 */
	protected String doParser(Document document){
		NodeList nodeList = document.getElementsByTagName(ASSEMBLY).item(0).getChildNodes();
		
		int size = nodeList.getLength();
		
		String symbolicName = null;
		String name = null;
		String description = null;
		String vendor = null;
		String container = null;
		
		for (int i=0; i<size; i++){
			Node node = nodeList.item(i);
			switch (node.getNodeName()){
				case SYMBOLICNAME :{
					symbolicName = node.getTextContent();
					break;
				}
				case NAME : {
					name = node.getTextContent();
					break;
				}
				case DESCRIPTION : {
					description = node.getTextContent();
					break;
				}
				case VENDOR : {
					vendor = node.getTextContent();
					
					break;
				}
				case ASSEMBLYCONTEXT : {
					container = node.getTextContent();
					break;
				}
			}
		}
		
		this.setAssemblyInformation(symbolicName, name, description, vendor);
		return container;
	}
	
	/**
	 * 解析初始化参数
	 * @param document {@link Document}
	 */
	protected void doParserInitParams(Document document){
		Map<String, String> map = new HashMap<String, String>();
		NodeList list =document.getElementsByTagName(INIT_PARAMS);
		if (list != null){
			list = list.item(0).getChildNodes();
			int size = list.getLength();
			for (int i=0; i<size; i++){
				Node node = list.item(i);
				if (node.getNodeName().equals(PARAMETER)){
					Node key = node.getAttributes().getNamedItem(PARAMETER_KEY);
					Node value = node.getAttributes().getNamedItem(PARAMETER_VALUE);
					
					map.put(key.getTextContent(), 
							this.getTextContent(value));
				}
			}
		}
		
		this.setInitParams(map);
	}
	
	/**
	 * 解析依赖服务
	 * @param document {@link Document}
	 * @throws ClassNotFoundException 未找到类时抛出异常
	 */
	protected void doParserDependencyService(Document document) throws ClassNotFoundException{
		NodeList list = document.getElementsByTagName(DEPENDENCY_SERVICE);
		if (list != null){
			List<DependencyServiceResource> resources = 
					new ArrayList<DependencyServiceResource>();
			int size = list.getLength();
			for (int i=0; i<size; i++){
				Node node = list.item(i);
				String cName = node.getAttributes().getNamedItem(CNAME).getTextContent();
				String className = null;
				String filterString = null;
				NodeList childs = node.getChildNodes();
				int count = childs.getLength();
				for (int j=0; j<count; j++){
					Node child = childs.item(j);
					switch (child.getNodeName()){
						case CLASS:{
							className = this.getTextContent(child);
							break;
						}
						case FILTERSTRING: {
							filterString = this.getTextContent(child);
							break;
						}
					}
				}
				
				Class<?> clazz = this.loadClass(className);
				SimpleDependencyServiceResource resource =
						new SimpleDependencyServiceResource(cName, clazz, filterString, this.getAssembly());
				resources.add(resource);
			}
			
			if (!CollectionUtils.isEmpty(resources)){
				this.addDependencyServiceResources(resources);
			}
		}
	}
	
	/**
	 * 解析输出服务
	 * @param document {@link Document}
	 * @throws ClassNotFoundException 类型未找到时抛出异常
	 */
	protected void doParserExportService(Document document) throws ClassNotFoundException{
		NodeList list = document.getElementsByTagName(DEPENDENCY_SERVICE);
		if (list != null){
			List<ExportServiceResource> resources =
					new ArrayList<ExportServiceResource>();
			int size = list.getLength();
			for (int i=0; i<size; i++){
				Node node = list.item(i);
				String cName = node.getAttributes().getNamedItem(CNAME).getTextContent();
				List<String> classNames = new ArrayList<String>();
				Map<String, Object> props = null;
				
				NodeList childs = node.getChildNodes();
				int count = childs.getLength();
				for (int j=0; j<count; j++){
					Node child = childs.item(j);
					switch (child.getNodeName()){
						case CLASS :{
							classNames.add(this.getTextContent(child));
							break;
						}
						case PROPERTIES: {
							props = this.doParserProperties(node.getChildNodes());
							break;
						}
					}
				}
				
				Class<?>[] classes = new Class<?>[classNames.size()];
				for (int k=0; k<classNames.size(); k++){
					classes[k] = this.loadClass(classNames.get(k));
				}
				
				SimpleExportServiceResource resource =
						new SimpleExportServiceResource(cName, classes, props);
				resources.add(resource);
			}
			
			if (!CollectionUtils.isEmpty(resources)){
				this.addExportServiceResources(resources);
			}
		}
	}
	
	/**
	 * 解析属性
	 * @param list {@link List}
	 * @return {@link Map}
	 */
	protected Map<String, Object> doParserProperties(NodeList list){
		Map<String, Object> result = new HashMap<String, Object>();
		int size = list.getLength();
		for (int i=0; i<size; i++){
			Node node = list.item(i);
			if (node.getNodeName().equals(PROPERTY)){
				Node attr = node.getAttributes().getNamedItem(PROPERTY_NAME);
				String name = attr.getTextContent();
				attr = node.getAttributes().getNamedItem(PROPERTY_TYPE);
				String type = attr == null? "string": attr.getTextContent().toLowerCase();
				attr = node.getAttributes().getNamedItem(PROPERTY_VALUE);
				String value = this.getTextContent(attr);
				
				switch(type){
					case "string":{
						result.put(name, value);
						break;
					}
					case "int" : {
						result.put(name, Integer.parseInt(value));
						break;
					}
					case "boolean" : {
						result.put(name, Boolean.parseBoolean(value));
						break;
					}
					case "long" : {
						result.put(name, Long.parseLong(value));
						break;
					}
					case "float" : {
						result.put(name, Float.parseFloat(value));
						break;
					}
					case "double": {
						result.put(name, Double.parseDouble(value));
						break;
					}
					case "char": {
						result.put(name, value.toCharArray());
						break;
					}
					case "byte": {
						result.put(name, Byte.parseByte(value));
						break;
					}
				}
				
				//服务排名
				if (name.equals(Constants.SERVICE_RANKING)){
					result.put(name, Integer.parseInt(value));
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 使用className加载类
	 * @param className 类名
	 * @return {@link Class}
	 * @throws ClassNotFoundException
	 */
	protected Class<?> loadClass(String className) throws ClassNotFoundException{
		ClassLoader loader = ClassLoaderReference.adaptFrom(this.getAssembly());
		return loader.loadClass(className);
	}
	
	/**
	 * 验证Xml配置文件的格式
	 * @param url 资源定位
	 * @throws SAXException SAX验证不通过抛出异常
	 * @throws IOException 发生IO读写错误时抛出异常
	 */
	protected final void validatingXmlConfiguration(URL url) throws SAXException, IOException{
		InputStream inputStream = null;
		try{
			inputStream = url.openStream();
			StreamSource source = new StreamSource(inputStream);
			validator.validate(source);
		}finally{
			IOUtils.closeStream(inputStream);
		}
	}

	/**
	 * 创建Xml Document对象
	 * @param inputStream 输入流
	 * @return {@link Document}
	 * @throws ParserConfigurationException 解析发生错误抛出的异常
	 * @throws IOException IO读写发生异常抛出的异常
	 * @throws SAXException SAX解析发生错误抛出的异常
	 */
	protected final Document createDocument(InputStream inputStream) 
			throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=factory.newDocumentBuilder();
		return builder.parse(inputStream);
	}
	
	/**
	 * 获取节点的TextContent数据
	 * @param node 节点
	 * @return {@link String}, 可以返回null.
	 */
	protected String getTextContent(Node node){
		String result = node.getTextContent();
		if (result == null){
			return null;
		}
		if (result.isEmpty()){
			return null;
		}
		
		return result;
	}
	
	/**
	 * 获取配置文件
	 * @return {@link URL}
	 */
	protected URL getXmlConfiguration(){
		ConfigFileHandler handler = this.getHandler(ConfigFileHandler.class);
		return handler.getConfigFile();
	}
}
