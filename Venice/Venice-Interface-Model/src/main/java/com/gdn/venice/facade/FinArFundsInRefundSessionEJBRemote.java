package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArFundsInRefund;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinArFundsInRefundSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinArFundsInRefund
	 */
	public List<FinArFundsInRefund> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinArFundsInRefund persists a country
	 * 
	 * @param finArFundsInRefund
	 * @return the persisted FinArFundsInRefund
	 */
	public FinArFundsInRefund persistFinArFundsInRefund(FinArFundsInRefund finArFundsInRefund);

	/**
	 * persistFinArFundsInRefundList - persists a list of FinArFundsInRefund
	 * 
	 * @param finArFundsInRefundList
	 * @return the list of persisted FinArFundsInRefund
	 */
	public ArrayList<FinArFundsInRefund> persistFinArFundsInRefundList(
			List<FinArFundsInRefund> finArFundsInRefundList);

	/**
	 * mergeFinArFundsInRefund - merges a FinArFundsInRefund
	 * 
	 * @param finArFundsInRefund
	 * @return the merged FinArFundsInRefund
	 */
	public FinArFundsInRefund mergeFinArFundsInRefund(FinArFundsInRefund finArFundsInRefund);

	/**
	 * mergeFinArFundsInRefundList - merges a list of FinArFundsInRefund
	 * 
	 * @param finArFundsInRefundList
	 * @return the merged list of FinArFundsInRefund
	 */
	public ArrayList<FinArFundsInRefund> mergeFinArFundsInRefundList(
			List<FinArFundsInRefund> finArFundsInRefundList);

	/**
	 * removeFinArFundsInRefund - removes a FinArFundsInRefund
	 * 
	 * @param finArFundsInRefund
	 */
	public void removeFinArFundsInRefund(FinArFundsInRefund finArFundsInRefund);

	/**
	 * removeFinArFundsInRefundList - removes a list of FinArFundsInRefund
	 * 
	 * @param finArFundsInRefundList
	 */
	public void removeFinArFundsInRefundList(List<FinArFundsInRefund> finArFundsInRefundList);

	/**
	 * findByFinArFundsInRefundLike - finds a list of FinArFundsInRefund Like
	 * 
	 * @param finArFundsInRefund
	 * @return the list of FinArFundsInRefund found
	 */
	public List<FinArFundsInRefund> findByFinArFundsInRefundLike(FinArFundsInRefund finArFundsInRefund,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinArFundsInRefund>LikeFR - finds a list of FinArFundsInRefund> Like with a finder return object
	 * 
	 * @param finArFundsInRefund
	 * @return the list of FinArFundsInRefund found
	 */
	public FinderReturn findByFinArFundsInRefundLikeFR(FinArFundsInRefund finArFundsInRefund,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
