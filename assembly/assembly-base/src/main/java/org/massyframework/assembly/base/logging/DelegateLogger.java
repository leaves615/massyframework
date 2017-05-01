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
* @日   期:  2017年4月16日
*/
package org.massyframework.assembly.base.logging;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * 代理的日志记录器
 */
public class DelegateLogger implements Logger, LoggerReference{
	
	private final Logger logger;
	
	public DelegateLogger(Assembly assembly){
		Asserts.notNull(assembly, "assembly cannot be null.");
		this.logger = LoggerFactory.getLogger(assembly.getSymbolicName());
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.ObjectReference#getReference()
	 */
	@Override
	public Logger getReference() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
		this.logger.debug(arg0, arg1, arg2, arg3);		
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void debug(Marker arg0, String arg1, Object... arg2) {
		this.logger.debug(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String, java.lang.Object)
	 */
	@Override
	public void debug(Marker arg0, String arg1, Object arg2) {
		this.logger.debug(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void debug(Marker arg0, String arg1, Throwable arg2) {
		this.logger.debug(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String)
	 */
	@Override
	public void debug(Marker arg0, String arg1) {
		this.logger.debug(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void debug(String arg0, Object arg1, Object arg2) {
		this.logger.debug(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void debug(String arg0, Object... arg1) {
		this.logger.debug(arg0, arg1);
		
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object)
	 */
	@Override
	public void debug(String arg0, Object arg1) {
		this.logger.debug(arg0, arg1);
		
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void debug(String arg0, Throwable arg1) {
		this.logger.debug(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#debug(java.lang.String)
	 */
	@Override
	public void debug(String arg0) {
		this.logger.debug(arg0);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void error(Marker arg0, String arg1, Object arg2, Object arg3) {
		this.logger.error(arg0, arg1, arg2, arg3);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void error(Marker arg0, String arg1, Object... arg2) {
		this.logger.error(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String, java.lang.Object)
	 */
	@Override
	public void error(Marker arg0, String arg1, Object arg2) {
		this.logger.error(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void error(Marker arg0, String arg1, Throwable arg2) {
		this.logger.error(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String)
	 */
	@Override
	public void error(Marker arg0, String arg1) {
		this.logger.error(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void error(String arg0, Object arg1, Object arg2) {
		this.logger.error(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void error(String arg0, Object... arg1) {
		this.logger.error(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object)
	 */
	@Override
	public void error(String arg0, Object arg1) {
		this.logger.error(arg0, arg1);
		
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void error(String arg0, Throwable arg1) {
		this.logger.error(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#error(java.lang.String)
	 */
	@Override
	public void error(String arg0) {
		this.logger.error(arg0);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#getName()
	 */
	@Override
	public String getName() {
		return this.logger.getName();
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void info(Marker arg0, String arg1, Object arg2, Object arg3) {
		this.logger.info(arg0, arg1, arg2, arg3);
		
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void info(Marker arg0, String arg1, Object... arg2) {
		this.logger.info(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String, java.lang.Object)
	 */
	@Override
	public void info(Marker arg0, String arg1, Object arg2) {
		this.logger.info(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void info(Marker arg0, String arg1, Throwable arg2) {
		this.logger.info(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String)
	 */
	@Override
	public void info(Marker arg0, String arg1) {
		this.logger.info(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void info(String arg0, Object arg1, Object arg2) {
		this.logger.info(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void info(String arg0, Object... arg1) {
		this.logger.info(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object)
	 */
	@Override
	public void info(String arg0, Object arg1) {
		this.logger.info(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void info(String arg0, Throwable arg1) {
		this.logger.info(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#info(java.lang.String)
	 */
	@Override
	public void info(String arg0) {
		this.logger.info(arg0);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#isDebugEnabled()
	 */
	@Override
	public boolean isDebugEnabled() {
		return this.logger.isDebugEnabled();
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#isDebugEnabled(org.slf4j.Marker)
	 */
	@Override
	public boolean isDebugEnabled(Marker arg0) {
		return this.logger.isDebugEnabled(arg0);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#isErrorEnabled()
	 */
	@Override
	public boolean isErrorEnabled() {
		return this.logger.isErrorEnabled();
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#isErrorEnabled(org.slf4j.Marker)
	 */
	@Override
	public boolean isErrorEnabled(Marker arg0) {
		return this.logger.isErrorEnabled(arg0);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#isInfoEnabled()
	 */
	@Override
	public boolean isInfoEnabled() {
		return this.logger.isInfoEnabled();
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#isInfoEnabled(org.slf4j.Marker)
	 */
	@Override
	public boolean isInfoEnabled(Marker arg0) {
		return this.logger.isInfoEnabled(arg0);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#isTraceEnabled()
	 */
	@Override
	public boolean isTraceEnabled() {
		return this.logger.isTraceEnabled();
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#isTraceEnabled(org.slf4j.Marker)
	 */
	@Override
	public boolean isTraceEnabled(Marker arg0) {
		return this.logger.isTraceEnabled(arg0);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#isWarnEnabled()
	 */
	@Override
	public boolean isWarnEnabled() {
		return this.logger.isWarnEnabled();
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#isWarnEnabled(org.slf4j.Marker)
	 */
	@Override
	public boolean isWarnEnabled(Marker arg0) {
		return this.logger.isWarnEnabled(arg0);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void trace(Marker arg0, String arg1, Object arg2, Object arg3) {
		this.logger.trace(arg0, arg1, arg2, arg3);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void trace(Marker arg0, String arg1, Object... arg2) {
		this.logger.trace(arg0, arg1, arg2);
		
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String, java.lang.Object)
	 */
	@Override
	public void trace(Marker arg0, String arg1, Object arg2) {
		this.logger.trace(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void trace(Marker arg0, String arg1, Throwable arg2) {
		this.logger.trace(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String)
	 */
	@Override
	public void trace(Marker arg0, String arg1) {
		this.logger.trace(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void trace(String arg0, Object arg1, Object arg2) {
		this.logger.trace(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void trace(String arg0, Object... arg1) {
		this.logger.trace(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object)
	 */
	@Override
	public void trace(String arg0, Object arg1) {
		this.logger.trace(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void trace(String arg0, Throwable arg1) {
		this.logger.trace(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#trace(java.lang.String)
	 */
	@Override
	public void trace(String arg0) {
		this.logger.trace(arg0);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void warn(Marker arg0, String arg1, Object arg2, Object arg3) {
		this.logger.warn(arg0, arg1, arg2, arg3);
		
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void warn(Marker arg0, String arg1, Object... arg2) {
		this.logger.warn(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String, java.lang.Object)
	 */
	@Override
	public void warn(Marker arg0, String arg1, Object arg2) {
		this.logger.warn(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void warn(Marker arg0, String arg1, Throwable arg2) {
		this.logger.warn(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String)
	 */
	@Override
	public void warn(Marker arg0, String arg1) {
		this.logger.warn(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void warn(String arg0, Object arg1, Object arg2) {
		this.logger.warn(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void warn(String arg0, Object... arg1) {
		this.logger.warn(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object)
	 */
	@Override
	public void warn(String arg0, Object arg1) {
		this.logger.warn(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void warn(String arg0, Throwable arg1) {
		this.logger.warn(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.slf4j.Logger#warn(java.lang.String)
	 */
	@Override
	public void warn(String arg0) {
		this.logger.warn(arg0);
	}
	
	

}
