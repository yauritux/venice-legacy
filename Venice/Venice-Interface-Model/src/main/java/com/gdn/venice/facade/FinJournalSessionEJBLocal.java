package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinJournal;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinJournalSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinJournal> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalSessionEJBRemote#persistFinJournal(com
	 * .gdn.venice.persistence.FinJournal)
	 */
	public FinJournal persistFinJournal(FinJournal finJournal);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalSessionEJBRemote#persistFinJournalList
	 * (java.util.List)
	 */
	public ArrayList<FinJournal> persistFinJournalList(
			List<FinJournal> finJournalList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalSessionEJBRemote#mergeFinJournal(com.
	 * gdn.venice.persistence.FinJournal)
	 */
	public FinJournal mergeFinJournal(FinJournal finJournal);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalSessionEJBRemote#mergeFinJournalList(
	 * java.util.List)
	 */
	public ArrayList<FinJournal> mergeFinJournalList(
			List<FinJournal> finJournalList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalSessionEJBRemote#removeFinJournal(com
	 * .gdn.venice.persistence.FinJournal)
	 */
	public void removeFinJournal(FinJournal finJournal);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalSessionEJBRemote#removeFinJournalList
	 * (java.util.List)
	 */
	public void removeFinJournalList(List<FinJournal> finJournalList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalSessionEJBRemote#findByFinJournalLike
	 * (com.gdn.venice.persistence.FinJournal, int, int)
	 */
	public List<FinJournal> findByFinJournalLike(FinJournal finJournal,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalSessionEJBRemote#findByFinJournalLikeFR
	 * (com.gdn.venice.persistence.FinJournal, int, int)
	 */
	public FinderReturn findByFinJournalLikeFR(FinJournal finJournal,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
