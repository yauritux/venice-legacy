package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenCardType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenCardTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenCardType
	 */
	public List<VenCardType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenCardType persists a country
	 * 
	 * @param venCardType
	 * @return the persisted VenCardType
	 */
	public VenCardType persistVenCardType(VenCardType venCardType);

	/**
	 * persistVenCardTypeList - persists a list of VenCardType
	 * 
	 * @param venCardTypeList
	 * @return the list of persisted VenCardType
	 */
	public ArrayList<VenCardType> persistVenCardTypeList(
			List<VenCardType> venCardTypeList);

	/**
	 * mergeVenCardType - merges a VenCardType
	 * 
	 * @param venCardType
	 * @return the merged VenCardType
	 */
	public VenCardType mergeVenCardType(VenCardType venCardType);

	/**
	 * mergeVenCardTypeList - merges a list of VenCardType
	 * 
	 * @param venCardTypeList
	 * @return the merged list of VenCardType
	 */
	public ArrayList<VenCardType> mergeVenCardTypeList(
			List<VenCardType> venCardTypeList);

	/**
	 * removeVenCardType - removes a VenCardType
	 * 
	 * @param venCardType
	 */
	public void removeVenCardType(VenCardType venCardType);

	/**
	 * removeVenCardTypeList - removes a list of VenCardType
	 * 
	 * @param venCardTypeList
	 */
	public void removeVenCardTypeList(List<VenCardType> venCardTypeList);

	/**
	 * findByVenCardTypeLike - finds a list of VenCardType Like
	 * 
	 * @param venCardType
	 * @return the list of VenCardType found
	 */
	public List<VenCardType> findByVenCardTypeLike(VenCardType venCardType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenCardType>LikeFR - finds a list of VenCardType> Like with a finder return object
	 * 
	 * @param venCardType
	 * @return the list of VenCardType found
	 */
	public FinderReturn findByVenCardTypeLikeFR(VenCardType venCardType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
