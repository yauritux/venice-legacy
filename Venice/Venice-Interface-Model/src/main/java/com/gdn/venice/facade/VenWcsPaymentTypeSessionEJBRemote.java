package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenWcsPaymentType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenWcsPaymentTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenWcsPaymentType
	 */
	public List<VenWcsPaymentType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenWcsPaymentType persists a country
	 * 
	 * @param venWcsPaymentType
	 * @return the persisted VenWcsPaymentType
	 */
	public VenWcsPaymentType persistVenWcsPaymentType(VenWcsPaymentType venWcsPaymentType);

	/**
	 * persistVenWcsPaymentTypeList - persists a list of VenWcsPaymentType
	 * 
	 * @param venWcsPaymentTypeList
	 * @return the list of persisted VenWcsPaymentType
	 */
	public ArrayList<VenWcsPaymentType> persistVenWcsPaymentTypeList(
			List<VenWcsPaymentType> venWcsPaymentTypeList);

	/**
	 * mergeVenWcsPaymentType - merges a VenWcsPaymentType
	 * 
	 * @param venWcsPaymentType
	 * @return the merged VenWcsPaymentType
	 */
	public VenWcsPaymentType mergeVenWcsPaymentType(VenWcsPaymentType venWcsPaymentType);

	/**
	 * mergeVenWcsPaymentTypeList - merges a list of VenWcsPaymentType
	 * 
	 * @param venWcsPaymentTypeList
	 * @return the merged list of VenWcsPaymentType
	 */
	public ArrayList<VenWcsPaymentType> mergeVenWcsPaymentTypeList(
			List<VenWcsPaymentType> venWcsPaymentTypeList);

	/**
	 * removeVenWcsPaymentType - removes a VenWcsPaymentType
	 * 
	 * @param venWcsPaymentType
	 */
	public void removeVenWcsPaymentType(VenWcsPaymentType venWcsPaymentType);

	/**
	 * removeVenWcsPaymentTypeList - removes a list of VenWcsPaymentType
	 * 
	 * @param venWcsPaymentTypeList
	 */
	public void removeVenWcsPaymentTypeList(List<VenWcsPaymentType> venWcsPaymentTypeList);

	/**
	 * findByVenWcsPaymentTypeLike - finds a list of VenWcsPaymentType Like
	 * 
	 * @param venWcsPaymentType
	 * @return the list of VenWcsPaymentType found
	 */
	public List<VenWcsPaymentType> findByVenWcsPaymentTypeLike(VenWcsPaymentType venWcsPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenWcsPaymentType>LikeFR - finds a list of VenWcsPaymentType> Like with a finder return object
	 * 
	 * @param venWcsPaymentType
	 * @return the list of VenWcsPaymentType found
	 */
	public FinderReturn findByVenWcsPaymentTypeLikeFR(VenWcsPaymentType venWcsPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
