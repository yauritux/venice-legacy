package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdFraudActionLog;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdFraudActionLogSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdFraudActionLog
	 */
	public List<FrdFraudActionLog> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdFraudActionLog persists a country
	 * 
	 * @param frdFraudActionLog
	 * @return the persisted FrdFraudActionLog
	 */
	public FrdFraudActionLog persistFrdFraudActionLog(FrdFraudActionLog frdFraudActionLog);

	/**
	 * persistFrdFraudActionLogList - persists a list of FrdFraudActionLog
	 * 
	 * @param frdFraudActionLogList
	 * @return the list of persisted FrdFraudActionLog
	 */
	public ArrayList<FrdFraudActionLog> persistFrdFraudActionLogList(
			List<FrdFraudActionLog> frdFraudActionLogList);

	/**
	 * mergeFrdFraudActionLog - merges a FrdFraudActionLog
	 * 
	 * @param frdFraudActionLog
	 * @return the merged FrdFraudActionLog
	 */
	public FrdFraudActionLog mergeFrdFraudActionLog(FrdFraudActionLog frdFraudActionLog);

	/**
	 * mergeFrdFraudActionLogList - merges a list of FrdFraudActionLog
	 * 
	 * @param frdFraudActionLogList
	 * @return the merged list of FrdFraudActionLog
	 */
	public ArrayList<FrdFraudActionLog> mergeFrdFraudActionLogList(
			List<FrdFraudActionLog> frdFraudActionLogList);

	/**
	 * removeFrdFraudActionLog - removes a FrdFraudActionLog
	 * 
	 * @param frdFraudActionLog
	 */
	public void removeFrdFraudActionLog(FrdFraudActionLog frdFraudActionLog);

	/**
	 * removeFrdFraudActionLogList - removes a list of FrdFraudActionLog
	 * 
	 * @param frdFraudActionLogList
	 */
	public void removeFrdFraudActionLogList(List<FrdFraudActionLog> frdFraudActionLogList);

	/**
	 * findByFrdFraudActionLogLike - finds a list of FrdFraudActionLog Like
	 * 
	 * @param frdFraudActionLog
	 * @return the list of FrdFraudActionLog found
	 */
	public List<FrdFraudActionLog> findByFrdFraudActionLogLike(FrdFraudActionLog frdFraudActionLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdFraudActionLog>LikeFR - finds a list of FrdFraudActionLog> Like with a finder return object
	 * 
	 * @param frdFraudActionLog
	 * @return the list of FrdFraudActionLog found
	 */
	public FinderReturn findByFrdFraudActionLogLikeFR(FrdFraudActionLog frdFraudActionLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
