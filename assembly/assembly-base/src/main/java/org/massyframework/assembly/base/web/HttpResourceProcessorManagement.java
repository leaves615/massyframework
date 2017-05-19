/**
* @Copyright: 2017 smarabbit studio. 
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
* @日   期:  2017年5月17日
*/
package org.massyframework.assembly.base.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.massyframework.assembly.ExportServiceReference;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.ExportServiceTracker;
import org.massyframework.assembly.util.Asserts;
import org.massyframework.assembly.web.HttpResource;
import org.massyframework.assembly.web.HttpResourceProcessor;
import org.massyframework.assembly.web.ServletContextAware;

/**
 * HttpResourceProcessor跟踪管理器
 * @author huangkaihui
 *
 */
class HttpResourceProcessorManagement 
	extends ExportServiceTracker<HttpResourceProcessor> 
	implements HttpResourceProcessor{
	
	private static final String EXTENSION_SEPARATOR = ".";
	private static final String METHOD_GET = "GET";
	
	private static final String LAST_MODIFIED = "Last-Modified"; 
	private static final String IF_MODIFIED_SINCE = "If-Modified-Since"; 
	private static final String IF_NONE_MATCH = "If-None-Match";
	private static final String ETAG = "ETag"; 
	
	private ServletContext servletContext;
	private Map<String, HttpResourceProcessor> processorMap =
			new ConcurrentHashMap<String, HttpResourceProcessor>();

	
	/**
	 * @param serviceRepository
	 * @param serviceType
	 * @param filterString
	 */
	public HttpResourceProcessorManagement(ExportServiceRepository serviceRepository, ServletContext servletContext) {
		super(serviceRepository, HttpResourceProcessor.class, null);
		Asserts.notNull(servletContext, "servletContext cannot be null.");
		this.servletContext = servletContext;
	}
	

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.web.HttpResourceProcessor#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.massyframework.assembly.web.HttpResource)
	 */
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp, HttpResource resource)
			throws ServletException, IOException {
		String path = req.getPathInfo();
		String extensionName = this.parseExtensionName(path);
		
		HttpResourceProcessor processor = null;
		if (extensionName != null){
			processor = this.processorMap.get(extensionName);
		}
		if (processor != null){
			processor.process(req, resp, resource);
		}else{
			if (req.getMethod().equals(METHOD_GET)){
				this.doGet(req, resp, resource);
			}else{
				this.sendError(resp, HttpServletResponse.SC_NOT_FOUND);
			}
		}
	}
	
	/**
	 * 默认的处理方式，仅支持Http Get方法,作为静态资源的文件加载
	 * @param request
	 * @param response
	 * @param resource
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, HttpResource resource) throws IOException{
		String path = req.getPathInfo();
		if (path != null && path.startsWith("/WEB-INF/")) { //$NON-NLS-1$
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		if (!path.startsWith("/")){
			path = "/" + path;
		}
				
		String resourceName = resource.getName() + path;
		URL url = resource.getAssemblyClassLoader().getResource(resourceName);
		if (url != null){
			this.writeResource(req, resp, path, url);
		}else{
			this.sendError(resp, HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	/**
	 * 加载静态资源并写入Http响应
	 * @param req http请求
	 * @param resp http响应
	 * @param resourcePath 资源路径
	 * @param resourceURL 资源URL
	 * @throws IOException 发生IO异常
	 */
	private void writeResource(final HttpServletRequest req, final HttpServletResponse resp, 
			final String pathInfo, final URL resourceURL) throws IOException {
			URLConnection connection = resourceURL.openConnection();
			long lastModified = connection.getLastModified();
			int contentLength = connection.getContentLength();

			String etag = null;
			if (lastModified != -1 && contentLength != -1)
				etag = "W/\"" + contentLength + "-" + lastModified + "\""; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

			// Check for cache revalidation.
			// We should prefer ETag validation as the guarantees are stronger and all HTTP 1.1 clients should be using it
			String ifNoneMatch = req.getHeader(IF_NONE_MATCH);
			if (ifNoneMatch != null && etag != null && ifNoneMatch.indexOf(etag) != -1) {
				resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			}

			long ifModifiedSince = req.getDateHeader(IF_MODIFIED_SINCE);
			// for purposes of comparison we add 999 to ifModifiedSince since the fidelity
			// of the IMS header generally doesn't include milli-seconds
			if (ifModifiedSince > -1 && lastModified > 0 && lastModified <= (ifModifiedSince + 999)) {
				resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			}

			// return the full contents regularly
			if (contentLength != -1)
				resp.setContentLength(contentLength);

			String contentType = getServletContext().getMimeType(pathInfo);
			if (contentType != null)
				resp.setContentType(contentType);

			if (lastModified > 0)
				resp.setDateHeader(LAST_MODIFIED, lastModified);

			if (etag != null)
				resp.setHeader(ETAG, etag);

			if (contentLength != 0) {
				// open the input stream
				InputStream is = null;
				try {
					is = connection.getInputStream();
					// write the resource
					try {
						OutputStream os = resp.getOutputStream();
						int writtenContentLength = writeResourceToOutputStream(is, os);
						if (contentLength == -1 || contentLength != writtenContentLength)
							resp.setContentLength(writtenContentLength);
					} catch (IllegalStateException e) { // can occur if the response output is already open as a Writer
						Writer writer = resp.getWriter();
						writeResourceToWriter(is, writer);
						// Since ContentLength is a measure of the number of bytes contained in the body
						// of a message when we use a Writer we lose control of the exact byte count and
						// defer the problem to the Servlet Engine's Writer implementation.
					}
				} catch (FileNotFoundException e) {
					// FileNotFoundException may indicate the following scenarios
					// - url is a directory
					// - url is not accessible
					sendError(resp, HttpServletResponse.SC_FORBIDDEN);
				} catch (SecurityException e) {
					// SecurityException may indicate the following scenarios
					// - url is not accessible
					sendError(resp, HttpServletResponse.SC_FORBIDDEN);
				} finally {
					if (is != null)
						try {
							is.close();
						} catch (IOException e) {
							// ignore
						}
				}
			}
					
	}
	
	/**
	 * 写入资源到输出流
	 * @param is
	 * @param os
	 * @return
	 * @throws IOException
	 */
	private int writeResourceToOutputStream(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[8192];
		int bytesRead = is.read(buffer);
		int writtenContentLength = 0;
		while (bytesRead != -1) {
			os.write(buffer, 0, bytesRead);
			writtenContentLength += bytesRead;
			bytesRead = is.read(buffer);
		}
		return writtenContentLength;
	}
	
	/**
	 * 写入Writer中
	 * @param is
	 * @param writer
	 * @throws IOException
	 */
	private void writeResourceToWriter(InputStream is, Writer writer) throws IOException {
		Reader reader = new InputStreamReader(is);
		try {
			char[] buffer = new char[8192];
			int charsRead = reader.read(buffer);
			while (charsRead != -1) {
				writer.write(buffer, 0, charsRead);
				charsRead = reader.read(buffer);
			}
		} finally {
			if (reader != null) {
				reader.close(); // will also close input stream
			}
		}
	}
	
	/**
	 * 发生错误消息
	 * @param resp
	 * @param sc
	 * @throws IOException
	 */
	private void sendError(final HttpServletResponse resp, int sc) throws IOException {
		try {
			// we need to reset headers for 302 and 403
			resp.reset();
			resp.sendError(sc);
		} catch (IllegalStateException e) {
			// this could happen if the response has already been committed
		}
	}
	
	protected ServletContext getServletContext(){
		return this.servletContext;
	}

	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.web.HttpResourceProcessor#getExtensionName()
	 */
	@Override
	public String[] getExtensionNames() {
		return null;
	}
	
	protected String parseExtensionName(String path){
		int index = StringUtils.lastIndexOf(path, AbstractHttpService.PATH_SEPARATOR);
		int index_ext = StringUtils.lastIndexOf(path, EXTENSION_SEPARATOR);
		if (index_ext > index){
			return StringUtils.substring(path, index_ext);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceTracker#addService(org.massyframework.assembly.ExportServiceReference, java.lang.Object)
	 */
	@Override
	protected void addService(ExportServiceReference<HttpResourceProcessor> reference, HttpResourceProcessor service) {
		super.addService(reference, service);
		String[] extensionNames = service.getExtensionNames();
		for (String extensionName: extensionNames){
			if (extensionName != null){
				if (extensionName.startsWith("*")){
					extensionName = StringUtils.substring(extensionName, 1);
				}
				if (!extensionName.startsWith(".")){
					extensionName = "." + extensionName;
				}
				
				ServletContextAware.maybeToBind(service, servletContext);
				processorMap.putIfAbsent(extensionName, service);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ExportServiceTracker#removeService(org.massyframework.assembly.ExportServiceReference, java.lang.Object)
	 */
	@Override
	protected void removeService(ExportServiceReference<HttpResourceProcessor> reference,
			HttpResourceProcessor service) {
		String[] extensionNames = service.getExtensionNames();
		for (String extensionName: extensionNames){
			if (extensionName != null){
				if (extensionName.startsWith("*")){
					extensionName = StringUtils.substring(extensionName, 1);
				}
				if (!extensionName.startsWith(".")){
					extensionName = "." + extensionName;
				}
				
				processorMap.remove(extensionName, service);
			}
		}
		super.removeService(reference, service);
	}

	
}
