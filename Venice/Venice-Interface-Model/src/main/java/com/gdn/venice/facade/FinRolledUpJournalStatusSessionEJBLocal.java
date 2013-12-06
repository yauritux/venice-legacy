package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinRolledUpJournalStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinRolledUpJournalStatusSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalStatusSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinRolledUpJournalStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalStatusSessionEJBRemote#persistFinRolledUpJournalStatus(com
	 * .gdn.venice.persistence.FinRolledUpJournalStatus)
	 */
	public FinRolledUpJournalStatus persistFinRolledUpJournalStatus(FinRolledUpJournalStatus finRolledUpJournalStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalStatusSessionEJBRemote#persistFinRolledUpJournalStatusList
	 * (java.util.List)
	 */
	public ArrayList<FinRolledUpJournalStatus> persistFinRolledUpJournalStatusList(
			List<FinRolledUpJournalStatus> finRolledUpJournalStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalStatusSessionEJBRemote#mergeFinRolledUpJournalStatus(com.
	 * gdn.venice.persistence.FinRolledUpJournalStatus)
	 */
	public FinRolledUpJournalStatus mergeFinRolledUpJournalStatus(FinRolledUpJournalStatus finRolledUpJournalStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalStatusSessionEJBRemote#mergeFinRolledUpJournalStatusList(
	 * java.util.List)
	 */
	public ArrayList<FinRolledUpJournalStatus> mergeFinRolledUpJournalStatusList(
			List<FinRolledUpJournalStatus> finRolledUpJournalStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalStatusSessionEJBRemote#removeFinRolledUpJournalStatus(com
	 * .gdn.venice.persistence.FinRolledUpJournalStatus)
	 */
	public void removeFinRolledUpJournalStatus(FinRolledUpJournalStatus finRolledUpJournalStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalStatusSessionEJBRemote#removeFinRolledUpJournalStatusList
	 * (java.util.List)
	 */
	public void removeFinRolledUpJournalStatusList(List<FinRolledUpJournalStatus> finRolledUpJournalStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalStatusSessionEJBRemote#findByFinRolledUpJournalStatusLike
	 * (com.gdn.venice.persistence.FinRolledUpJournalStatus, int, int)
	 */
	public List<FinRolledUpJournalStatus> findByFinRolledUpJournalStatusLike(FinRolledUpJournalStatus finRolledUpJournalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalStatusSessionEJBRemote#findByFinRolledUpJournalStatusLikeFR
	 * (com.gdn.venice.persistence.FinRolledUpJournalStatus, int, int)
	 */
	public FinderReturn findByFinRolledUpJournalStatusLikeFR(FinRolledUpJournalStatus finRolledUpJournalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
