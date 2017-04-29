/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*   
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月15日
*/
package org.massyframework.modules.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.massyframework.assembly.util.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Sun Jdk 模块配置文件输出器
 * 
 * @author huangkaihui
 *
 */
public class SunJdkModuleExporter {

	/**
	 * 
	 */
	public SunJdkModuleExporter() {
	}

	public Document genericDocument(String rtJar) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 允许名字空间
		factory.setNamespaceAware(true);
		// 允许验证
		factory.setValidating(true);

		Document result = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			result = builder.newDocument();
			//设置XML的版本
			 result.setXmlVersion("1.0");

			Element root = result.createElement("module");
			root.setAttribute("xmlns", "urn:jboss:module:1.5");
			root.setAttribute("name", "sun.jdk");
			result.appendChild(root);

			Element dependencies = result.createElement("dependencies");
			root.appendChild(dependencies);
			Element system = result.createElement("system");
			system.setAttribute("export", "true");
			dependencies.appendChild(system);

			Element paths = result.createElement("paths");
			system.appendChild(paths);

			List<String> packageNames = this.getSunJdkPackageNames(rtJar);
			for (String packageName : packageNames) {
				Element path = result.createElement("path");
				path.setAttribute("name", packageName);
				paths.appendChild(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取SunJdk的所有包名
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	protected List<String> getSunJdkPackageNames(String rtJar) throws IOException {
		Set<String> packageNames = new HashSet<String>();
		JarFile file = null;
		try {
			file = new JarFile(rtJar);
			Enumeration<JarEntry> em = file.entries();
			while (em.hasMoreElements()) {
				JarEntry entry = em.nextElement();
				if (!entry.isDirectory()) {
					String name = entry.getName();
					if (name.endsWith(".class")) {
						int index = StringUtils.lastIndexOf(name, "/");
						packageNames.add(StringUtils.substring(name, 0, index));
					}
				}
			}
		} finally {
			IOUtils.closeStream(file);
		}
		List<String> result = new ArrayList<String>(packageNames);
		Collections.sort(result);
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SunJdkModuleExporter exporter = new SunJdkModuleExporter();

		try {
			String rtJar = "/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar";
			Document document = exporter.genericDocument(rtJar);

			// 开始把Document映射到文件
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transFormer = transFactory.newTransformer();
			transFormer.setOutputProperty(OutputKeys.INDENT, "yes"); 

			// 设置输出结果
			DOMSource domSource = new DOMSource(document);

			// 生成xml文件
			File file = new File("module.xml");

			// 判断是否存在,如果不存在,则创建
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			// 文件输出流
			FileOutputStream out = new FileOutputStream(file);

			// 设置输入源
			StreamResult xmlResult = new StreamResult(out);

			// 输出xml文件
			transFormer.transform(domSource, xmlResult);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
