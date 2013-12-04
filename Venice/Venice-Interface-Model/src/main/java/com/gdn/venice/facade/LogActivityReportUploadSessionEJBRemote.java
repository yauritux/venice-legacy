package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogActivityReportUpload;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogActivityReportUploadSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogActivityReportUpload
	 */
	public List<LogActivityReportUpload> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogActivityReportUpload persists a country
	 * 
	 * @param logActivityReportUpload
	 * @return the persisted LogActivityReportUpload
	 */
	public LogActivityReportUpload persistLogActivityReportUpload(LogActivityReportUpload logActivityReportUpload);

	/**
	 * persistLogActivityReportUploadList - persists a list of LogActivityReportUpload
	 * 
	 * @param logActivityReportUploadList
	 * @return the list of persisted LogActivityReportUpload
	 */
	public ArrayList<LogActivityReportUpload> persistLogActivityReportUploadList(
			List<LogActivityReportUpload> logActivityReportUploadList);

	/**
	 * mergeLogActivityReportUpload - merges a LogActivityReportUpload
	 * 
	 * @param logActivityReportUpload
	 * @return the merged LogActivityReportUpload
	 */
	public LogActivityReportUpload mergeLogActivityReportUpload(LogActivityReportUpload logActivityReportUpload);

	/**
	 * mergeLogActivityReportUploadList - merges a list of LogActivityReportUpload
	 * 
	 * @param logActivityReportUploadList
	 * @return the merged list of LogActivityReportUpload
	 */
	public ArrayList<LogActivityReportUpload> mergeLogActivityReportUploadList(
			List<LogActivityReportUpload> logActivityReportUploadList);

	/**
	 * removeLogActivityReportUpload - removes a LogActivityReportUpload
	 * 
	 * @param logActivityReportUpload
	 */
	public void removeLogActivityReportUpload(LogActivityReportUpload logActivityReportUpload);

	/**
	 * removeLogActivityReportUploadList - removes a list of LogActivityReportUpload
	 * 
	 * @param logActivityReportUploadList
	 */
	public void removeLogActivityReportUploadList(List<LogActivityReportUpload> logActivityReportUploadList);

	/**
	 * findByLogActivityReportUploadLike - finds a list of LogActivityReportUpload Like
	 * 
	 * @param logActivityReportUpload
	 * @return the list of LogActivityReportUpload found
	 */
	public List<LogActivityReportUpload> findByLogActivityReportUploadLike(LogActivityReportUpload logActivityReportUpload,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogActivityReportUpload>LikeFR - finds a list of LogActivityReportUpload> Like with a finder return object
	 * 
	 * @param logActivityReportUpload
	 * @return the list of LogActivityReportUpload found
	 */
	public FinderReturn findByLogActivityReportUploadLikeFR(LogActivityReportUpload logActivityReportUpload,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
