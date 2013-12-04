package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinApprovalStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinApprovalStatusSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApprovalStatusSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinApprovalStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApprovalStatusSessionEJBRemote#persistFinApprovalStatus(com
	 * .gdn.venice.persistence.FinApprovalStatus)
	 */
	public FinApprovalStatus persistFinApprovalStatus(FinApprovalStatus finApprovalStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApprovalStatusSessionEJBRemote#persistFinApprovalStatusList
	 * (java.util.List)
	 */
	public ArrayList<FinApprovalStatus> persistFinApprovalStatusList(
			List<FinApprovalStatus> finApprovalStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApprovalStatusSessionEJBRemote#mergeFinApprovalStatus(com.
	 * gdn.venice.persistence.FinApprovalStatus)
	 */
	public FinApprovalStatus mergeFinApprovalStatus(FinApprovalStatus finApprovalStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApprovalStatusSessionEJBRemote#mergeFinApprovalStatusList(
	 * java.util.List)
	 */
	public ArrayList<FinApprovalStatus> mergeFinApprovalStatusList(
			List<FinApprovalStatus> finApprovalStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApprovalStatusSessionEJBRemote#removeFinApprovalStatus(com
	 * .gdn.venice.persistence.FinApprovalStatus)
	 */
	public void removeFinApprovalStatus(FinApprovalStatus finApprovalStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApprovalStatusSessionEJBRemote#removeFinApprovalStatusList
	 * (java.util.List)
	 */
	public void removeFinApprovalStatusList(List<FinApprovalStatus> finApprovalStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApprovalStatusSessionEJBRemote#findByFinApprovalStatusLike
	 * (com.gdn.venice.persistence.FinApprovalStatus, int, int)
	 */
	public List<FinApprovalStatus> findByFinApprovalStatusLike(FinApprovalStatus finApprovalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApprovalStatusSessionEJBRemote#findByFinApprovalStatusLikeFR
	 * (com.gdn.venice.persistence.FinApprovalStatus, int, int)
	 */
	public FinderReturn findByFinApprovalStatusLikeFR(FinApprovalStatus finApprovalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
