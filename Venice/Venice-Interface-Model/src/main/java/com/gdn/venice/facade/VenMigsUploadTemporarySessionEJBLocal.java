package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenMigsUploadTemporary;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenMigsUploadTemporarySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenMigsUploadTemporary> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote#persistVenMigsUploadTemporary(com
	 * .gdn.venice.persistence.VenMigsUploadTemporary)
	 */
	public VenMigsUploadTemporary persistVenMigsUploadTemporary(VenMigsUploadTemporary venMigsUploadTemporary);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote#persistVenMigsUploadTemporaryList
	 * (java.util.List)
	 */
	public ArrayList<VenMigsUploadTemporary> persistVenMigsUploadTemporaryList(
			List<VenMigsUploadTemporary> venMigsUploadTemporaryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote#mergeVenMigsUploadTemporary(com.
	 * gdn.venice.persistence.VenMigsUploadTemporary)
	 */
	public VenMigsUploadTemporary mergeVenMigsUploadTemporary(VenMigsUploadTemporary venMigsUploadTemporary);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote#mergeVenMigsUploadTemporaryList(
	 * java.util.List)
	 */
	public ArrayList<VenMigsUploadTemporary> mergeVenMigsUploadTemporaryList(
			List<VenMigsUploadTemporary> venMigsUploadTemporaryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote#removeVenMigsUploadTemporary(com
	 * .gdn.venice.persistence.VenMigsUploadTemporary)
	 */
	public void removeVenMigsUploadTemporary(VenMigsUploadTemporary venMigsUploadTemporary);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote#removeVenMigsUploadTemporaryList
	 * (java.util.List)
	 */
	public void removeVenMigsUploadTemporaryList(List<VenMigsUploadTemporary> venMigsUploadTemporaryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote#findByVenMigsUploadTemporaryLike
	 * (com.gdn.venice.persistence.VenMigsUploadTemporary, int, int)
	 */
	public List<VenMigsUploadTemporary> findByVenMigsUploadTemporaryLike(VenMigsUploadTemporary venMigsUploadTemporary,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote#findByVenMigsUploadTemporaryLikeFR
	 * (com.gdn.venice.persistence.VenMigsUploadTemporary, int, int)
	 */
	public FinderReturn findByVenMigsUploadTemporaryLikeFR(VenMigsUploadTemporary venMigsUploadTemporary,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
