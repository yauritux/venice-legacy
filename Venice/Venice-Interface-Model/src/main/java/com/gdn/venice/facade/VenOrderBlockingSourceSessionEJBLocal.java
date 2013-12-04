package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderBlockingSource;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderBlockingSourceSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderBlockingSourceSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderBlockingSource> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderBlockingSourceSessionEJBRemote#persistVenOrderBlockingSource(com
	 * .gdn.venice.persistence.VenOrderBlockingSource)
	 */
	public VenOrderBlockingSource persistVenOrderBlockingSource(VenOrderBlockingSource venOrderBlockingSource);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderBlockingSourceSessionEJBRemote#persistVenOrderBlockingSourceList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderBlockingSource> persistVenOrderBlockingSourceList(
			List<VenOrderBlockingSource> venOrderBlockingSourceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderBlockingSourceSessionEJBRemote#mergeVenOrderBlockingSource(com.
	 * gdn.venice.persistence.VenOrderBlockingSource)
	 */
	public VenOrderBlockingSource mergeVenOrderBlockingSource(VenOrderBlockingSource venOrderBlockingSource);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderBlockingSourceSessionEJBRemote#mergeVenOrderBlockingSourceList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderBlockingSource> mergeVenOrderBlockingSourceList(
			List<VenOrderBlockingSource> venOrderBlockingSourceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderBlockingSourceSessionEJBRemote#removeVenOrderBlockingSource(com
	 * .gdn.venice.persistence.VenOrderBlockingSource)
	 */
	public void removeVenOrderBlockingSource(VenOrderBlockingSource venOrderBlockingSource);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderBlockingSourceSessionEJBRemote#removeVenOrderBlockingSourceList
	 * (java.util.List)
	 */
	public void removeVenOrderBlockingSourceList(List<VenOrderBlockingSource> venOrderBlockingSourceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderBlockingSourceSessionEJBRemote#findByVenOrderBlockingSourceLike
	 * (com.gdn.venice.persistence.VenOrderBlockingSource, int, int)
	 */
	public List<VenOrderBlockingSource> findByVenOrderBlockingSourceLike(VenOrderBlockingSource venOrderBlockingSource,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderBlockingSourceSessionEJBRemote#findByVenOrderBlockingSourceLikeFR
	 * (com.gdn.venice.persistence.VenOrderBlockingSource, int, int)
	 */
	public FinderReturn findByVenOrderBlockingSourceLikeFR(VenOrderBlockingSource venOrderBlockingSource,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
