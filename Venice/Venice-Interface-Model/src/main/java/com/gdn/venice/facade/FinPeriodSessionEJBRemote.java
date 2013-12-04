package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinPeriod;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinPeriodSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinPeriod
	 */
	public List<FinPeriod> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinPeriod persists a country
	 * 
	 * @param finPeriod
	 * @return the persisted FinPeriod
	 */
	public FinPeriod persistFinPeriod(FinPeriod finPeriod);

	/**
	 * persistFinPeriodList - persists a list of FinPeriod
	 * 
	 * @param finPeriodList
	 * @return the list of persisted FinPeriod
	 */
	public ArrayList<FinPeriod> persistFinPeriodList(
			List<FinPeriod> finPeriodList);

	/**
	 * mergeFinPeriod - merges a FinPeriod
	 * 
	 * @param finPeriod
	 * @return the merged FinPeriod
	 */
	public FinPeriod mergeFinPeriod(FinPeriod finPeriod);

	/**
	 * mergeFinPeriodList - merges a list of FinPeriod
	 * 
	 * @param finPeriodList
	 * @return the merged list of FinPeriod
	 */
	public ArrayList<FinPeriod> mergeFinPeriodList(
			List<FinPeriod> finPeriodList);

	/**
	 * removeFinPeriod - removes a FinPeriod
	 * 
	 * @param finPeriod
	 */
	public void removeFinPeriod(FinPeriod finPeriod);

	/**
	 * removeFinPeriodList - removes a list of FinPeriod
	 * 
	 * @param finPeriodList
	 */
	public void removeFinPeriodList(List<FinPeriod> finPeriodList);

	/**
	 * findByFinPeriodLike - finds a list of FinPeriod Like
	 * 
	 * @param finPeriod
	 * @return the list of FinPeriod found
	 */
	public List<FinPeriod> findByFinPeriodLike(FinPeriod finPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinPeriod>LikeFR - finds a list of FinPeriod> Like with a finder return object
	 * 
	 * @param finPeriod
	 * @return the list of FinPeriod found
	 */
	public FinderReturn findByFinPeriodLikeFR(FinPeriod finPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
