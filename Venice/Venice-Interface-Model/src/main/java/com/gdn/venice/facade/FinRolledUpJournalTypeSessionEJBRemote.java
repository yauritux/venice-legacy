package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinRolledUpJournalType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinRolledUpJournalTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinRolledUpJournalType
	 */
	public List<FinRolledUpJournalType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinRolledUpJournalType persists a country
	 * 
	 * @param finRolledUpJournalType
	 * @return the persisted FinRolledUpJournalType
	 */
	public FinRolledUpJournalType persistFinRolledUpJournalType(FinRolledUpJournalType finRolledUpJournalType);

	/**
	 * persistFinRolledUpJournalTypeList - persists a list of FinRolledUpJournalType
	 * 
	 * @param finRolledUpJournalTypeList
	 * @return the list of persisted FinRolledUpJournalType
	 */
	public ArrayList<FinRolledUpJournalType> persistFinRolledUpJournalTypeList(
			List<FinRolledUpJournalType> finRolledUpJournalTypeList);

	/**
	 * mergeFinRolledUpJournalType - merges a FinRolledUpJournalType
	 * 
	 * @param finRolledUpJournalType
	 * @return the merged FinRolledUpJournalType
	 */
	public FinRolledUpJournalType mergeFinRolledUpJournalType(FinRolledUpJournalType finRolledUpJournalType);

	/**
	 * mergeFinRolledUpJournalTypeList - merges a list of FinRolledUpJournalType
	 * 
	 * @param finRolledUpJournalTypeList
	 * @return the merged list of FinRolledUpJournalType
	 */
	public ArrayList<FinRolledUpJournalType> mergeFinRolledUpJournalTypeList(
			List<FinRolledUpJournalType> finRolledUpJournalTypeList);

	/**
	 * removeFinRolledUpJournalType - removes a FinRolledUpJournalType
	 * 
	 * @param finRolledUpJournalType
	 */
	public void removeFinRolledUpJournalType(FinRolledUpJournalType finRolledUpJournalType);

	/**
	 * removeFinRolledUpJournalTypeList - removes a list of FinRolledUpJournalType
	 * 
	 * @param finRolledUpJournalTypeList
	 */
	public void removeFinRolledUpJournalTypeList(List<FinRolledUpJournalType> finRolledUpJournalTypeList);

	/**
	 * findByFinRolledUpJournalTypeLike - finds a list of FinRolledUpJournalType Like
	 * 
	 * @param finRolledUpJournalType
	 * @return the list of FinRolledUpJournalType found
	 */
	public List<FinRolledUpJournalType> findByFinRolledUpJournalTypeLike(FinRolledUpJournalType finRolledUpJournalType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinRolledUpJournalType>LikeFR - finds a list of FinRolledUpJournalType> Like with a finder return object
	 * 
	 * @param finRolledUpJournalType
	 * @return the list of FinRolledUpJournalType found
	 */
	public FinderReturn findByFinRolledUpJournalTypeLikeFR(FinRolledUpJournalType finRolledUpJournalType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
