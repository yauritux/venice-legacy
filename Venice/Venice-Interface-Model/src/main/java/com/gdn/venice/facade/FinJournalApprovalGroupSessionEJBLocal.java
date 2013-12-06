package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinJournalApprovalGroup;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinJournalApprovalGroupSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinJournalApprovalGroup> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote#persistFinJournalApprovalGroup(com
	 * .gdn.venice.persistence.FinJournalApprovalGroup)
	 */
	public FinJournalApprovalGroup persistFinJournalApprovalGroup(FinJournalApprovalGroup finJournalApprovalGroup);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote#persistFinJournalApprovalGroupList
	 * (java.util.List)
	 */
	public ArrayList<FinJournalApprovalGroup> persistFinJournalApprovalGroupList(
			List<FinJournalApprovalGroup> finJournalApprovalGroupList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote#mergeFinJournalApprovalGroup(com.
	 * gdn.venice.persistence.FinJournalApprovalGroup)
	 */
	public FinJournalApprovalGroup mergeFinJournalApprovalGroup(FinJournalApprovalGroup finJournalApprovalGroup);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote#mergeFinJournalApprovalGroupList(
	 * java.util.List)
	 */
	public ArrayList<FinJournalApprovalGroup> mergeFinJournalApprovalGroupList(
			List<FinJournalApprovalGroup> finJournalApprovalGroupList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote#removeFinJournalApprovalGroup(com
	 * .gdn.venice.persistence.FinJournalApprovalGroup)
	 */
	public void removeFinJournalApprovalGroup(FinJournalApprovalGroup finJournalApprovalGroup);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote#removeFinJournalApprovalGroupList
	 * (java.util.List)
	 */
	public void removeFinJournalApprovalGroupList(List<FinJournalApprovalGroup> finJournalApprovalGroupList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote#findByFinJournalApprovalGroupLike
	 * (com.gdn.venice.persistence.FinJournalApprovalGroup, int, int)
	 */
	public List<FinJournalApprovalGroup> findByFinJournalApprovalGroupLike(FinJournalApprovalGroup finJournalApprovalGroup,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote#findByFinJournalApprovalGroupLikeFR
	 * (com.gdn.venice.persistence.FinJournalApprovalGroup, int, int)
	 */
	public FinderReturn findByFinJournalApprovalGroupLikeFR(FinJournalApprovalGroup finJournalApprovalGroup,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
