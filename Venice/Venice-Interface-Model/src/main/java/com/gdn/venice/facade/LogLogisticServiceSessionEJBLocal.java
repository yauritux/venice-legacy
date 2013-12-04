package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogLogisticService;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogLogisticServiceSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogLogisticService> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote#persistLogLogisticService(com
	 * .gdn.venice.persistence.LogLogisticService)
	 */
	public LogLogisticService persistLogLogisticService(LogLogisticService logLogisticService);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote#persistLogLogisticServiceList
	 * (java.util.List)
	 */
	public ArrayList<LogLogisticService> persistLogLogisticServiceList(
			List<LogLogisticService> logLogisticServiceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote#mergeLogLogisticService(com.
	 * gdn.venice.persistence.LogLogisticService)
	 */
	public LogLogisticService mergeLogLogisticService(LogLogisticService logLogisticService);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote#mergeLogLogisticServiceList(
	 * java.util.List)
	 */
	public ArrayList<LogLogisticService> mergeLogLogisticServiceList(
			List<LogLogisticService> logLogisticServiceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote#removeLogLogisticService(com
	 * .gdn.venice.persistence.LogLogisticService)
	 */
	public void removeLogLogisticService(LogLogisticService logLogisticService);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote#removeLogLogisticServiceList
	 * (java.util.List)
	 */
	public void removeLogLogisticServiceList(List<LogLogisticService> logLogisticServiceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote#findByLogLogisticServiceLike
	 * (com.gdn.venice.persistence.LogLogisticService, int, int)
	 */
	public List<LogLogisticService> findByLogLogisticServiceLike(LogLogisticService logLogisticService,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote#findByLogLogisticServiceLikeFR
	 * (com.gdn.venice.persistence.LogLogisticService, int, int)
	 */
	public FinderReturn findByLogLogisticServiceLikeFR(LogLogisticService logLogisticService,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
