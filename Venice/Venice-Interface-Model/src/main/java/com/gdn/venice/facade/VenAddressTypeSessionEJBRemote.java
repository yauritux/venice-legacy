package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenAddressType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenAddressTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenAddressType
	 */
	public List<VenAddressType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenAddressType persists a country
	 * 
	 * @param venAddressType
	 * @return the persisted VenAddressType
	 */
	public VenAddressType persistVenAddressType(VenAddressType venAddressType);

	/**
	 * persistVenAddressTypeList - persists a list of VenAddressType
	 * 
	 * @param venAddressTypeList
	 * @return the list of persisted VenAddressType
	 */
	public ArrayList<VenAddressType> persistVenAddressTypeList(
			List<VenAddressType> venAddressTypeList);

	/**
	 * mergeVenAddressType - merges a VenAddressType
	 * 
	 * @param venAddressType
	 * @return the merged VenAddressType
	 */
	public VenAddressType mergeVenAddressType(VenAddressType venAddressType);

	/**
	 * mergeVenAddressTypeList - merges a list of VenAddressType
	 * 
	 * @param venAddressTypeList
	 * @return the merged list of VenAddressType
	 */
	public ArrayList<VenAddressType> mergeVenAddressTypeList(
			List<VenAddressType> venAddressTypeList);

	/**
	 * removeVenAddressType - removes a VenAddressType
	 * 
	 * @param venAddressType
	 */
	public void removeVenAddressType(VenAddressType venAddressType);

	/**
	 * removeVenAddressTypeList - removes a list of VenAddressType
	 * 
	 * @param venAddressTypeList
	 */
	public void removeVenAddressTypeList(List<VenAddressType> venAddressTypeList);

	/**
	 * findByVenAddressTypeLike - finds a list of VenAddressType Like
	 * 
	 * @param venAddressType
	 * @return the list of VenAddressType found
	 */
	public List<VenAddressType> findByVenAddressTypeLike(VenAddressType venAddressType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenAddressType>LikeFR - finds a list of VenAddressType> Like with a finder return object
	 * 
	 * @param venAddressType
	 * @return the list of VenAddressType found
	 */
	public FinderReturn findByVenAddressTypeLikeFR(VenAddressType venAddressType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
