package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdFraudFileAttachment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdFraudFileAttachmentSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdFraudFileAttachment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote#persistFrdFraudFileAttachment(com
	 * .gdn.venice.persistence.FrdFraudFileAttachment)
	 */
	public FrdFraudFileAttachment persistFrdFraudFileAttachment(FrdFraudFileAttachment frdFraudFileAttachment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote#persistFrdFraudFileAttachmentList
	 * (java.util.List)
	 */
	public ArrayList<FrdFraudFileAttachment> persistFrdFraudFileAttachmentList(
			List<FrdFraudFileAttachment> frdFraudFileAttachmentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote#mergeFrdFraudFileAttachment(com.
	 * gdn.venice.persistence.FrdFraudFileAttachment)
	 */
	public FrdFraudFileAttachment mergeFrdFraudFileAttachment(FrdFraudFileAttachment frdFraudFileAttachment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote#mergeFrdFraudFileAttachmentList(
	 * java.util.List)
	 */
	public ArrayList<FrdFraudFileAttachment> mergeFrdFraudFileAttachmentList(
			List<FrdFraudFileAttachment> frdFraudFileAttachmentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote#removeFrdFraudFileAttachment(com
	 * .gdn.venice.persistence.FrdFraudFileAttachment)
	 */
	public void removeFrdFraudFileAttachment(FrdFraudFileAttachment frdFraudFileAttachment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote#removeFrdFraudFileAttachmentList
	 * (java.util.List)
	 */
	public void removeFrdFraudFileAttachmentList(List<FrdFraudFileAttachment> frdFraudFileAttachmentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote#findByFrdFraudFileAttachmentLike
	 * (com.gdn.venice.persistence.FrdFraudFileAttachment, int, int)
	 */
	public List<FrdFraudFileAttachment> findByFrdFraudFileAttachmentLike(FrdFraudFileAttachment frdFraudFileAttachment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote#findByFrdFraudFileAttachmentLikeFR
	 * (com.gdn.venice.persistence.FrdFraudFileAttachment, int, int)
	 */
	public FinderReturn findByFrdFraudFileAttachmentLikeFR(FrdFraudFileAttachment frdFraudFileAttachment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
