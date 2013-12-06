package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinJournalType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinJournalTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinJournalType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTypeSessionEJBRemote#persistFinJournalType(com
	 * .gdn.venice.persistence.FinJournalType)
	 */
	public FinJournalType persistFinJournalType(FinJournalType finJournalType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTypeSessionEJBRemote#persistFinJournalTypeList
	 * (java.util.List)
	 */
	public ArrayList<FinJournalType> persistFinJournalTypeList(
			List<FinJournalType> finJournalTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTypeSessionEJBRemote#mergeFinJournalType(com.
	 * gdn.venice.persistence.FinJournalType)
	 */
	public FinJournalType mergeFinJournalType(FinJournalType finJournalType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTypeSessionEJBRemote#mergeFinJournalTypeList(
	 * java.util.List)
	 */
	public ArrayList<FinJournalType> mergeFinJournalTypeList(
			List<FinJournalType> finJournalTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTypeSessionEJBRemote#removeFinJournalType(com
	 * .gdn.venice.persistence.FinJournalType)
	 */
	public void removeFinJournalType(FinJournalType finJournalType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTypeSessionEJBRemote#removeFinJournalTypeList
	 * (java.util.List)
	 */
	public void removeFinJournalTypeList(List<FinJournalType> finJournalTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTypeSessionEJBRemote#findByFinJournalTypeLike
	 * (com.gdn.venice.persistence.FinJournalType, int, int)
	 */
	public List<FinJournalType> findByFinJournalTypeLike(FinJournalType finJournalType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinJournalTypeSessionEJBRemote#findByFinJournalTypeLikeFR
	 * (com.gdn.venice.persistence.FinJournalType, int, int)
	 */
	public FinderReturn findByFinJournalTypeLikeFR(FinJournalType finJournalType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
