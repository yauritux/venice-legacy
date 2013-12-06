package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenState;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenStateSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenState
	 */
	public List<VenState> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenState persists a country
	 * 
	 * @param venState
	 * @return the persisted VenState
	 */
	public VenState persistVenState(VenState venState);

	/**
	 * persistVenStateList - persists a list of VenState
	 * 
	 * @param venStateList
	 * @return the list of persisted VenState
	 */
	public ArrayList<VenState> persistVenStateList(
			List<VenState> venStateList);

	/**
	 * mergeVenState - merges a VenState
	 * 
	 * @param venState
	 * @return the merged VenState
	 */
	public VenState mergeVenState(VenState venState);

	/**
	 * mergeVenStateList - merges a list of VenState
	 * 
	 * @param venStateList
	 * @return the merged list of VenState
	 */
	public ArrayList<VenState> mergeVenStateList(
			List<VenState> venStateList);

	/**
	 * removeVenState - removes a VenState
	 * 
	 * @param venState
	 */
	public void removeVenState(VenState venState);

	/**
	 * removeVenStateList - removes a list of VenState
	 * 
	 * @param venStateList
	 */
	public void removeVenStateList(List<VenState> venStateList);

	/**
	 * findByVenStateLike - finds a list of VenState Like
	 * 
	 * @param venState
	 * @return the list of VenState found
	 */
	public List<VenState> findByVenStateLike(VenState venState,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenState>LikeFR - finds a list of VenState> Like with a finder return object
	 * 
	 * @param venState
	 * @return the list of VenState found
	 */
	public FinderReturn findByVenStateLikeFR(VenState venState,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
