package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinAccount;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinAccountSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinAccount
	 */
	public List<FinAccount> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinAccount persists a country
	 * 
	 * @param finAccount
	 * @return the persisted FinAccount
	 */
	public FinAccount persistFinAccount(FinAccount finAccount);

	/**
	 * persistFinAccountList - persists a list of FinAccount
	 * 
	 * @param finAccountList
	 * @return the list of persisted FinAccount
	 */
	public ArrayList<FinAccount> persistFinAccountList(
			List<FinAccount> finAccountList);

	/**
	 * mergeFinAccount - merges a FinAccount
	 * 
	 * @param finAccount
	 * @return the merged FinAccount
	 */
	public FinAccount mergeFinAccount(FinAccount finAccount);

	/**
	 * mergeFinAccountList - merges a list of FinAccount
	 * 
	 * @param finAccountList
	 * @return the merged list of FinAccount
	 */
	public ArrayList<FinAccount> mergeFinAccountList(
			List<FinAccount> finAccountList);

	/**
	 * removeFinAccount - removes a FinAccount
	 * 
	 * @param finAccount
	 */
	public void removeFinAccount(FinAccount finAccount);

	/**
	 * removeFinAccountList - removes a list of FinAccount
	 * 
	 * @param finAccountList
	 */
	public void removeFinAccountList(List<FinAccount> finAccountList);

	/**
	 * findByFinAccountLike - finds a list of FinAccount Like
	 * 
	 * @param finAccount
	 * @return the list of FinAccount found
	 */
	public List<FinAccount> findByFinAccountLike(FinAccount finAccount,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinAccount>LikeFR - finds a list of FinAccount> Like with a finder return object
	 * 
	 * @param finAccount
	 * @return the list of FinAccount found
	 */
	public FinderReturn findByFinAccountLikeFR(FinAccount finAccount,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
