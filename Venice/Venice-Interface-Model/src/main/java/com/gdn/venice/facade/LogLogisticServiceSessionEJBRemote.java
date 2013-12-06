package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogLogisticService;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogLogisticServiceSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogLogisticService
	 */
	public List<LogLogisticService> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogLogisticService persists a country
	 * 
	 * @param logLogisticService
	 * @return the persisted LogLogisticService
	 */
	public LogLogisticService persistLogLogisticService(LogLogisticService logLogisticService);

	/**
	 * persistLogLogisticServiceList - persists a list of LogLogisticService
	 * 
	 * @param logLogisticServiceList
	 * @return the list of persisted LogLogisticService
	 */
	public ArrayList<LogLogisticService> persistLogLogisticServiceList(
			List<LogLogisticService> logLogisticServiceList);

	/**
	 * mergeLogLogisticService - merges a LogLogisticService
	 * 
	 * @param logLogisticService
	 * @return the merged LogLogisticService
	 */
	public LogLogisticService mergeLogLogisticService(LogLogisticService logLogisticService);

	/**
	 * mergeLogLogisticServiceList - merges a list of LogLogisticService
	 * 
	 * @param logLogisticServiceList
	 * @return the merged list of LogLogisticService
	 */
	public ArrayList<LogLogisticService> mergeLogLogisticServiceList(
			List<LogLogisticService> logLogisticServiceList);

	/**
	 * removeLogLogisticService - removes a LogLogisticService
	 * 
	 * @param logLogisticService
	 */
	public void removeLogLogisticService(LogLogisticService logLogisticService);

	/**
	 * removeLogLogisticServiceList - removes a list of LogLogisticService
	 * 
	 * @param logLogisticServiceList
	 */
	public void removeLogLogisticServiceList(List<LogLogisticService> logLogisticServiceList);

	/**
	 * findByLogLogisticServiceLike - finds a list of LogLogisticService Like
	 * 
	 * @param logLogisticService
	 * @return the list of LogLogisticService found
	 */
	public List<LogLogisticService> findByLogLogisticServiceLike(LogLogisticService logLogisticService,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogLogisticService>LikeFR - finds a list of LogLogisticService> Like with a finder return object
	 * 
	 * @param logLogisticService
	 * @return the list of LogLogisticService found
	 */
	public FinderReturn findByLogLogisticServiceLikeFR(LogLogisticService logLogisticService,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
