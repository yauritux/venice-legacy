package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArFundsIdReportTime;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinArFundsIdReportTimeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinArFundsIdReportTime
	 */
	public List<FinArFundsIdReportTime> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinArFundsIdReportTime persists a country
	 * 
	 * @param finArFundsIdReportTime
	 * @return the persisted FinArFundsIdReportTime
	 */
	public FinArFundsIdReportTime persistFinArFundsIdReportTime(FinArFundsIdReportTime finArFundsIdReportTime);

	/**
	 * persistFinArFundsIdReportTimeList - persists a list of FinArFundsIdReportTime
	 * 
	 * @param finArFundsIdReportTimeList
	 * @return the list of persisted FinArFundsIdReportTime
	 */
	public ArrayList<FinArFundsIdReportTime> persistFinArFundsIdReportTimeList(
			List<FinArFundsIdReportTime> finArFundsIdReportTimeList);

	/**
	 * mergeFinArFundsIdReportTime - merges a FinArFundsIdReportTime
	 * 
	 * @param finArFundsIdReportTime
	 * @return the merged FinArFundsIdReportTime
	 */
	public FinArFundsIdReportTime mergeFinArFundsIdReportTime(FinArFundsIdReportTime finArFundsIdReportTime);

	/**
	 * mergeFinArFundsIdReportTimeList - merges a list of FinArFundsIdReportTime
	 * 
	 * @param finArFundsIdReportTimeList
	 * @return the merged list of FinArFundsIdReportTime
	 */
	public ArrayList<FinArFundsIdReportTime> mergeFinArFundsIdReportTimeList(
			List<FinArFundsIdReportTime> finArFundsIdReportTimeList);

	/**
	 * removeFinArFundsIdReportTime - removes a FinArFundsIdReportTime
	 * 
	 * @param finArFundsIdReportTime
	 */
	public void removeFinArFundsIdReportTime(FinArFundsIdReportTime finArFundsIdReportTime);

	/**
	 * removeFinArFundsIdReportTimeList - removes a list of FinArFundsIdReportTime
	 * 
	 * @param finArFundsIdReportTimeList
	 */
	public void removeFinArFundsIdReportTimeList(List<FinArFundsIdReportTime> finArFundsIdReportTimeList);

	/**
	 * findByFinArFundsIdReportTimeLike - finds a list of FinArFundsIdReportTime Like
	 * 
	 * @param finArFundsIdReportTime
	 * @return the list of FinArFundsIdReportTime found
	 */
	public List<FinArFundsIdReportTime> findByFinArFundsIdReportTimeLike(FinArFundsIdReportTime finArFundsIdReportTime,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinArFundsIdReportTime>LikeFR - finds a list of FinArFundsIdReportTime> Like with a finder return object
	 * 
	 * @param finArFundsIdReportTime
	 * @return the list of FinArFundsIdReportTime found
	 */
	public FinderReturn findByFinArFundsIdReportTimeLikeFR(FinArFundsIdReportTime finArFundsIdReportTime,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
