package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderBlockingSource;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderBlockingSourceSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderBlockingSource
	 */
	public List<VenOrderBlockingSource> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderBlockingSource persists a country
	 * 
	 * @param venOrderBlockingSource
	 * @return the persisted VenOrderBlockingSource
	 */
	public VenOrderBlockingSource persistVenOrderBlockingSource(VenOrderBlockingSource venOrderBlockingSource);

	/**
	 * persistVenOrderBlockingSourceList - persists a list of VenOrderBlockingSource
	 * 
	 * @param venOrderBlockingSourceList
	 * @return the list of persisted VenOrderBlockingSource
	 */
	public ArrayList<VenOrderBlockingSource> persistVenOrderBlockingSourceList(
			List<VenOrderBlockingSource> venOrderBlockingSourceList);

	/**
	 * mergeVenOrderBlockingSource - merges a VenOrderBlockingSource
	 * 
	 * @param venOrderBlockingSource
	 * @return the merged VenOrderBlockingSource
	 */
	public VenOrderBlockingSource mergeVenOrderBlockingSource(VenOrderBlockingSource venOrderBlockingSource);

	/**
	 * mergeVenOrderBlockingSourceList - merges a list of VenOrderBlockingSource
	 * 
	 * @param venOrderBlockingSourceList
	 * @return the merged list of VenOrderBlockingSource
	 */
	public ArrayList<VenOrderBlockingSource> mergeVenOrderBlockingSourceList(
			List<VenOrderBlockingSource> venOrderBlockingSourceList);

	/**
	 * removeVenOrderBlockingSource - removes a VenOrderBlockingSource
	 * 
	 * @param venOrderBlockingSource
	 */
	public void removeVenOrderBlockingSource(VenOrderBlockingSource venOrderBlockingSource);

	/**
	 * removeVenOrderBlockingSourceList - removes a list of VenOrderBlockingSource
	 * 
	 * @param venOrderBlockingSourceList
	 */
	public void removeVenOrderBlockingSourceList(List<VenOrderBlockingSource> venOrderBlockingSourceList);

	/**
	 * findByVenOrderBlockingSourceLike - finds a list of VenOrderBlockingSource Like
	 * 
	 * @param venOrderBlockingSource
	 * @return the list of VenOrderBlockingSource found
	 */
	public List<VenOrderBlockingSource> findByVenOrderBlockingSourceLike(VenOrderBlockingSource venOrderBlockingSource,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderBlockingSource>LikeFR - finds a list of VenOrderBlockingSource> Like with a finder return object
	 * 
	 * @param venOrderBlockingSource
	 * @return the list of VenOrderBlockingSource found
	 */
	public FinderReturn findByVenOrderBlockingSourceLikeFR(VenOrderBlockingSource venOrderBlockingSource,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
