package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdFraudActionLog;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdFraudActionLogSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdFraudActionLog> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote#persistFrdFraudActionLog(com
	 * .gdn.venice.persistence.FrdFraudActionLog)
	 */
	public FrdFraudActionLog persistFrdFraudActionLog(FrdFraudActionLog frdFraudActionLog);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote#persistFrdFraudActionLogList
	 * (java.util.List)
	 */
	public ArrayList<FrdFraudActionLog> persistFrdFraudActionLogList(
			List<FrdFraudActionLog> frdFraudActionLogList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote#mergeFrdFraudActionLog(com.
	 * gdn.venice.persistence.FrdFraudActionLog)
	 */
	public FrdFraudActionLog mergeFrdFraudActionLog(FrdFraudActionLog frdFraudActionLog);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote#mergeFrdFraudActionLogList(
	 * java.util.List)
	 */
	public ArrayList<FrdFraudActionLog> mergeFrdFraudActionLogList(
			List<FrdFraudActionLog> frdFraudActionLogList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote#removeFrdFraudActionLog(com
	 * .gdn.venice.persistence.FrdFraudActionLog)
	 */
	public void removeFrdFraudActionLog(FrdFraudActionLog frdFraudActionLog);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote#removeFrdFraudActionLogList
	 * (java.util.List)
	 */
	public void removeFrdFraudActionLogList(List<FrdFraudActionLog> frdFraudActionLogList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote#findByFrdFraudActionLogLike
	 * (com.gdn.venice.persistence.FrdFraudActionLog, int, int)
	 */
	public List<FrdFraudActionLog> findByFrdFraudActionLogLike(FrdFraudActionLog frdFraudActionLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote#findByFrdFraudActionLogLikeFR
	 * (com.gdn.venice.persistence.FrdFraudActionLog, int, int)
	 */
	public FinderReturn findByFrdFraudActionLogLikeFR(FrdFraudActionLog frdFraudActionLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
