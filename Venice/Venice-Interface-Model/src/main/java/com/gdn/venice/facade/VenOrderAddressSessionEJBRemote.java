package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderAddressSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderAddress
	 */
	public List<VenOrderAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderAddress persists a country
	 * 
	 * @param venOrderAddress
	 * @return the persisted VenOrderAddress
	 */
	public VenOrderAddress persistVenOrderAddress(VenOrderAddress venOrderAddress);

	/**
	 * persistVenOrderAddressList - persists a list of VenOrderAddress
	 * 
	 * @param venOrderAddressList
	 * @return the list of persisted VenOrderAddress
	 */
	public ArrayList<VenOrderAddress> persistVenOrderAddressList(
			List<VenOrderAddress> venOrderAddressList);

	/**
	 * mergeVenOrderAddress - merges a VenOrderAddress
	 * 
	 * @param venOrderAddress
	 * @return the merged VenOrderAddress
	 */
	public VenOrderAddress mergeVenOrderAddress(VenOrderAddress venOrderAddress);

	/**
	 * mergeVenOrderAddressList - merges a list of VenOrderAddress
	 * 
	 * @param venOrderAddressList
	 * @return the merged list of VenOrderAddress
	 */
	public ArrayList<VenOrderAddress> mergeVenOrderAddressList(
			List<VenOrderAddress> venOrderAddressList);

	/**
	 * removeVenOrderAddress - removes a VenOrderAddress
	 * 
	 * @param venOrderAddress
	 */
	public void removeVenOrderAddress(VenOrderAddress venOrderAddress);

	/**
	 * removeVenOrderAddressList - removes a list of VenOrderAddress
	 * 
	 * @param venOrderAddressList
	 */
	public void removeVenOrderAddressList(List<VenOrderAddress> venOrderAddressList);

	/**
	 * findByVenOrderAddressLike - finds a list of VenOrderAddress Like
	 * 
	 * @param venOrderAddress
	 * @return the list of VenOrderAddress found
	 */
	public List<VenOrderAddress> findByVenOrderAddressLike(VenOrderAddress venOrderAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderAddress>LikeFR - finds a list of VenOrderAddress> Like with a finder return object
	 * 
	 * @param venOrderAddress
	 * @return the list of VenOrderAddress found
	 */
	public FinderReturn findByVenOrderAddressLikeFR(VenOrderAddress venOrderAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
