package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogApprovalStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogApprovalStatusSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogApprovalStatusSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogApprovalStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogApprovalStatusSessionEJBRemote#persistLogApprovalStatus(com
	 * .gdn.venice.persistence.LogApprovalStatus)
	 */
	public LogApprovalStatus persistLogApprovalStatus(LogApprovalStatus logApprovalStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogApprovalStatusSessionEJBRemote#persistLogApprovalStatusList
	 * (java.util.List)
	 */
	public ArrayList<LogApprovalStatus> persistLogApprovalStatusList(
			List<LogApprovalStatus> logApprovalStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogApprovalStatusSessionEJBRemote#mergeLogApprovalStatus(com.
	 * gdn.venice.persistence.LogApprovalStatus)
	 */
	public LogApprovalStatus mergeLogApprovalStatus(LogApprovalStatus logApprovalStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogApprovalStatusSessionEJBRemote#mergeLogApprovalStatusList(
	 * java.util.List)
	 */
	public ArrayList<LogApprovalStatus> mergeLogApprovalStatusList(
			List<LogApprovalStatus> logApprovalStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogApprovalStatusSessionEJBRemote#removeLogApprovalStatus(com
	 * .gdn.venice.persistence.LogApprovalStatus)
	 */
	public void removeLogApprovalStatus(LogApprovalStatus logApprovalStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogApprovalStatusSessionEJBRemote#removeLogApprovalStatusList
	 * (java.util.List)
	 */
	public void removeLogApprovalStatusList(List<LogApprovalStatus> logApprovalStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogApprovalStatusSessionEJBRemote#findByLogApprovalStatusLike
	 * (com.gdn.venice.persistence.LogApprovalStatus, int, int)
	 */
	public List<LogApprovalStatus> findByLogApprovalStatusLike(LogApprovalStatus logApprovalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogApprovalStatusSessionEJBRemote#findByLogApprovalStatusLikeFR
	 * (com.gdn.venice.persistence.LogApprovalStatus, int, int)
	 */
	public FinderReturn findByLogApprovalStatusLikeFR(LogApprovalStatus logApprovalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
