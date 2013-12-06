package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderPayment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderPaymentSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderPayment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote#persistVenOrderPayment(com
	 * .gdn.venice.persistence.VenOrderPayment)
	 */
	public VenOrderPayment persistVenOrderPayment(VenOrderPayment venOrderPayment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote#persistVenOrderPaymentList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderPayment> persistVenOrderPaymentList(
			List<VenOrderPayment> venOrderPaymentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote#mergeVenOrderPayment(com.
	 * gdn.venice.persistence.VenOrderPayment)
	 */
	public VenOrderPayment mergeVenOrderPayment(VenOrderPayment venOrderPayment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote#mergeVenOrderPaymentList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderPayment> mergeVenOrderPaymentList(
			List<VenOrderPayment> venOrderPaymentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote#removeVenOrderPayment(com
	 * .gdn.venice.persistence.VenOrderPayment)
	 */
	public void removeVenOrderPayment(VenOrderPayment venOrderPayment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote#removeVenOrderPaymentList
	 * (java.util.List)
	 */
	public void removeVenOrderPaymentList(List<VenOrderPayment> venOrderPaymentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote#findByVenOrderPaymentLike
	 * (com.gdn.venice.persistence.VenOrderPayment, int, int)
	 */
	public List<VenOrderPayment> findByVenOrderPaymentLike(VenOrderPayment venOrderPayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote#findByVenOrderPaymentLikeFR
	 * (com.gdn.venice.persistence.VenOrderPayment, int, int)
	 */
	public FinderReturn findByVenOrderPaymentLikeFR(VenOrderPayment venOrderPayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
