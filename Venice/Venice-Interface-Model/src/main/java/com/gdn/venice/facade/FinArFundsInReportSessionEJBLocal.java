package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinArFundsInReport;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinArFundsInReportSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinArFundsInReport> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportSessionEJBRemote#persistFinArFundsInReport(com
	 * .gdn.venice.persistence.FinArFundsInReport)
	 */
	public FinArFundsInReport persistFinArFundsInReport(FinArFundsInReport finArFundsInReport);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportSessionEJBRemote#persistFinArFundsInReportList
	 * (java.util.List)
	 */
	public ArrayList<FinArFundsInReport> persistFinArFundsInReportList(
			List<FinArFundsInReport> finArFundsInReportList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportSessionEJBRemote#mergeFinArFundsInReport(com.
	 * gdn.venice.persistence.FinArFundsInReport)
	 */
	public FinArFundsInReport mergeFinArFundsInReport(FinArFundsInReport finArFundsInReport);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportSessionEJBRemote#mergeFinArFundsInReportList(
	 * java.util.List)
	 */
	public ArrayList<FinArFundsInReport> mergeFinArFundsInReportList(
			List<FinArFundsInReport> finArFundsInReportList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportSessionEJBRemote#removeFinArFundsInReport(com
	 * .gdn.venice.persistence.FinArFundsInReport)
	 */
	public void removeFinArFundsInReport(FinArFundsInReport finArFundsInReport);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportSessionEJBRemote#removeFinArFundsInReportList
	 * (java.util.List)
	 */
	public void removeFinArFundsInReportList(List<FinArFundsInReport> finArFundsInReportList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportSessionEJBRemote#findByFinArFundsInReportLike
	 * (com.gdn.venice.persistence.FinArFundsInReport, int, int)
	 */
	public List<FinArFundsInReport> findByFinArFundsInReportLike(FinArFundsInReport finArFundsInReport,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReportSessionEJBRemote#findByFinArFundsInReportLikeFR
	 * (com.gdn.venice.persistence.FinArFundsInReport, int, int)
	 */
	public FinderReturn findByFinArFundsInReportLikeFR(FinArFundsInReport finArFundsInReport,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
