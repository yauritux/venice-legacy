package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinApPaymentType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinApPaymentTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinApPaymentType
	 */
	public List<FinApPaymentType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinApPaymentType persists a country
	 * 
	 * @param finApPaymentType
	 * @return the persisted FinApPaymentType
	 */
	public FinApPaymentType persistFinApPaymentType(FinApPaymentType finApPaymentType);

	/**
	 * persistFinApPaymentTypeList - persists a list of FinApPaymentType
	 * 
	 * @param finApPaymentTypeList
	 * @return the list of persisted FinApPaymentType
	 */
	public ArrayList<FinApPaymentType> persistFinApPaymentTypeList(
			List<FinApPaymentType> finApPaymentTypeList);

	/**
	 * mergeFinApPaymentType - merges a FinApPaymentType
	 * 
	 * @param finApPaymentType
	 * @return the merged FinApPaymentType
	 */
	public FinApPaymentType mergeFinApPaymentType(FinApPaymentType finApPaymentType);

	/**
	 * mergeFinApPaymentTypeList - merges a list of FinApPaymentType
	 * 
	 * @param finApPaymentTypeList
	 * @return the merged list of FinApPaymentType
	 */
	public ArrayList<FinApPaymentType> mergeFinApPaymentTypeList(
			List<FinApPaymentType> finApPaymentTypeList);

	/**
	 * removeFinApPaymentType - removes a FinApPaymentType
	 * 
	 * @param finApPaymentType
	 */
	public void removeFinApPaymentType(FinApPaymentType finApPaymentType);

	/**
	 * removeFinApPaymentTypeList - removes a list of FinApPaymentType
	 * 
	 * @param finApPaymentTypeList
	 */
	public void removeFinApPaymentTypeList(List<FinApPaymentType> finApPaymentTypeList);

	/**
	 * findByFinApPaymentTypeLike - finds a list of FinApPaymentType Like
	 * 
	 * @param finApPaymentType
	 * @return the list of FinApPaymentType found
	 */
	public List<FinApPaymentType> findByFinApPaymentTypeLike(FinApPaymentType finApPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinApPaymentType>LikeFR - finds a list of FinApPaymentType> Like with a finder return object
	 * 
	 * @param finApPaymentType
	 * @return the list of FinApPaymentType found
	 */
	public FinderReturn findByFinApPaymentTypeLikeFR(FinApPaymentType finApPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
