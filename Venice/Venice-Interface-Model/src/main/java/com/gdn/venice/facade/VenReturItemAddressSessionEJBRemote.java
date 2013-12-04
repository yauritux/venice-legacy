package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenReturItemAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenReturItemAddressSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenReturItemAddress
	 */
	public List<VenReturItemAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenReturItemAddress persists a country
	 * 
	 * @param venReturItemAddress
	 * @return the persisted VenReturItemAddress
	 */
	public VenReturItemAddress persistVenReturItemAddress(VenReturItemAddress venReturItemAddress);

	/**
	 * persistVenReturItemAddressList - persists a list of VenReturItemAddress
	 * 
	 * @param venReturItemAddressList
	 * @return the list of persisted VenReturItemAddress
	 */
	public ArrayList<VenReturItemAddress> persistVenReturItemAddressList(
			List<VenReturItemAddress> venReturItemAddressList);

	/**
	 * mergeVenReturItemAddress - merges a VenReturItemAddress
	 * 
	 * @param venReturItemAddress
	 * @return the merged VenReturItemAddress
	 */
	public VenReturItemAddress mergeVenReturItemAddress(VenReturItemAddress venReturItemAddress);

	/**
	 * mergeVenReturItemAddressList - merges a list of VenReturItemAddress
	 * 
	 * @param venReturItemAddressList
	 * @return the merged list of VenReturItemAddress
	 */
	public ArrayList<VenReturItemAddress> mergeVenReturItemAddressList(
			List<VenReturItemAddress> venReturItemAddressList);

	/**
	 * removeVenReturItemAddress - removes a VenReturItemAddress
	 * 
	 * @param venReturItemAddress
	 */
	public void removeVenReturItemAddress(VenReturItemAddress venReturItemAddress);

	/**
	 * removeVenReturItemAddressList - removes a list of VenReturItemAddress
	 * 
	 * @param venReturItemAddressList
	 */
	public void removeVenReturItemAddressList(List<VenReturItemAddress> venReturItemAddressList);

	/**
	 * findByVenReturItemAddressLike - finds a list of VenReturItemAddress Like
	 * 
	 * @param venReturItemAddress
	 * @return the list of VenReturItemAddress found
	 */
	public List<VenReturItemAddress> findByVenReturItemAddressLike(VenReturItemAddress venReturItemAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenReturItemAddress>LikeFR - finds a list of VenReturItemAddress> Like with a finder return object
	 * 
	 * @param venReturItemAddress
	 * @return the list of VenReturItemAddress found
	 */
	public FinderReturn findByVenReturItemAddressLikeFR(VenReturItemAddress venReturItemAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
