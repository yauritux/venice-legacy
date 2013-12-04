package com.gdn.venice.facade.finance.Dashboard;

import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinSalesRecord;

@Remote
public interface FinSalesRecordWithNativeQuerySessionEJBRemote {

	/**
	 * queryByRangeNative - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinSalesRecord
	 */
	
	public List<FinSalesRecord> queryByRangeWithNativeQueryUseToFinanceDashboard(String jpqlStmt);
}
