package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenRetur;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenReturSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenRetur> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturSessionEJBRemote#persistVenRetur(com
	 * .gdn.venice.persistence.VenRetur)
	 */
	public VenRetur persistVenRetur(VenRetur venRetur);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturSessionEJBRemote#persistVenReturList
	 * (java.util.List)
	 */
	public ArrayList<VenRetur> persistVenReturList(
			List<VenRetur> venReturList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturSessionEJBRemote#mergeVenRetur(com.
	 * gdn.venice.persistence.VenRetur)
	 */
	public VenRetur mergeVenRetur(VenRetur venRetur);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturSessionEJBRemote#mergeVenReturList(
	 * java.util.List)
	 */
	public ArrayList<VenRetur> mergeVenReturList(
			List<VenRetur> venReturList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturSessionEJBRemote#removeVenRetur(com
	 * .gdn.venice.persistence.VenRetur)
	 */
	public void removeVenRetur(VenRetur venRetur);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturSessionEJBRemote#removeVenReturList
	 * (java.util.List)
	 */
	public void removeVenReturList(List<VenRetur> venReturList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturSessionEJBRemote#findByVenReturLike
	 * (com.gdn.venice.persistence.VenRetur, int, int)
	 */
	public List<VenRetur> findByVenReturLike(VenRetur venRetur,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturSessionEJBRemote#findByVenReturLikeFR
	 * (com.gdn.venice.persistence.VenRetur, int, int)
	 */
	public FinderReturn findByVenReturLikeFR(VenRetur venRetur,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
