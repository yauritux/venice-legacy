package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenReturItem;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenReturItemSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenReturItem> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemSessionEJBRemote#persistVenReturItem(com
	 * .gdn.venice.persistence.VenReturItem)
	 */
	public VenReturItem persistVenReturItem(VenReturItem venReturItem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemSessionEJBRemote#persistVenReturItemList
	 * (java.util.List)
	 */
	public ArrayList<VenReturItem> persistVenReturItemList(
			List<VenReturItem> venReturItemList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemSessionEJBRemote#mergeVenReturItem(com.
	 * gdn.venice.persistence.VenReturItem)
	 */
	public VenReturItem mergeVenReturItem(VenReturItem venReturItem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemSessionEJBRemote#mergeVenReturItemList(
	 * java.util.List)
	 */
	public ArrayList<VenReturItem> mergeVenReturItemList(
			List<VenReturItem> venReturItemList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemSessionEJBRemote#removeVenReturItem(com
	 * .gdn.venice.persistence.VenReturItem)
	 */
	public void removeVenReturItem(VenReturItem venReturItem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemSessionEJBRemote#removeVenReturItemList
	 * (java.util.List)
	 */
	public void removeVenReturItemList(List<VenReturItem> venReturItemList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemSessionEJBRemote#findByVenReturItemLike
	 * (com.gdn.venice.persistence.VenReturItem, int, int)
	 */
	public List<VenReturItem> findByVenReturItemLike(VenReturItem venReturItem,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemSessionEJBRemote#findByVenReturItemLikeFR
	 * (com.gdn.venice.persistence.VenReturItem, int, int)
	 */
	public FinderReturn findByVenReturItemLikeFR(VenReturItem venReturItem,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
