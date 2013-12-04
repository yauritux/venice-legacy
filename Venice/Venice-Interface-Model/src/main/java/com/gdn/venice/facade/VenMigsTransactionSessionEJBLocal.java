package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenMigsTransaction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenMigsTransactionSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsTransactionSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenMigsTransaction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsTransactionSessionEJBRemote#persistVenMigsTransaction(com
	 * .gdn.venice.persistence.VenMigsTransaction)
	 */
	public VenMigsTransaction persistVenMigsTransaction(VenMigsTransaction venMigsTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsTransactionSessionEJBRemote#persistVenMigsTransactionList
	 * (java.util.List)
	 */
	public ArrayList<VenMigsTransaction> persistVenMigsTransactionList(
			List<VenMigsTransaction> venMigsTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsTransactionSessionEJBRemote#mergeVenMigsTransaction(com.
	 * gdn.venice.persistence.VenMigsTransaction)
	 */
	public VenMigsTransaction mergeVenMigsTransaction(VenMigsTransaction venMigsTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsTransactionSessionEJBRemote#mergeVenMigsTransactionList(
	 * java.util.List)
	 */
	public ArrayList<VenMigsTransaction> mergeVenMigsTransactionList(
			List<VenMigsTransaction> venMigsTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsTransactionSessionEJBRemote#removeVenMigsTransaction(com
	 * .gdn.venice.persistence.VenMigsTransaction)
	 */
	public void removeVenMigsTransaction(VenMigsTransaction venMigsTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsTransactionSessionEJBRemote#removeVenMigsTransactionList
	 * (java.util.List)
	 */
	public void removeVenMigsTransactionList(List<VenMigsTransaction> venMigsTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsTransactionSessionEJBRemote#findByVenMigsTransactionLike
	 * (com.gdn.venice.persistence.VenMigsTransaction, int, int)
	 */
	public List<VenMigsTransaction> findByVenMigsTransactionLike(VenMigsTransaction venMigsTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsTransactionSessionEJBRemote#findByVenMigsTransactionLikeFR
	 * (com.gdn.venice.persistence.VenMigsTransaction, int, int)
	 */
	public FinderReturn findByVenMigsTransactionLikeFR(VenMigsTransaction venMigsTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
