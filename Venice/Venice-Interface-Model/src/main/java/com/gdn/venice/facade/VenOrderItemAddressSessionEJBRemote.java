package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderItemAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderItemAddressSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderItemAddress
	 */
	public List<VenOrderItemAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderItemAddress persists a country
	 * 
	 * @param venOrderItemAddress
	 * @return the persisted VenOrderItemAddress
	 */
	public VenOrderItemAddress persistVenOrderItemAddress(VenOrderItemAddress venOrderItemAddress);

	/**
	 * persistVenOrderItemAddressList - persists a list of VenOrderItemAddress
	 * 
	 * @param venOrderItemAddressList
	 * @return the list of persisted VenOrderItemAddress
	 */
	public ArrayList<VenOrderItemAddress> persistVenOrderItemAddressList(
			List<VenOrderItemAddress> venOrderItemAddressList);

	/**
	 * mergeVenOrderItemAddress - merges a VenOrderItemAddress
	 * 
	 * @param venOrderItemAddress
	 * @return the merged VenOrderItemAddress
	 */
	public VenOrderItemAddress mergeVenOrderItemAddress(VenOrderItemAddress venOrderItemAddress);

	/**
	 * mergeVenOrderItemAddressList - merges a list of VenOrderItemAddress
	 * 
	 * @param venOrderItemAddressList
	 * @return the merged list of VenOrderItemAddress
	 */
	public ArrayList<VenOrderItemAddress> mergeVenOrderItemAddressList(
			List<VenOrderItemAddress> venOrderItemAddressList);

	/**
	 * removeVenOrderItemAddress - removes a VenOrderItemAddress
	 * 
	 * @param venOrderItemAddress
	 */
	public void removeVenOrderItemAddress(VenOrderItemAddress venOrderItemAddress);

	/**
	 * removeVenOrderItemAddressList - removes a list of VenOrderItemAddress
	 * 
	 * @param venOrderItemAddressList
	 */
	public void removeVenOrderItemAddressList(List<VenOrderItemAddress> venOrderItemAddressList);

	/**
	 * findByVenOrderItemAddressLike - finds a list of VenOrderItemAddress Like
	 * 
	 * @param venOrderItemAddress
	 * @return the list of VenOrderItemAddress found
	 */
	public List<VenOrderItemAddress> findByVenOrderItemAddressLike(VenOrderItemAddress venOrderItemAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderItemAddress>LikeFR - finds a list of VenOrderItemAddress> Like with a finder return object
	 * 
	 * @param venOrderItemAddress
	 * @return the list of VenOrderItemAddress found
	 */
	public FinderReturn findByVenOrderItemAddressLikeFR(VenOrderItemAddress venOrderItemAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
