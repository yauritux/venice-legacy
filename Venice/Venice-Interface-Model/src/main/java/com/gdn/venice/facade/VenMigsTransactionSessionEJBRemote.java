package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenMigsTransaction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenMigsTransactionSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenMigsTransaction
	 */
	public List<VenMigsTransaction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenMigsTransaction persists a country
	 * 
	 * @param venMigsTransaction
	 * @return the persisted VenMigsTransaction
	 */
	public VenMigsTransaction persistVenMigsTransaction(VenMigsTransaction venMigsTransaction);

	/**
	 * persistVenMigsTransactionList - persists a list of VenMigsTransaction
	 * 
	 * @param venMigsTransactionList
	 * @return the list of persisted VenMigsTransaction
	 */
	public ArrayList<VenMigsTransaction> persistVenMigsTransactionList(
			List<VenMigsTransaction> venMigsTransactionList);

	/**
	 * mergeVenMigsTransaction - merges a VenMigsTransaction
	 * 
	 * @param venMigsTransaction
	 * @return the merged VenMigsTransaction
	 */
	public VenMigsTransaction mergeVenMigsTransaction(VenMigsTransaction venMigsTransaction);

	/**
	 * mergeVenMigsTransactionList - merges a list of VenMigsTransaction
	 * 
	 * @param venMigsTransactionList
	 * @return the merged list of VenMigsTransaction
	 */
	public ArrayList<VenMigsTransaction> mergeVenMigsTransactionList(
			List<VenMigsTransaction> venMigsTransactionList);

	/**
	 * removeVenMigsTransaction - removes a VenMigsTransaction
	 * 
	 * @param venMigsTransaction
	 */
	public void removeVenMigsTransaction(VenMigsTransaction venMigsTransaction);

	/**
	 * removeVenMigsTransactionList - removes a list of VenMigsTransaction
	 * 
	 * @param venMigsTransactionList
	 */
	public void removeVenMigsTransactionList(List<VenMigsTransaction> venMigsTransactionList);

	/**
	 * findByVenMigsTransactionLike - finds a list of VenMigsTransaction Like
	 * 
	 * @param venMigsTransaction
	 * @return the list of VenMigsTransaction found
	 */
	public List<VenMigsTransaction> findByVenMigsTransactionLike(VenMigsTransaction venMigsTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenMigsTransaction>LikeFR - finds a list of VenMigsTransaction> Like with a finder return object
	 * 
	 * @param venMigsTransaction
	 * @return the list of VenMigsTransaction found
	 */
	public FinderReturn findByVenMigsTransactionLikeFR(VenMigsTransaction venMigsTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
