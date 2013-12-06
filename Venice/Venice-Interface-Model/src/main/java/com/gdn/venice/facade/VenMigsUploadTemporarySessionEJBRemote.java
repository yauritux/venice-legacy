package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenMigsUploadTemporary;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenMigsUploadTemporarySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenMigsUploadTemporary
	 */
	public List<VenMigsUploadTemporary> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenMigsUploadTemporary persists a country
	 * 
	 * @param venMigsUploadTemporary
	 * @return the persisted VenMigsUploadTemporary
	 */
	public VenMigsUploadTemporary persistVenMigsUploadTemporary(VenMigsUploadTemporary venMigsUploadTemporary);

	/**
	 * persistVenMigsUploadTemporaryList - persists a list of VenMigsUploadTemporary
	 * 
	 * @param venMigsUploadTemporaryList
	 * @return the list of persisted VenMigsUploadTemporary
	 */
	public ArrayList<VenMigsUploadTemporary> persistVenMigsUploadTemporaryList(
			List<VenMigsUploadTemporary> venMigsUploadTemporaryList);

	/**
	 * mergeVenMigsUploadTemporary - merges a VenMigsUploadTemporary
	 * 
	 * @param venMigsUploadTemporary
	 * @return the merged VenMigsUploadTemporary
	 */
	public VenMigsUploadTemporary mergeVenMigsUploadTemporary(VenMigsUploadTemporary venMigsUploadTemporary);

	/**
	 * mergeVenMigsUploadTemporaryList - merges a list of VenMigsUploadTemporary
	 * 
	 * @param venMigsUploadTemporaryList
	 * @return the merged list of VenMigsUploadTemporary
	 */
	public ArrayList<VenMigsUploadTemporary> mergeVenMigsUploadTemporaryList(
			List<VenMigsUploadTemporary> venMigsUploadTemporaryList);

	/**
	 * removeVenMigsUploadTemporary - removes a VenMigsUploadTemporary
	 * 
	 * @param venMigsUploadTemporary
	 */
	public void removeVenMigsUploadTemporary(VenMigsUploadTemporary venMigsUploadTemporary);

	/**
	 * removeVenMigsUploadTemporaryList - removes a list of VenMigsUploadTemporary
	 * 
	 * @param venMigsUploadTemporaryList
	 */
	public void removeVenMigsUploadTemporaryList(List<VenMigsUploadTemporary> venMigsUploadTemporaryList);

	/**
	 * findByVenMigsUploadTemporaryLike - finds a list of VenMigsUploadTemporary Like
	 * 
	 * @param venMigsUploadTemporary
	 * @return the list of VenMigsUploadTemporary found
	 */
	public List<VenMigsUploadTemporary> findByVenMigsUploadTemporaryLike(VenMigsUploadTemporary venMigsUploadTemporary,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenMigsUploadTemporary>LikeFR - finds a list of VenMigsUploadTemporary> Like with a finder return object
	 * 
	 * @param venMigsUploadTemporary
	 * @return the list of VenMigsUploadTemporary found
	 */
	public FinderReturn findByVenMigsUploadTemporaryLikeFR(VenMigsUploadTemporary venMigsUploadTemporary,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
