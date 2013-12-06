package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinRolledUpJournalEntry;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinRolledUpJournalEntrySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalEntrySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinRolledUpJournalEntry> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalEntrySessionEJBRemote#persistFinRolledUpJournalEntry(com
	 * .gdn.venice.persistence.FinRolledUpJournalEntry)
	 */
	public FinRolledUpJournalEntry persistFinRolledUpJournalEntry(FinRolledUpJournalEntry finRolledUpJournalEntry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalEntrySessionEJBRemote#persistFinRolledUpJournalEntryList
	 * (java.util.List)
	 */
	public ArrayList<FinRolledUpJournalEntry> persistFinRolledUpJournalEntryList(
			List<FinRolledUpJournalEntry> finRolledUpJournalEntryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalEntrySessionEJBRemote#mergeFinRolledUpJournalEntry(com.
	 * gdn.venice.persistence.FinRolledUpJournalEntry)
	 */
	public FinRolledUpJournalEntry mergeFinRolledUpJournalEntry(FinRolledUpJournalEntry finRolledUpJournalEntry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalEntrySessionEJBRemote#mergeFinRolledUpJournalEntryList(
	 * java.util.List)
	 */
	public ArrayList<FinRolledUpJournalEntry> mergeFinRolledUpJournalEntryList(
			List<FinRolledUpJournalEntry> finRolledUpJournalEntryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalEntrySessionEJBRemote#removeFinRolledUpJournalEntry(com
	 * .gdn.venice.persistence.FinRolledUpJournalEntry)
	 */
	public void removeFinRolledUpJournalEntry(FinRolledUpJournalEntry finRolledUpJournalEntry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalEntrySessionEJBRemote#removeFinRolledUpJournalEntryList
	 * (java.util.List)
	 */
	public void removeFinRolledUpJournalEntryList(List<FinRolledUpJournalEntry> finRolledUpJournalEntryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalEntrySessionEJBRemote#findByFinRolledUpJournalEntryLike
	 * (com.gdn.venice.persistence.FinRolledUpJournalEntry, int, int)
	 */
	public List<FinRolledUpJournalEntry> findByFinRolledUpJournalEntryLike(FinRolledUpJournalEntry finRolledUpJournalEntry,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalEntrySessionEJBRemote#findByFinRolledUpJournalEntryLikeFR
	 * (com.gdn.venice.persistence.FinRolledUpJournalEntry, int, int)
	 */
	public FinderReturn findByFinRolledUpJournalEntryLikeFR(FinRolledUpJournalEntry finRolledUpJournalEntry,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
