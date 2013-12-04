package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenMigsUploadMaster;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenMigsUploadMasterSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenMigsUploadMaster
	 */
	public List<VenMigsUploadMaster> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenMigsUploadMaster persists a country
	 * 
	 * @param VenMigsUploadMaster
	 * @return the persisted VenMigsUploadMaster
	 */
	public VenMigsUploadMaster persistVenMigsUploadMaster(VenMigsUploadMaster VenMigsUploadMaster);

	/**
	 * persistVenMigsUploadMasterList - persists a list of VenMigsUploadMaster
	 * 
	 * @param VenMigsUploadMasterList
	 * @return the list of persisted VenMigsUploadMaster
	 */
	public ArrayList<VenMigsUploadMaster> persistVenMigsUploadMasterList(
			List<VenMigsUploadMaster> VenMigsUploadMasterList);

	/**
	 * mergeVenMigsUploadMaster - merges a VenMigsUploadMaster
	 * 
	 * @param VenMigsUploadMaster
	 * @return the merged VenMigsUploadMaster
	 */
	public VenMigsUploadMaster mergeVenMigsUploadMaster(VenMigsUploadMaster VenMigsUploadMaster);

	/**
	 * mergeVenMigsUploadMasterList - merges a list of VenMigsUploadMaster
	 * 
	 * @param VenMigsUploadMasterList
	 * @return the merged list of VenMigsUploadMaster
	 */
	public ArrayList<VenMigsUploadMaster> mergeVenMigsUploadMasterList(
			List<VenMigsUploadMaster> VenMigsUploadMasterList);

	/**
	 * removeVenMigsUploadMaster - removes a VenMigsUploadMaster
	 * 
	 * @param VenMigsUploadMaster
	 */
	public void removeVenMigsUploadMaster(VenMigsUploadMaster VenMigsUploadMaster);

	/**
	 * removeVenMigsUploadMasterList - removes a list of VenMigsUploadMaster
	 * 
	 * @param VenMigsUploadMasterList
	 */
	public void removeVenMigsUploadMasterList(List<VenMigsUploadMaster> VenMigsUploadMasterList);

	/**
	 * findByVenMigsUploadMasterLike - finds a list of VenMigsUploadMaster Like
	 * 
	 * @param VenMigsUploadMaster
	 * @return the list of VenMigsUploadMaster found
	 */
	public List<VenMigsUploadMaster> findByVenMigsUploadMasterLike(VenMigsUploadMaster VenMigsUploadMaster,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenMigsUploadMaster>LikeFR - finds a list of VenMigsUploadMaster> Like with a finder return object
	 * 
	 * @param VenMigsUploadMaster
	 * @return the list of VenMigsUploadMaster found
	 */
	public FinderReturn findByVenMigsUploadMasterLikeFR(VenMigsUploadMaster VenMigsUploadMaster,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
