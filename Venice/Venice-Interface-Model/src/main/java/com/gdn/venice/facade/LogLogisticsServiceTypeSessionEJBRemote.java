package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogLogisticsServiceType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogLogisticsServiceTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogLogisticsServiceType
	 */
	public List<LogLogisticsServiceType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogLogisticsServiceType persists a country
	 * 
	 * @param logLogisticsServiceType
	 * @return the persisted LogLogisticsServiceType
	 */
	public LogLogisticsServiceType persistLogLogisticsServiceType(LogLogisticsServiceType logLogisticsServiceType);

	/**
	 * persistLogLogisticsServiceTypeList - persists a list of LogLogisticsServiceType
	 * 
	 * @param logLogisticsServiceTypeList
	 * @return the list of persisted LogLogisticsServiceType
	 */
	public ArrayList<LogLogisticsServiceType> persistLogLogisticsServiceTypeList(
			List<LogLogisticsServiceType> logLogisticsServiceTypeList);

	/**
	 * mergeLogLogisticsServiceType - merges a LogLogisticsServiceType
	 * 
	 * @param logLogisticsServiceType
	 * @return the merged LogLogisticsServiceType
	 */
	public LogLogisticsServiceType mergeLogLogisticsServiceType(LogLogisticsServiceType logLogisticsServiceType);

	/**
	 * mergeLogLogisticsServiceTypeList - merges a list of LogLogisticsServiceType
	 * 
	 * @param logLogisticsServiceTypeList
	 * @return the merged list of LogLogisticsServiceType
	 */
	public ArrayList<LogLogisticsServiceType> mergeLogLogisticsServiceTypeList(
			List<LogLogisticsServiceType> logLogisticsServiceTypeList);

	/**
	 * removeLogLogisticsServiceType - removes a LogLogisticsServiceType
	 * 
	 * @param logLogisticsServiceType
	 */
	public void removeLogLogisticsServiceType(LogLogisticsServiceType logLogisticsServiceType);

	/**
	 * removeLogLogisticsServiceTypeList - removes a list of LogLogisticsServiceType
	 * 
	 * @param logLogisticsServiceTypeList
	 */
	public void removeLogLogisticsServiceTypeList(List<LogLogisticsServiceType> logLogisticsServiceTypeList);

	/**
	 * findByLogLogisticsServiceTypeLike - finds a list of LogLogisticsServiceType Like
	 * 
	 * @param logLogisticsServiceType
	 * @return the list of LogLogisticsServiceType found
	 */
	public List<LogLogisticsServiceType> findByLogLogisticsServiceTypeLike(LogLogisticsServiceType logLogisticsServiceType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogLogisticsServiceType>LikeFR - finds a list of LogLogisticsServiceType> Like with a finder return object
	 * 
	 * @param logLogisticsServiceType
	 * @return the list of LogLogisticsServiceType found
	 */
	public FinderReturn findByLogLogisticsServiceTypeLikeFR(LogLogisticsServiceType logLogisticsServiceType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
