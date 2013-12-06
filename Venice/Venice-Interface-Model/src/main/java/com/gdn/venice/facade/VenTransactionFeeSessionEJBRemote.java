package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenTransactionFee;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenTransactionFeeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenTransactionFee
	 */
	public List<VenTransactionFee> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenTransactionFee persists a country
	 * 
	 * @param venTransactionFee
	 * @return the persisted VenTransactionFee
	 */
	public VenTransactionFee persistVenTransactionFee(VenTransactionFee venTransactionFee);

	/**
	 * persistVenTransactionFeeList - persists a list of VenTransactionFee
	 * 
	 * @param venTransactionFeeList
	 * @return the list of persisted VenTransactionFee
	 */
	public ArrayList<VenTransactionFee> persistVenTransactionFeeList(
			List<VenTransactionFee> venTransactionFeeList);

	/**
	 * mergeVenTransactionFee - merges a VenTransactionFee
	 * 
	 * @param venTransactionFee
	 * @return the merged VenTransactionFee
	 */
	public VenTransactionFee mergeVenTransactionFee(VenTransactionFee venTransactionFee);

	/**
	 * mergeVenTransactionFeeList - merges a list of VenTransactionFee
	 * 
	 * @param venTransactionFeeList
	 * @return the merged list of VenTransactionFee
	 */
	public ArrayList<VenTransactionFee> mergeVenTransactionFeeList(
			List<VenTransactionFee> venTransactionFeeList);

	/**
	 * removeVenTransactionFee - removes a VenTransactionFee
	 * 
	 * @param venTransactionFee
	 */
	public void removeVenTransactionFee(VenTransactionFee venTransactionFee);

	/**
	 * removeVenTransactionFeeList - removes a list of VenTransactionFee
	 * 
	 * @param venTransactionFeeList
	 */
	public void removeVenTransactionFeeList(List<VenTransactionFee> venTransactionFeeList);

	/**
	 * findByVenTransactionFeeLike - finds a list of VenTransactionFee Like
	 * 
	 * @param venTransactionFee
	 * @return the list of VenTransactionFee found
	 */
	public List<VenTransactionFee> findByVenTransactionFeeLike(VenTransactionFee venTransactionFee,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenTransactionFee>LikeFR - finds a list of VenTransactionFee> Like with a finder return object
	 * 
	 * @param venTransactionFee
	 * @return the list of VenTransactionFee found
	 */
	public FinderReturn findByVenTransactionFeeLikeFR(VenTransactionFee venTransactionFee,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
