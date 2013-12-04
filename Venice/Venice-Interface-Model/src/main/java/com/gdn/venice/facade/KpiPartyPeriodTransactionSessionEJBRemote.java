package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.KpiPartyPeriodTransaction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface KpiPartyPeriodTransactionSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of KpiPartyPeriodTransaction
	 */
	public List<KpiPartyPeriodTransaction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistKpiPartyPeriodTransaction persists a country
	 * 
	 * @param kpiPartyPeriodTransaction
	 * @return the persisted KpiPartyPeriodTransaction
	 */
	public KpiPartyPeriodTransaction persistKpiPartyPeriodTransaction(KpiPartyPeriodTransaction kpiPartyPeriodTransaction);

	/**
	 * persistKpiPartyPeriodTransactionList - persists a list of KpiPartyPeriodTransaction
	 * 
	 * @param kpiPartyPeriodTransactionList
	 * @return the list of persisted KpiPartyPeriodTransaction
	 */
	public ArrayList<KpiPartyPeriodTransaction> persistKpiPartyPeriodTransactionList(
			List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList);

	/**
	 * mergeKpiPartyPeriodTransaction - merges a KpiPartyPeriodTransaction
	 * 
	 * @param kpiPartyPeriodTransaction
	 * @return the merged KpiPartyPeriodTransaction
	 */
	public KpiPartyPeriodTransaction mergeKpiPartyPeriodTransaction(KpiPartyPeriodTransaction kpiPartyPeriodTransaction);

	/**
	 * mergeKpiPartyPeriodTransactionList - merges a list of KpiPartyPeriodTransaction
	 * 
	 * @param kpiPartyPeriodTransactionList
	 * @return the merged list of KpiPartyPeriodTransaction
	 */
	public ArrayList<KpiPartyPeriodTransaction> mergeKpiPartyPeriodTransactionList(
			List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList);

	/**
	 * removeKpiPartyPeriodTransaction - removes a KpiPartyPeriodTransaction
	 * 
	 * @param kpiPartyPeriodTransaction
	 */
	public void removeKpiPartyPeriodTransaction(KpiPartyPeriodTransaction kpiPartyPeriodTransaction);

	/**
	 * removeKpiPartyPeriodTransactionList - removes a list of KpiPartyPeriodTransaction
	 * 
	 * @param kpiPartyPeriodTransactionList
	 */
	public void removeKpiPartyPeriodTransactionList(List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList);

	/**
	 * findByKpiPartyPeriodTransactionLike - finds a list of KpiPartyPeriodTransaction Like
	 * 
	 * @param kpiPartyPeriodTransaction
	 * @return the list of KpiPartyPeriodTransaction found
	 */
	public List<KpiPartyPeriodTransaction> findByKpiPartyPeriodTransactionLike(KpiPartyPeriodTransaction kpiPartyPeriodTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByKpiPartyPeriodTransaction>LikeFR - finds a list of KpiPartyPeriodTransaction> Like with a finder return object
	 * 
	 * @param kpiPartyPeriodTransaction
	 * @return the list of KpiPartyPeriodTransaction found
	 */
	public FinderReturn findByKpiPartyPeriodTransactionLikeFR(KpiPartyPeriodTransaction kpiPartyPeriodTransaction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
