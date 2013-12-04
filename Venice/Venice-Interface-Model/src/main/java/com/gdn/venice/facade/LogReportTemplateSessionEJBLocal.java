package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogReportTemplate;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogReportTemplateSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportTemplateSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogReportTemplate> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportTemplateSessionEJBRemote#persistLogReportTemplate(com
	 * .gdn.venice.persistence.LogReportTemplate)
	 */
	public LogReportTemplate persistLogReportTemplate(LogReportTemplate logReportTemplate);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportTemplateSessionEJBRemote#persistLogReportTemplateList
	 * (java.util.List)
	 */
	public ArrayList<LogReportTemplate> persistLogReportTemplateList(
			List<LogReportTemplate> logReportTemplateList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportTemplateSessionEJBRemote#mergeLogReportTemplate(com.
	 * gdn.venice.persistence.LogReportTemplate)
	 */
	public LogReportTemplate mergeLogReportTemplate(LogReportTemplate logReportTemplate);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportTemplateSessionEJBRemote#mergeLogReportTemplateList(
	 * java.util.List)
	 */
	public ArrayList<LogReportTemplate> mergeLogReportTemplateList(
			List<LogReportTemplate> logReportTemplateList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportTemplateSessionEJBRemote#removeLogReportTemplate(com
	 * .gdn.venice.persistence.LogReportTemplate)
	 */
	public void removeLogReportTemplate(LogReportTemplate logReportTemplate);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportTemplateSessionEJBRemote#removeLogReportTemplateList
	 * (java.util.List)
	 */
	public void removeLogReportTemplateList(List<LogReportTemplate> logReportTemplateList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportTemplateSessionEJBRemote#findByLogReportTemplateLike
	 * (com.gdn.venice.persistence.LogReportTemplate, int, int)
	 */
	public List<LogReportTemplate> findByLogReportTemplateLike(LogReportTemplate logReportTemplate,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportTemplateSessionEJBRemote#findByLogReportTemplateLikeFR
	 * (com.gdn.venice.persistence.LogReportTemplate, int, int)
	 */
	public FinderReturn findByLogReportTemplateLikeFR(LogReportTemplate logReportTemplate,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
