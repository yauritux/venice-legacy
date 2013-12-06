package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinApPaymentType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinApPaymentTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinApPaymentType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentTypeSessionEJBRemote#persistFinApPaymentType(com
	 * .gdn.venice.persistence.FinApPaymentType)
	 */
	public FinApPaymentType persistFinApPaymentType(FinApPaymentType finApPaymentType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentTypeSessionEJBRemote#persistFinApPaymentTypeList
	 * (java.util.List)
	 */
	public ArrayList<FinApPaymentType> persistFinApPaymentTypeList(
			List<FinApPaymentType> finApPaymentTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentTypeSessionEJBRemote#mergeFinApPaymentType(com.
	 * gdn.venice.persistence.FinApPaymentType)
	 */
	public FinApPaymentType mergeFinApPaymentType(FinApPaymentType finApPaymentType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentTypeSessionEJBRemote#mergeFinApPaymentTypeList(
	 * java.util.List)
	 */
	public ArrayList<FinApPaymentType> mergeFinApPaymentTypeList(
			List<FinApPaymentType> finApPaymentTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentTypeSessionEJBRemote#removeFinApPaymentType(com
	 * .gdn.venice.persistence.FinApPaymentType)
	 */
	public void removeFinApPaymentType(FinApPaymentType finApPaymentType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentTypeSessionEJBRemote#removeFinApPaymentTypeList
	 * (java.util.List)
	 */
	public void removeFinApPaymentTypeList(List<FinApPaymentType> finApPaymentTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentTypeSessionEJBRemote#findByFinApPaymentTypeLike
	 * (com.gdn.venice.persistence.FinApPaymentType, int, int)
	 */
	public List<FinApPaymentType> findByFinApPaymentTypeLike(FinApPaymentType finApPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApPaymentTypeSessionEJBRemote#findByFinApPaymentTypeLikeFR
	 * (com.gdn.venice.persistence.FinApPaymentType, int, int)
	 */
	public FinderReturn findByFinApPaymentTypeLikeFR(FinApPaymentType finApPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
