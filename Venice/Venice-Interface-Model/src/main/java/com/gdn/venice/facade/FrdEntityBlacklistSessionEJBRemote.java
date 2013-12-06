package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdEntityBlacklist;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdEntityBlacklistSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdEntityBlacklist
	 */
	public List<FrdEntityBlacklist> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdEntityBlacklist persists a country
	 * 
	 * @param frdEntityBlacklist
	 * @return the persisted FrdEntityBlacklist
	 */
	public FrdEntityBlacklist persistFrdEntityBlacklist(FrdEntityBlacklist frdEntityBlacklist);

	/**
	 * persistFrdEntityBlacklistList - persists a list of FrdEntityBlacklist
	 * 
	 * @param frdEntityBlacklistList
	 * @return the list of persisted FrdEntityBlacklist
	 */
	public ArrayList<FrdEntityBlacklist> persistFrdEntityBlacklistList(
			List<FrdEntityBlacklist> frdEntityBlacklistList);

	/**
	 * mergeFrdEntityBlacklist - merges a FrdEntityBlacklist
	 * 
	 * @param frdEntityBlacklist
	 * @return the merged FrdEntityBlacklist
	 */
	public FrdEntityBlacklist mergeFrdEntityBlacklist(FrdEntityBlacklist frdEntityBlacklist);

	/**
	 * mergeFrdEntityBlacklistList - merges a list of FrdEntityBlacklist
	 * 
	 * @param frdEntityBlacklistList
	 * @return the merged list of FrdEntityBlacklist
	 */
	public ArrayList<FrdEntityBlacklist> mergeFrdEntityBlacklistList(
			List<FrdEntityBlacklist> frdEntityBlacklistList);

	/**
	 * removeFrdEntityBlacklist - removes a FrdEntityBlacklist
	 * 
	 * @param frdEntityBlacklist
	 */
	public void removeFrdEntityBlacklist(FrdEntityBlacklist frdEntityBlacklist);

	/**
	 * removeFrdEntityBlacklistList - removes a list of FrdEntityBlacklist
	 * 
	 * @param frdEntityBlacklistList
	 */
	public void removeFrdEntityBlacklistList(List<FrdEntityBlacklist> frdEntityBlacklistList);

	/**
	 * findByFrdEntityBlacklistLike - finds a list of FrdEntityBlacklist Like
	 * 
	 * @param frdEntityBlacklist
	 * @return the list of FrdEntityBlacklist found
	 */
	public List<FrdEntityBlacklist> findByFrdEntityBlacklistLike(FrdEntityBlacklist frdEntityBlacklist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdEntityBlacklist>LikeFR - finds a list of FrdEntityBlacklist> Like with a finder return object
	 * 
	 * @param frdEntityBlacklist
	 * @return the list of FrdEntityBlacklist found
	 */
	public FinderReturn findByFrdEntityBlacklistLikeFR(FrdEntityBlacklist frdEntityBlacklist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
