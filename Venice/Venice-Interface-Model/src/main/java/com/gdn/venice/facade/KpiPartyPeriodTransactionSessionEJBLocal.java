package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.KpiPartyPeriodTransaction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface KpiPartyPeriodTransactionSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<KpiPartyPeriodTransaction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBRemote#persistKpiPartyPeriodTransaction(com
	 * .gdn.venice.persistence.KpiPartyPeriodTransaction)
	 */
	public KpiPartyPeriodTransaction persistKpiPartyPeriodTransaction(KpiPartyPeriodTransaction kpiPartyPeriodTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBRemote#persistKpiPartyPeriodTransactionList
	 * (java.util.List)
	 */
	public ArrayList<KpiPartyPeriodTransaction> persistKpiPartyPeriodTransactionList(
			List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBRemote#mergeKpiPartyPeriodTransaction(com.
	 * gdn.venice.persistence.KpiPartyPeriodTransaction)
	 */
	public KpiPartyPeriodTransaction mergeKpiPartyPeriodTransaction(KpiPartyPeriodTransaction kpiPartyPeriodTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBRemote#mergeKpiPartyPeriodTransactionList(
	 * java.util.List)
	 */
	public ArrayList<KpiPartyPeriodTransaction> mergeKpiPartyPeriodTransactionList(
			List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBRemote#removeKpiPartyPeriodTransaction(com
	 * .gdn.venice.persistence.KpiPartyPeriodTransaction)
	 */
	public void removeKpiPartyPeriodTransaction(KpiPartyPeriodTransaction kpiPartyPeriodTransaction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBRemote#removeKpiPartyPeriodTransactionList
	 * (java.util.List)
	 */
	public void removeKpiPartyPeriodTransactionList(List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBRemote#findByKpiPartyPeriodTransactionLike
	 * (com.gdn.venice.persistence.KpiPartyPeriodTransaction, int, int)
	 */
	public List<KpiPartyPeriodTransaction> findByKpiPartyPeriodTransactionLike(KpiPartyPeriodTransaction kpiPartyPeriodTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBRemote#findByKpiPartyPeriodTransactionLikeFR
	 * (com.gdn.venice.persistence.KpiPartyPeriodTransaction, int, int)
	 */
	public FinderReturn findByKpiPartyPeriodTransactionLikeFR(KpiPartyPeriodTransaction kpiPartyPeriodTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
