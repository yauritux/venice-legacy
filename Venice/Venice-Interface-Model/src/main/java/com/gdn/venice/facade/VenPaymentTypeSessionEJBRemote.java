package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenPaymentType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenPaymentTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenPaymentType
	 */
	public List<VenPaymentType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenPaymentType persists a country
	 * 
	 * @param venPaymentType
	 * @return the persisted VenPaymentType
	 */
	public VenPaymentType persistVenPaymentType(VenPaymentType venPaymentType);

	/**
	 * persistVenPaymentTypeList - persists a list of VenPaymentType
	 * 
	 * @param venPaymentTypeList
	 * @return the list of persisted VenPaymentType
	 */
	public ArrayList<VenPaymentType> persistVenPaymentTypeList(
			List<VenPaymentType> venPaymentTypeList);

	/**
	 * mergeVenPaymentType - merges a VenPaymentType
	 * 
	 * @param venPaymentType
	 * @return the merged VenPaymentType
	 */
	public VenPaymentType mergeVenPaymentType(VenPaymentType venPaymentType);

	/**
	 * mergeVenPaymentTypeList - merges a list of VenPaymentType
	 * 
	 * @param venPaymentTypeList
	 * @return the merged list of VenPaymentType
	 */
	public ArrayList<VenPaymentType> mergeVenPaymentTypeList(
			List<VenPaymentType> venPaymentTypeList);

	/**
	 * removeVenPaymentType - removes a VenPaymentType
	 * 
	 * @param venPaymentType
	 */
	public void removeVenPaymentType(VenPaymentType venPaymentType);

	/**
	 * removeVenPaymentTypeList - removes a list of VenPaymentType
	 * 
	 * @param venPaymentTypeList
	 */
	public void removeVenPaymentTypeList(List<VenPaymentType> venPaymentTypeList);

	/**
	 * findByVenPaymentTypeLike - finds a list of VenPaymentType Like
	 * 
	 * @param venPaymentType
	 * @return the list of VenPaymentType found
	 */
	public List<VenPaymentType> findByVenPaymentTypeLike(VenPaymentType venPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenPaymentType>LikeFR - finds a list of VenPaymentType> Like with a finder return object
	 * 
	 * @param venPaymentType
	 * @return the list of VenPaymentType found
	 */
	public FinderReturn findByVenPaymentTypeLikeFR(VenPaymentType venPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
