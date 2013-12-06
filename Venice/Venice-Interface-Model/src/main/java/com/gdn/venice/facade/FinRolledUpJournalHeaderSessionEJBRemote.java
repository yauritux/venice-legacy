package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinRolledUpJournalHeader;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinRolledUpJournalHeaderSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinRolledUpJournalHeader
	 */
	public List<FinRolledUpJournalHeader> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinRolledUpJournalHeader persists a country
	 * 
	 * @param finRolledUpJournalHeader
	 * @return the persisted FinRolledUpJournalHeader
	 */
	public FinRolledUpJournalHeader persistFinRolledUpJournalHeader(FinRolledUpJournalHeader finRolledUpJournalHeader);

	/**
	 * persistFinRolledUpJournalHeaderList - persists a list of FinRolledUpJournalHeader
	 * 
	 * @param finRolledUpJournalHeaderList
	 * @return the list of persisted FinRolledUpJournalHeader
	 */
	public ArrayList<FinRolledUpJournalHeader> persistFinRolledUpJournalHeaderList(
			List<FinRolledUpJournalHeader> finRolledUpJournalHeaderList);

	/**
	 * mergeFinRolledUpJournalHeader - merges a FinRolledUpJournalHeader
	 * 
	 * @param finRolledUpJournalHeader
	 * @return the merged FinRolledUpJournalHeader
	 */
	public FinRolledUpJournalHeader mergeFinRolledUpJournalHeader(FinRolledUpJournalHeader finRolledUpJournalHeader);

	/**
	 * mergeFinRolledUpJournalHeaderList - merges a list of FinRolledUpJournalHeader
	 * 
	 * @param finRolledUpJournalHeaderList
	 * @return the merged list of FinRolledUpJournalHeader
	 */
	public ArrayList<FinRolledUpJournalHeader> mergeFinRolledUpJournalHeaderList(
			List<FinRolledUpJournalHeader> finRolledUpJournalHeaderList);

	/**
	 * removeFinRolledUpJournalHeader - removes a FinRolledUpJournalHeader
	 * 
	 * @param finRolledUpJournalHeader
	 */
	public void removeFinRolledUpJournalHeader(FinRolledUpJournalHeader finRolledUpJournalHeader);

	/**
	 * removeFinRolledUpJournalHeaderList - removes a list of FinRolledUpJournalHeader
	 * 
	 * @param finRolledUpJournalHeaderList
	 */
	public void removeFinRolledUpJournalHeaderList(List<FinRolledUpJournalHeader> finRolledUpJournalHeaderList);

	/**
	 * findByFinRolledUpJournalHeaderLike - finds a list of FinRolledUpJournalHeader Like
	 * 
	 * @param finRolledUpJournalHeader
	 * @return the list of FinRolledUpJournalHeader found
	 */
	public List<FinRolledUpJournalHeader> findByFinRolledUpJournalHeaderLike(FinRolledUpJournalHeader finRolledUpJournalHeader,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinRolledUpJournalHeader>LikeFR - finds a list of FinRolledUpJournalHeader> Like with a finder return object
	 * 
	 * @param finRolledUpJournalHeader
	 * @return the list of FinRolledUpJournalHeader found
	 */
	public FinderReturn findByFinRolledUpJournalHeaderLikeFR(FinRolledUpJournalHeader finRolledUpJournalHeader,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
