package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinTransactionType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinTransactionTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinTransactionType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionTypeSessionEJBRemote#persistFinTransactionType(com
	 * .gdn.venice.persistence.FinTransactionType)
	 */
	public FinTransactionType persistFinTransactionType(FinTransactionType finTransactionType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionTypeSessionEJBRemote#persistFinTransactionTypeList
	 * (java.util.List)
	 */
	public ArrayList<FinTransactionType> persistFinTransactionTypeList(
			List<FinTransactionType> finTransactionTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionTypeSessionEJBRemote#mergeFinTransactionType(com.
	 * gdn.venice.persistence.FinTransactionType)
	 */
	public FinTransactionType mergeFinTransactionType(FinTransactionType finTransactionType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionTypeSessionEJBRemote#mergeFinTransactionTypeList(
	 * java.util.List)
	 */
	public ArrayList<FinTransactionType> mergeFinTransactionTypeList(
			List<FinTransactionType> finTransactionTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionTypeSessionEJBRemote#removeFinTransactionType(com
	 * .gdn.venice.persistence.FinTransactionType)
	 */
	public void removeFinTransactionType(FinTransactionType finTransactionType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionTypeSessionEJBRemote#removeFinTransactionTypeList
	 * (java.util.List)
	 */
	public void removeFinTransactionTypeList(List<FinTransactionType> finTransactionTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionTypeSessionEJBRemote#findByFinTransactionTypeLike
	 * (com.gdn.venice.persistence.FinTransactionType, int, int)
	 */
	public List<FinTransactionType> findByFinTransactionTypeLike(FinTransactionType finTransactionType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinTransactionTypeSessionEJBRemote#findByFinTransactionTypeLikeFR
	 * (com.gdn.venice.persistence.FinTransactionType, int, int)
	 */
	public FinderReturn findByFinTransactionTypeLikeFR(FinTransactionType finTransactionType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
