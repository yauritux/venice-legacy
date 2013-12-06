package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogProviderAgreement;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogProviderAgreementSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogProviderAgreement
	 */
	public List<LogProviderAgreement> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogProviderAgreement persists a country
	 * 
	 * @param logProviderAgreement
	 * @return the persisted LogProviderAgreement
	 */
	public LogProviderAgreement persistLogProviderAgreement(LogProviderAgreement logProviderAgreement);

	/**
	 * persistLogProviderAgreementList - persists a list of LogProviderAgreement
	 * 
	 * @param logProviderAgreementList
	 * @return the list of persisted LogProviderAgreement
	 */
	public ArrayList<LogProviderAgreement> persistLogProviderAgreementList(
			List<LogProviderAgreement> logProviderAgreementList);

	/**
	 * mergeLogProviderAgreement - merges a LogProviderAgreement
	 * 
	 * @param logProviderAgreement
	 * @return the merged LogProviderAgreement
	 */
	public LogProviderAgreement mergeLogProviderAgreement(LogProviderAgreement logProviderAgreement);

	/**
	 * mergeLogProviderAgreementList - merges a list of LogProviderAgreement
	 * 
	 * @param logProviderAgreementList
	 * @return the merged list of LogProviderAgreement
	 */
	public ArrayList<LogProviderAgreement> mergeLogProviderAgreementList(
			List<LogProviderAgreement> logProviderAgreementList);

	/**
	 * removeLogProviderAgreement - removes a LogProviderAgreement
	 * 
	 * @param logProviderAgreement
	 */
	public void removeLogProviderAgreement(LogProviderAgreement logProviderAgreement);

	/**
	 * removeLogProviderAgreementList - removes a list of LogProviderAgreement
	 * 
	 * @param logProviderAgreementList
	 */
	public void removeLogProviderAgreementList(List<LogProviderAgreement> logProviderAgreementList);

	/**
	 * findByLogProviderAgreementLike - finds a list of LogProviderAgreement Like
	 * 
	 * @param logProviderAgreement
	 * @return the list of LogProviderAgreement found
	 */
	public List<LogProviderAgreement> findByLogProviderAgreementLike(LogProviderAgreement logProviderAgreement,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogProviderAgreement>LikeFR - finds a list of LogProviderAgreement> Like with a finder return object
	 * 
	 * @param logProviderAgreement
	 * @return the list of LogProviderAgreement found
	 */
	public FinderReturn findByLogProviderAgreementLikeFR(LogProviderAgreement logProviderAgreement,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
