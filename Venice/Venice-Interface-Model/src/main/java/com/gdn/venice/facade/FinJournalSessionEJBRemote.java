package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinJournal;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinJournalSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinJournal
	 */
	public List<FinJournal> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinJournal persists a country
	 * 
	 * @param finJournal
	 * @return the persisted FinJournal
	 */
	public FinJournal persistFinJournal(FinJournal finJournal);

	/**
	 * persistFinJournalList - persists a list of FinJournal
	 * 
	 * @param finJournalList
	 * @return the list of persisted FinJournal
	 */
	public ArrayList<FinJournal> persistFinJournalList(
			List<FinJournal> finJournalList);

	/**
	 * mergeFinJournal - merges a FinJournal
	 * 
	 * @param finJournal
	 * @return the merged FinJournal
	 */
	public FinJournal mergeFinJournal(FinJournal finJournal);

	/**
	 * mergeFinJournalList - merges a list of FinJournal
	 * 
	 * @param finJournalList
	 * @return the merged list of FinJournal
	 */
	public ArrayList<FinJournal> mergeFinJournalList(
			List<FinJournal> finJournalList);

	/**
	 * removeFinJournal - removes a FinJournal
	 * 
	 * @param finJournal
	 */
	public void removeFinJournal(FinJournal finJournal);

	/**
	 * removeFinJournalList - removes a list of FinJournal
	 * 
	 * @param finJournalList
	 */
	public void removeFinJournalList(List<FinJournal> finJournalList);

	/**
	 * findByFinJournalLike - finds a list of FinJournal Like
	 * 
	 * @param finJournal
	 * @return the list of FinJournal found
	 */
	public List<FinJournal> findByFinJournalLike(FinJournal finJournal,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinJournal>LikeFR - finds a list of FinJournal> Like with a finder return object
	 * 
	 * @param finJournal
	 * @return the list of FinJournal found
	 */
	public FinderReturn findByFinJournalLikeFR(FinJournal finJournal,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
