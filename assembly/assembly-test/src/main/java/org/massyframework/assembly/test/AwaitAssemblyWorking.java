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
* @日   期:  2017年5月1日
*/
package org.massyframework.assembly.test;

import java.util.concurrent.CountDownLatch;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyEvent;
import org.massyframework.assembly.AssemblyListener;
import org.massyframework.assembly.Framework;
import org.massyframework.assembly.util.Asserts;

/**
 * 等待装配件进入工作状态
 */
public class AwaitAssemblyWorking {

	private Framework framework;
	private String symboilcName;
	private CountDownLatch latch;
	
	/**
	 * 构造方法
	 */
	public AwaitAssemblyWorking(Framework framework, String symboilcName) {
		Asserts.notNull(framework, "framework cannot be null.");
		Asserts.notNull(symboilcName, "symboilcName cannot be null.");
		this.framework = framework;
		this.symboilcName = symboilcName;
		this.latch = new CountDownLatch(1);
		
	}
	
	/**
	 * 确保装配件进入工作状态，否则一直等待
	 */
	public void ensureWorking(){
		System.out.println("=========================================================");
		System.out.println("await assembly[" + this.symboilcName + "] into working...");
		
		AssemblyListener listener = new AssemblyEventAdapter();
		framework.addListener(new AssemblyEventAdapter());
		
		if (this.framework.containsAssembly(this.symboilcName)){
			Assembly assembly = this.framework.getAssembly(this.symboilcName);
			if (assembly.isWorking()){	
				this.framework.removeListener(listener);
				System.out.println("assembly[" + this.symboilcName + "] is working.");
				System.out.println("=========================================================");
				return;
			}
		}
		
		try {
			this.latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("assembly[" + this.symboilcName + "] is working.");
		System.out.println("=========================================================");
	}
	
	/**
	 * 运行框架
	 * @return
	 */
	public Framework getFramework(){
		return this.framework;
	}

	private class AssemblyEventAdapter implements AssemblyListener {

		@Override
		public void onChanged(AssemblyEvent event) {
			if (symboilcName.equals(event.getAssmebly().getSymbolicName())){
				if (event.getType() == AssemblyEvent.ACTIVATED){
					latch.countDown();
					framework.removeListener(this);
				}
			}
					
		}
		
	}
}
