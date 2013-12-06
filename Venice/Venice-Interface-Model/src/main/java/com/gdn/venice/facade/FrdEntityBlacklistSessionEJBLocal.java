package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdEntityBlacklist;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdEntityBlacklistSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdEntityBlacklist> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote#persistFrdEntityBlacklist(com
	 * .gdn.venice.persistence.FrdEntityBlacklist)
	 */
	public FrdEntityBlacklist persistFrdEntityBlacklist(FrdEntityBlacklist frdEntityBlacklist);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote#persistFrdEntityBlacklistList
	 * (java.util.List)
	 */
	public ArrayList<FrdEntityBlacklist> persistFrdEntityBlacklistList(
			List<FrdEntityBlacklist> frdEntityBlacklistList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote#mergeFrdEntityBlacklist(com.
	 * gdn.venice.persistence.FrdEntityBlacklist)
	 */
	public FrdEntityBlacklist mergeFrdEntityBlacklist(FrdEntityBlacklist frdEntityBlacklist);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote#mergeFrdEntityBlacklistList(
	 * java.util.List)
	 */
	public ArrayList<FrdEntityBlacklist> mergeFrdEntityBlacklistList(
			List<FrdEntityBlacklist> frdEntityBlacklistList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote#removeFrdEntityBlacklist(com
	 * .gdn.venice.persistence.FrdEntityBlacklist)
	 */
	public void removeFrdEntityBlacklist(FrdEntityBlacklist frdEntityBlacklist);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote#removeFrdEntityBlacklistList
	 * (java.util.List)
	 */
	public void removeFrdEntityBlacklistList(List<FrdEntityBlacklist> frdEntityBlacklistList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote#findByFrdEntityBlacklistLike
	 * (com.gdn.venice.persistence.FrdEntityBlacklist, int, int)
	 */
	public List<FrdEntityBlacklist> findByFrdEntityBlacklistLike(FrdEntityBlacklist frdEntityBlacklist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote#findByFrdEntityBlacklistLikeFR
	 * (com.gdn.venice.persistence.FrdEntityBlacklist, int, int)
	 */
	public FinderReturn findByFrdEntityBlacklistLikeFR(FrdEntityBlacklist frdEntityBlacklist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
