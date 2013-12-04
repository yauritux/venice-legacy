package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdCustomerWhitelistBlacklist;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdCustomerWhitelistBlacklistSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdCustomerWhitelistBlacklist
	 */
	public List<FrdCustomerWhitelistBlacklist> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdCustomerWhitelistBlacklist persists a country
	 * 
	 * @param frdCustomerWhitelistBlacklist
	 * @return the persisted FrdCustomerWhitelistBlacklist
	 */
	public FrdCustomerWhitelistBlacklist persistFrdCustomerWhitelistBlacklist(FrdCustomerWhitelistBlacklist frdCustomerWhitelistBlacklist);

	/**
	 * persistFrdCustomerWhitelistBlacklistList - persists a list of FrdCustomerWhitelistBlacklist
	 * 
	 * @param frdCustomerWhitelistBlacklistList
	 * @return the list of persisted FrdCustomerWhitelistBlacklist
	 */
	public ArrayList<FrdCustomerWhitelistBlacklist> persistFrdCustomerWhitelistBlacklistList(
			List<FrdCustomerWhitelistBlacklist> frdCustomerWhitelistBlacklistList);

	/**
	 * mergeFrdCustomerWhitelistBlacklist - merges a FrdCustomerWhitelistBlacklist
	 * 
	 * @param frdCustomerWhitelistBlacklist
	 * @return the merged FrdCustomerWhitelistBlacklist
	 */
	public FrdCustomerWhitelistBlacklist mergeFrdCustomerWhitelistBlacklist(FrdCustomerWhitelistBlacklist frdCustomerWhitelistBlacklist);

	/**
	 * mergeFrdCustomerWhitelistBlacklistList - merges a list of FrdCustomerWhitelistBlacklist
	 * 
	 * @param frdCustomerWhitelistBlacklistList
	 * @return the merged list of FrdCustomerWhitelistBlacklist
	 */
	public ArrayList<FrdCustomerWhitelistBlacklist> mergeFrdCustomerWhitelistBlacklistList(
			List<FrdCustomerWhitelistBlacklist> frdCustomerWhitelistBlacklistList);

	/**
	 * removeFrdCustomerWhitelistBlacklist - removes a FrdCustomerWhitelistBlacklist
	 * 
	 * @param frdCustomerWhitelistBlacklist
	 */
	public void removeFrdCustomerWhitelistBlacklist(FrdCustomerWhitelistBlacklist frdCustomerWhitelistBlacklist);

	/**
	 * removeFrdCustomerWhitelistBlacklistList - removes a list of FrdCustomerWhitelistBlacklist
	 * 
	 * @param frdCustomerWhitelistBlacklistList
	 */
	public void removeFrdCustomerWhitelistBlacklistList(List<FrdCustomerWhitelistBlacklist> frdCustomerWhitelistBlacklistList);

	/**
	 * findByFrdCustomerWhitelistBlacklistLike - finds a list of FrdCustomerWhitelistBlacklist Like
	 * 
	 * @param frdCustomerWhitelistBlacklist
	 * @return the list of FrdCustomerWhitelistBlacklist found
	 */
	public List<FrdCustomerWhitelistBlacklist> findByFrdCustomerWhitelistBlacklistLike(FrdCustomerWhitelistBlacklist frdCustomerWhitelistBlacklist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdCustomerWhitelistBlacklist>LikeFR - finds a list of FrdCustomerWhitelistBlacklist> Like with a finder return object
	 * 
	 * @param frdCustomerWhitelistBlacklist
	 * @return the list of FrdCustomerWhitelistBlacklist found
	 */
	public FinderReturn findByFrdCustomerWhitelistBlacklistLikeFR(FrdCustomerWhitelistBlacklist frdCustomerWhitelistBlacklist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
