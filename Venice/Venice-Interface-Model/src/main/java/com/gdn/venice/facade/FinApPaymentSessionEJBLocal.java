package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinApPayment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinApPaymentSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinApPayment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentSessionEJBRemote#persistFinApPayment(com
	 * .gdn.venice.persistence.FinApPayment)
	 */
	public FinApPayment persistFinApPayment(FinApPayment finApPayment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentSessionEJBRemote#persistFinApPaymentList
	 * (java.util.List)
	 */
	public ArrayList<FinApPayment> persistFinApPaymentList(
			List<FinApPayment> finApPaymentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentSessionEJBRemote#mergeFinApPayment(com.
	 * gdn.venice.persistence.FinApPayment)
	 */
	public FinApPayment mergeFinApPayment(FinApPayment finApPayment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentSessionEJBRemote#mergeFinApPaymentList(
	 * java.util.List)
	 */
	public ArrayList<FinApPayment> mergeFinApPaymentList(
			List<FinApPayment> finApPaymentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentSessionEJBRemote#removeFinApPayment(com
	 * .gdn.venice.persistence.FinApPayment)
	 */
	public void removeFinApPayment(FinApPayment finApPayment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentSessionEJBRemote#removeFinApPaymentList
	 * (java.util.List)
	 */
	public void removeFinApPaymentList(List<FinApPayment> finApPaymentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentSessionEJBRemote#findByFinApPaymentLike
	 * (com.gdn.venice.persistence.FinApPayment, int, int)
	 */
	public List<FinApPayment> findByFinApPaymentLike(FinApPayment finApPayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentSessionEJBRemote#findByFinApPaymentLikeFR
	 * (com.gdn.venice.persistence.FinApPayment, int, int)
	 */
	public FinderReturn findByFinApPaymentLikeFR(FinApPayment finApPayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
