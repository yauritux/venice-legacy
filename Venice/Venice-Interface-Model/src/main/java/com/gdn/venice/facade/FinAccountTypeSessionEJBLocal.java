package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinAccountType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinAccountTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinAccountType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountTypeSessionEJBRemote#persistFinAccountType(com
	 * .gdn.venice.persistence.FinAccountType)
	 */
	public FinAccountType persistFinAccountType(FinAccountType finAccountType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountTypeSessionEJBRemote#persistFinAccountTypeList
	 * (java.util.List)
	 */
	public ArrayList<FinAccountType> persistFinAccountTypeList(
			List<FinAccountType> finAccountTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountTypeSessionEJBRemote#mergeFinAccountType(com.
	 * gdn.venice.persistence.FinAccountType)
	 */
	public FinAccountType mergeFinAccountType(FinAccountType finAccountType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountTypeSessionEJBRemote#mergeFinAccountTypeList(
	 * java.util.List)
	 */
	public ArrayList<FinAccountType> mergeFinAccountTypeList(
			List<FinAccountType> finAccountTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountTypeSessionEJBRemote#removeFinAccountType(com
	 * .gdn.venice.persistence.FinAccountType)
	 */
	public void removeFinAccountType(FinAccountType finAccountType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountTypeSessionEJBRemote#removeFinAccountTypeList
	 * (java.util.List)
	 */
	public void removeFinAccountTypeList(List<FinAccountType> finAccountTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountTypeSessionEJBRemote#findByFinAccountTypeLike
	 * (com.gdn.venice.persistence.FinAccountType, int, int)
	 */
	public List<FinAccountType> findByFinAccountTypeLike(FinAccountType finAccountType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountTypeSessionEJBRemote#findByFinAccountTypeLikeFR
	 * (com.gdn.venice.persistence.FinAccountType, int, int)
	 */
	public FinderReturn findByFinAccountTypeLikeFR(FinAccountType finAccountType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
