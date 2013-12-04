package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderPayment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderPaymentSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderPayment
	 */
	public List<VenOrderPayment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderPayment persists a country
	 * 
	 * @param venOrderPayment
	 * @return the persisted VenOrderPayment
	 */
	public VenOrderPayment persistVenOrderPayment(VenOrderPayment venOrderPayment);

	/**
	 * persistVenOrderPaymentList - persists a list of VenOrderPayment
	 * 
	 * @param venOrderPaymentList
	 * @return the list of persisted VenOrderPayment
	 */
	public ArrayList<VenOrderPayment> persistVenOrderPaymentList(
			List<VenOrderPayment> venOrderPaymentList);

	/**
	 * mergeVenOrderPayment - merges a VenOrderPayment
	 * 
	 * @param venOrderPayment
	 * @return the merged VenOrderPayment
	 */
	public VenOrderPayment mergeVenOrderPayment(VenOrderPayment venOrderPayment);

	/**
	 * mergeVenOrderPaymentList - merges a list of VenOrderPayment
	 * 
	 * @param venOrderPaymentList
	 * @return the merged list of VenOrderPayment
	 */
	public ArrayList<VenOrderPayment> mergeVenOrderPaymentList(
			List<VenOrderPayment> venOrderPaymentList);

	/**
	 * removeVenOrderPayment - removes a VenOrderPayment
	 * 
	 * @param venOrderPayment
	 */
	public void removeVenOrderPayment(VenOrderPayment venOrderPayment);

	/**
	 * removeVenOrderPaymentList - removes a list of VenOrderPayment
	 * 
	 * @param venOrderPaymentList
	 */
	public void removeVenOrderPaymentList(List<VenOrderPayment> venOrderPaymentList);

	/**
	 * findByVenOrderPaymentLike - finds a list of VenOrderPayment Like
	 * 
	 * @param venOrderPayment
	 * @return the list of VenOrderPayment found
	 */
	public List<VenOrderPayment> findByVenOrderPaymentLike(VenOrderPayment venOrderPayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderPayment>LikeFR - finds a list of VenOrderPayment> Like with a finder return object
	 * 
	 * @param venOrderPayment
	 * @return the list of VenOrderPayment found
	 */
	public FinderReturn findByVenOrderPaymentLikeFR(VenOrderPayment venOrderPayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
