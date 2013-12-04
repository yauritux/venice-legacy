package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogLogisticsServiceType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogLogisticsServiceTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsServiceTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogLogisticsServiceType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsServiceTypeSessionEJBRemote#persistLogLogisticsServiceType(com
	 * .gdn.venice.persistence.LogLogisticsServiceType)
	 */
	public LogLogisticsServiceType persistLogLogisticsServiceType(LogLogisticsServiceType logLogisticsServiceType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsServiceTypeSessionEJBRemote#persistLogLogisticsServiceTypeList
	 * (java.util.List)
	 */
	public ArrayList<LogLogisticsServiceType> persistLogLogisticsServiceTypeList(
			List<LogLogisticsServiceType> logLogisticsServiceTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsServiceTypeSessionEJBRemote#mergeLogLogisticsServiceType(com.
	 * gdn.venice.persistence.LogLogisticsServiceType)
	 */
	public LogLogisticsServiceType mergeLogLogisticsServiceType(LogLogisticsServiceType logLogisticsServiceType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsServiceTypeSessionEJBRemote#mergeLogLogisticsServiceTypeList(
	 * java.util.List)
	 */
	public ArrayList<LogLogisticsServiceType> mergeLogLogisticsServiceTypeList(
			List<LogLogisticsServiceType> logLogisticsServiceTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsServiceTypeSessionEJBRemote#removeLogLogisticsServiceType(com
	 * .gdn.venice.persistence.LogLogisticsServiceType)
	 */
	public void removeLogLogisticsServiceType(LogLogisticsServiceType logLogisticsServiceType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsServiceTypeSessionEJBRemote#removeLogLogisticsServiceTypeList
	 * (java.util.List)
	 */
	public void removeLogLogisticsServiceTypeList(List<LogLogisticsServiceType> logLogisticsServiceTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsServiceTypeSessionEJBRemote#findByLogLogisticsServiceTypeLike
	 * (com.gdn.venice.persistence.LogLogisticsServiceType, int, int)
	 */
	public List<LogLogisticsServiceType> findByLogLogisticsServiceTypeLike(LogLogisticsServiceType logLogisticsServiceType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticsServiceTypeSessionEJBRemote#findByLogLogisticsServiceTypeLikeFR
	 * (com.gdn.venice.persistence.LogLogisticsServiceType, int, int)
	 */
	public FinderReturn findByLogLogisticsServiceTypeLikeFR(LogLogisticsServiceType logLogisticsServiceType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
