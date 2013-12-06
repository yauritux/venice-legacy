package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogMerchantPickupInstruction;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogMerchantPickupInstructionSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogMerchantPickupInstruction
	 */
	public List<LogMerchantPickupInstruction> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogMerchantPickupInstruction persists a country
	 * 
	 * @param logMerchantPickupInstruction
	 * @return the persisted LogMerchantPickupInstruction
	 */
	public LogMerchantPickupInstruction persistLogMerchantPickupInstruction(LogMerchantPickupInstruction logMerchantPickupInstruction);

	/**
	 * persistLogMerchantPickupInstructionList - persists a list of LogMerchantPickupInstruction
	 * 
	 * @param logMerchantPickupInstructionList
	 * @return the list of persisted LogMerchantPickupInstruction
	 */
	public ArrayList<LogMerchantPickupInstruction> persistLogMerchantPickupInstructionList(
			List<LogMerchantPickupInstruction> logMerchantPickupInstructionList);

	/**
	 * mergeLogMerchantPickupInstruction - merges a LogMerchantPickupInstruction
	 * 
	 * @param logMerchantPickupInstruction
	 * @return the merged LogMerchantPickupInstruction
	 */
	public LogMerchantPickupInstruction mergeLogMerchantPickupInstruction(LogMerchantPickupInstruction logMerchantPickupInstruction);

	/**
	 * mergeLogMerchantPickupInstructionList - merges a list of LogMerchantPickupInstruction
	 * 
	 * @param logMerchantPickupInstructionList
	 * @return the merged list of LogMerchantPickupInstruction
	 */
	public ArrayList<LogMerchantPickupInstruction> mergeLogMerchantPickupInstructionList(
			List<LogMerchantPickupInstruction> logMerchantPickupInstructionList);

	/**
	 * removeLogMerchantPickupInstruction - removes a LogMerchantPickupInstruction
	 * 
	 * @param logMerchantPickupInstruction
	 */
	public void removeLogMerchantPickupInstruction(LogMerchantPickupInstruction logMerchantPickupInstruction);

	/**
	 * removeLogMerchantPickupInstructionList - removes a list of LogMerchantPickupInstruction
	 * 
	 * @param logMerchantPickupInstructionList
	 */
	public void removeLogMerchantPickupInstructionList(List<LogMerchantPickupInstruction> logMerchantPickupInstructionList);

	/**
	 * findByLogMerchantPickupInstructionLike - finds a list of LogMerchantPickupInstruction Like
	 * 
	 * @param logMerchantPickupInstruction
	 * @return the list of LogMerchantPickupInstruction found
	 */
	public List<LogMerchantPickupInstruction> findByLogMerchantPickupInstructionLike(LogMerchantPickupInstruction logMerchantPickupInstruction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogMerchantPickupInstruction>LikeFR - finds a list of LogMerchantPickupInstruction> Like with a finder return object
	 * 
	 * @param logMerchantPickupInstruction
	 * @return the list of LogMerchantPickupInstruction found
	 */
	public FinderReturn findByLogMerchantPickupInstructionLikeFR(LogMerchantPickupInstruction logMerchantPickupInstruction,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
