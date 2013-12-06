package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdRelatedFraudCase;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdRelatedFraudCaseSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRelatedFraudCaseSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdRelatedFraudCase> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRelatedFraudCaseSessionEJBRemote#persistFrdRelatedFraudCase(com
	 * .gdn.venice.persistence.FrdRelatedFraudCase)
	 */
	public FrdRelatedFraudCase persistFrdRelatedFraudCase(FrdRelatedFraudCase frdRelatedFraudCase);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRelatedFraudCaseSessionEJBRemote#persistFrdRelatedFraudCaseList
	 * (java.util.List)
	 */
	public ArrayList<FrdRelatedFraudCase> persistFrdRelatedFraudCaseList(
			List<FrdRelatedFraudCase> frdRelatedFraudCaseList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRelatedFraudCaseSessionEJBRemote#mergeFrdRelatedFraudCase(com.
	 * gdn.venice.persistence.FrdRelatedFraudCase)
	 */
	public FrdRelatedFraudCase mergeFrdRelatedFraudCase(FrdRelatedFraudCase frdRelatedFraudCase);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRelatedFraudCaseSessionEJBRemote#mergeFrdRelatedFraudCaseList(
	 * java.util.List)
	 */
	public ArrayList<FrdRelatedFraudCase> mergeFrdRelatedFraudCaseList(
			List<FrdRelatedFraudCase> frdRelatedFraudCaseList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRelatedFraudCaseSessionEJBRemote#removeFrdRelatedFraudCase(com
	 * .gdn.venice.persistence.FrdRelatedFraudCase)
	 */
	public void removeFrdRelatedFraudCase(FrdRelatedFraudCase frdRelatedFraudCase);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRelatedFraudCaseSessionEJBRemote#removeFrdRelatedFraudCaseList
	 * (java.util.List)
	 */
	public void removeFrdRelatedFraudCaseList(List<FrdRelatedFraudCase> frdRelatedFraudCaseList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRelatedFraudCaseSessionEJBRemote#findByFrdRelatedFraudCaseLike
	 * (com.gdn.venice.persistence.FrdRelatedFraudCase, int, int)
	 */
	public List<FrdRelatedFraudCase> findByFrdRelatedFraudCaseLike(FrdRelatedFraudCase frdRelatedFraudCase,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRelatedFraudCaseSessionEJBRemote#findByFrdRelatedFraudCaseLikeFR
	 * (com.gdn.venice.persistence.FrdRelatedFraudCase, int, int)
	 */
	public FinderReturn findByFrdRelatedFraudCaseLikeFR(FrdRelatedFraudCase frdRelatedFraudCase,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
