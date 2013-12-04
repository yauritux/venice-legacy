package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinApManualJournalTransaction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinApManualJournalTransactionSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinApManualJournalTransaction
	 */
	public List<FinApManualJournalTransaction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinApManualJournalTransaction persists a country
	 * 
	 * @param finApManualJournalTransaction
	 * @return the persisted FinApManualJournalTransaction
	 */
	public FinApManualJournalTransaction persistFinApManualJournalTransaction(FinApManualJournalTransaction finApManualJournalTransaction);

	/**
	 * persistFinApManualJournalTransactionList - persists a list of FinApManualJournalTransaction
	 * 
	 * @param finApManualJournalTransactionList
	 * @return the list of persisted FinApManualJournalTransaction
	 */
	public ArrayList<FinApManualJournalTransaction> persistFinApManualJournalTransactionList(
			List<FinApManualJournalTransaction> finApManualJournalTransactionList);

	/**
	 * mergeFinApManualJournalTransaction - merges a FinApManualJournalTransaction
	 * 
	 * @param finApManualJournalTransaction
	 * @return the merged FinApManualJournalTransaction
	 */
	public FinApManualJournalTransaction mergeFinApManualJournalTransaction(FinApManualJournalTransaction finApManualJournalTransaction);

	/**
	 * mergeFinApManualJournalTransactionList - merges a list of FinApManualJournalTransaction
	 * 
	 * @param finApManualJournalTransactionList
	 * @return the merged list of FinApManualJournalTransaction
	 */
	public ArrayList<FinApManualJournalTransaction> mergeFinApManualJournalTransactionList(
			List<FinApManualJournalTransaction> finApManualJournalTransactionList);

	/**
	 * removeFinApManualJournalTransaction - removes a FinApManualJournalTransaction
	 * 
	 * @param finApManualJournalTransaction
	 */
	public void removeFinApManualJournalTransaction(FinApManualJournalTransaction finApManualJournalTransaction);

	/**
	 * removeFinApManualJournalTransactionList - removes a list of FinApManualJournalTransaction
	 * 
	 * @param finApManualJournalTransactionList
	 */
	public void removeFinApManualJournalTransactionList(List<FinApManualJournalTransaction> finApManualJournalTransactionList);

	/**
	 * findByFinApManualJournalTransactionLike - finds a list of FinApManualJournalTransaction Like
	 * 
	 * @param finApManualJournalTransaction
	 * @return the list of FinApManualJournalTransaction found
	 */
	public List<FinApManualJournalTransaction> findByFinApManualJournalTransactionLike(FinApManualJournalTransaction finApManualJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinApManualJournalTransaction>LikeFR - finds a list of FinApManualJournalTransaction> Like with a finder return object
	 * 
	 * @param finApManualJournalTransaction
	 * @return the list of FinApManualJournalTransaction found
	 */
	public FinderReturn findByFinApManualJournalTransactionLikeFR(FinApManualJournalTransaction finApManualJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
