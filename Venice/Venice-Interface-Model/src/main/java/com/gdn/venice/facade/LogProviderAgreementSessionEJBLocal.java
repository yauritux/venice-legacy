package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogProviderAgreement;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogProviderAgreementSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogProviderAgreement> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote#persistLogProviderAgreement(com
	 * .gdn.venice.persistence.LogProviderAgreement)
	 */
	public LogProviderAgreement persistLogProviderAgreement(LogProviderAgreement logProviderAgreement);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote#persistLogProviderAgreementList
	 * (java.util.List)
	 */
	public ArrayList<LogProviderAgreement> persistLogProviderAgreementList(
			List<LogProviderAgreement> logProviderAgreementList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote#mergeLogProviderAgreement(com.
	 * gdn.venice.persistence.LogProviderAgreement)
	 */
	public LogProviderAgreement mergeLogProviderAgreement(LogProviderAgreement logProviderAgreement);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote#mergeLogProviderAgreementList(
	 * java.util.List)
	 */
	public ArrayList<LogProviderAgreement> mergeLogProviderAgreementList(
			List<LogProviderAgreement> logProviderAgreementList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote#removeLogProviderAgreement(com
	 * .gdn.venice.persistence.LogProviderAgreement)
	 */
	public void removeLogProviderAgreement(LogProviderAgreement logProviderAgreement);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote#removeLogProviderAgreementList
	 * (java.util.List)
	 */
	public void removeLogProviderAgreementList(List<LogProviderAgreement> logProviderAgreementList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote#findByLogProviderAgreementLike
	 * (com.gdn.venice.persistence.LogProviderAgreement, int, int)
	 */
	public List<LogProviderAgreement> findByLogProviderAgreementLike(LogProviderAgreement logProviderAgreement,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote#findByLogProviderAgreementLikeFR
	 * (com.gdn.venice.persistence.LogProviderAgreement, int, int)
	 */
	public FinderReturn findByLogProviderAgreementLikeFR(LogProviderAgreement logProviderAgreement,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
