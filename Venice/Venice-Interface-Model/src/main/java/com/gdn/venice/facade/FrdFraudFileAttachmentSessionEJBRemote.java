package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdFraudFileAttachment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdFraudFileAttachmentSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdFraudFileAttachment
	 */
	public List<FrdFraudFileAttachment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdFraudFileAttachment persists a country
	 * 
	 * @param frdFraudFileAttachment
	 * @return the persisted FrdFraudFileAttachment
	 */
	public FrdFraudFileAttachment persistFrdFraudFileAttachment(FrdFraudFileAttachment frdFraudFileAttachment);

	/**
	 * persistFrdFraudFileAttachmentList - persists a list of FrdFraudFileAttachment
	 * 
	 * @param frdFraudFileAttachmentList
	 * @return the list of persisted FrdFraudFileAttachment
	 */
	public ArrayList<FrdFraudFileAttachment> persistFrdFraudFileAttachmentList(
			List<FrdFraudFileAttachment> frdFraudFileAttachmentList);

	/**
	 * mergeFrdFraudFileAttachment - merges a FrdFraudFileAttachment
	 * 
	 * @param frdFraudFileAttachment
	 * @return the merged FrdFraudFileAttachment
	 */
	public FrdFraudFileAttachment mergeFrdFraudFileAttachment(FrdFraudFileAttachment frdFraudFileAttachment);

	/**
	 * mergeFrdFraudFileAttachmentList - merges a list of FrdFraudFileAttachment
	 * 
	 * @param frdFraudFileAttachmentList
	 * @return the merged list of FrdFraudFileAttachment
	 */
	public ArrayList<FrdFraudFileAttachment> mergeFrdFraudFileAttachmentList(
			List<FrdFraudFileAttachment> frdFraudFileAttachmentList);

	/**
	 * removeFrdFraudFileAttachment - removes a FrdFraudFileAttachment
	 * 
	 * @param frdFraudFileAttachment
	 */
	public void removeFrdFraudFileAttachment(FrdFraudFileAttachment frdFraudFileAttachment);

	/**
	 * removeFrdFraudFileAttachmentList - removes a list of FrdFraudFileAttachment
	 * 
	 * @param frdFraudFileAttachmentList
	 */
	public void removeFrdFraudFileAttachmentList(List<FrdFraudFileAttachment> frdFraudFileAttachmentList);

	/**
	 * findByFrdFraudFileAttachmentLike - finds a list of FrdFraudFileAttachment Like
	 * 
	 * @param frdFraudFileAttachment
	 * @return the list of FrdFraudFileAttachment found
	 */
	public List<FrdFraudFileAttachment> findByFrdFraudFileAttachmentLike(FrdFraudFileAttachment frdFraudFileAttachment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdFraudFileAttachment>LikeFR - finds a list of FrdFraudFileAttachment> Like with a finder return object
	 * 
	 * @param frdFraudFileAttachment
	 * @return the list of FrdFraudFileAttachment found
	 */
	public FinderReturn findByFrdFraudFileAttachmentLikeFR(FrdFraudFileAttachment frdFraudFileAttachment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
