package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogAirwayBill;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogAirwayBillSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogAirwayBill> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);
	
	public String countQueryByRange(String jpqlStmt, int firstResult,
			int maxResults);
	
	public  List<LogAirwayBill> queryByRangeWithNativeQuery(String jpqlStmt, int firstResult,
			int maxResults);
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#persistLogAirwayBill(com
	 * .gdn.venice.persistence.LogAirwayBill)
	 */
	public LogAirwayBill persistLogAirwayBill(LogAirwayBill logAirwayBill);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#persistLogAirwayBillList
	 * (java.util.List)
	 */
	public ArrayList<LogAirwayBill> persistLogAirwayBillList(
			List<LogAirwayBill> logAirwayBillList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#mergeLogAirwayBill(com.
	 * gdn.venice.persistence.LogAirwayBill)
	 */
	public LogAirwayBill mergeLogAirwayBill(LogAirwayBill logAirwayBill);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#mergeLogAirwayBillList(
	 * java.util.List)
	 */
	public ArrayList<LogAirwayBill> mergeLogAirwayBillList(
			List<LogAirwayBill> logAirwayBillList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#removeLogAirwayBill(com
	 * .gdn.venice.persistence.LogAirwayBill)
	 */
	public void removeLogAirwayBill(LogAirwayBill logAirwayBill);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#removeLogAirwayBillList
	 * (java.util.List)
	 */
	public void removeLogAirwayBillList(List<LogAirwayBill> logAirwayBillList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#findByLogAirwayBillLike
	 * (com.gdn.venice.persistence.LogAirwayBill, int, int)
	 */
	public List<LogAirwayBill> findByLogAirwayBillLike(LogAirwayBill logAirwayBill,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#findByLogAirwayBillLikeFR
	 * (com.gdn.venice.persistence.LogAirwayBill, int, int)
	 */
	public FinderReturn findByLogAirwayBillLikeFR(LogAirwayBill logAirwayBill,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
