package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdFraudSuspicionCaseSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdFraudSuspicionCase> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote#persistFrdFraudSuspicionCase(com
	 * .gdn.venice.persistence.FrdFraudSuspicionCase)
	 */
	public FrdFraudSuspicionCase persistFrdFraudSuspicionCase(FrdFraudSuspicionCase frdFraudSuspicionCase);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote#persistFrdFraudSuspicionCaseList
	 * (java.util.List)
	 */
	public ArrayList<FrdFraudSuspicionCase> persistFrdFraudSuspicionCaseList(
			List<FrdFraudSuspicionCase> frdFraudSuspicionCaseList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote#mergeFrdFraudSuspicionCase(com.
	 * gdn.venice.persistence.FrdFraudSuspicionCase)
	 */
	public FrdFraudSuspicionCase mergeFrdFraudSuspicionCase(FrdFraudSuspicionCase frdFraudSuspicionCase);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote#mergeFrdFraudSuspicionCaseList(
	 * java.util.List)
	 */
	public ArrayList<FrdFraudSuspicionCase> mergeFrdFraudSuspicionCaseList(
			List<FrdFraudSuspicionCase> frdFraudSuspicionCaseList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote#removeFrdFraudSuspicionCase(com
	 * .gdn.venice.persistence.FrdFraudSuspicionCase)
	 */
	public void removeFrdFraudSuspicionCase(FrdFraudSuspicionCase frdFraudSuspicionCase);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote#removeFrdFraudSuspicionCaseList
	 * (java.util.List)
	 */
	public void removeFrdFraudSuspicionCaseList(List<FrdFraudSuspicionCase> frdFraudSuspicionCaseList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote#findByFrdFraudSuspicionCaseLike
	 * (com.gdn.venice.persistence.FrdFraudSuspicionCase, int, int)
	 */
	public List<FrdFraudSuspicionCase> findByFrdFraudSuspicionCaseLike(FrdFraudSuspicionCase frdFraudSuspicionCase,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote#findByFrdFraudSuspicionCaseLikeFR
	 * (com.gdn.venice.persistence.FrdFraudSuspicionCase, int, int)
	 */
	public FinderReturn findByFrdFraudSuspicionCaseLikeFR(FrdFraudSuspicionCase frdFraudSuspicionCase,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
