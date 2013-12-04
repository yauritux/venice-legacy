package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinJournalTransaction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinJournalTransactionSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinJournalTransaction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote#persistFinJournalTransaction(com
	 * .gdn.venice.persistence.FinJournalTransaction)
	 */
	public FinJournalTransaction persistFinJournalTransaction(FinJournalTransaction finJournalTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote#persistFinJournalTransactionList
	 * (java.util.List)
	 */
	public ArrayList<FinJournalTransaction> persistFinJournalTransactionList(
			List<FinJournalTransaction> finJournalTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote#mergeFinJournalTransaction(com.
	 * gdn.venice.persistence.FinJournalTransaction)
	 */
	public FinJournalTransaction mergeFinJournalTransaction(FinJournalTransaction finJournalTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote#mergeFinJournalTransactionList(
	 * java.util.List)
	 */
	public ArrayList<FinJournalTransaction> mergeFinJournalTransactionList(
			List<FinJournalTransaction> finJournalTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote#removeFinJournalTransaction(com
	 * .gdn.venice.persistence.FinJournalTransaction)
	 */
	public void removeFinJournalTransaction(FinJournalTransaction finJournalTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote#removeFinJournalTransactionList
	 * (java.util.List)
	 */
	public void removeFinJournalTransactionList(List<FinJournalTransaction> finJournalTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote#findByFinJournalTransactionLike
	 * (com.gdn.venice.persistence.FinJournalTransaction, int, int)
	 */
	public List<FinJournalTransaction> findByFinJournalTransactionLike(FinJournalTransaction finJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote#findByFinJournalTransactionLikeFR
	 * (com.gdn.venice.persistence.FinJournalTransaction, int, int)
	 */
	public FinderReturn findByFinJournalTransactionLikeFR(FinJournalTransaction finJournalTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
