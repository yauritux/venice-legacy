package com.gdn.venice.facade.logistics.dashboard;

import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrder;

@Remote
public interface LogisticsDashBoardSessionEJBRemote {
	/**
	 * queryByRangeNative - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogisticsDasboard
	 */
	
	public List<VenOrder> queryByRangeWithNativeQueryUseToLogisticsDashboard(String jpqlStmt);
	
	public List<Double> queryByRangeWithNativeQuery(String jpqlStmt);
}
