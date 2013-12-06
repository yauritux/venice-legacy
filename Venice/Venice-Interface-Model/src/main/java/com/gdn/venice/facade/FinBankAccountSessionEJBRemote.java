package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinBankAccount;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinBankAccountSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinBankAccount
	 */
	public List<FinBankAccount> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinBankAccount persists a country
	 * 
	 * @param finBankAccount
	 * @return the persisted FinBankAccount
	 */
	public FinBankAccount persistFinBankAccount(FinBankAccount finBankAccount);

	/**
	 * persistFinBankAccountList - persists a list of FinBankAccount
	 * 
	 * @param finBankAccountList
	 * @return the list of persisted FinBankAccount
	 */
	public ArrayList<FinBankAccount> persistFinBankAccountList(
			List<FinBankAccount> finBankAccountList);

	/**
	 * mergeFinBankAccount - merges a FinBankAccount
	 * 
	 * @param finBankAccount
	 * @return the merged FinBankAccount
	 */
	public FinBankAccount mergeFinBankAccount(FinBankAccount finBankAccount);

	/**
	 * mergeFinBankAccountList - merges a list of FinBankAccount
	 * 
	 * @param finBankAccountList
	 * @return the merged list of FinBankAccount
	 */
	public ArrayList<FinBankAccount> mergeFinBankAccountList(
			List<FinBankAccount> finBankAccountList);

	/**
	 * removeFinBankAccount - removes a FinBankAccount
	 * 
	 * @param finBankAccount
	 */
	public void removeFinBankAccount(FinBankAccount finBankAccount);

	/**
	 * removeFinBankAccountList - removes a list of FinBankAccount
	 * 
	 * @param finBankAccountList
	 */
	public void removeFinBankAccountList(List<FinBankAccount> finBankAccountList);

	/**
	 * findByFinBankAccountLike - finds a list of FinBankAccount Like
	 * 
	 * @param finBankAccount
	 * @return the list of FinBankAccount found
	 */
	public List<FinBankAccount> findByFinBankAccountLike(FinBankAccount finBankAccount,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinBankAccount>LikeFR - finds a list of FinBankAccount> Like with a finder return object
	 * 
	 * @param finBankAccount
	 * @return the list of FinBankAccount found
	 */
	public FinderReturn findByFinBankAccountLikeFR(FinBankAccount finBankAccount,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
