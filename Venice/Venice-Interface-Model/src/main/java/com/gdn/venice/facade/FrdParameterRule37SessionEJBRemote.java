package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule37;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule37SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule37
	 */
	public List<FrdParameterRule37> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule37 persists a country
	 * 
	 * @param frdParameterRule37
	 * @return the persisted FrdParameterRule37
	 */
	public FrdParameterRule37 persistFrdParameterRule37(FrdParameterRule37 frdParameterRule37);

	/**
	 * persistFrdParameterRule37List - persists a list of FrdParameterRule37
	 * 
	 * @param frdParameterRule37List
	 * @return the list of persisted FrdParameterRule37
	 */
	public ArrayList<FrdParameterRule37> persistFrdParameterRule37List(
			List<FrdParameterRule37> frdParameterRule37List);

	/**
	 * mergeFrdParameterRule37 - merges a FrdParameterRule37
	 * 
	 * @param frdParameterRule37
	 * @return the merged FrdParameterRule37
	 */
	public FrdParameterRule37 mergeFrdParameterRule37(FrdParameterRule37 frdParameterRule37);

	/**
	 * mergeFrdParameterRule37List - merges a list of FrdParameterRule37
	 * 
	 * @param frdParameterRule37List
	 * @return the merged list of FrdParameterRule37
	 */
	public ArrayList<FrdParameterRule37> mergeFrdParameterRule37List(
			List<FrdParameterRule37> frdParameterRule37List);

	/**
	 * removeFrdParameterRule37 - removes a FrdParameterRule37
	 * 
	 * @param frdParameterRule37
	 */
	public void removeFrdParameterRule37(FrdParameterRule37 frdParameterRule37);

	/**
	 * removeFrdParameterRule37List - removes a list of FrdParameterRule37
	 * 
	 * @param frdParameterRule37List
	 */
	public void removeFrdParameterRule37List(List<FrdParameterRule37> frdParameterRule37List);

	/**
	 * findByFrdParameterRule37Like - finds a list of FrdParameterRule37 Like
	 * 
	 * @param frdParameterRule37
	 * @return the list of FrdParameterRule37 found
	 */
	public List<FrdParameterRule37> findByFrdParameterRule37Like(FrdParameterRule37 frdParameterRule37,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule37>LikeFR - finds a list of FrdParameterRule37> Like with a finder return object
	 * 
	 * @param frdParameterRule37
	 * @return the list of FrdParameterRule37 found
	 */
	public FinderReturn findByFrdParameterRule37LikeFR(FrdParameterRule37 frdParameterRule37,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
