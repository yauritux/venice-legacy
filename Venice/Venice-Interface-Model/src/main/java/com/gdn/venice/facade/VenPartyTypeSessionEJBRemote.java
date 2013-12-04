package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenPartyType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenPartyTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenPartyType
	 */
	public List<VenPartyType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenPartyType persists a country
	 * 
	 * @param venPartyType
	 * @return the persisted VenPartyType
	 */
	public VenPartyType persistVenPartyType(VenPartyType venPartyType);

	/**
	 * persistVenPartyTypeList - persists a list of VenPartyType
	 * 
	 * @param venPartyTypeList
	 * @return the list of persisted VenPartyType
	 */
	public ArrayList<VenPartyType> persistVenPartyTypeList(
			List<VenPartyType> venPartyTypeList);

	/**
	 * mergeVenPartyType - merges a VenPartyType
	 * 
	 * @param venPartyType
	 * @return the merged VenPartyType
	 */
	public VenPartyType mergeVenPartyType(VenPartyType venPartyType);

	/**
	 * mergeVenPartyTypeList - merges a list of VenPartyType
	 * 
	 * @param venPartyTypeList
	 * @return the merged list of VenPartyType
	 */
	public ArrayList<VenPartyType> mergeVenPartyTypeList(
			List<VenPartyType> venPartyTypeList);

	/**
	 * removeVenPartyType - removes a VenPartyType
	 * 
	 * @param venPartyType
	 */
	public void removeVenPartyType(VenPartyType venPartyType);

	/**
	 * removeVenPartyTypeList - removes a list of VenPartyType
	 * 
	 * @param venPartyTypeList
	 */
	public void removeVenPartyTypeList(List<VenPartyType> venPartyTypeList);

	/**
	 * findByVenPartyTypeLike - finds a list of VenPartyType Like
	 * 
	 * @param venPartyType
	 * @return the list of VenPartyType found
	 */
	public List<VenPartyType> findByVenPartyTypeLike(VenPartyType venPartyType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenPartyType>LikeFR - finds a list of VenPartyType> Like with a finder return object
	 * 
	 * @param venPartyType
	 * @return the list of VenPartyType found
	 */
	public FinderReturn findByVenPartyTypeLikeFR(VenPartyType venPartyType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
