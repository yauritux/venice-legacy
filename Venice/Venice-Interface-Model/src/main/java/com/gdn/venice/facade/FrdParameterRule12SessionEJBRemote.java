package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule12;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule12SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule12
	 */
	public List<FrdParameterRule12> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule12 persists a country
	 * 
	 * @param frdParameterRule12
	 * @return the persisted FrdParameterRule12
	 */
	public FrdParameterRule12 persistFrdParameterRule12(FrdParameterRule12 frdParameterRule12);

	/**
	 * persistFrdParameterRule12List - persists a list of FrdParameterRule12
	 * 
	 * @param frdParameterRule12List
	 * @return the list of persisted FrdParameterRule12
	 */
	public ArrayList<FrdParameterRule12> persistFrdParameterRule12List(
			List<FrdParameterRule12> frdParameterRule12List);

	/**
	 * mergeFrdParameterRule12 - merges a FrdParameterRule12
	 * 
	 * @param frdParameterRule12
	 * @return the merged FrdParameterRule12
	 */
	public FrdParameterRule12 mergeFrdParameterRule12(FrdParameterRule12 frdParameterRule12);

	/**
	 * mergeFrdParameterRule12List - merges a list of FrdParameterRule12
	 * 
	 * @param frdParameterRule12List
	 * @return the merged list of FrdParameterRule12
	 */
	public ArrayList<FrdParameterRule12> mergeFrdParameterRule12List(
			List<FrdParameterRule12> frdParameterRule12List);

	/**
	 * removeFrdParameterRule12 - removes a FrdParameterRule12
	 * 
	 * @param frdParameterRule12
	 */
	public void removeFrdParameterRule12(FrdParameterRule12 frdParameterRule12);

	/**
	 * removeFrdParameterRule12List - removes a list of FrdParameterRule12
	 * 
	 * @param frdParameterRule12List
	 */
	public void removeFrdParameterRule12List(List<FrdParameterRule12> frdParameterRule12List);

	/**
	 * findByFrdParameterRule12Like - finds a list of FrdParameterRule12 Like
	 * 
	 * @param frdParameterRule12
	 * @return the list of FrdParameterRule12 found
	 */
	public List<FrdParameterRule12> findByFrdParameterRule12Like(FrdParameterRule12 frdParameterRule12,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule12>LikeFR - finds a list of FrdParameterRule12> Like with a finder return object
	 * 
	 * @param frdParameterRule12
	 * @return the list of FrdParameterRule12 found
	 */
	public FinderReturn findByFrdParameterRule12LikeFR(FrdParameterRule12 frdParameterRule12,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
