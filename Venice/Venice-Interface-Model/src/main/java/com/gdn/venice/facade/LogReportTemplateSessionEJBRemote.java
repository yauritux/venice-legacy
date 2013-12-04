package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogReportTemplate;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogReportTemplateSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogReportTemplate
	 */
	public List<LogReportTemplate> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogReportTemplate persists a country
	 * 
	 * @param logReportTemplate
	 * @return the persisted LogReportTemplate
	 */
	public LogReportTemplate persistLogReportTemplate(LogReportTemplate logReportTemplate);

	/**
	 * persistLogReportTemplateList - persists a list of LogReportTemplate
	 * 
	 * @param logReportTemplateList
	 * @return the list of persisted LogReportTemplate
	 */
	public ArrayList<LogReportTemplate> persistLogReportTemplateList(
			List<LogReportTemplate> logReportTemplateList);

	/**
	 * mergeLogReportTemplate - merges a LogReportTemplate
	 * 
	 * @param logReportTemplate
	 * @return the merged LogReportTemplate
	 */
	public LogReportTemplate mergeLogReportTemplate(LogReportTemplate logReportTemplate);

	/**
	 * mergeLogReportTemplateList - merges a list of LogReportTemplate
	 * 
	 * @param logReportTemplateList
	 * @return the merged list of LogReportTemplate
	 */
	public ArrayList<LogReportTemplate> mergeLogReportTemplateList(
			List<LogReportTemplate> logReportTemplateList);

	/**
	 * removeLogReportTemplate - removes a LogReportTemplate
	 * 
	 * @param logReportTemplate
	 */
	public void removeLogReportTemplate(LogReportTemplate logReportTemplate);

	/**
	 * removeLogReportTemplateList - removes a list of LogReportTemplate
	 * 
	 * @param logReportTemplateList
	 */
	public void removeLogReportTemplateList(List<LogReportTemplate> logReportTemplateList);

	/**
	 * findByLogReportTemplateLike - finds a list of LogReportTemplate Like
	 * 
	 * @param logReportTemplate
	 * @return the list of LogReportTemplate found
	 */
	public List<LogReportTemplate> findByLogReportTemplateLike(LogReportTemplate logReportTemplate,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogReportTemplate>LikeFR - finds a list of LogReportTemplate> Like with a finder return object
	 * 
	 * @param logReportTemplate
	 * @return the list of LogReportTemplate found
	 */
	public FinderReturn findByLogReportTemplateLikeFR(LogReportTemplate logReportTemplate,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
