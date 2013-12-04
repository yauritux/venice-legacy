package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdRuleConfigTreshold;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdRuleConfigTresholdSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdRuleConfigTreshold> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote#persistFrdRuleConfigTreshold(com
	 * .gdn.venice.persistence.FrdRuleConfigTreshold)
	 */
	public FrdRuleConfigTreshold persistFrdRuleConfigTreshold(FrdRuleConfigTreshold frdRuleConfigTreshold);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote#persistFrdRuleConfigTresholdList
	 * (java.util.List)
	 */
	public ArrayList<FrdRuleConfigTreshold> persistFrdRuleConfigTresholdList(
			List<FrdRuleConfigTreshold> frdRuleConfigTresholdList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote#mergeFrdRuleConfigTreshold(com.
	 * gdn.venice.persistence.FrdRuleConfigTreshold)
	 */
	public FrdRuleConfigTreshold mergeFrdRuleConfigTreshold(FrdRuleConfigTreshold frdRuleConfigTreshold);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote#mergeFrdRuleConfigTresholdList(
	 * java.util.List)
	 */
	public ArrayList<FrdRuleConfigTreshold> mergeFrdRuleConfigTresholdList(
			List<FrdRuleConfigTreshold> frdRuleConfigTresholdList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote#removeFrdRuleConfigTreshold(com
	 * .gdn.venice.persistence.FrdRuleConfigTreshold)
	 */
	public void removeFrdRuleConfigTreshold(FrdRuleConfigTreshold frdRuleConfigTreshold);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote#removeFrdRuleConfigTresholdList
	 * (java.util.List)
	 */
	public void removeFrdRuleConfigTresholdList(List<FrdRuleConfigTreshold> frdRuleConfigTresholdList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote#findByFrdRuleConfigTresholdLike
	 * (com.gdn.venice.persistence.FrdRuleConfigTreshold, int, int)
	 */
	public List<FrdRuleConfigTreshold> findByFrdRuleConfigTresholdLike(FrdRuleConfigTreshold frdRuleConfigTreshold,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote#findByFrdRuleConfigTresholdLikeFR
	 * (com.gdn.venice.persistence.FrdRuleConfigTreshold, int, int)
	 */
	public FinderReturn findByFrdRuleConfigTresholdLikeFR(FrdRuleConfigTreshold frdRuleConfigTreshold,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
