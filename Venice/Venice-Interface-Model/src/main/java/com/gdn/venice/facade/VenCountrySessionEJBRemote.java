package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenCountry;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenCountrySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenCountry
	 */
	public List<VenCountry> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenCountry persists a country
	 * 
	 * @param venCountry
	 * @return the persisted VenCountry
	 */
	public VenCountry persistVenCountry(VenCountry venCountry);

	/**
	 * persistVenCountryList - persists a list of VenCountry
	 * 
	 * @param venCountryList
	 * @return the list of persisted VenCountry
	 */
	public ArrayList<VenCountry> persistVenCountryList(
			List<VenCountry> venCountryList);

	/**
	 * mergeVenCountry - merges a VenCountry
	 * 
	 * @param venCountry
	 * @return the merged VenCountry
	 */
	public VenCountry mergeVenCountry(VenCountry venCountry);

	/**
	 * mergeVenCountryList - merges a list of VenCountry
	 * 
	 * @param venCountryList
	 * @return the merged list of VenCountry
	 */
	public ArrayList<VenCountry> mergeVenCountryList(
			List<VenCountry> venCountryList);

	/**
	 * removeVenCountry - removes a VenCountry
	 * 
	 * @param venCountry
	 */
	public void removeVenCountry(VenCountry venCountry);

	/**
	 * removeVenCountryList - removes a list of VenCountry
	 * 
	 * @param venCountryList
	 */
	public void removeVenCountryList(List<VenCountry> venCountryList);

	/**
	 * findByVenCountryLike - finds a list of VenCountry Like
	 * 
	 * @param venCountry
	 * @return the list of VenCountry found
	 */
	public List<VenCountry> findByVenCountryLike(VenCountry venCountry,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenCountry>LikeFR - finds a list of VenCountry> Like with a finder return object
	 * 
	 * @param venCountry
	 * @return the list of VenCountry found
	 */
	public FinderReturn findByVenCountryLikeFR(VenCountry venCountry,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
