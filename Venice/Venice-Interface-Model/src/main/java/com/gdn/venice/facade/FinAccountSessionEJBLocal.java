package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinAccount;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinAccountSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinAccount> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountSessionEJBRemote#persistFinAccount(com
	 * .gdn.venice.persistence.FinAccount)
	 */
	public FinAccount persistFinAccount(FinAccount finAccount);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountSessionEJBRemote#persistFinAccountList
	 * (java.util.List)
	 */
	public ArrayList<FinAccount> persistFinAccountList(
			List<FinAccount> finAccountList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountSessionEJBRemote#mergeFinAccount(com.
	 * gdn.venice.persistence.FinAccount)
	 */
	public FinAccount mergeFinAccount(FinAccount finAccount);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountSessionEJBRemote#mergeFinAccountList(
	 * java.util.List)
	 */
	public ArrayList<FinAccount> mergeFinAccountList(
			List<FinAccount> finAccountList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountSessionEJBRemote#removeFinAccount(com
	 * .gdn.venice.persistence.FinAccount)
	 */
	public void removeFinAccount(FinAccount finAccount);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountSessionEJBRemote#removeFinAccountList
	 * (java.util.List)
	 */
	public void removeFinAccountList(List<FinAccount> finAccountList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountSessionEJBRemote#findByFinAccountLike
	 * (com.gdn.venice.persistence.FinAccount, int, int)
	 */
	public List<FinAccount> findByFinAccountLike(FinAccount finAccount,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountSessionEJBRemote#findByFinAccountLikeFR
	 * (com.gdn.venice.persistence.FinAccount, int, int)
	 */
	public FinderReturn findByFinAccountLikeFR(FinAccount finAccount,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
