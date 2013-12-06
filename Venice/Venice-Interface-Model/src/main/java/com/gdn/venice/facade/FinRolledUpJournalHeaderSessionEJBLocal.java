package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinRolledUpJournalHeader;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinRolledUpJournalHeaderSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinRolledUpJournalHeader> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#persistFinRolledUpJournalHeader(com
	 * .gdn.venice.persistence.FinRolledUpJournalHeader)
	 */
	public FinRolledUpJournalHeader persistFinRolledUpJournalHeader(FinRolledUpJournalHeader finRolledUpJournalHeader);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#persistFinRolledUpJournalHeaderList
	 * (java.util.List)
	 */
	public ArrayList<FinRolledUpJournalHeader> persistFinRolledUpJournalHeaderList(
			List<FinRolledUpJournalHeader> finRolledUpJournalHeaderList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#mergeFinRolledUpJournalHeader(com.
	 * gdn.venice.persistence.FinRolledUpJournalHeader)
	 */
	public FinRolledUpJournalHeader mergeFinRolledUpJournalHeader(FinRolledUpJournalHeader finRolledUpJournalHeader);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#mergeFinRolledUpJournalHeaderList(
	 * java.util.List)
	 */
	public ArrayList<FinRolledUpJournalHeader> mergeFinRolledUpJournalHeaderList(
			List<FinRolledUpJournalHeader> finRolledUpJournalHeaderList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#removeFinRolledUpJournalHeader(com
	 * .gdn.venice.persistence.FinRolledUpJournalHeader)
	 */
	public void removeFinRolledUpJournalHeader(FinRolledUpJournalHeader finRolledUpJournalHeader);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#removeFinRolledUpJournalHeaderList
	 * (java.util.List)
	 */
	public void removeFinRolledUpJournalHeaderList(List<FinRolledUpJournalHeader> finRolledUpJournalHeaderList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#findByFinRolledUpJournalHeaderLike
	 * (com.gdn.venice.persistence.FinRolledUpJournalHeader, int, int)
	 */
	public List<FinRolledUpJournalHeader> findByFinRolledUpJournalHeaderLike(FinRolledUpJournalHeader finRolledUpJournalHeader,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#findByFinRolledUpJournalHeaderLikeFR
	 * (com.gdn.venice.persistence.FinRolledUpJournalHeader, int, int)
	 */
	public FinderReturn findByFinRolledUpJournalHeaderLikeFR(FinRolledUpJournalHeader finRolledUpJournalHeader,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
