package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenMasterChangeType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenMasterChangeTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenMasterChangeType
	 */
	public List<VenMasterChangeType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenMasterChangeType persists a country
	 * 
	 * @param venMasterChangeType
	 * @return the persisted VenMasterChangeType
	 */
	public VenMasterChangeType persistVenMasterChangeType(VenMasterChangeType venMasterChangeType);

	/**
	 * persistVenMasterChangeTypeList - persists a list of VenMasterChangeType
	 * 
	 * @param venMasterChangeTypeList
	 * @return the list of persisted VenMasterChangeType
	 */
	public ArrayList<VenMasterChangeType> persistVenMasterChangeTypeList(
			List<VenMasterChangeType> venMasterChangeTypeList);

	/**
	 * mergeVenMasterChangeType - merges a VenMasterChangeType
	 * 
	 * @param venMasterChangeType
	 * @return the merged VenMasterChangeType
	 */
	public VenMasterChangeType mergeVenMasterChangeType(VenMasterChangeType venMasterChangeType);

	/**
	 * mergeVenMasterChangeTypeList - merges a list of VenMasterChangeType
	 * 
	 * @param venMasterChangeTypeList
	 * @return the merged list of VenMasterChangeType
	 */
	public ArrayList<VenMasterChangeType> mergeVenMasterChangeTypeList(
			List<VenMasterChangeType> venMasterChangeTypeList);

	/**
	 * removeVenMasterChangeType - removes a VenMasterChangeType
	 * 
	 * @param venMasterChangeType
	 */
	public void removeVenMasterChangeType(VenMasterChangeType venMasterChangeType);

	/**
	 * removeVenMasterChangeTypeList - removes a list of VenMasterChangeType
	 * 
	 * @param venMasterChangeTypeList
	 */
	public void removeVenMasterChangeTypeList(List<VenMasterChangeType> venMasterChangeTypeList);

	/**
	 * findByVenMasterChangeTypeLike - finds a list of VenMasterChangeType Like
	 * 
	 * @param venMasterChangeType
	 * @return the list of VenMasterChangeType found
	 */
	public List<VenMasterChangeType> findByVenMasterChangeTypeLike(VenMasterChangeType venMasterChangeType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenMasterChangeType>LikeFR - finds a list of VenMasterChangeType> Like with a finder return object
	 * 
	 * @param venMasterChangeType
	 * @return the list of VenMasterChangeType found
	 */
	public FinderReturn findByVenMasterChangeTypeLikeFR(VenMasterChangeType venMasterChangeType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
