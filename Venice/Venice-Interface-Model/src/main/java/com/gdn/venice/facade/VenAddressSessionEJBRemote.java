package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenAddressSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenAddress
	 */
	public List<VenAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenAddress persists a country
	 * 
	 * @param venAddress
	 * @return the persisted VenAddress
	 */
	public VenAddress persistVenAddress(VenAddress venAddress);

	/**
	 * persistVenAddressList - persists a list of VenAddress
	 * 
	 * @param venAddressList
	 * @return the list of persisted VenAddress
	 */
	public ArrayList<VenAddress> persistVenAddressList(
			List<VenAddress> venAddressList);

	/**
	 * mergeVenAddress - merges a VenAddress
	 * 
	 * @param venAddress
	 * @return the merged VenAddress
	 */
	public VenAddress mergeVenAddress(VenAddress venAddress);

	/**
	 * mergeVenAddressList - merges a list of VenAddress
	 * 
	 * @param venAddressList
	 * @return the merged list of VenAddress
	 */
	public ArrayList<VenAddress> mergeVenAddressList(
			List<VenAddress> venAddressList);

	/**
	 * removeVenAddress - removes a VenAddress
	 * 
	 * @param venAddress
	 */
	public void removeVenAddress(VenAddress venAddress);

	/**
	 * removeVenAddressList - removes a list of VenAddress
	 * 
	 * @param venAddressList
	 */
	public void removeVenAddressList(List<VenAddress> venAddressList);

	/**
	 * findByVenAddressLike - finds a list of VenAddress Like
	 * 
	 * @param venAddress
	 * @return the list of VenAddress found
	 */
	public List<VenAddress> findByVenAddressLike(VenAddress venAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenAddress>LikeFR - finds a list of VenAddress> Like with a finder return object
	 * 
	 * @param venAddress
	 * @return the list of VenAddress found
	 */
	public FinderReturn findByVenAddressLikeFR(VenAddress venAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
