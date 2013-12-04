package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinJournalTransaction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinJournalTransactionSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinJournalTransaction
	 */
	public List<FinJournalTransaction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinJournalTransaction persists a country
	 * 
	 * @param finJournalTransaction
	 * @return the persisted FinJournalTransaction
	 */
	public FinJournalTransaction persistFinJournalTransaction(FinJournalTransaction finJournalTransaction);

	/**
	 * persistFinJournalTransactionList - persists a list of FinJournalTransaction
	 * 
	 * @param finJournalTransactionList
	 * @return the list of persisted FinJournalTransaction
	 */
	public ArrayList<FinJournalTransaction> persistFinJournalTransactionList(
			List<FinJournalTransaction> finJournalTransactionList);

	/**
	 * mergeFinJournalTransaction - merges a FinJournalTransaction
	 * 
	 * @param finJournalTransaction
	 * @return the merged FinJournalTransaction
	 */
	public FinJournalTransaction mergeFinJournalTransaction(FinJournalTransaction finJournalTransaction);

	/**
	 * mergeFinJournalTransactionList - merges a list of FinJournalTransaction
	 * 
	 * @param finJournalTransactionList
	 * @return the merged list of FinJournalTransaction
	 */
	public ArrayList<FinJournalTransaction> mergeFinJournalTransactionList(
			List<FinJournalTransaction> finJournalTransactionList);

	/**
	 * removeFinJournalTransaction - removes a FinJournalTransaction
	 * 
	 * @param finJournalTransaction
	 */
	public void removeFinJournalTransaction(FinJournalTransaction finJournalTransaction);

	/**
	 * removeFinJournalTransactionList - removes a list of FinJournalTransaction
	 * 
	 * @param finJournalTransactionList
	 */
	public void removeFinJournalTransactionList(List<FinJournalTransaction> finJournalTransactionList);

	/**
	 * findByFinJournalTransactionLike - finds a list of FinJournalTransaction Like
	 * 
	 * @param finJournalTransaction
	 * @return the list of FinJournalTransaction found
	 */
	public List<FinJournalTransaction> findByFinJournalTransactionLike(FinJournalTransaction finJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinJournalTransaction>LikeFR - finds a list of FinJournalTransaction> Like with a finder return object
	 * 
	 * @param finJournalTransaction
	 * @return the list of FinJournalTransaction found
	 */
	public FinderReturn findByFinJournalTransactionLikeFR(FinJournalTransaction finJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
