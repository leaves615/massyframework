/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月10日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime.service.registry;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.Constants;
import org.massyframework.assembly.ExportServiceListener;
import org.massyframework.assembly.Filter;
import org.massyframework.assembly.LoggerReference;
import org.massyframework.assembly.ServiceEvent;
import org.massyframework.assembly.ServiceFactory;
import org.massyframework.assembly.util.Asserts;
import org.slf4j.Logger;

/**
 * 服务监听管理器
 */
final class ServiceListenerManagement {

	private final List<ExportServiceListenerWrapper> listeners =
			new CopyOnWriteArrayList<ExportServiceListenerWrapper>();
	private volatile ExecutorService executor;
	/**
	 * 
	 */
	public ServiceListenerManagement() {
	
	}
	
	/**
	 * 添加事件监听器
	 * @param listener 事件监听器
	 * @param filter 筛选器
	 * @param assembly 注册事件监听器的装配件
	 */
	public void addListener(ExportServiceListener listener, Filter filter, Assembly assembly){
		ExportServiceListenerWrapper wrapper =
				new ExportServiceListenerWrapper(listener, filter, assembly);
		this.listeners.add(wrapper);
	}
	
	/**
	 * 移除事件监听器
	 * @param listener 事件监听器
	 */
	public void removeListener(ExportServiceListener listener, Assembly assembly){
		Iterator<ExportServiceListenerWrapper> it = this.listeners.iterator();
		while (it.hasNext()){
			ExportServiceListenerWrapper wrapper = it.next();
			if (wrapper.isEqual(listener, assembly)){
				it.remove();
			}
		}
	}
	
	/**
	 * 发布服务事件
	 * @param event
	 */
	public void publishServiceEvent(ServiceEvent event){
		if (event.getType() == ServiceEvent.REGISTED){
			//启动异步线程通知
			ExecutorService executor = this.executor;
			if (executor != null){
				executor.execute(new Task(event));
			}else{
				new Thread(new Task(event)).start();
			}
		}else{
			//注销事件，同步处理
			this.doPublishServiceEvent(event);
		}
	}
	
	/**
	 * 执行发布事件
	 * @param event
	 */
	protected void doPublishServiceEvent(ServiceEvent event){
		if (event == null) return;
		
		Iterator<ExportServiceListenerWrapper> it = this.listeners.iterator();
		while (it.hasNext()){
			ExportServiceListenerWrapper wrapper = it.next();
			wrapper.onChanged(event);
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
	 * 尝试设置ExecutorService
	 * @param executor the executor to set
	 */
	@SuppressWarnings("rawtypes")
	void maybeToSetExecutorService(ExportServiceRegistrationImpl<?> registration, Assembly assembly) {
		if (this.executor == null){
			boolean found = false;
			Class<?>[] objectClass =
					(Class<?>[])registration.getReference().getProperty(Constants.OBJECT_CLASS);
			for (Class<?> clazz: objectClass){
				if (clazz == ExecutorService.class){
					found = true;
					break;
				}
			}
			if (found){
				this.executor =(ExecutorService) 
						((ServiceFactory)registration).getService(assembly) ;
			}
		}
	}

	/**
	 * 设置执行线程池
	 * @param executor
	 */
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	/**
	 * 输出事件监听封装器，封装事件处理过过程并提供事件过滤能力
	 */
	private class ExportServiceListenerWrapper implements ExportServiceListener {
		
		private ExportServiceListener listener;
		private Filter filter;
		private Assembly assembly;
		
		public ExportServiceListenerWrapper(ExportServiceListener listener, Filter filter, Assembly assembly){
			Asserts.notNull(listener, "listener cannot be null.");
			Asserts.notNull(filter,   "filter cannot be null.");
			Asserts.notNull(assembly, "assembly cannot be null.");
			
			this.listener = listener;
			this.filter = filter;
			this.assembly = assembly;
		}

		@Override
		public void onChanged(ServiceEvent event) {
			if (this.filter.match(event.getServiceReference())){
				try{
					this.listener.onChanged(event);
				}catch(Exception e){
					Logger logger = LoggerReference.adaptFrom(this.assembly);
					if (logger != null){
						if (logger.isErrorEnabled()){
							logger.error(this.listener + " onChanged() falied.", e); 
						}
					}
				}
			}
		}
		
		public boolean isEqual(ExportServiceListener listener, Assembly assembly){
			if ((this.listener == listener) && (this.assembly == assembly)){
				return true;
			}
			return false;
		}
	}
	
	private class Task implements Runnable {
		
		private ServiceEvent event;
		
		public Task(ServiceEvent event){
			this.event =event;
		}

		@Override
		public void run() {
			doPublishServiceEvent(this.event);			
		}
	}
}
