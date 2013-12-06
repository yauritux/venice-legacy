package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinRolledUpJournalEntry;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinRolledUpJournalEntrySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinRolledUpJournalEntry
	 */
	public List<FinRolledUpJournalEntry> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinRolledUpJournalEntry persists a country
	 * 
	 * @param finRolledUpJournalEntry
	 * @return the persisted FinRolledUpJournalEntry
	 */
	public FinRolledUpJournalEntry persistFinRolledUpJournalEntry(FinRolledUpJournalEntry finRolledUpJournalEntry);

	/**
	 * persistFinRolledUpJournalEntryList - persists a list of FinRolledUpJournalEntry
	 * 
	 * @param finRolledUpJournalEntryList
	 * @return the list of persisted FinRolledUpJournalEntry
	 */
	public ArrayList<FinRolledUpJournalEntry> persistFinRolledUpJournalEntryList(
			List<FinRolledUpJournalEntry> finRolledUpJournalEntryList);

	/**
	 * mergeFinRolledUpJournalEntry - merges a FinRolledUpJournalEntry
	 * 
	 * @param finRolledUpJournalEntry
	 * @return the merged FinRolledUpJournalEntry
	 */
	public FinRolledUpJournalEntry mergeFinRolledUpJournalEntry(FinRolledUpJournalEntry finRolledUpJournalEntry);

	/**
	 * mergeFinRolledUpJournalEntryList - merges a list of FinRolledUpJournalEntry
	 * 
	 * @param finRolledUpJournalEntryList
	 * @return the merged list of FinRolledUpJournalEntry
	 */
	public ArrayList<FinRolledUpJournalEntry> mergeFinRolledUpJournalEntryList(
			List<FinRolledUpJournalEntry> finRolledUpJournalEntryList);

	/**
	 * removeFinRolledUpJournalEntry - removes a FinRolledUpJournalEntry
	 * 
	 * @param finRolledUpJournalEntry
	 */
	public void removeFinRolledUpJournalEntry(FinRolledUpJournalEntry finRolledUpJournalEntry);

	/**
	 * removeFinRolledUpJournalEntryList - removes a list of FinRolledUpJournalEntry
	 * 
	 * @param finRolledUpJournalEntryList
	 */
	public void removeFinRolledUpJournalEntryList(List<FinRolledUpJournalEntry> finRolledUpJournalEntryList);

	/**
	 * findByFinRolledUpJournalEntryLike - finds a list of FinRolledUpJournalEntry Like
	 * 
	 * @param finRolledUpJournalEntry
	 * @return the list of FinRolledUpJournalEntry found
	 */
	public List<FinRolledUpJournalEntry> findByFinRolledUpJournalEntryLike(FinRolledUpJournalEntry finRolledUpJournalEntry,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinRolledUpJournalEntry>LikeFR - finds a list of FinRolledUpJournalEntry> Like with a finder return object
	 * 
	 * @param finRolledUpJournalEntry
	 * @return the list of FinRolledUpJournalEntry found
	 */
	public FinderReturn findByFinRolledUpJournalEntryLikeFR(FinRolledUpJournalEntry finRolledUpJournalEntry,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
