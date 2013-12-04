package com.gdn.venice.facade.kpi;

import javax.ejb.Remote;
import javax.jws.WebService;

/**
 * KPI_TransactionPosterSessionEJBRemote.java
 * 
 * Session Bean remote interface for posting KPI transactions
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
@Remote
@WebService(name = "KPI_TransactionPosterSessionEJBPortType", targetNamespace = "http://kpi.venice.gdn.com/services")
public interface KPI_TransactionPosterSessionEJBRemote {
	/**
	 * Method to post KPI transactions for specific parties within a period
	 * @param kpiId
	 * @param periodId
	 * @param partyId
	 * @param value
	 * @param reason
	 * @return true if the operation succeeds else false
	 */
	public Boolean postKpiTransaction(Long kpiId, Long periodId, Long partyId, Integer value, String reason);
}
