package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenContactDetailType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenContactDetailTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenContactDetailType
	 */
	public List<VenContactDetailType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenContactDetailType persists a country
	 * 
	 * @param venContactDetailType
	 * @return the persisted VenContactDetailType
	 */
	public VenContactDetailType persistVenContactDetailType(VenContactDetailType venContactDetailType);

	/**
	 * persistVenContactDetailTypeList - persists a list of VenContactDetailType
	 * 
	 * @param venContactDetailTypeList
	 * @return the list of persisted VenContactDetailType
	 */
	public ArrayList<VenContactDetailType> persistVenContactDetailTypeList(
			List<VenContactDetailType> venContactDetailTypeList);

	/**
	 * mergeVenContactDetailType - merges a VenContactDetailType
	 * 
	 * @param venContactDetailType
	 * @return the merged VenContactDetailType
	 */
	public VenContactDetailType mergeVenContactDetailType(VenContactDetailType venContactDetailType);

	/**
	 * mergeVenContactDetailTypeList - merges a list of VenContactDetailType
	 * 
	 * @param venContactDetailTypeList
	 * @return the merged list of VenContactDetailType
	 */
	public ArrayList<VenContactDetailType> mergeVenContactDetailTypeList(
			List<VenContactDetailType> venContactDetailTypeList);

	/**
	 * removeVenContactDetailType - removes a VenContactDetailType
	 * 
	 * @param venContactDetailType
	 */
	public void removeVenContactDetailType(VenContactDetailType venContactDetailType);

	/**
	 * removeVenContactDetailTypeList - removes a list of VenContactDetailType
	 * 
	 * @param venContactDetailTypeList
	 */
	public void removeVenContactDetailTypeList(List<VenContactDetailType> venContactDetailTypeList);

	/**
	 * findByVenContactDetailTypeLike - finds a list of VenContactDetailType Like
	 * 
	 * @param venContactDetailType
	 * @return the list of VenContactDetailType found
	 */
	public List<VenContactDetailType> findByVenContactDetailTypeLike(VenContactDetailType venContactDetailType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenContactDetailType>LikeFR - finds a list of VenContactDetailType> Like with a finder return object
	 * 
	 * @param venContactDetailType
	 * @return the list of VenContactDetailType found
	 */
	public FinderReturn findByVenContactDetailTypeLikeFR(VenContactDetailType venContactDetailType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
