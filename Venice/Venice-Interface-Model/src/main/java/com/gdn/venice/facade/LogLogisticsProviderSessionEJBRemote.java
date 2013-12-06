package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogLogisticsProvider;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogLogisticsProviderSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogLogisticsProvider
	 */
	public List<LogLogisticsProvider> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogLogisticsProvider persists a country
	 * 
	 * @param logLogisticsProvider
	 * @return the persisted LogLogisticsProvider
	 */
	public LogLogisticsProvider persistLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider);

	/**
	 * persistLogLogisticsProviderList - persists a list of LogLogisticsProvider
	 * 
	 * @param logLogisticsProviderList
	 * @return the list of persisted LogLogisticsProvider
	 */
	public ArrayList<LogLogisticsProvider> persistLogLogisticsProviderList(
			List<LogLogisticsProvider> logLogisticsProviderList);

	/**
	 * mergeLogLogisticsProvider - merges a LogLogisticsProvider
	 * 
	 * @param logLogisticsProvider
	 * @return the merged LogLogisticsProvider
	 */
	public LogLogisticsProvider mergeLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider);

	/**
	 * mergeLogLogisticsProviderList - merges a list of LogLogisticsProvider
	 * 
	 * @param logLogisticsProviderList
	 * @return the merged list of LogLogisticsProvider
	 */
	public ArrayList<LogLogisticsProvider> mergeLogLogisticsProviderList(
			List<LogLogisticsProvider> logLogisticsProviderList);

	/**
	 * removeLogLogisticsProvider - removes a LogLogisticsProvider
	 * 
	 * @param logLogisticsProvider
	 */
	public void removeLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider);

	/**
	 * removeLogLogisticsProviderList - removes a list of LogLogisticsProvider
	 * 
	 * @param logLogisticsProviderList
	 */
	public void removeLogLogisticsProviderList(List<LogLogisticsProvider> logLogisticsProviderList);

	/**
	 * findByLogLogisticsProviderLike - finds a list of LogLogisticsProvider Like
	 * 
	 * @param logLogisticsProvider
	 * @return the list of LogLogisticsProvider found
	 */
	public List<LogLogisticsProvider> findByLogLogisticsProviderLike(LogLogisticsProvider logLogisticsProvider,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogLogisticsProvider>LikeFR - finds a list of LogLogisticsProvider> Like with a finder return object
	 * 
	 * @param logLogisticsProvider
	 * @return the list of LogLogisticsProvider found
	 */
	public FinderReturn findByLogLogisticsProviderLikeFR(LogLogisticsProvider logLogisticsProvider,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
