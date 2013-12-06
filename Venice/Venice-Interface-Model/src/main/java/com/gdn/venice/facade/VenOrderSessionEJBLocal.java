package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPayment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrder> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	public String countQueryByRange(String jpqlStmt, int firstResult,
			int maxResults);
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderSessionEJBRemote#persistVenOrder(com
	 * .gdn.venice.persistence.VenOrder)
	 */
	public VenOrder persistVenOrder(VenOrder venOrder);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderSessionEJBRemote#persistVenOrderList
	 * (java.util.List)
	 */
	public ArrayList<VenOrder> persistVenOrderList(
			List<VenOrder> venOrderList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderSessionEJBRemote#mergeVenOrder(com.
	 * gdn.venice.persistence.VenOrder)
	 */
	public VenOrder mergeVenOrder(VenOrder venOrder);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderSessionEJBRemote#mergeVenOrderList(
	 * java.util.List)
	 */
	public ArrayList<VenOrder> mergeVenOrderList(
			List<VenOrder> venOrderList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderSessionEJBRemote#removeVenOrder(com
	 * .gdn.venice.persistence.VenOrder)
	 */
	public void removeVenOrder(VenOrder venOrder);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderSessionEJBRemote#removeVenOrderList
	 * (java.util.List)
	 */
	public void removeVenOrderList(List<VenOrder> venOrderList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderSessionEJBRemote#findByVenOrderLike
	 * (com.gdn.venice.persistence.VenOrder, int, int)
	 */
	public List<VenOrder> findByVenOrderLike(VenOrder venOrder,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	public String countFindByVenOrderLike(VenOrder venOrder,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderSessionEJBRemote#findByVenOrderLikeFR
	 * (com.gdn.venice.persistence.VenOrder, int, int)
	 */
	public FinderReturn findByVenOrderLikeFR(VenOrder venOrder,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	
	public Boolean republish(VenOrder venOrder, String blockingSource);
	
	public Boolean republish(String wcsOrderId, VenOrderPayment venOrderPayment);
}
