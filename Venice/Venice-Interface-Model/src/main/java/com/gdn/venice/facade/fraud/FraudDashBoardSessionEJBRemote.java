package com.gdn.venice.facade.fraud;

import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrder;

@Remote
public interface FraudDashBoardSessionEJBRemote {
	/**
	 * queryByRangeNative - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FraudDasboard
	 */
	
	public List<VenOrder> queryByRangeWithNativeQueryUseToFraudDashboard(String jpqlStmt);
	
	public Double queryByRangeWithNativeQuery(String jpqlStmt);
}
