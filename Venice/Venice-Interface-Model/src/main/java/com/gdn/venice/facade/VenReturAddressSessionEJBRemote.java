package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenReturAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenReturAddressSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenReturAddress
	 */
	public List<VenReturAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenReturAddress persists a country
	 * 
	 * @param venReturAddress
	 * @return the persisted VenReturAddress
	 */
	public VenReturAddress persistVenReturAddress(VenReturAddress venReturAddress);

	/**
	 * persistVenReturAddressList - persists a list of VenReturAddress
	 * 
	 * @param venReturAddressList
	 * @return the list of persisted VenReturAddress
	 */
	public ArrayList<VenReturAddress> persistVenReturAddressList(
			List<VenReturAddress> venReturAddressList);

	/**
	 * mergeVenReturAddress - merges a VenReturAddress
	 * 
	 * @param venReturAddress
	 * @return the merged VenReturAddress
	 */
	public VenReturAddress mergeVenReturAddress(VenReturAddress venReturAddress);

	/**
	 * mergeVenReturAddressList - merges a list of VenReturAddress
	 * 
	 * @param venReturAddressList
	 * @return the merged list of VenReturAddress
	 */
	public ArrayList<VenReturAddress> mergeVenReturAddressList(
			List<VenReturAddress> venReturAddressList);

	/**
	 * removeVenReturAddress - removes a VenReturAddress
	 * 
	 * @param venReturAddress
	 */
	public void removeVenReturAddress(VenReturAddress venReturAddress);

	/**
	 * removeVenReturAddressList - removes a list of VenReturAddress
	 * 
	 * @param venReturAddressList
	 */
	public void removeVenReturAddressList(List<VenReturAddress> venReturAddressList);

	/**
	 * findByVenReturAddressLike - finds a list of VenReturAddress Like
	 * 
	 * @param venReturAddress
	 * @return the list of VenReturAddress found
	 */
	public List<VenReturAddress> findByVenReturAddressLike(VenReturAddress venReturAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenReturAddress>LikeFR - finds a list of VenReturAddress> Like with a finder return object
	 * 
	 * @param venReturAddress
	 * @return the list of VenReturAddress found
	 */
	public FinderReturn findByVenReturAddressLikeFR(VenReturAddress venReturAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
