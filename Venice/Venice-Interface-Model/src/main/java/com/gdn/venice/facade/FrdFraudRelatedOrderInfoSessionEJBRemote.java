package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdFraudRelatedOrderInfo;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdFraudRelatedOrderInfoSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdFraudRelatedOrderInfo
	 */
	public List<FrdFraudRelatedOrderInfo> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdFraudRelatedOrderInfo persists a country
	 * 
	 * @param frdFraudRelatedOrderInfo
	 * @return the persisted FrdFraudRelatedOrderInfo
	 */
	public FrdFraudRelatedOrderInfo persistFrdFraudRelatedOrderInfo(FrdFraudRelatedOrderInfo frdFraudRelatedOrderInfo);

	/**
	 * persistFrdFraudRelatedOrderInfoList - persists a list of FrdFraudRelatedOrderInfo
	 * 
	 * @param frdFraudRelatedOrderInfoList
	 * @return the list of persisted FrdFraudRelatedOrderInfo
	 */
	public ArrayList<FrdFraudRelatedOrderInfo> persistFrdFraudRelatedOrderInfoList(
			List<FrdFraudRelatedOrderInfo> frdFraudRelatedOrderInfoList);

	/**
	 * mergeFrdFraudRelatedOrderInfo - merges a FrdFraudRelatedOrderInfo
	 * 
	 * @param frdFraudRelatedOrderInfo
	 * @return the merged FrdFraudRelatedOrderInfo
	 */
	public FrdFraudRelatedOrderInfo mergeFrdFraudRelatedOrderInfo(FrdFraudRelatedOrderInfo frdFraudRelatedOrderInfo);

	/**
	 * mergeFrdFraudRelatedOrderInfoList - merges a list of FrdFraudRelatedOrderInfo
	 * 
	 * @param frdFraudRelatedOrderInfoList
	 * @return the merged list of FrdFraudRelatedOrderInfo
	 */
	public ArrayList<FrdFraudRelatedOrderInfo> mergeFrdFraudRelatedOrderInfoList(
			List<FrdFraudRelatedOrderInfo> frdFraudRelatedOrderInfoList);

	/**
	 * removeFrdFraudRelatedOrderInfo - removes a FrdFraudRelatedOrderInfo
	 * 
	 * @param frdFraudRelatedOrderInfo
	 */
	public void removeFrdFraudRelatedOrderInfo(FrdFraudRelatedOrderInfo frdFraudRelatedOrderInfo);

	/**
	 * removeFrdFraudRelatedOrderInfoList - removes a list of FrdFraudRelatedOrderInfo
	 * 
	 * @param frdFraudRelatedOrderInfoList
	 */
	public void removeFrdFraudRelatedOrderInfoList(List<FrdFraudRelatedOrderInfo> frdFraudRelatedOrderInfoList);

	/**
	 * findByFrdFraudRelatedOrderInfoLike - finds a list of FrdFraudRelatedOrderInfo Like
	 * 
	 * @param frdFraudRelatedOrderInfo
	 * @return the list of FrdFraudRelatedOrderInfo found
	 */
	public List<FrdFraudRelatedOrderInfo> findByFrdFraudRelatedOrderInfoLike(FrdFraudRelatedOrderInfo frdFraudRelatedOrderInfo,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdFraudRelatedOrderInfo>LikeFR - finds a list of FrdFraudRelatedOrderInfo> Like with a finder return object
	 * 
	 * @param frdFraudRelatedOrderInfo
	 * @return the list of FrdFraudRelatedOrderInfo found
	 */
	public FinderReturn findByFrdFraudRelatedOrderInfoLikeFR(FrdFraudRelatedOrderInfo frdFraudRelatedOrderInfo,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
