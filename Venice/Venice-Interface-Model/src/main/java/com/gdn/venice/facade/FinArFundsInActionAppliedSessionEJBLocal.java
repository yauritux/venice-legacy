package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinArFundsInActionApplied;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinArFundsInActionAppliedSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinArFundsInActionApplied> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedSessionEJBRemote#persistFinArFundsInActionApplied(com
	 * .gdn.venice.persistence.FinArFundsInActionApplied)
	 */
	public FinArFundsInActionApplied persistFinArFundsInActionApplied(FinArFundsInActionApplied finArFundsInActionApplied);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedSessionEJBRemote#persistFinArFundsInActionAppliedList
	 * (java.util.List)
	 */
	public ArrayList<FinArFundsInActionApplied> persistFinArFundsInActionAppliedList(
			List<FinArFundsInActionApplied> finArFundsInActionAppliedList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedSessionEJBRemote#mergeFinArFundsInActionApplied(com.
	 * gdn.venice.persistence.FinArFundsInActionApplied)
	 */
	public FinArFundsInActionApplied mergeFinArFundsInActionApplied(FinArFundsInActionApplied finArFundsInActionApplied);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedSessionEJBRemote#mergeFinArFundsInActionAppliedList(
	 * java.util.List)
	 */
	public ArrayList<FinArFundsInActionApplied> mergeFinArFundsInActionAppliedList(
			List<FinArFundsInActionApplied> finArFundsInActionAppliedList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedSessionEJBRemote#removeFinArFundsInActionApplied(com
	 * .gdn.venice.persistence.FinArFundsInActionApplied)
	 */
	public void removeFinArFundsInActionApplied(FinArFundsInActionApplied finArFundsInActionApplied);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedSessionEJBRemote#removeFinArFundsInActionAppliedList
	 * (java.util.List)
	 */
	public void removeFinArFundsInActionAppliedList(List<FinArFundsInActionApplied> finArFundsInActionAppliedList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedSessionEJBRemote#findByFinArFundsInActionAppliedLike
	 * (com.gdn.venice.persistence.FinArFundsInActionApplied, int, int)
	 */
	public List<FinArFundsInActionApplied> findByFinArFundsInActionAppliedLike(FinArFundsInActionApplied finArFundsInActionApplied,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedSessionEJBRemote#findByFinArFundsInActionAppliedLikeFR
	 * (com.gdn.venice.persistence.FinArFundsInActionApplied, int, int)
	 */
	public FinderReturn findByFinArFundsInActionAppliedLikeFR(FinArFundsInActionApplied finArFundsInActionApplied,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
