package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinRolledUpJournalType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinRolledUpJournalTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinRolledUpJournalType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalTypeSessionEJBRemote#persistFinRolledUpJournalType(com
	 * .gdn.venice.persistence.FinRolledUpJournalType)
	 */
	public FinRolledUpJournalType persistFinRolledUpJournalType(FinRolledUpJournalType finRolledUpJournalType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalTypeSessionEJBRemote#persistFinRolledUpJournalTypeList
	 * (java.util.List)
	 */
	public ArrayList<FinRolledUpJournalType> persistFinRolledUpJournalTypeList(
			List<FinRolledUpJournalType> finRolledUpJournalTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalTypeSessionEJBRemote#mergeFinRolledUpJournalType(com.
	 * gdn.venice.persistence.FinRolledUpJournalType)
	 */
	public FinRolledUpJournalType mergeFinRolledUpJournalType(FinRolledUpJournalType finRolledUpJournalType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalTypeSessionEJBRemote#mergeFinRolledUpJournalTypeList(
	 * java.util.List)
	 */
	public ArrayList<FinRolledUpJournalType> mergeFinRolledUpJournalTypeList(
			List<FinRolledUpJournalType> finRolledUpJournalTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalTypeSessionEJBRemote#removeFinRolledUpJournalType(com
	 * .gdn.venice.persistence.FinRolledUpJournalType)
	 */
	public void removeFinRolledUpJournalType(FinRolledUpJournalType finRolledUpJournalType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalTypeSessionEJBRemote#removeFinRolledUpJournalTypeList
	 * (java.util.List)
	 */
	public void removeFinRolledUpJournalTypeList(List<FinRolledUpJournalType> finRolledUpJournalTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalTypeSessionEJBRemote#findByFinRolledUpJournalTypeLike
	 * (com.gdn.venice.persistence.FinRolledUpJournalType, int, int)
	 */
	public List<FinRolledUpJournalType> findByFinRolledUpJournalTypeLike(FinRolledUpJournalType finRolledUpJournalType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalTypeSessionEJBRemote#findByFinRolledUpJournalTypeLikeFR
	 * (com.gdn.venice.persistence.FinRolledUpJournalType, int, int)
	 */
	public FinderReturn findByFinRolledUpJournalTypeLikeFR(FinRolledUpJournalType finRolledUpJournalType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
