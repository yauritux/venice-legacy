package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogActivityReportUpload;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogActivityReportUploadSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReportUploadSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogActivityReportUpload> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReportUploadSessionEJBRemote#persistLogActivityReportUpload(com
	 * .gdn.venice.persistence.LogActivityReportUpload)
	 */
	public LogActivityReportUpload persistLogActivityReportUpload(LogActivityReportUpload logActivityReportUpload);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReportUploadSessionEJBRemote#persistLogActivityReportUploadList
	 * (java.util.List)
	 */
	public ArrayList<LogActivityReportUpload> persistLogActivityReportUploadList(
			List<LogActivityReportUpload> logActivityReportUploadList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReportUploadSessionEJBRemote#mergeLogActivityReportUpload(com.
	 * gdn.venice.persistence.LogActivityReportUpload)
	 */
	public LogActivityReportUpload mergeLogActivityReportUpload(LogActivityReportUpload logActivityReportUpload);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReportUploadSessionEJBRemote#mergeLogActivityReportUploadList(
	 * java.util.List)
	 */
	public ArrayList<LogActivityReportUpload> mergeLogActivityReportUploadList(
			List<LogActivityReportUpload> logActivityReportUploadList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReportUploadSessionEJBRemote#removeLogActivityReportUpload(com
	 * .gdn.venice.persistence.LogActivityReportUpload)
	 */
	public void removeLogActivityReportUpload(LogActivityReportUpload logActivityReportUpload);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReportUploadSessionEJBRemote#removeLogActivityReportUploadList
	 * (java.util.List)
	 */
	public void removeLogActivityReportUploadList(List<LogActivityReportUpload> logActivityReportUploadList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReportUploadSessionEJBRemote#findByLogActivityReportUploadLike
	 * (com.gdn.venice.persistence.LogActivityReportUpload, int, int)
	 */
	public List<LogActivityReportUpload> findByLogActivityReportUploadLike(LogActivityReportUpload logActivityReportUpload,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReportUploadSessionEJBRemote#findByLogActivityReportUploadLikeFR
	 * (com.gdn.venice.persistence.LogActivityReportUpload, int, int)
	 */
	public FinderReturn findByLogActivityReportUploadLikeFR(LogActivityReportUpload logActivityReportUpload,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
