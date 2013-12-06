package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenCity;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenCitySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenCity
	 */
	public List<VenCity> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenCity persists a country
	 * 
	 * @param venCity
	 * @return the persisted VenCity
	 */
	public VenCity persistVenCity(VenCity venCity);

	/**
	 * persistVenCityList - persists a list of VenCity
	 * 
	 * @param venCityList
	 * @return the list of persisted VenCity
	 */
	public ArrayList<VenCity> persistVenCityList(
			List<VenCity> venCityList);

	/**
	 * mergeVenCity - merges a VenCity
	 * 
	 * @param venCity
	 * @return the merged VenCity
	 */
	public VenCity mergeVenCity(VenCity venCity);

	/**
	 * mergeVenCityList - merges a list of VenCity
	 * 
	 * @param venCityList
	 * @return the merged list of VenCity
	 */
	public ArrayList<VenCity> mergeVenCityList(
			List<VenCity> venCityList);

	/**
	 * removeVenCity - removes a VenCity
	 * 
	 * @param venCity
	 */
	public void removeVenCity(VenCity venCity);

	/**
	 * removeVenCityList - removes a list of VenCity
	 * 
	 * @param venCityList
	 */
	public void removeVenCityList(List<VenCity> venCityList);

	/**
	 * findByVenCityLike - finds a list of VenCity Like
	 * 
	 * @param venCity
	 * @return the list of VenCity found
	 */
	public List<VenCity> findByVenCityLike(VenCity venCity,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenCity>LikeFR - finds a list of VenCity> Like with a finder return object
	 * 
	 * @param venCity
	 * @return the list of VenCity found
	 */
	public FinderReturn findByVenCityLikeFR(VenCity venCity,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
