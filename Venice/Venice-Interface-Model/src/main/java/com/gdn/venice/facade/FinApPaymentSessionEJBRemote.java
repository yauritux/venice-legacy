package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinApPayment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinApPaymentSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinApPayment
	 */
	public List<FinApPayment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinApPayment persists a country
	 * 
	 * @param finApPayment
	 * @return the persisted FinApPayment
	 */
	public FinApPayment persistFinApPayment(FinApPayment finApPayment);

	/**
	 * persistFinApPaymentList - persists a list of FinApPayment
	 * 
	 * @param finApPaymentList
	 * @return the list of persisted FinApPayment
	 */
	public ArrayList<FinApPayment> persistFinApPaymentList(
			List<FinApPayment> finApPaymentList);

	/**
	 * mergeFinApPayment - merges a FinApPayment
	 * 
	 * @param finApPayment
	 * @return the merged FinApPayment
	 */
	public FinApPayment mergeFinApPayment(FinApPayment finApPayment);

	/**
	 * mergeFinApPaymentList - merges a list of FinApPayment
	 * 
	 * @param finApPaymentList
	 * @return the merged list of FinApPayment
	 */
	public ArrayList<FinApPayment> mergeFinApPaymentList(
			List<FinApPayment> finApPaymentList);

	/**
	 * removeFinApPayment - removes a FinApPayment
	 * 
	 * @param finApPayment
	 */
	public void removeFinApPayment(FinApPayment finApPayment);

	/**
	 * removeFinApPaymentList - removes a list of FinApPayment
	 * 
	 * @param finApPaymentList
	 */
	public void removeFinApPaymentList(List<FinApPayment> finApPaymentList);

	/**
	 * findByFinApPaymentLike - finds a list of FinApPayment Like
	 * 
	 * @param finApPayment
	 * @return the list of FinApPayment found
	 */
	public List<FinApPayment> findByFinApPaymentLike(FinApPayment finApPayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinApPayment>LikeFR - finds a list of FinApPayment> Like with a finder return object
	 * 
	 * @param finApPayment
	 * @return the list of FinApPayment found
	 */
	public FinderReturn findByFinApPaymentLikeFR(FinApPayment finApPayment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
