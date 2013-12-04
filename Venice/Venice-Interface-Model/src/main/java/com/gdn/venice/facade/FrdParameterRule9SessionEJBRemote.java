package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule9;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule9SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule9
	 */
	public List<FrdParameterRule9> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule9 persists a country
	 * 
	 * @param frdParameterRule9
	 * @return the persisted FrdParameterRule9
	 */
	public FrdParameterRule9 persistFrdParameterRule9(FrdParameterRule9 frdParameterRule9);

	/**
	 * persistFrdParameterRule9List - persists a list of FrdParameterRule9
	 * 
	 * @param frdParameterRule9List
	 * @return the list of persisted FrdParameterRule9
	 */
	public ArrayList<FrdParameterRule9> persistFrdParameterRule9List(
			List<FrdParameterRule9> frdParameterRule9List);

	/**
	 * mergeFrdParameterRule9 - merges a FrdParameterRule9
	 * 
	 * @param frdParameterRule9
	 * @return the merged FrdParameterRule9
	 */
	public FrdParameterRule9 mergeFrdParameterRule9(FrdParameterRule9 frdParameterRule9);

	/**
	 * mergeFrdParameterRule9List - merges a list of FrdParameterRule9
	 * 
	 * @param frdParameterRule9List
	 * @return the merged list of FrdParameterRule9
	 */
	public ArrayList<FrdParameterRule9> mergeFrdParameterRule9List(
			List<FrdParameterRule9> frdParameterRule9List);

	/**
	 * removeFrdParameterRule9 - removes a FrdParameterRule9
	 * 
	 * @param frdParameterRule9
	 */
	public void removeFrdParameterRule9(FrdParameterRule9 frdParameterRule9);

	/**
	 * removeFrdParameterRule9List - removes a list of FrdParameterRule9
	 * 
	 * @param frdParameterRule9List
	 */
	public void removeFrdParameterRule9List(List<FrdParameterRule9> frdParameterRule9List);

	/**
	 * findByFrdParameterRule9Like - finds a list of FrdParameterRule9 Like
	 * 
	 * @param frdParameterRule9
	 * @return the list of FrdParameterRule9 found
	 */
	public List<FrdParameterRule9> findByFrdParameterRule9Like(FrdParameterRule9 frdParameterRule9,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule9>LikeFR - finds a list of FrdParameterRule9> Like with a finder return object
	 * 
	 * @param frdParameterRule9
	 * @return the list of FrdParameterRule9 found
	 */
	public FinderReturn findByFrdParameterRule9LikeFR(FrdParameterRule9 frdParameterRule9,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
