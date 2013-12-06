package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArFundsInReconComment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinArFundsInReconCommentSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinArFundsInReconComment
	 */
	public List<FinArFundsInReconComment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinArFundsInReconComment persists a country
	 * 
	 * @param finArFundsInReconComment
	 * @return the persisted FinArFundsInReconComment
	 */
	public FinArFundsInReconComment persistFinArFundsInReconComment(FinArFundsInReconComment finArFundsInReconComment);

	/**
	 * persistFinArFundsInReconCommentList - persists a list of FinArFundsInReconComment
	 * 
	 * @param finArFundsInReconCommentList
	 * @return the list of persisted FinArFundsInReconComment
	 */
	public ArrayList<FinArFundsInReconComment> persistFinArFundsInReconCommentList(
			List<FinArFundsInReconComment> finArFundsInReconCommentList);

	/**
	 * mergeFinArFundsInReconComment - merges a FinArFundsInReconComment
	 * 
	 * @param finArFundsInReconComment
	 * @return the merged FinArFundsInReconComment
	 */
	public FinArFundsInReconComment mergeFinArFundsInReconComment(FinArFundsInReconComment finArFundsInReconComment);

	/**
	 * mergeFinArFundsInReconCommentList - merges a list of FinArFundsInReconComment
	 * 
	 * @param finArFundsInReconCommentList
	 * @return the merged list of FinArFundsInReconComment
	 */
	public ArrayList<FinArFundsInReconComment> mergeFinArFundsInReconCommentList(
			List<FinArFundsInReconComment> finArFundsInReconCommentList);

	/**
	 * removeFinArFundsInReconComment - removes a FinArFundsInReconComment
	 * 
	 * @param finArFundsInReconComment
	 */
	public void removeFinArFundsInReconComment(FinArFundsInReconComment finArFundsInReconComment);

	/**
	 * removeFinArFundsInReconCommentList - removes a list of FinArFundsInReconComment
	 * 
	 * @param finArFundsInReconCommentList
	 */
	public void removeFinArFundsInReconCommentList(List<FinArFundsInReconComment> finArFundsInReconCommentList);

	/**
	 * findByFinArFundsInReconCommentLike - finds a list of FinArFundsInReconComment Like
	 * 
	 * @param finArFundsInReconComment
	 * @return the list of FinArFundsInReconComment found
	 */
	public List<FinArFundsInReconComment> findByFinArFundsInReconCommentLike(FinArFundsInReconComment finArFundsInReconComment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinArFundsInReconComment>LikeFR - finds a list of FinArFundsInReconComment> Like with a finder return object
	 * 
	 * @param finArFundsInReconComment
	 * @return the list of FinArFundsInReconComment found
	 */
	public FinderReturn findByFinArFundsInReconCommentLikeFR(FinArFundsInReconComment finArFundsInReconComment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
