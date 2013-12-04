package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdBlacklistReason;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdBlacklistReasonSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdBlacklistReason
	 */
	public List<FrdBlacklistReason> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdBlacklistReason persists a country
	 * 
	 * @param frdBlacklistReason
	 * @return the persisted FrdBlacklistReason
	 */
	public FrdBlacklistReason persistFrdBlacklistReason(FrdBlacklistReason frdBlacklistReason);

	/**
	 * persistFrdBlacklistReasonList - persists a list of FrdBlacklistReason
	 * 
	 * @param frdBlacklistReasonList
	 * @return the list of persisted FrdBlacklistReason
	 */
	public ArrayList<FrdBlacklistReason> persistFrdBlacklistReasonList(
			List<FrdBlacklistReason> frdBlacklistReasonList);

	/**
	 * mergeFrdBlacklistReason - merges a FrdBlacklistReason
	 * 
	 * @param frdBlacklistReason
	 * @return the merged FrdBlacklistReason
	 */
	public FrdBlacklistReason mergeFrdBlacklistReason(FrdBlacklistReason frdBlacklistReason);

	/**
	 * mergeFrdBlacklistReasonList - merges a list of FrdBlacklistReason
	 * 
	 * @param frdBlacklistReasonList
	 * @return the merged list of FrdBlacklistReason
	 */
	public ArrayList<FrdBlacklistReason> mergeFrdBlacklistReasonList(
			List<FrdBlacklistReason> frdBlacklistReasonList);

	/**
	 * removeFrdBlacklistReason - removes a FrdBlacklistReason
	 * 
	 * @param frdBlacklistReason
	 */
	public void removeFrdBlacklistReason(FrdBlacklistReason frdBlacklistReason);

	/**
	 * removeFrdBlacklistReasonList - removes a list of FrdBlacklistReason
	 * 
	 * @param frdBlacklistReasonList
	 */
	public void removeFrdBlacklistReasonList(List<FrdBlacklistReason> frdBlacklistReasonList);

	/**
	 * findByFrdBlacklistReasonLike - finds a list of FrdBlacklistReason Like
	 * 
	 * @param frdBlacklistReason
	 * @return the list of FrdBlacklistReason found
	 */
	public List<FrdBlacklistReason> findByFrdBlacklistReasonLike(FrdBlacklistReason frdBlacklistReason,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdBlacklistReason>LikeFR - finds a list of FrdBlacklistReason> Like with a finder return object
	 * 
	 * @param frdBlacklistReason
	 * @return the list of FrdBlacklistReason found
	 */
	public FinderReturn findByFrdBlacklistReasonLikeFR(FrdBlacklistReason frdBlacklistReason,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
