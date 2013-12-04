package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdFraudRelatedOrderInfo;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdFraudRelatedOrderInfoSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdFraudRelatedOrderInfo> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote#persistFrdFraudRelatedOrderInfo(com
	 * .gdn.venice.persistence.FrdFraudRelatedOrderInfo)
	 */
	public FrdFraudRelatedOrderInfo persistFrdFraudRelatedOrderInfo(FrdFraudRelatedOrderInfo frdFraudRelatedOrderInfo);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote#persistFrdFraudRelatedOrderInfoList
	 * (java.util.List)
	 */
	public ArrayList<FrdFraudRelatedOrderInfo> persistFrdFraudRelatedOrderInfoList(
			List<FrdFraudRelatedOrderInfo> frdFraudRelatedOrderInfoList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote#mergeFrdFraudRelatedOrderInfo(com.
	 * gdn.venice.persistence.FrdFraudRelatedOrderInfo)
	 */
	public FrdFraudRelatedOrderInfo mergeFrdFraudRelatedOrderInfo(FrdFraudRelatedOrderInfo frdFraudRelatedOrderInfo);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote#mergeFrdFraudRelatedOrderInfoList(
	 * java.util.List)
	 */
	public ArrayList<FrdFraudRelatedOrderInfo> mergeFrdFraudRelatedOrderInfoList(
			List<FrdFraudRelatedOrderInfo> frdFraudRelatedOrderInfoList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote#removeFrdFraudRelatedOrderInfo(com
	 * .gdn.venice.persistence.FrdFraudRelatedOrderInfo)
	 */
	public void removeFrdFraudRelatedOrderInfo(FrdFraudRelatedOrderInfo frdFraudRelatedOrderInfo);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote#removeFrdFraudRelatedOrderInfoList
	 * (java.util.List)
	 */
	public void removeFrdFraudRelatedOrderInfoList(List<FrdFraudRelatedOrderInfo> frdFraudRelatedOrderInfoList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote#findByFrdFraudRelatedOrderInfoLike
	 * (com.gdn.venice.persistence.FrdFraudRelatedOrderInfo, int, int)
	 */
	public List<FrdFraudRelatedOrderInfo> findByFrdFraudRelatedOrderInfoLike(FrdFraudRelatedOrderInfo frdFraudRelatedOrderInfo,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote#findByFrdFraudRelatedOrderInfoLikeFR
	 * (com.gdn.venice.persistence.FrdFraudRelatedOrderInfo, int, int)
	 */
	public FinderReturn findByFrdFraudRelatedOrderInfoLikeFR(FrdFraudRelatedOrderInfo frdFraudRelatedOrderInfo,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
