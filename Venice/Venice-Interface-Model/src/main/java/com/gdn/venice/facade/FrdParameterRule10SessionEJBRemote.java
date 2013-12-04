package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule10;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule10SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule10
	 */
	public List<FrdParameterRule10> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule10 persists a country
	 * 
	 * @param frdParameterRule10
	 * @return the persisted FrdParameterRule10
	 */
	public FrdParameterRule10 persistFrdParameterRule10(FrdParameterRule10 frdParameterRule10);

	/**
	 * persistFrdParameterRule10List - persists a list of FrdParameterRule10
	 * 
	 * @param frdParameterRule10List
	 * @return the list of persisted FrdParameterRule10
	 */
	public ArrayList<FrdParameterRule10> persistFrdParameterRule10List(
			List<FrdParameterRule10> frdParameterRule10List);

	/**
	 * mergeFrdParameterRule10 - merges a FrdParameterRule10
	 * 
	 * @param frdParameterRule10
	 * @return the merged FrdParameterRule10
	 */
	public FrdParameterRule10 mergeFrdParameterRule10(FrdParameterRule10 frdParameterRule10);

	/**
	 * mergeFrdParameterRule10List - merges a list of FrdParameterRule10
	 * 
	 * @param frdParameterRule10List
	 * @return the merged list of FrdParameterRule10
	 */
	public ArrayList<FrdParameterRule10> mergeFrdParameterRule10List(
			List<FrdParameterRule10> frdParameterRule10List);

	/**
	 * removeFrdParameterRule10 - removes a FrdParameterRule10
	 * 
	 * @param frdParameterRule10
	 */
	public void removeFrdParameterRule10(FrdParameterRule10 frdParameterRule10);

	/**
	 * removeFrdParameterRule10List - removes a list of FrdParameterRule10
	 * 
	 * @param frdParameterRule10List
	 */
	public void removeFrdParameterRule10List(List<FrdParameterRule10> frdParameterRule10List);

	/**
	 * findByFrdParameterRule10Like - finds a list of FrdParameterRule10 Like
	 * 
	 * @param frdParameterRule10
	 * @return the list of FrdParameterRule10 found
	 */
	public List<FrdParameterRule10> findByFrdParameterRule10Like(FrdParameterRule10 frdParameterRule10,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule10>LikeFR - finds a list of FrdParameterRule10> Like with a finder return object
	 * 
	 * @param frdParameterRule10
	 * @return the list of FrdParameterRule10 found
	 */
	public FinderReturn findByFrdParameterRule10LikeFR(FrdParameterRule10 frdParameterRule10,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
