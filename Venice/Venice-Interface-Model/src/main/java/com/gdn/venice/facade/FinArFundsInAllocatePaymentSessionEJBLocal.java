package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinArFundsInAllocatePayment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinArFundsInAllocatePaymentSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinArFundsInAllocatePayment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote#persistFinArFundsInAllocatePayment(com
	 * .gdn.venice.persistence.FinArFundsInAllocatePayment)
	 */
	public FinArFundsInAllocatePayment persistFinArFundsInAllocatePayment(FinArFundsInAllocatePayment finArFundsInAllocatePayment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote#persistFinArFundsInAllocatePaymentList
	 * (java.util.List)
	 */
	public ArrayList<FinArFundsInAllocatePayment> persistFinArFundsInAllocatePaymentList(
			List<FinArFundsInAllocatePayment> finArFundsInAllocatePaymentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote#mergeFinArFundsInAllocatePayment(com.
	 * gdn.venice.persistence.FinArFundsInAllocatePayment)
	 */
	public FinArFundsInAllocatePayment mergeFinArFundsInAllocatePayment(FinArFundsInAllocatePayment finArFundsInAllocatePayment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote#mergeFinArFundsInAllocatePaymentList(
	 * java.util.List)
	 */
	public ArrayList<FinArFundsInAllocatePayment> mergeFinArFundsInAllocatePaymentList(
			List<FinArFundsInAllocatePayment> finArFundsInAllocatePaymentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote#removeFinArFundsInAllocatePayment(com
	 * .gdn.venice.persistence.FinArFundsInAllocatePayment)
	 */
	public void removeFinArFundsInAllocatePayment(FinArFundsInAllocatePayment finArFundsInAllocatePayment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote#removeFinArFundsInAllocatePaymentList
	 * (java.util.List)
	 */
	public void removeFinArFundsInAllocatePaymentList(List<FinArFundsInAllocatePayment> finArFundsInAllocatePaymentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote#findByFinArFundsInAllocatePaymentLike
	 * (com.gdn.venice.persistence.FinArFundsInAllocatePayment, int, int)
	 */
	public List<FinArFundsInAllocatePayment> findByFinArFundsInAllocatePaymentLike(FinArFundsInAllocatePayment finArFundsInAllocatePayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote#findByFinArFundsInAllocatePaymentLikeFR
	 * (com.gdn.venice.persistence.FinArFundsInAllocatePayment, int, int)
	 */
	public FinderReturn findByFinArFundsInAllocatePaymentLikeFR(FinArFundsInAllocatePayment finArFundsInAllocatePayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
