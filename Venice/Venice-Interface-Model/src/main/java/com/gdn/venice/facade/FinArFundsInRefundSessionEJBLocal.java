package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinArFundsInRefund;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinArFundsInRefundSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinArFundsInRefund> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote#persistFinArFundsInRefund(com
	 * .gdn.venice.persistence.FinArFundsInRefund)
	 */
	public FinArFundsInRefund persistFinArFundsInRefund(FinArFundsInRefund finArFundsInRefund);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote#persistFinArFundsInRefundList
	 * (java.util.List)
	 */
	public ArrayList<FinArFundsInRefund> persistFinArFundsInRefundList(
			List<FinArFundsInRefund> finArFundsInRefundList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote#mergeFinArFundsInRefund(com.
	 * gdn.venice.persistence.FinArFundsInRefund)
	 */
	public FinArFundsInRefund mergeFinArFundsInRefund(FinArFundsInRefund finArFundsInRefund);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote#mergeFinArFundsInRefundList(
	 * java.util.List)
	 */
	public ArrayList<FinArFundsInRefund> mergeFinArFundsInRefundList(
			List<FinArFundsInRefund> finArFundsInRefundList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote#removeFinArFundsInRefund(com
	 * .gdn.venice.persistence.FinArFundsInRefund)
	 */
	public void removeFinArFundsInRefund(FinArFundsInRefund finArFundsInRefund);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote#removeFinArFundsInRefundList
	 * (java.util.List)
	 */
	public void removeFinArFundsInRefundList(List<FinArFundsInRefund> finArFundsInRefundList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote#findByFinArFundsInRefundLike
	 * (com.gdn.venice.persistence.FinArFundsInRefund, int, int)
	 */
	public List<FinArFundsInRefund> findByFinArFundsInRefundLike(FinArFundsInRefund finArFundsInRefund,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote#findByFinArFundsInRefundLikeFR
	 * (com.gdn.venice.persistence.FinArFundsInRefund, int, int)
	 */
	public FinderReturn findByFinArFundsInRefundLikeFR(FinArFundsInRefund finArFundsInRefund,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
