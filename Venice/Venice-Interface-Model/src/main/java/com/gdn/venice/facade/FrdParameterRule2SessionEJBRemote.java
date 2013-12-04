package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule2;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule2SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule2
	 */
	public List<FrdParameterRule2> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule2 persists a country
	 * 
	 * @param frdParameterRule2
	 * @return the persisted FrdParameterRule2
	 */
	public FrdParameterRule2 persistFrdParameterRule2(FrdParameterRule2 frdParameterRule2);

	/**
	 * persistFrdParameterRule2List - persists a list of FrdParameterRule2
	 * 
	 * @param frdParameterRule2List
	 * @return the list of persisted FrdParameterRule2
	 */
	public ArrayList<FrdParameterRule2> persistFrdParameterRule2List(
			List<FrdParameterRule2> frdParameterRule2List);

	/**
	 * mergeFrdParameterRule2 - merges a FrdParameterRule2
	 * 
	 * @param frdParameterRule2
	 * @return the merged FrdParameterRule2
	 */
	public FrdParameterRule2 mergeFrdParameterRule2(FrdParameterRule2 frdParameterRule2);

	/**
	 * mergeFrdParameterRule2List - merges a list of FrdParameterRule2
	 * 
	 * @param frdParameterRule2List
	 * @return the merged list of FrdParameterRule2
	 */
	public ArrayList<FrdParameterRule2> mergeFrdParameterRule2List(
			List<FrdParameterRule2> frdParameterRule2List);

	/**
	 * removeFrdParameterRule2 - removes a FrdParameterRule2
	 * 
	 * @param frdParameterRule2
	 */
	public void removeFrdParameterRule2(FrdParameterRule2 frdParameterRule2);

	/**
	 * removeFrdParameterRule2List - removes a list of FrdParameterRule2
	 * 
	 * @param frdParameterRule2List
	 */
	public void removeFrdParameterRule2List(List<FrdParameterRule2> frdParameterRule2List);

	/**
	 * findByFrdParameterRule2Like - finds a list of FrdParameterRule2 Like
	 * 
	 * @param frdParameterRule2
	 * @return the list of FrdParameterRule2 found
	 */
	public List<FrdParameterRule2> findByFrdParameterRule2Like(FrdParameterRule2 frdParameterRule2,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule2>LikeFR - finds a list of FrdParameterRule2> Like with a finder return object
	 * 
	 * @param frdParameterRule2
	 * @return the list of FrdParameterRule2 found
	 */
	public FinderReturn findByFrdParameterRule2LikeFR(FrdParameterRule2 frdParameterRule2,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
