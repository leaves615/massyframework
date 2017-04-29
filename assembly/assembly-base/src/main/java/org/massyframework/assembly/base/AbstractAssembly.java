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
import org.massyframework.assembly.base.handle.support.DefaultHandlerRegistry;
import org.massyframework.assembly.base.logging.DelegateLogger;
import org.massyframework.assembly.base.support.InitParams;

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
	private String vendor;

	/**
	 * 
	 */
	public AbstractAssembly() {
		this(null);
	}
	
	protected AbstractAssembly(String symbolicName){
		this.id = AssemblyIdFactory.genericAssemblyId();
		this.handlerRegistry = new DefaultHandlerRegistry(this);
		if (symbolicName != null){
			this.symbolicName = symbolicName;
		}
		
		this.adaptManagement = 
				new AdaptManagement(this.getExportServiceRepository());
		if (symbolicName != null){
			this.adaptManagement.addAdaptObject(
					new DelegateLogger(this));
		}
		
		this.handlerRegistry.register(new InformationSetting());
		this.init();
	}
	
	/**
	 * 
	 */
	protected void init(){
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
		if (this.adaptManagement == null) return null;
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
		InitParams initParams = 
				this.getHandlerRegistry().findHandler(InitParams.class);
		return initParams == null ? null : initParams.getParameter(key);
	}

	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getinitParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public String getInitParameter(String key, String defaultValue) {
		String result = this.getInitParameter(key);
		if (result == null){
			result = defaultValue;
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getCategory()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see org.massyframework.assembly.Assembly#getVendor()
	 */
	@Override
	public String getVendor() {
		return this.vendor;
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
		return status == AssemblyStatus.WORKING;
	}

	/**
	 * 处理注册器
	 * @return
	 */
	protected HandlerRegistry getHandlerRegistry(){
		return this.handlerRegistry;
	}
	
	/**
	 * 获取输出服务仓储
	 * @return {@link ExportServiceRepository}
	 */
	protected abstract ExportServiceRepository getExportServiceRepository();
		
	/**
	 * 基本信息设置
	 */
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
				
				initAdaptObject(
						new DelegateLogger(AbstractAssembly.this));
				return true;
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see org.massyframework.assembly.base.handle.AssemblyInformationHandler#setVendor(java.lang.String)
		 */
		@Override
		public void setVendor(String vendor) {
			AbstractAssembly.this.vendor = vendor;
		}
	}
}
