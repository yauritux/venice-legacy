package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinArFundsInJournalTransaction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinArFundsInJournalTransactionSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInJournalTransactionSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinArFundsInJournalTransaction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInJournalTransactionSessionEJBRemote#persistFinArFundsInJournalTransaction(com
	 * .gdn.venice.persistence.FinArFundsInJournalTransaction)
	 */
	public FinArFundsInJournalTransaction persistFinArFundsInJournalTransaction(FinArFundsInJournalTransaction finArFundsInJournalTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInJournalTransactionSessionEJBRemote#persistFinArFundsInJournalTransactionList
	 * (java.util.List)
	 */
	public ArrayList<FinArFundsInJournalTransaction> persistFinArFundsInJournalTransactionList(
			List<FinArFundsInJournalTransaction> finArFundsInJournalTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInJournalTransactionSessionEJBRemote#mergeFinArFundsInJournalTransaction(com.
	 * gdn.venice.persistence.FinArFundsInJournalTransaction)
	 */
	public FinArFundsInJournalTransaction mergeFinArFundsInJournalTransaction(FinArFundsInJournalTransaction finArFundsInJournalTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInJournalTransactionSessionEJBRemote#mergeFinArFundsInJournalTransactionList(
	 * java.util.List)
	 */
	public ArrayList<FinArFundsInJournalTransaction> mergeFinArFundsInJournalTransactionList(
			List<FinArFundsInJournalTransaction> finArFundsInJournalTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInJournalTransactionSessionEJBRemote#removeFinArFundsInJournalTransaction(com
	 * .gdn.venice.persistence.FinArFundsInJournalTransaction)
	 */
	public void removeFinArFundsInJournalTransaction(FinArFundsInJournalTransaction finArFundsInJournalTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInJournalTransactionSessionEJBRemote#removeFinArFundsInJournalTransactionList
	 * (java.util.List)
	 */
	public void removeFinArFundsInJournalTransactionList(List<FinArFundsInJournalTransaction> finArFundsInJournalTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInJournalTransactionSessionEJBRemote#findByFinArFundsInJournalTransactionLike
	 * (com.gdn.venice.persistence.FinArFundsInJournalTransaction, int, int)
	 */
	public List<FinArFundsInJournalTransaction> findByFinArFundsInJournalTransactionLike(FinArFundsInJournalTransaction finArFundsInJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInJournalTransactionSessionEJBRemote#findByFinArFundsInJournalTransactionLikeFR
	 * (com.gdn.venice.persistence.FinArFundsInJournalTransaction, int, int)
	 */
	public FinderReturn findByFinArFundsInJournalTransactionLikeFR(FinArFundsInJournalTransaction finArFundsInJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
