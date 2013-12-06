package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogLogisticsProvider;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogLogisticsProviderSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogLogisticsProvider> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote#persistLogLogisticsProvider(com
	 * .gdn.venice.persistence.LogLogisticsProvider)
	 */
	public LogLogisticsProvider persistLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote#persistLogLogisticsProviderList
	 * (java.util.List)
	 */
	public ArrayList<LogLogisticsProvider> persistLogLogisticsProviderList(
			List<LogLogisticsProvider> logLogisticsProviderList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote#mergeLogLogisticsProvider(com.
	 * gdn.venice.persistence.LogLogisticsProvider)
	 */
	public LogLogisticsProvider mergeLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote#mergeLogLogisticsProviderList(
	 * java.util.List)
	 */
	public ArrayList<LogLogisticsProvider> mergeLogLogisticsProviderList(
			List<LogLogisticsProvider> logLogisticsProviderList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote#removeLogLogisticsProvider(com
	 * .gdn.venice.persistence.LogLogisticsProvider)
	 */
	public void removeLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote#removeLogLogisticsProviderList
	 * (java.util.List)
	 */
	public void removeLogLogisticsProviderList(List<LogLogisticsProvider> logLogisticsProviderList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote#findByLogLogisticsProviderLike
	 * (com.gdn.venice.persistence.LogLogisticsProvider, int, int)
	 */
	public List<LogLogisticsProvider> findByLogLogisticsProviderLike(LogLogisticsProvider logLogisticsProvider,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote#findByLogLogisticsProviderLikeFR
	 * (com.gdn.venice.persistence.LogLogisticsProvider, int, int)
	 */
	public FinderReturn findByLogLogisticsProviderLikeFR(LogLogisticsProvider logLogisticsProvider,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
