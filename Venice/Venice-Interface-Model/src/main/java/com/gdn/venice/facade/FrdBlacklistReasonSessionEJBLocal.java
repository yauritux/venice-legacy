package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdBlacklistReason;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdBlacklistReasonSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdBlacklistReason> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote#persistFrdBlacklistReason(com
	 * .gdn.venice.persistence.FrdBlacklistReason)
	 */
	public FrdBlacklistReason persistFrdBlacklistReason(FrdBlacklistReason frdBlacklistReason);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote#persistFrdBlacklistReasonList
	 * (java.util.List)
	 */
	public ArrayList<FrdBlacklistReason> persistFrdBlacklistReasonList(
			List<FrdBlacklistReason> frdBlacklistReasonList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote#mergeFrdBlacklistReason(com.
	 * gdn.venice.persistence.FrdBlacklistReason)
	 */
	public FrdBlacklistReason mergeFrdBlacklistReason(FrdBlacklistReason frdBlacklistReason);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote#mergeFrdBlacklistReasonList(
	 * java.util.List)
	 */
	public ArrayList<FrdBlacklistReason> mergeFrdBlacklistReasonList(
			List<FrdBlacklistReason> frdBlacklistReasonList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote#removeFrdBlacklistReason(com
	 * .gdn.venice.persistence.FrdBlacklistReason)
	 */
	public void removeFrdBlacklistReason(FrdBlacklistReason frdBlacklistReason);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote#removeFrdBlacklistReasonList
	 * (java.util.List)
	 */
	public void removeFrdBlacklistReasonList(List<FrdBlacklistReason> frdBlacklistReasonList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote#findByFrdBlacklistReasonLike
	 * (com.gdn.venice.persistence.FrdBlacklistReason, int, int)
	 */
	public List<FrdBlacklistReason> findByFrdBlacklistReasonLike(FrdBlacklistReason frdBlacklistReason,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote#findByFrdBlacklistReasonLikeFR
	 * (com.gdn.venice.persistence.FrdBlacklistReason, int, int)
	 */
	public FinderReturn findByFrdBlacklistReasonLikeFR(FrdBlacklistReason frdBlacklistReason,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
