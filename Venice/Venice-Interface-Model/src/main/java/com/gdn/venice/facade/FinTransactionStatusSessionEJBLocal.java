package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinTransactionStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinTransactionStatusSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionStatusSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinTransactionStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionStatusSessionEJBRemote#persistFinTransactionStatus(com
	 * .gdn.venice.persistence.FinTransactionStatus)
	 */
	public FinTransactionStatus persistFinTransactionStatus(FinTransactionStatus finTransactionStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionStatusSessionEJBRemote#persistFinTransactionStatusList
	 * (java.util.List)
	 */
	public ArrayList<FinTransactionStatus> persistFinTransactionStatusList(
			List<FinTransactionStatus> finTransactionStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionStatusSessionEJBRemote#mergeFinTransactionStatus(com.
	 * gdn.venice.persistence.FinTransactionStatus)
	 */
	public FinTransactionStatus mergeFinTransactionStatus(FinTransactionStatus finTransactionStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionStatusSessionEJBRemote#mergeFinTransactionStatusList(
	 * java.util.List)
	 */
	public ArrayList<FinTransactionStatus> mergeFinTransactionStatusList(
			List<FinTransactionStatus> finTransactionStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionStatusSessionEJBRemote#removeFinTransactionStatus(com
	 * .gdn.venice.persistence.FinTransactionStatus)
	 */
	public void removeFinTransactionStatus(FinTransactionStatus finTransactionStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionStatusSessionEJBRemote#removeFinTransactionStatusList
	 * (java.util.List)
	 */
	public void removeFinTransactionStatusList(List<FinTransactionStatus> finTransactionStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionStatusSessionEJBRemote#findByFinTransactionStatusLike
	 * (com.gdn.venice.persistence.FinTransactionStatus, int, int)
	 */
	public List<FinTransactionStatus> findByFinTransactionStatusLike(FinTransactionStatus finTransactionStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionStatusSessionEJBRemote#findByFinTransactionStatusLikeFR
	 * (com.gdn.venice.persistence.FinTransactionStatus, int, int)
	 */
	public FinderReturn findByFinTransactionStatusLikeFR(FinTransactionStatus finTransactionStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
