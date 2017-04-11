/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月9日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base;

import org.massyframework.assembly.Assembly;
import org.massyframework.assembly.AssemblyContext;
import org.massyframework.assembly.AssemblyStatus;
import org.massyframework.assembly.ExportServiceRepository;
import org.massyframework.assembly.base.handle.AssemblyInformationHandler;
import org.massyframework.assembly.base.handle.HandlerRegistry;
import org.massyframework.assembly.base.handle.LifecycleProcessHandler;

/**
 * 装配件的基类
 */
public abstract class AbstractAssembly implements Assembly {

	private final long id;
	private final HandlerRegistry handlerRegistry;
	private final AdaptManagement adaptManagement;
	
	private String name;
	private String symbolicName;
	private String description;

	/**
	 * 
	 */
	public AbstractAssembly(ExportServiceRepository serviceRepository) {
		this.id = AssemblyIdFactory.genericAssemblyId();
		this.handlerRegistry = new DefaultHandlerRegistry(this);
		this.adaptManagement = new AdaptManagement(serviceRepository);
		this.handlerRegistry.register(new InformationSetting());
	}
		
	/**
	 * 初始化可适配对象
	 * @param adaptObject
	 */
	public void initAdaptObject(Object adaptObject){
		this.adaptManagement.addAdaptObject(adaptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#adapt(java.lang.Class)
	 */
	@Override
	public <T> T adapt(Class<T> adaptType) {
		return this.adaptManagement.adapt(adaptType);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Assembly o) {
		return Long.compare(this.id, o.getAssemblyId());
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getAssemblyId()
	 */
	@Override
	public long getAssemblyId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getAssmeblyContext()
	 */
	@Override
	public AssemblyContext getAssmeblyContext() {
		AssemblyContext result =
				this.getHandlerRegistry().findHandler(AssemblyContext.class);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getAssemblyStatus()
	 */
	@Override
	public AssemblyStatus getAssemblyStatus() {
		LifecycleProcessHandler handler =
				this.getHandlerRegistry().getHandler(LifecycleProcessHandler.class);
		return handler.getAssemblyStatus();
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getInitParameter(java.lang.String)
	 */
	@Override
	public String getInitParameter(String key) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getinitParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public String getInitParameter(String key, String defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getCategory()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getSymbolicName()
	 */
	@Override
	public String getSymbolicName() {
		return this.symbolicName;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#isReady()
	 */
	@Override
	public boolean isReady() {
		AssemblyStatus status = this.getAssemblyStatus();
		return status != AssemblyStatus.READY || status != AssemblyStatus.WORKING;
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#isWorking()
	 */
	@Override
	public boolean isWorking() {
		AssemblyStatus status = this.getAssemblyStatus();
		return status != AssemblyStatus.WORKING;
	}

	/**
	 * 处理注册器
	 * @return
	 */
	protected HandlerRegistry getHandlerRegistry(){
		return this.handlerRegistry;
	}
	
	private class InformationSetting implements AssemblyInformationHandler {

		@Override
		public void setName(String name) {
			AbstractAssembly.this.name = name;
		}

		@Override
		public void setDescription(String description) {
			AbstractAssembly.this.description = description;
		}

		@Override
		public boolean setSymbolicName(String symbolicName) {
			if (AbstractAssembly.this.symbolicName == null){
				AbstractAssembly.this.symbolicName = symbolicName;
				return true;
			}
			return false;
		}
		
	}
}
