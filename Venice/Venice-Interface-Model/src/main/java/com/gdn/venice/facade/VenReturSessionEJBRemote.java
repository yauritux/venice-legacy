package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenRetur;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenReturSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenRetur
	 */
	public List<VenRetur> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenRetur persists a country
	 * 
	 * @param venRetur
	 * @return the persisted VenRetur
	 */
	public VenRetur persistVenRetur(VenRetur venRetur);

	/**
	 * persistVenReturList - persists a list of VenRetur
	 * 
	 * @param venReturList
	 * @return the list of persisted VenRetur
	 */
	public ArrayList<VenRetur> persistVenReturList(
			List<VenRetur> venReturList);

	/**
	 * mergeVenRetur - merges a VenRetur
	 * 
	 * @param venRetur
	 * @return the merged VenRetur
	 */
	public VenRetur mergeVenRetur(VenRetur venRetur);

	/**
	 * mergeVenReturList - merges a list of VenRetur
	 * 
	 * @param venReturList
	 * @return the merged list of VenRetur
	 */
	public ArrayList<VenRetur> mergeVenReturList(
			List<VenRetur> venReturList);

	/**
	 * removeVenRetur - removes a VenRetur
	 * 
	 * @param venRetur
	 */
	public void removeVenRetur(VenRetur venRetur);

	/**
	 * removeVenReturList - removes a list of VenRetur
	 * 
	 * @param venReturList
	 */
	public void removeVenReturList(List<VenRetur> venReturList);

	/**
	 * findByVenReturLike - finds a list of VenRetur Like
	 * 
	 * @param venRetur
	 * @return the list of VenRetur found
	 */
	public List<VenRetur> findByVenReturLike(VenRetur venRetur,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenRetur>LikeFR - finds a list of VenRetur> Like with a finder return object
	 * 
	 * @param venRetur
	 * @return the list of VenRetur found
	 */
	public FinderReturn findByVenReturLikeFR(VenRetur venRetur,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
