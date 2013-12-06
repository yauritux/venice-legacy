package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinApManualJournalTransaction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinApManualJournalTransactionSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinApManualJournalTransaction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote#persistFinApManualJournalTransaction(com
	 * .gdn.venice.persistence.FinApManualJournalTransaction)
	 */
	public FinApManualJournalTransaction persistFinApManualJournalTransaction(FinApManualJournalTransaction finApManualJournalTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote#persistFinApManualJournalTransactionList
	 * (java.util.List)
	 */
	public ArrayList<FinApManualJournalTransaction> persistFinApManualJournalTransactionList(
			List<FinApManualJournalTransaction> finApManualJournalTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote#mergeFinApManualJournalTransaction(com.
	 * gdn.venice.persistence.FinApManualJournalTransaction)
	 */
	public FinApManualJournalTransaction mergeFinApManualJournalTransaction(FinApManualJournalTransaction finApManualJournalTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote#mergeFinApManualJournalTransactionList(
	 * java.util.List)
	 */
	public ArrayList<FinApManualJournalTransaction> mergeFinApManualJournalTransactionList(
			List<FinApManualJournalTransaction> finApManualJournalTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote#removeFinApManualJournalTransaction(com
	 * .gdn.venice.persistence.FinApManualJournalTransaction)
	 */
	public void removeFinApManualJournalTransaction(FinApManualJournalTransaction finApManualJournalTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote#removeFinApManualJournalTransactionList
	 * (java.util.List)
	 */
	public void removeFinApManualJournalTransactionList(List<FinApManualJournalTransaction> finApManualJournalTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote#findByFinApManualJournalTransactionLike
	 * (com.gdn.venice.persistence.FinApManualJournalTransaction, int, int)
	 */
	public List<FinApManualJournalTransaction> findByFinApManualJournalTransactionLike(FinApManualJournalTransaction finApManualJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote#findByFinApManualJournalTransactionLikeFR
	 * (com.gdn.venice.persistence.FinApManualJournalTransaction, int, int)
	 */
	public FinderReturn findByFinApManualJournalTransactionLikeFR(FinApManualJournalTransaction finApManualJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
