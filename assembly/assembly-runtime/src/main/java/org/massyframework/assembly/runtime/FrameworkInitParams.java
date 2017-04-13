/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月12日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.massyframework.assembly.InitParameterEvent;
import org.massyframework.assembly.InitParameterListener;
import org.massyframework.assembly.base.handle.support.AbstractHandler;
import org.massyframework.assembly.base.support.InitParams;

/**
 * 运行框架的初始化参数
 *
 */
final class FrameworkInitParams extends AbstractHandler implements InitParams {

	private volatile boolean modified = true;
	private Map<String, String> props = new HashMap<String, String>();
	private List<InitParameterListener> listeners;
	
	/**
	 * 
	 */
	public FrameworkInitParams() {
		this.listeners = new ArrayList<InitParameterListener>();
	}
	
	/**
	 * 添加事件监听器
	 * @param listener {@link InitParameterListener}
	 */
	public synchronized void addListener(InitParameterListener listener){
		if (listener != null){
			this.listeners.add(listener);
		}
	}
	
	/**
	 * 当不能在增加，清除所有事件监听器
	 */
	public synchronized void cleanAll(){
		this.listeners.clear();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.support.InitParams#containsParameter(java.lang.String)
	 */
	@Override
	public boolean containsParameter(String key) {
		if (!this.canModified()){
			return this.props.containsKey(key);
		}else{
			synchronized(this){
				return this.props.containsKey(key);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.support.InitParams#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String key) {
		if (!this.canModified()){
			//只读模型
			return props.get(key);
		}else{
			//读写模型，需要进行同步
			synchronized (this){
				return props.get(key);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.base.support.InitParams#getParameterKeys()
	 */
	@Override
	public List<String> getParameterKeys() {
		if (!this.canModified()){
			//只读模型
			return new ArrayList<String>(this.props.keySet());
		}else{
			//读写模型，需要进行同步
			synchronized (this){
				return new ArrayList<String>(this.props.keySet());
			}
		}
	}
	
	/**
	 * 设置参数
	 * @param key 参数键
	 * @param value 参数值
	 * @return <code>true</code>设置成功, <code>false</code>设置失败
	 */
	boolean setParameter(String key, String value){
		if (this.canModified()){
			synchronized(this){
				if (!this.props.containsKey(key)){
					this.props.put(key, value);
					return true;
				}else{
					return false;
				}
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 设置参数，并返回未设置成功的键
	 * @param params Map参数
	 * @return {@link List},未设置成功的键
	 */
	List<String> setParameters(Map<String, String> params){
		List<String> result = new ArrayList<String>();
		if (this.canModified()){
			synchronized(this){
				for (Entry<String, String> entry: params.entrySet()){
					if (!this.props.containsKey(entry.getKey())){
						this.props.put(entry.getKey(), entry.getValue());
					}else{
						result.add(entry.getKey());
					}
				}
			}
		}
		
		return result;
	}
	
	protected boolean canModified(){
		return this.modified;
	}
	
	void setReadOnly(){
		this.modified = false;
	}
	
	protected void publishChangeEvent(InitParameterEvent event){
		List<InitParameterListener> list = null;
		synchronized(this){
			list = Collections.unmodifiableList(this.listeners);
		}
		
		for (InitParameterListener listener: list){
			try{
				listener.onParameterChange(event);
			}catch(Exception e){
				
			}
		}
	}
}
