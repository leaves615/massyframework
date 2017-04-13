/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyAware;
import org.massyframework.assembly.AssemblyEvent;
import org.massyframework.assembly.AssemblyListener;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.util.Asserts;
import org.slf4j.Logger;

/**
 * 装配件事件监听管理器
 */
public class AssemblyListenerManagement implements AssemblyAware{
	
	private final List<AssemblyListener> listeners =
			new CopyOnWriteArrayList<AssemblyListener>();
	private volatile ExecutorService executor;

	private volatile Assembly assembly;
	/**
	 * 构造方法
	 */
	public AssemblyListenerManagement() {
	}
	
	/**
	 * 添加事件监听器
	 * @param listener 事件监听器
	 */
	public void addListener(AssemblyListener listener){
		if (listener != null){
			this.listeners.add(listener);
		}
	}
	
	/**
	 * 移除事件监听器
	 * @param listener 事件监听器
	 */
	public void removeListener(AssemblyListener listener){
		if (listener != null){
			this.listeners.remove(listener);
		}
	}
	
	/**
	 * 发布装配件事件
	 * @param event 装配件事件
	 */
	public void publishAssemblyEvent(AssemblyEvent event){
		if (event.getType() == AssemblyEvent.INSTALLED
				|| event.getType() == AssemblyEvent.ACTIVATED){
			ExecutorService executor = this.executor;
			if (executor != null){
				executor.execute(new Task(event));
			}else{
				new Thread(new Task(event)).start();
			}
		}else{
			this.doPublishAssemblyEvent(event);
		}
	}

	/**
	 * 执行装配件事件发布
	 * @param event 装配件事件
	 */
	private void doPublishAssemblyEvent(AssemblyEvent event){
		for (AssemblyListener listener: this.listeners){
			try{
				listener.onChanged(event);
			}catch(Exception e){
				Logger logger = LoggerReference.adaptFrom(this.assembly);
				if (logger != null){
					if (logger.isErrorEnabled()){
						logger.error(listener +" onChanged(AssemblyEvent) failed.", e); 
					}
				}
			}
		}
	}
	
	/**
	 * 执行线程池
	 * @return
	 */
	public ExecutorService getExecutor() {
		return executor;
	}
	
	/**
	 * 设置执行线程池
	 * @param executor
	 */
	void setExecutorService(ExecutorService executor){
		this.executor = executor;
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.AssemblyAware#setAssembly(org.massyframework.assembly.Assembly)
	 */
	@Override
	public void setAssembly(Assembly assembly) {
		this.assembly = assembly;
	}

	private class Task implements Runnable {
		
		private AssemblyEvent event;
		
		public Task(AssemblyEvent event){
			Asserts.notNull(event, "event cannot be null.");
			this.event = event;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			doPublishAssemblyEvent(event);
		}
	}
}
