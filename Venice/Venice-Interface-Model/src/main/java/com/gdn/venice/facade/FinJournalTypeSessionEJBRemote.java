package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinJournalType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinJournalTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinJournalType
	 */
	public List<FinJournalType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinJournalType persists a country
	 * 
	 * @param finJournalType
	 * @return the persisted FinJournalType
	 */
	public FinJournalType persistFinJournalType(FinJournalType finJournalType);

	/**
	 * persistFinJournalTypeList - persists a list of FinJournalType
	 * 
	 * @param finJournalTypeList
	 * @return the list of persisted FinJournalType
	 */
	public ArrayList<FinJournalType> persistFinJournalTypeList(
			List<FinJournalType> finJournalTypeList);

	/**
	 * mergeFinJournalType - merges a FinJournalType
	 * 
	 * @param finJournalType
	 * @return the merged FinJournalType
	 */
	public FinJournalType mergeFinJournalType(FinJournalType finJournalType);

	/**
	 * mergeFinJournalTypeList - merges a list of FinJournalType
	 * 
	 * @param finJournalTypeList
	 * @return the merged list of FinJournalType
	 */
	public ArrayList<FinJournalType> mergeFinJournalTypeList(
			List<FinJournalType> finJournalTypeList);

	/**
	 * removeFinJournalType - removes a FinJournalType
	 * 
	 * @param finJournalType
	 */
	public void removeFinJournalType(FinJournalType finJournalType);

	/**
	 * removeFinJournalTypeList - removes a list of FinJournalType
	 * 
	 * @param finJournalTypeList
	 */
	public void removeFinJournalTypeList(List<FinJournalType> finJournalTypeList);

	/**
	 * findByFinJournalTypeLike - finds a list of FinJournalType Like
	 * 
	 * @param finJournalType
	 * @return the list of FinJournalType found
	 */
	public List<FinJournalType> findByFinJournalTypeLike(FinJournalType finJournalType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinJournalType>LikeFR - finds a list of FinJournalType> Like with a finder return object
	 * 
	 * @param finJournalType
	 * @return the list of FinJournalType found
	 */
	public FinderReturn findByFinJournalTypeLikeFR(FinJournalType finJournalType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
