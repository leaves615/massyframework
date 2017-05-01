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
* @日   期:  2017年4月11日
*/
package org.massyframework.assembly.base.handle.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.massyframework.assembly.AssemblyStatus;
import org.massyframework.assembly.base.handle.ActivationHandler;
import org.massyframework.assembly.base.handle.DependencyServiceResourceMatchHandler;
import org.massyframework.assembly.base.handle.LifecycleListener;
import org.massyframework.assembly.base.handle.LifecycleProcessHandler;
import org.massyframework.assembly.base.handle.ReadyingHandler;
import org.massyframework.assembly.base.handle.RegisterableHandler;
import org.massyframework.assembly.base.handle.ResolveHandler;
import org.massyframework.assembly.base.handle.AssemblyContextHandler;
import org.slf4j.Logger;

/**
 * 生命周期管理
 */
public final class LifecycleManagement extends AbstractHandler 
	implements LifecycleProcessHandler, RegisterableHandler{

	private final AtomicReference<AssemblyStatus> reference;
	private List<LifecycleListener> listeners =
			new ArrayList<LifecycleListener>();
	
	/**
	 * 
	 */
	public LifecycleManagement() {
		this.reference = new AtomicReference<AssemblyStatus>();
		this.reference.set(AssemblyStatus.RESOLVE);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleProcessHandler#addListener(org.massyframework.assembly.base.handle.LifecycleListener)
	 */
	@Override
	public synchronized void addListener(LifecycleListener listener) {
		if (listener != null){
			this.listeners.add(listener);
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleProcessHandler#getAssemblyStatus()
	 */
	@Override
	public AssemblyStatus getAssemblyStatus() {
		return this.reference.get();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleProcessHandler#start()
	 */
	@Override
	public void start() throws Exception {
		Logger logger = this.getLogger();
		try{
			this.doResolve();
			
			if (!this.doReady()){
				if (logger != null){
					if (logger.isDebugEnabled()){
						logger.debug("dependency service resource cannot matched all.");
					}
				}
			}
			
			this.doStart();
		}catch(Exception e){
			if (logger!= null){
				if (logger.isErrorEnabled()){
					logger.error("start " + this.getAssembly() + " failed.", e);
				}
			}
			
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleProcessHandler#stop()
	 */
	@Override
	public void stop() throws Exception {
		try{
			this.doStop();
			this.doUnready();
		}catch(Exception e){
			Logger logger = this.getLogger();
			if (logger!= null){
				if (logger.isErrorEnabled()){
					logger.error("stop " + this.getAssembly() + " failed.", e);
				}
			}
			
			throw e;
		}

	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.LifecycleProcessHandler#removeListener(org.massyframework.assembly.base.handle.LifecycleListener)
	 */
	@Override
	public synchronized void removeListener(LifecycleListener listener) {
		if (listener != null){
			this.listeners.remove(listener);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.RegisterableHandler#registCompleted()
	 */
	@Override
	public void registCompleted() {
		this.publishInstalledEvent();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.handle.RegisterableHandler#unregistering()
	 */
	@Override
	public void unregistering() {
		this.doUninstalling();		
	}

	/**
	 * 执行解析
	 * @throws Exception
	 */
	protected synchronized void doResolve() throws Exception{
		if (this.reference.get() == AssemblyStatus.RESOLVE){
			ResolveHandler handler =
					this.findHandler(ResolveHandler.class);
			if (handler != null){
				handler.doResolve();
			}
			
			this.reference.set(AssemblyStatus.PREPARE);
			Logger logger = this.getLogger();
			if (logger != null){
				logger.debug("assembly is resolved.");
			}
			this.publishResolvedEvent();
		}
	}
	
	/**
	 * 进入就绪
	 * @return 
	 * @throws Exception
	 */
	protected synchronized boolean doReady() throws Exception{
		if (this.reference.get() == AssemblyStatus.PREPARE){
			if (this.isPrepare()){
				List<ReadyingHandler> handlers =
						this.getHandlers(ReadyingHandler.class);
				for (ReadyingHandler handler: handlers){
					handler.doReadying();
				}
				
				this.reference.set(AssemblyStatus.READY);
				Logger logger = this.getLogger();
				if (logger != null){
					logger.debug("assembly is readied.");
				}
				this.publishReadiedEvent();
				return true;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 是否准备就绪
	 * @throws Exception
	 */
	private synchronized boolean isPrepare() throws Exception{
		DependencyServiceResourceMatchHandler handler =
				this.findHandler(DependencyServiceResourceMatchHandler.class);
		if (handler != null){
			return handler.isAllMatched();
		}
		return true;
	}
	
	
	
	/**
	 * 执行启动
	 * @throws Exception
	 */
	protected synchronized void doStart() throws Exception{
		if (this.reference.get() == AssemblyStatus.READY){
			AssemblyContextHandler handler =
					this.getHandler(AssemblyContextHandler.class);
			if (handler.canWork()){
				List<ActivationHandler> handlers =
						this.getHandlers(ActivationHandler.class);
				for (ActivationHandler actHandler: handlers){
					actHandler.doStarting();
				}
				
				this.reference.set(AssemblyStatus.WORKING);
				Logger logger = this.getLogger();
				if (logger != null){
					logger.debug("assembly is working.");
				}
				this.publishActivatedEvent();
			}
		}
	}
	
	/**
	 * 执行停止
	 * @throws Exception
	 */
	protected synchronized void doStop() throws Exception{
		if (this.reference.get() == AssemblyStatus.WORKING){
			
			this.publishInactivatingEvent();
						
			this.reference.set(AssemblyStatus.READY);
			Logger logger = this.getLogger();
			if (logger != null){
				logger.debug("assembly is readied.");
			}
			
			List<ActivationHandler> handlers =
					this.getHandlers(ActivationHandler.class);
			for (ActivationHandler actHandler: handlers){
				try{
					actHandler.doStopped();
				}catch(Exception e){
					if (logger != null){
						if (logger.isDebugEnabled()){
							logger.debug(actHandler + " doStopped() failed.", e);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 执行退出就绪状态
	 * @throws Exception
	 */
	protected synchronized void doUnready() throws Exception {
		if (this.reference.get() == AssemblyStatus.READY){
			if (!this.isPrepare()){
								
				this.publishUnreadyingEvent();
								
				this.reference.set(AssemblyStatus.PREPARE);
				
				List<ReadyingHandler> handlers =
						this.getHandlers(ReadyingHandler.class);
				for (ReadyingHandler handler: handlers){
					handler.doUnreadied();
				}
				Logger logger = this.getLogger();
				if (logger != null){
					logger.debug("assembly is prepared.");
				}
			}
		}
	}
	
	/**
	 * 执行取消安装处理
	 * @throws Exception
	 */
	protected synchronized void doUninstalling(){
		if (this.reference.get() != AssemblyStatus.PREPARE){
			try {
				this.stop();
			} catch (Exception e) {
			}
		}
		
		this.publishUninstallEvent();
	}
	
	/**
	 * 发布装配件安装完成事件
	 */
	protected synchronized void publishInstalledEvent() {
		for (LifecycleListener listener: this.listeners){
			try{
				listener.onInstalled();
			}catch(Exception e){
				Logger logger = this.getLogger();
				if (logger!= null){
					if (logger.isWarnEnabled()){
						logger.warn( listener + " onInstalled() failed.", e);
					}
				}
			}
		}
	}
	
	/**
	 * 发布解析完成事件
	 */
	protected synchronized void publishResolvedEvent() {
		for (LifecycleListener listener: this.listeners){
			try{
				listener.onResolved();
			}catch(Exception e){
				Logger logger = this.getLogger();
				if (logger!= null){
					if (logger.isWarnEnabled()){
						logger.warn( listener + " onResolved() failed.", e);
					}
				}
			}
		}
	}
	
	/**
	 * 发布进入准备就绪
	 */
	protected synchronized void publishReadiedEvent(){
		for (LifecycleListener listener: this.listeners){
			try{
				listener.onReadied();
			}catch(Exception e){
				Logger logger = this.getLogger();
				if (logger!= null){
					if (logger.isWarnEnabled()){
						logger.warn( listener + " onReadied() failed.", e);
					}
				}
			}
		}
	}
	
	/**
	 * 发布装配件激活事件
	 */
	protected synchronized void publishActivatedEvent(){
		for (LifecycleListener listener: this.listeners){
			try{
				listener.onActivated();
			}catch(Exception e){
				Logger logger = this.getLogger();
				if (logger!= null){
					if (logger.isWarnEnabled()){
						logger.warn( listener + " onActivated() failed.", e);
					}
				}
			}
		}
	}
	
	/**
	 * 发布装配件钝化事件
	 */
	protected synchronized void publishInactivatingEvent(){
		for (LifecycleListener listener: this.listeners){
			try{
				listener.onInactivating();
			}catch(Exception e){
				Logger logger = this.getLogger();
				if (logger!= null){
					if (logger.isWarnEnabled()){
						logger.warn( listener + " onInactivating() failed.", e);
					}
				}
			}
		}
	}
	
	/**
	 * 发布装配件退出就绪事件
	 */
	protected synchronized void publishUnreadyingEvent(){
		for (LifecycleListener listener: this.listeners){
			try{
				listener.onUnreadying();
			}catch(Exception e){
				Logger logger = this.getLogger();
				if (logger!= null){
					if (logger.isWarnEnabled()){
						logger.warn( listener + " onUnreadying() failed.", e);
					}
				}
			}
		}
	}
	
	/**
	 * 发布装配件取消注册事件
	 */
	protected synchronized void publishUninstallEvent(){
		for (LifecycleListener listener: this.listeners){
			try{
				listener.onUninstalling();
			}catch(Exception e){
				Logger logger = this.getLogger();
				if (logger!= null){
					if (logger.isWarnEnabled()){
						logger.warn( listener + " onUninstalling() failed.", e);
					}
				}
			}
		}
	}
	
}
