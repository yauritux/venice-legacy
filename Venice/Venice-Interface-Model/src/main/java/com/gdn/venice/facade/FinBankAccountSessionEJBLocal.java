package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinBankAccount;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinBankAccountSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinBankAccountSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinBankAccount> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinBankAccountSessionEJBRemote#persistFinBankAccount(com
	 * .gdn.venice.persistence.FinBankAccount)
	 */
	public FinBankAccount persistFinBankAccount(FinBankAccount finBankAccount);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinBankAccountSessionEJBRemote#persistFinBankAccountList
	 * (java.util.List)
	 */
	public ArrayList<FinBankAccount> persistFinBankAccountList(
			List<FinBankAccount> finBankAccountList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinBankAccountSessionEJBRemote#mergeFinBankAccount(com.
	 * gdn.venice.persistence.FinBankAccount)
	 */
	public FinBankAccount mergeFinBankAccount(FinBankAccount finBankAccount);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinBankAccountSessionEJBRemote#mergeFinBankAccountList(
	 * java.util.List)
	 */
	public ArrayList<FinBankAccount> mergeFinBankAccountList(
			List<FinBankAccount> finBankAccountList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinBankAccountSessionEJBRemote#removeFinBankAccount(com
	 * .gdn.venice.persistence.FinBankAccount)
	 */
	public void removeFinBankAccount(FinBankAccount finBankAccount);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinBankAccountSessionEJBRemote#removeFinBankAccountList
	 * (java.util.List)
	 */
	public void removeFinBankAccountList(List<FinBankAccount> finBankAccountList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinBankAccountSessionEJBRemote#findByFinBankAccountLike
	 * (com.gdn.venice.persistence.FinBankAccount, int, int)
	 */
	public List<FinBankAccount> findByFinBankAccountLike(FinBankAccount finBankAccount,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinBankAccountSessionEJBRemote#findByFinBankAccountLikeFR
	 * (com.gdn.venice.persistence.FinBankAccount, int, int)
	 */
	public FinderReturn findByFinBankAccountLikeFR(FinBankAccount finBankAccount,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
