package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArFundsInReportType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinArFundsInReportTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinArFundsInReportType
	 */
	public List<FinArFundsInReportType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinArFundsInReportType persists a country
	 * 
	 * @param finArFundsInReportType
	 * @return the persisted FinArFundsInReportType
	 */
	public FinArFundsInReportType persistFinArFundsInReportType(FinArFundsInReportType finArFundsInReportType);

	/**
	 * persistFinArFundsInReportTypeList - persists a list of FinArFundsInReportType
	 * 
	 * @param finArFundsInReportTypeList
	 * @return the list of persisted FinArFundsInReportType
	 */
	public ArrayList<FinArFundsInReportType> persistFinArFundsInReportTypeList(
			List<FinArFundsInReportType> finArFundsInReportTypeList);

	/**
	 * mergeFinArFundsInReportType - merges a FinArFundsInReportType
	 * 
	 * @param finArFundsInReportType
	 * @return the merged FinArFundsInReportType
	 */
	public FinArFundsInReportType mergeFinArFundsInReportType(FinArFundsInReportType finArFundsInReportType);

	/**
	 * mergeFinArFundsInReportTypeList - merges a list of FinArFundsInReportType
	 * 
	 * @param finArFundsInReportTypeList
	 * @return the merged list of FinArFundsInReportType
	 */
	public ArrayList<FinArFundsInReportType> mergeFinArFundsInReportTypeList(
			List<FinArFundsInReportType> finArFundsInReportTypeList);

	/**
	 * removeFinArFundsInReportType - removes a FinArFundsInReportType
	 * 
	 * @param finArFundsInReportType
	 */
	public void removeFinArFundsInReportType(FinArFundsInReportType finArFundsInReportType);

	/**
	 * removeFinArFundsInReportTypeList - removes a list of FinArFundsInReportType
	 * 
	 * @param finArFundsInReportTypeList
	 */
	public void removeFinArFundsInReportTypeList(List<FinArFundsInReportType> finArFundsInReportTypeList);

	/**
	 * findByFinArFundsInReportTypeLike - finds a list of FinArFundsInReportType Like
	 * 
	 * @param finArFundsInReportType
	 * @return the list of FinArFundsInReportType found
	 */
	public List<FinArFundsInReportType> findByFinArFundsInReportTypeLike(FinArFundsInReportType finArFundsInReportType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinArFundsInReportType>LikeFR - finds a list of FinArFundsInReportType> Like with a finder return object
	 * 
	 * @param finArFundsInReportType
	 * @return the list of FinArFundsInReportType found
	 */
	public FinderReturn findByFinArFundsInReportTypeLikeFR(FinArFundsInReportType finArFundsInReportType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
