package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule20;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule20SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule20
	 */
	public List<FrdParameterRule20> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule20 persists a country
	 * 
	 * @param frdParameterRule20
	 * @return the persisted FrdParameterRule20
	 */
	public FrdParameterRule20 persistFrdParameterRule20(FrdParameterRule20 frdParameterRule20);

	/**
	 * persistFrdParameterRule20List - persists a list of FrdParameterRule20
	 * 
	 * @param frdParameterRule20List
	 * @return the list of persisted FrdParameterRule20
	 */
	public ArrayList<FrdParameterRule20> persistFrdParameterRule20List(
			List<FrdParameterRule20> frdParameterRule20List);

	/**
	 * mergeFrdParameterRule20 - merges a FrdParameterRule20
	 * 
	 * @param frdParameterRule20
	 * @return the merged FrdParameterRule20
	 */
	public FrdParameterRule20 mergeFrdParameterRule20(FrdParameterRule20 frdParameterRule20);

	/**
	 * mergeFrdParameterRule20List - merges a list of FrdParameterRule20
	 * 
	 * @param frdParameterRule20List
	 * @return the merged list of FrdParameterRule20
	 */
	public ArrayList<FrdParameterRule20> mergeFrdParameterRule20List(
			List<FrdParameterRule20> frdParameterRule20List);

	/**
	 * removeFrdParameterRule20 - removes a FrdParameterRule20
	 * 
	 * @param frdParameterRule20
	 */
	public void removeFrdParameterRule20(FrdParameterRule20 frdParameterRule20);

	/**
	 * removeFrdParameterRule20List - removes a list of FrdParameterRule20
	 * 
	 * @param frdParameterRule20List
	 */
	public void removeFrdParameterRule20List(List<FrdParameterRule20> frdParameterRule20List);

	/**
	 * findByFrdParameterRule20Like - finds a list of FrdParameterRule20 Like
	 * 
	 * @param frdParameterRule20
	 * @return the list of FrdParameterRule20 found
	 */
	public List<FrdParameterRule20> findByFrdParameterRule20Like(FrdParameterRule20 frdParameterRule20,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule20>LikeFR - finds a list of FrdParameterRule20> Like with a finder return object
	 * 
	 * @param frdParameterRule20
	 * @return the list of FrdParameterRule20 found
	 */
	public FinderReturn findByFrdParameterRule20LikeFR(FrdParameterRule20 frdParameterRule20,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
