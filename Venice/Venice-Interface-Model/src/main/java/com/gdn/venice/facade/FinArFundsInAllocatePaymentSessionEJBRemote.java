package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArFundsInAllocatePayment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinArFundsInAllocatePaymentSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinArFundsInAllocatePayment
	 */
	public List<FinArFundsInAllocatePayment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinArFundsInAllocatePayment persists a country
	 * 
	 * @param finArFundsInAllocatePayment
	 * @return the persisted FinArFundsInAllocatePayment
	 */
	public FinArFundsInAllocatePayment persistFinArFundsInAllocatePayment(FinArFundsInAllocatePayment finArFundsInAllocatePayment);

	/**
	 * persistFinArFundsInAllocatePaymentList - persists a list of FinArFundsInAllocatePayment
	 * 
	 * @param finArFundsInAllocatePaymentList
	 * @return the list of persisted FinArFundsInAllocatePayment
	 */
	public ArrayList<FinArFundsInAllocatePayment> persistFinArFundsInAllocatePaymentList(
			List<FinArFundsInAllocatePayment> finArFundsInAllocatePaymentList);

	/**
	 * mergeFinArFundsInAllocatePayment - merges a FinArFundsInAllocatePayment
	 * 
	 * @param finArFundsInAllocatePayment
	 * @return the merged FinArFundsInAllocatePayment
	 */
	public FinArFundsInAllocatePayment mergeFinArFundsInAllocatePayment(FinArFundsInAllocatePayment finArFundsInAllocatePayment);

	/**
	 * mergeFinArFundsInAllocatePaymentList - merges a list of FinArFundsInAllocatePayment
	 * 
	 * @param finArFundsInAllocatePaymentList
	 * @return the merged list of FinArFundsInAllocatePayment
	 */
	public ArrayList<FinArFundsInAllocatePayment> mergeFinArFundsInAllocatePaymentList(
			List<FinArFundsInAllocatePayment> finArFundsInAllocatePaymentList);

	/**
	 * removeFinArFundsInAllocatePayment - removes a FinArFundsInAllocatePayment
	 * 
	 * @param finArFundsInAllocatePayment
	 */
	public void removeFinArFundsInAllocatePayment(FinArFundsInAllocatePayment finArFundsInAllocatePayment);

	/**
	 * removeFinArFundsInAllocatePaymentList - removes a list of FinArFundsInAllocatePayment
	 * 
	 * @param finArFundsInAllocatePaymentList
	 */
	public void removeFinArFundsInAllocatePaymentList(List<FinArFundsInAllocatePayment> finArFundsInAllocatePaymentList);

	/**
	 * findByFinArFundsInAllocatePaymentLike - finds a list of FinArFundsInAllocatePayment Like
	 * 
	 * @param finArFundsInAllocatePayment
	 * @return the list of FinArFundsInAllocatePayment found
	 */
	public List<FinArFundsInAllocatePayment> findByFinArFundsInAllocatePaymentLike(FinArFundsInAllocatePayment finArFundsInAllocatePayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinArFundsInAllocatePayment>LikeFR - finds a list of FinArFundsInAllocatePayment> Like with a finder return object
	 * 
	 * @param finArFundsInAllocatePayment
	 * @return the list of FinArFundsInAllocatePayment found
	 */
	public FinderReturn findByFinArFundsInAllocatePaymentLikeFR(FinArFundsInAllocatePayment finArFundsInAllocatePayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
