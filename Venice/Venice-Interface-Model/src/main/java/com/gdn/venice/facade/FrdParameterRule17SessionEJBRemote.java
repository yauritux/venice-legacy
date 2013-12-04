package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule17;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule17SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule17
	 */
	public List<FrdParameterRule17> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule17 persists a country
	 * 
	 * @param frdParameterRule17
	 * @return the persisted FrdParameterRule17
	 */
	public FrdParameterRule17 persistFrdParameterRule17(FrdParameterRule17 frdParameterRule17);

	/**
	 * persistFrdParameterRule17List - persists a list of FrdParameterRule17
	 * 
	 * @param frdParameterRule17List
	 * @return the list of persisted FrdParameterRule17
	 */
	public ArrayList<FrdParameterRule17> persistFrdParameterRule17List(
			List<FrdParameterRule17> frdParameterRule17List);

	/**
	 * mergeFrdParameterRule17 - merges a FrdParameterRule17
	 * 
	 * @param frdParameterRule17
	 * @return the merged FrdParameterRule17
	 */
	public FrdParameterRule17 mergeFrdParameterRule17(FrdParameterRule17 frdParameterRule17);

	/**
	 * mergeFrdParameterRule17List - merges a list of FrdParameterRule17
	 * 
	 * @param frdParameterRule17List
	 * @return the merged list of FrdParameterRule17
	 */
	public ArrayList<FrdParameterRule17> mergeFrdParameterRule17List(
			List<FrdParameterRule17> frdParameterRule17List);

	/**
	 * removeFrdParameterRule17 - removes a FrdParameterRule17
	 * 
	 * @param frdParameterRule17
	 */
	public void removeFrdParameterRule17(FrdParameterRule17 frdParameterRule17);

	/**
	 * removeFrdParameterRule17List - removes a list of FrdParameterRule17
	 * 
	 * @param frdParameterRule17List
	 */
	public void removeFrdParameterRule17List(List<FrdParameterRule17> frdParameterRule17List);

	/**
	 * findByFrdParameterRule17Like - finds a list of FrdParameterRule17 Like
	 * 
	 * @param frdParameterRule17
	 * @return the list of FrdParameterRule17 found
	 */
	public List<FrdParameterRule17> findByFrdParameterRule17Like(FrdParameterRule17 frdParameterRule17,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule17>LikeFR - finds a list of FrdParameterRule17> Like with a finder return object
	 * 
	 * @param frdParameterRule17
	 * @return the list of FrdParameterRule17 found
	 */
	public FinderReturn findByFrdParameterRule17LikeFR(FrdParameterRule17 frdParameterRule17,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
