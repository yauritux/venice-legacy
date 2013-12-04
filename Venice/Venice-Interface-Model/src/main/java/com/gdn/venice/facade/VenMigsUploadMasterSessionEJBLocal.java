package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenMigsUploadMaster;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenMigsUploadMasterSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenMigsUploadMaster> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote#persistVenMigsUploadMaster(com
	 * .gdn.venice.persistence.VenMigsUploadMaster)
	 */
	public VenMigsUploadMaster persistVenMigsUploadMaster(VenMigsUploadMaster VenMigsUploadMaster);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote#persistVenMigsUploadMasterList
	 * (java.util.List)
	 */
	public ArrayList<VenMigsUploadMaster> persistVenMigsUploadMasterList(
			List<VenMigsUploadMaster> VenMigsUploadMasterList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote#mergeVenMigsUploadMaster(com.
	 * gdn.venice.persistence.VenMigsUploadMaster)
	 */
	public VenMigsUploadMaster mergeVenMigsUploadMaster(VenMigsUploadMaster VenMigsUploadMaster);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote#mergeVenMigsUploadMasterList(
	 * java.util.List)
	 */
	public ArrayList<VenMigsUploadMaster> mergeVenMigsUploadMasterList(
			List<VenMigsUploadMaster> VenMigsUploadMasterList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote#removeVenMigsUploadMaster(com
	 * .gdn.venice.persistence.VenMigsUploadMaster)
	 */
	public void removeVenMigsUploadMaster(VenMigsUploadMaster VenMigsUploadMaster);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote#removeVenMigsUploadMasterList
	 * (java.util.List)
	 */
	public void removeVenMigsUploadMasterList(List<VenMigsUploadMaster> VenMigsUploadMasterList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote#findByVenMigsUploadMasterLike
	 * (com.gdn.venice.persistence.VenMigsUploadMaster, int, int)
	 */
	public List<VenMigsUploadMaster> findByVenMigsUploadMasterLike(VenMigsUploadMaster VenMigsUploadMaster,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote#findByVenMigsUploadMasterLikeFR
	 * (com.gdn.venice.persistence.VenMigsUploadMaster, int, int)
	 */
	public FinderReturn findByVenMigsUploadMasterLikeFR(VenMigsUploadMaster VenMigsUploadMaster,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
