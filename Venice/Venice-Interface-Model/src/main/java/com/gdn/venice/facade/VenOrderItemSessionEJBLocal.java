package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderItem;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderItemSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderItem> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#persistVenOrderItem(com
	 * .gdn.venice.persistence.VenOrderItem)
	 */
	public VenOrderItem persistVenOrderItem(VenOrderItem venOrderItem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#persistVenOrderItemList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderItem> persistVenOrderItemList(
			List<VenOrderItem> venOrderItemList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#mergeVenOrderItem(com.
	 * gdn.venice.persistence.VenOrderItem)
	 */
	public VenOrderItem mergeVenOrderItem(VenOrderItem venOrderItem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#mergeVenOrderItemList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderItem> mergeVenOrderItemList(
			List<VenOrderItem> venOrderItemList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#removeVenOrderItem(com
	 * .gdn.venice.persistence.VenOrderItem)
	 */
	public void removeVenOrderItem(VenOrderItem venOrderItem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#removeVenOrderItemList
	 * (java.util.List)
	 */
	public void removeVenOrderItemList(List<VenOrderItem> venOrderItemList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#findByVenOrderItemLike
	 * (com.gdn.venice.persistence.VenOrderItem, int, int)
	 */
	public List<VenOrderItem> findByVenOrderItemLike(VenOrderItem venOrderItem,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#findByVenOrderItemLikeFR
	 * (com.gdn.venice.persistence.VenOrderItem, int, int)
	 */
	public FinderReturn findByVenOrderItemLikeFR(VenOrderItem venOrderItem,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
