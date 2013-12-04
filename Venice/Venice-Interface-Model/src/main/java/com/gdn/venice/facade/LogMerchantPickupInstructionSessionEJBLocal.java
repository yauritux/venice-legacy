package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogMerchantPickupInstruction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogMerchantPickupInstructionSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogMerchantPickupInstruction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote#persistLogMerchantPickupInstruction(com
	 * .gdn.venice.persistence.LogMerchantPickupInstruction)
	 */
	public LogMerchantPickupInstruction persistLogMerchantPickupInstruction(LogMerchantPickupInstruction logMerchantPickupInstruction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote#persistLogMerchantPickupInstructionList
	 * (java.util.List)
	 */
	public ArrayList<LogMerchantPickupInstruction> persistLogMerchantPickupInstructionList(
			List<LogMerchantPickupInstruction> logMerchantPickupInstructionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote#mergeLogMerchantPickupInstruction(com.
	 * gdn.venice.persistence.LogMerchantPickupInstruction)
	 */
	public LogMerchantPickupInstruction mergeLogMerchantPickupInstruction(LogMerchantPickupInstruction logMerchantPickupInstruction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote#mergeLogMerchantPickupInstructionList(
	 * java.util.List)
	 */
	public ArrayList<LogMerchantPickupInstruction> mergeLogMerchantPickupInstructionList(
			List<LogMerchantPickupInstruction> logMerchantPickupInstructionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote#removeLogMerchantPickupInstruction(com
	 * .gdn.venice.persistence.LogMerchantPickupInstruction)
	 */
	public void removeLogMerchantPickupInstruction(LogMerchantPickupInstruction logMerchantPickupInstruction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote#removeLogMerchantPickupInstructionList
	 * (java.util.List)
	 */
	public void removeLogMerchantPickupInstructionList(List<LogMerchantPickupInstruction> logMerchantPickupInstructionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote#findByLogMerchantPickupInstructionLike
	 * (com.gdn.venice.persistence.LogMerchantPickupInstruction, int, int)
	 */
	public List<LogMerchantPickupInstruction> findByLogMerchantPickupInstructionLike(LogMerchantPickupInstruction logMerchantPickupInstruction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote#findByLogMerchantPickupInstructionLikeFR
	 * (com.gdn.venice.persistence.LogMerchantPickupInstruction, int, int)
	 */
	public FinderReturn findByLogMerchantPickupInstructionLikeFR(LogMerchantPickupInstruction logMerchantPickupInstruction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
