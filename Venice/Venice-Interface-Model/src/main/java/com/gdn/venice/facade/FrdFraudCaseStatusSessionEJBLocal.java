package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdFraudCaseStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdFraudCaseStatusSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseStatusSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdFraudCaseStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseStatusSessionEJBRemote#persistFrdFraudCaseStatus(com
	 * .gdn.venice.persistence.FrdFraudCaseStatus)
	 */
	public FrdFraudCaseStatus persistFrdFraudCaseStatus(FrdFraudCaseStatus frdFraudCaseStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseStatusSessionEJBRemote#persistFrdFraudCaseStatusList
	 * (java.util.List)
	 */
	public ArrayList<FrdFraudCaseStatus> persistFrdFraudCaseStatusList(
			List<FrdFraudCaseStatus> frdFraudCaseStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseStatusSessionEJBRemote#mergeFrdFraudCaseStatus(com.
	 * gdn.venice.persistence.FrdFraudCaseStatus)
	 */
	public FrdFraudCaseStatus mergeFrdFraudCaseStatus(FrdFraudCaseStatus frdFraudCaseStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseStatusSessionEJBRemote#mergeFrdFraudCaseStatusList(
	 * java.util.List)
	 */
	public ArrayList<FrdFraudCaseStatus> mergeFrdFraudCaseStatusList(
			List<FrdFraudCaseStatus> frdFraudCaseStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseStatusSessionEJBRemote#removeFrdFraudCaseStatus(com
	 * .gdn.venice.persistence.FrdFraudCaseStatus)
	 */
	public void removeFrdFraudCaseStatus(FrdFraudCaseStatus frdFraudCaseStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseStatusSessionEJBRemote#removeFrdFraudCaseStatusList
	 * (java.util.List)
	 */
	public void removeFrdFraudCaseStatusList(List<FrdFraudCaseStatus> frdFraudCaseStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseStatusSessionEJBRemote#findByFrdFraudCaseStatusLike
	 * (com.gdn.venice.persistence.FrdFraudCaseStatus, int, int)
	 */
	public List<FrdFraudCaseStatus> findByFrdFraudCaseStatusLike(FrdFraudCaseStatus frdFraudCaseStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseStatusSessionEJBRemote#findByFrdFraudCaseStatusLikeFR
	 * (com.gdn.venice.persistence.FrdFraudCaseStatus, int, int)
	 */
	public FinderReturn findByFrdFraudCaseStatusLikeFR(FrdFraudCaseStatus frdFraudCaseStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
