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
* @日   期:  2017年4月9日
*/
package org.massyframework.assembly.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * IO工具类
 *
 */
public abstract class IOUtils {

	/**
	 * 关闭流
	 * @param stream
	 */
	public static void closeStream(Closeable stream){
		try{
			if (stream != null){
				stream.close();
			}
		}catch(IOException e){
			
		}
	}
	
	/**
	 * 复制文件
	 * @param source 源文件
	 * @param destination 目的文件
	 * @throws IOException
	 */
	public static void copy(File source, File destination) throws IOException{
		copy(new FileInputStream(source), destination);
	}
	
	/**
	 * 复制流到文件
	 * @param input 输入流
	 * @param destination 目的文件
	 * @throws IOException
	 */
	public static void copy(InputStream input, File destination) throws IOException{
		copy(input, new FileOutputStream(destination));
	}
	
	/**
	 * 复制流
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public static void copy(InputStream input, OutputStream output) throws IOException{
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(input);
			// 文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(output);

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			closeStream(inBuff);
			closeStream(outBuff);
		}
	}
	
	/**
	 * 将资源转为对应的本地路径
	 * @param resource 资源定位
	 * @return {@link Path},如果不是本地资源，则返回null.
	 * @throws IOException 发生非预期的读写例外
	 */
	public static Path toPath(URL resource) throws IOException{
		if (resource.getProtocol().equals("file")){
			return Paths.get(resource.getFile()); 
		}
		return null;
	}
}
