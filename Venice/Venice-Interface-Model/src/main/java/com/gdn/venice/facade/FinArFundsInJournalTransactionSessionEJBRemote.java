package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArFundsInJournalTransaction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinArFundsInJournalTransactionSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinArFundsInJournalTransaction
	 */
	public List<FinArFundsInJournalTransaction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinArFundsInJournalTransaction persists a country
	 * 
	 * @param finArFundsInJournalTransaction
	 * @return the persisted FinArFundsInJournalTransaction
	 */
	public FinArFundsInJournalTransaction persistFinArFundsInJournalTransaction(FinArFundsInJournalTransaction finArFundsInJournalTransaction);

	/**
	 * persistFinArFundsInJournalTransactionList - persists a list of FinArFundsInJournalTransaction
	 * 
	 * @param finArFundsInJournalTransactionList
	 * @return the list of persisted FinArFundsInJournalTransaction
	 */
	public ArrayList<FinArFundsInJournalTransaction> persistFinArFundsInJournalTransactionList(
			List<FinArFundsInJournalTransaction> finArFundsInJournalTransactionList);

	/**
	 * mergeFinArFundsInJournalTransaction - merges a FinArFundsInJournalTransaction
	 * 
	 * @param finArFundsInJournalTransaction
	 * @return the merged FinArFundsInJournalTransaction
	 */
	public FinArFundsInJournalTransaction mergeFinArFundsInJournalTransaction(FinArFundsInJournalTransaction finArFundsInJournalTransaction);

	/**
	 * mergeFinArFundsInJournalTransactionList - merges a list of FinArFundsInJournalTransaction
	 * 
	 * @param finArFundsInJournalTransactionList
	 * @return the merged list of FinArFundsInJournalTransaction
	 */
	public ArrayList<FinArFundsInJournalTransaction> mergeFinArFundsInJournalTransactionList(
			List<FinArFundsInJournalTransaction> finArFundsInJournalTransactionList);

	/**
	 * removeFinArFundsInJournalTransaction - removes a FinArFundsInJournalTransaction
	 * 
	 * @param finArFundsInJournalTransaction
	 */
	public void removeFinArFundsInJournalTransaction(FinArFundsInJournalTransaction finArFundsInJournalTransaction);

	/**
	 * removeFinArFundsInJournalTransactionList - removes a list of FinArFundsInJournalTransaction
	 * 
	 * @param finArFundsInJournalTransactionList
	 */
	public void removeFinArFundsInJournalTransactionList(List<FinArFundsInJournalTransaction> finArFundsInJournalTransactionList);

	/**
	 * findByFinArFundsInJournalTransactionLike - finds a list of FinArFundsInJournalTransaction Like
	 * 
	 * @param finArFundsInJournalTransaction
	 * @return the list of FinArFundsInJournalTransaction found
	 */
	public List<FinArFundsInJournalTransaction> findByFinArFundsInJournalTransactionLike(FinArFundsInJournalTransaction finArFundsInJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinArFundsInJournalTransaction>LikeFR - finds a list of FinArFundsInJournalTransaction> Like with a finder return object
	 * 
	 * @param finArFundsInJournalTransaction
	 * @return the list of FinArFundsInJournalTransaction found
	 */
	public FinderReturn findByFinArFundsInJournalTransactionLikeFR(FinArFundsInJournalTransaction finArFundsInJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
