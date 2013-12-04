package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinJournalApprovalGroup;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinJournalApprovalGroupSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinJournalApprovalGroup
	 */
	public List<FinJournalApprovalGroup> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinJournalApprovalGroup persists a country
	 * 
	 * @param finJournalApprovalGroup
	 * @return the persisted FinJournalApprovalGroup
	 */
	public FinJournalApprovalGroup persistFinJournalApprovalGroup(FinJournalApprovalGroup finJournalApprovalGroup);

	/**
	 * persistFinJournalApprovalGroupList - persists a list of FinJournalApprovalGroup
	 * 
	 * @param finJournalApprovalGroupList
	 * @return the list of persisted FinJournalApprovalGroup
	 */
	public ArrayList<FinJournalApprovalGroup> persistFinJournalApprovalGroupList(
			List<FinJournalApprovalGroup> finJournalApprovalGroupList);

	/**
	 * mergeFinJournalApprovalGroup - merges a FinJournalApprovalGroup
	 * 
	 * @param finJournalApprovalGroup
	 * @return the merged FinJournalApprovalGroup
	 */
	public FinJournalApprovalGroup mergeFinJournalApprovalGroup(FinJournalApprovalGroup finJournalApprovalGroup);

	/**
	 * mergeFinJournalApprovalGroupList - merges a list of FinJournalApprovalGroup
	 * 
	 * @param finJournalApprovalGroupList
	 * @return the merged list of FinJournalApprovalGroup
	 */
	public ArrayList<FinJournalApprovalGroup> mergeFinJournalApprovalGroupList(
			List<FinJournalApprovalGroup> finJournalApprovalGroupList);

	/**
	 * removeFinJournalApprovalGroup - removes a FinJournalApprovalGroup
	 * 
	 * @param finJournalApprovalGroup
	 */
	public void removeFinJournalApprovalGroup(FinJournalApprovalGroup finJournalApprovalGroup);

	/**
	 * removeFinJournalApprovalGroupList - removes a list of FinJournalApprovalGroup
	 * 
	 * @param finJournalApprovalGroupList
	 */
	public void removeFinJournalApprovalGroupList(List<FinJournalApprovalGroup> finJournalApprovalGroupList);

	/**
	 * findByFinJournalApprovalGroupLike - finds a list of FinJournalApprovalGroup Like
	 * 
	 * @param finJournalApprovalGroup
	 * @return the list of FinJournalApprovalGroup found
	 */
	public List<FinJournalApprovalGroup> findByFinJournalApprovalGroupLike(FinJournalApprovalGroup finJournalApprovalGroup,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinJournalApprovalGroup>LikeFR - finds a list of FinJournalApprovalGroup> Like with a finder return object
	 * 
	 * @param finJournalApprovalGroup
	 * @return the list of FinJournalApprovalGroup found
	 */
	public FinderReturn findByFinJournalApprovalGroupLikeFR(FinJournalApprovalGroup finJournalApprovalGroup,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
