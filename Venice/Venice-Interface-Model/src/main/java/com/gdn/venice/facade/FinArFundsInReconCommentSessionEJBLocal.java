package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinArFundsInReconComment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinArFundsInReconCommentSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconCommentSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinArFundsInReconComment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconCommentSessionEJBRemote#persistFinArFundsInReconComment(com
	 * .gdn.venice.persistence.FinArFundsInReconComment)
	 */
	public FinArFundsInReconComment persistFinArFundsInReconComment(FinArFundsInReconComment finArFundsInReconComment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconCommentSessionEJBRemote#persistFinArFundsInReconCommentList
	 * (java.util.List)
	 */
	public ArrayList<FinArFundsInReconComment> persistFinArFundsInReconCommentList(
			List<FinArFundsInReconComment> finArFundsInReconCommentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconCommentSessionEJBRemote#mergeFinArFundsInReconComment(com.
	 * gdn.venice.persistence.FinArFundsInReconComment)
	 */
	public FinArFundsInReconComment mergeFinArFundsInReconComment(FinArFundsInReconComment finArFundsInReconComment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconCommentSessionEJBRemote#mergeFinArFundsInReconCommentList(
	 * java.util.List)
	 */
	public ArrayList<FinArFundsInReconComment> mergeFinArFundsInReconCommentList(
			List<FinArFundsInReconComment> finArFundsInReconCommentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconCommentSessionEJBRemote#removeFinArFundsInReconComment(com
	 * .gdn.venice.persistence.FinArFundsInReconComment)
	 */
	public void removeFinArFundsInReconComment(FinArFundsInReconComment finArFundsInReconComment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconCommentSessionEJBRemote#removeFinArFundsInReconCommentList
	 * (java.util.List)
	 */
	public void removeFinArFundsInReconCommentList(List<FinArFundsInReconComment> finArFundsInReconCommentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconCommentSessionEJBRemote#findByFinArFundsInReconCommentLike
	 * (com.gdn.venice.persistence.FinArFundsInReconComment, int, int)
	 */
	public List<FinArFundsInReconComment> findByFinArFundsInReconCommentLike(FinArFundsInReconComment finArFundsInReconComment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconCommentSessionEJBRemote#findByFinArFundsInReconCommentLikeFR
	 * (com.gdn.venice.persistence.FinArFundsInReconComment, int, int)
	 */
	public FinderReturn findByFinArFundsInReconCommentLikeFR(FinArFundsInReconComment finArFundsInReconComment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
