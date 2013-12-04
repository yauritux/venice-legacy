package com.gdn.venice.facade.fraud;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule21;
import com.gdn.venice.persistence.FrdParameterRule26272829;
import com.gdn.venice.persistence.VenCustomer;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenParty;

@Remote
public interface FraudRuleWithNativeQueryRemote {
	/**
	 * queryByRangeNative - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FraudDasboard
	 */	
	public List<VenOrderPaymentAllocation> queryByRangeRuleVenOrderPaymentAllocation(String jpqlStmt);
	
	public List<VenOrder> queryByRangeVenOrder(String jpqlStmt);
	
	public List<VenCustomer> queryByRangeVenCustomer(String jpqlStmt);
	
	public Long queryByRangeGetId(String jpqlStmt);
	
	public List<VenParty> queryByRangeVenParty(String jpqlStmt);
	
	public List<FrdParameterRule26272829> queryByRangeRule26272829(String jpqlStmt);
	
	public Double queryByRangeValue(String jpqlStmt);
	
	public List<BigDecimal> queryByRangeValueList(String jpqlStmt);
	
	public List<FrdParameterRule21> queryByRangeNativeRule21(String jpqlStmt);
	
}

