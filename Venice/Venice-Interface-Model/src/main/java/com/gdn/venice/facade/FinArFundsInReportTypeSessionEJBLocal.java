package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinArFundsInReportType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinArFundsInReportTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinArFundsInReportType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportTypeSessionEJBRemote#persistFinArFundsInReportType(com
	 * .gdn.venice.persistence.FinArFundsInReportType)
	 */
	public FinArFundsInReportType persistFinArFundsInReportType(FinArFundsInReportType finArFundsInReportType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportTypeSessionEJBRemote#persistFinArFundsInReportTypeList
	 * (java.util.List)
	 */
	public ArrayList<FinArFundsInReportType> persistFinArFundsInReportTypeList(
			List<FinArFundsInReportType> finArFundsInReportTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportTypeSessionEJBRemote#mergeFinArFundsInReportType(com.
	 * gdn.venice.persistence.FinArFundsInReportType)
	 */
	public FinArFundsInReportType mergeFinArFundsInReportType(FinArFundsInReportType finArFundsInReportType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportTypeSessionEJBRemote#mergeFinArFundsInReportTypeList(
	 * java.util.List)
	 */
	public ArrayList<FinArFundsInReportType> mergeFinArFundsInReportTypeList(
			List<FinArFundsInReportType> finArFundsInReportTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportTypeSessionEJBRemote#removeFinArFundsInReportType(com
	 * .gdn.venice.persistence.FinArFundsInReportType)
	 */
	public void removeFinArFundsInReportType(FinArFundsInReportType finArFundsInReportType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportTypeSessionEJBRemote#removeFinArFundsInReportTypeList
	 * (java.util.List)
	 */
	public void removeFinArFundsInReportTypeList(List<FinArFundsInReportType> finArFundsInReportTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportTypeSessionEJBRemote#findByFinArFundsInReportTypeLike
	 * (com.gdn.venice.persistence.FinArFundsInReportType, int, int)
	 */
	public List<FinArFundsInReportType> findByFinArFundsInReportTypeLike(FinArFundsInReportType finArFundsInReportType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportTypeSessionEJBRemote#findByFinArFundsInReportTypeLikeFR
	 * (com.gdn.venice.persistence.FinArFundsInReportType, int, int)
	 */
	public FinderReturn findByFinArFundsInReportTypeLikeFR(FinArFundsInReportType finArFundsInReportType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
