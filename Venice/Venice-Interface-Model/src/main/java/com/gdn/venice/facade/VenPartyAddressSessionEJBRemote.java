package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenPartyAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenPartyAddressSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenPartyAddress
	 */
	public List<VenPartyAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenPartyAddress persists a country
	 * 
	 * @param venPartyAddress
	 * @return the persisted VenPartyAddress
	 */
	public VenPartyAddress persistVenPartyAddress(VenPartyAddress venPartyAddress);

	/**
	 * persistVenPartyAddressList - persists a list of VenPartyAddress
	 * 
	 * @param venPartyAddressList
	 * @return the list of persisted VenPartyAddress
	 */
	public ArrayList<VenPartyAddress> persistVenPartyAddressList(
			List<VenPartyAddress> venPartyAddressList);

	/**
	 * mergeVenPartyAddress - merges a VenPartyAddress
	 * 
	 * @param venPartyAddress
	 * @return the merged VenPartyAddress
	 */
	public VenPartyAddress mergeVenPartyAddress(VenPartyAddress venPartyAddress);

	/**
	 * mergeVenPartyAddressList - merges a list of VenPartyAddress
	 * 
	 * @param venPartyAddressList
	 * @return the merged list of VenPartyAddress
	 */
	public ArrayList<VenPartyAddress> mergeVenPartyAddressList(
			List<VenPartyAddress> venPartyAddressList);

	/**
	 * removeVenPartyAddress - removes a VenPartyAddress
	 * 
	 * @param venPartyAddress
	 */
	public void removeVenPartyAddress(VenPartyAddress venPartyAddress);

	/**
	 * removeVenPartyAddressList - removes a list of VenPartyAddress
	 * 
	 * @param venPartyAddressList
	 */
	public void removeVenPartyAddressList(List<VenPartyAddress> venPartyAddressList);

	/**
	 * findByVenPartyAddressLike - finds a list of VenPartyAddress Like
	 * 
	 * @param venPartyAddress
	 * @return the list of VenPartyAddress found
	 */
	public List<VenPartyAddress> findByVenPartyAddressLike(VenPartyAddress venPartyAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenPartyAddress>LikeFR - finds a list of VenPartyAddress> Like with a finder return object
	 * 
	 * @param venPartyAddress
	 * @return the list of VenPartyAddress found
	 */
	public FinderReturn findByVenPartyAddressLikeFR(VenPartyAddress venPartyAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
