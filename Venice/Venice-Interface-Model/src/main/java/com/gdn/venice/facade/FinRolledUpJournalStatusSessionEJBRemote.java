package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinRolledUpJournalStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinRolledUpJournalStatusSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinRolledUpJournalStatus
	 */
	public List<FinRolledUpJournalStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinRolledUpJournalStatus persists a country
	 * 
	 * @param finRolledUpJournalStatus
	 * @return the persisted FinRolledUpJournalStatus
	 */
	public FinRolledUpJournalStatus persistFinRolledUpJournalStatus(FinRolledUpJournalStatus finRolledUpJournalStatus);

	/**
	 * persistFinRolledUpJournalStatusList - persists a list of FinRolledUpJournalStatus
	 * 
	 * @param finRolledUpJournalStatusList
	 * @return the list of persisted FinRolledUpJournalStatus
	 */
	public ArrayList<FinRolledUpJournalStatus> persistFinRolledUpJournalStatusList(
			List<FinRolledUpJournalStatus> finRolledUpJournalStatusList);

	/**
	 * mergeFinRolledUpJournalStatus - merges a FinRolledUpJournalStatus
	 * 
	 * @param finRolledUpJournalStatus
	 * @return the merged FinRolledUpJournalStatus
	 */
	public FinRolledUpJournalStatus mergeFinRolledUpJournalStatus(FinRolledUpJournalStatus finRolledUpJournalStatus);

	/**
	 * mergeFinRolledUpJournalStatusList - merges a list of FinRolledUpJournalStatus
	 * 
	 * @param finRolledUpJournalStatusList
	 * @return the merged list of FinRolledUpJournalStatus
	 */
	public ArrayList<FinRolledUpJournalStatus> mergeFinRolledUpJournalStatusList(
			List<FinRolledUpJournalStatus> finRolledUpJournalStatusList);

	/**
	 * removeFinRolledUpJournalStatus - removes a FinRolledUpJournalStatus
	 * 
	 * @param finRolledUpJournalStatus
	 */
	public void removeFinRolledUpJournalStatus(FinRolledUpJournalStatus finRolledUpJournalStatus);

	/**
	 * removeFinRolledUpJournalStatusList - removes a list of FinRolledUpJournalStatus
	 * 
	 * @param finRolledUpJournalStatusList
	 */
	public void removeFinRolledUpJournalStatusList(List<FinRolledUpJournalStatus> finRolledUpJournalStatusList);

	/**
	 * findByFinRolledUpJournalStatusLike - finds a list of FinRolledUpJournalStatus Like
	 * 
	 * @param finRolledUpJournalStatus
	 * @return the list of FinRolledUpJournalStatus found
	 */
	public List<FinRolledUpJournalStatus> findByFinRolledUpJournalStatusLike(FinRolledUpJournalStatus finRolledUpJournalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinRolledUpJournalStatus>LikeFR - finds a list of FinRolledUpJournalStatus> Like with a finder return object
	 * 
	 * @param finRolledUpJournalStatus
	 * @return the list of FinRolledUpJournalStatus found
	 */
	public FinderReturn findByFinRolledUpJournalStatusLikeFR(FinRolledUpJournalStatus finRolledUpJournalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
