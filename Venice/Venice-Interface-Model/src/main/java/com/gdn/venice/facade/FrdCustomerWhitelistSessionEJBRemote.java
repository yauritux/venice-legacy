package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdCustomerWhitelist;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdCustomerWhitelistSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdCustomerWhitelist
	 */
	public List<FrdCustomerWhitelist> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdCustomerWhitelist persists a country
	 * 
	 * @param frdCustomerWhitelist
	 * @return the persisted FrdCustomerWhitelist
	 */
	public FrdCustomerWhitelist persistFrdCustomerWhitelist(FrdCustomerWhitelist frdCustomerWhitelist);

	/**
	 * persistFrdCustomerWhitelistList - persists a list of FrdCustomerWhitelist
	 * 
	 * @param frdCustomerWhitelistList
	 * @return the list of persisted FrdCustomerWhitelist
	 */
	public ArrayList<FrdCustomerWhitelist> persistFrdCustomerWhitelistList(
			List<FrdCustomerWhitelist> frdCustomerWhitelistList);

	/**
	 * mergeFrdCustomerWhitelist - merges a FrdCustomerWhitelist
	 * 
	 * @param frdCustomerWhitelist
	 * @return the merged FrdCustomerWhitelist
	 */
	public FrdCustomerWhitelist mergeFrdCustomerWhitelist(FrdCustomerWhitelist frdCustomerWhitelist);

	/**
	 * mergeFrdCustomerWhitelistList - merges a list of FrdCustomerWhitelist
	 * 
	 * @param frdCustomerWhitelistList
	 * @return the merged list of FrdCustomerWhitelist
	 */
	public ArrayList<FrdCustomerWhitelist> mergeFrdCustomerWhitelistList(
			List<FrdCustomerWhitelist> frdCustomerWhitelistList);

	/**
	 * removeFrdCustomerWhitelist - removes a FrdCustomerWhitelist
	 * 
	 * @param frdCustomerWhitelist
	 */
	public void removeFrdCustomerWhitelist(FrdCustomerWhitelist frdCustomerWhitelist);

	/**
	 * removeFrdCustomerWhitelistList - removes a list of FrdCustomerWhitelist
	 * 
	 * @param frdCustomerWhitelistList
	 */
	public void removeFrdCustomerWhitelistList(List<FrdCustomerWhitelist> frdCustomerWhitelistList);

	/**
	 * findByFrdCustomerWhitelistLike - finds a list of FrdCustomerWhitelist Like
	 * 
	 * @param frdCustomerWhitelist
	 * @return the list of FrdCustomerWhitelist found
	 */
	public List<FrdCustomerWhitelist> findByFrdCustomerWhitelistLike(FrdCustomerWhitelist frdCustomerWhitelist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdCustomerWhitelist>LikeFR - finds a list of FrdCustomerWhitelist> Like with a finder return object
	 * 
	 * @param frdCustomerWhitelist
	 * @return the list of FrdCustomerWhitelist found
	 */
	public FinderReturn findByFrdCustomerWhitelistLikeFR(FrdCustomerWhitelist frdCustomerWhitelist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
