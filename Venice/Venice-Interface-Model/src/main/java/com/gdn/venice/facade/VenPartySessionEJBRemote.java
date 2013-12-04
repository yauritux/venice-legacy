package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenParty;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenPartySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenParty
	 */
	public List<VenParty> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenParty persists a country
	 * 
	 * @param venParty
	 * @return the persisted VenParty
	 */
	public VenParty persistVenParty(VenParty venParty);

	/**
	 * persistVenPartyList - persists a list of VenParty
	 * 
	 * @param venPartyList
	 * @return the list of persisted VenParty
	 */
	public ArrayList<VenParty> persistVenPartyList(
			List<VenParty> venPartyList);

	/**
	 * mergeVenParty - merges a VenParty
	 * 
	 * @param venParty
	 * @return the merged VenParty
	 */
	public VenParty mergeVenParty(VenParty venParty);

	/**
	 * mergeVenPartyList - merges a list of VenParty
	 * 
	 * @param venPartyList
	 * @return the merged list of VenParty
	 */
	public ArrayList<VenParty> mergeVenPartyList(
			List<VenParty> venPartyList);

	/**
	 * removeVenParty - removes a VenParty
	 * 
	 * @param venParty
	 */
	public void removeVenParty(VenParty venParty);

	/**
	 * removeVenPartyList - removes a list of VenParty
	 * 
	 * @param venPartyList
	 */
	public void removeVenPartyList(List<VenParty> venPartyList);

	/**
	 * findByVenPartyLike - finds a list of VenParty Like
	 * 
	 * @param venParty
	 * @return the list of VenParty found
	 */
	public List<VenParty> findByVenPartyLike(VenParty venParty,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenParty>LikeFR - finds a list of VenParty> Like with a finder return object
	 * 
	 * @param venParty
	 * @return the list of VenParty found
	 */
	public FinderReturn findByVenPartyLikeFR(VenParty venParty,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
