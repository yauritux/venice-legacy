package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenProductType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenProductTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenProductType
	 */
	public List<VenProductType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenProductType persists a country
	 * 
	 * @param venProductType
	 * @return the persisted VenProductType
	 */
	public VenProductType persistVenProductType(VenProductType venProductType);

	/**
	 * persistVenProductTypeList - persists a list of VenProductType
	 * 
	 * @param venProductTypeList
	 * @return the list of persisted VenProductType
	 */
	public ArrayList<VenProductType> persistVenProductTypeList(
			List<VenProductType> venProductTypeList);

	/**
	 * mergeVenProductType - merges a VenProductType
	 * 
	 * @param venProductType
	 * @return the merged VenProductType
	 */
	public VenProductType mergeVenProductType(VenProductType venProductType);

	/**
	 * mergeVenProductTypeList - merges a list of VenProductType
	 * 
	 * @param venProductTypeList
	 * @return the merged list of VenProductType
	 */
	public ArrayList<VenProductType> mergeVenProductTypeList(
			List<VenProductType> venProductTypeList);

	/**
	 * removeVenProductType - removes a VenProductType
	 * 
	 * @param venProductType
	 */
	public void removeVenProductType(VenProductType venProductType);

	/**
	 * removeVenProductTypeList - removes a list of VenProductType
	 * 
	 * @param venProductTypeList
	 */
	public void removeVenProductTypeList(List<VenProductType> venProductTypeList);

	/**
	 * findByVenProductTypeLike - finds a list of VenProductType Like
	 * 
	 * @param venProductType
	 * @return the list of VenProductType found
	 */
	public List<VenProductType> findByVenProductTypeLike(VenProductType venProductType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenProductType>LikeFR - finds a list of VenProductType> Like with a finder return object
	 * 
	 * @param venProductType
	 * @return the list of VenProductType found
	 */
	public FinderReturn findByVenProductTypeLikeFR(VenProductType venProductType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
