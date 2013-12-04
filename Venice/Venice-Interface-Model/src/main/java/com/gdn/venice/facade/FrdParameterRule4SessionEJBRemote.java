package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule4;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule4SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule4
	 */
	public List<FrdParameterRule4> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule4 persists a country
	 * 
	 * @param frdParameterRule4
	 * @return the persisted FrdParameterRule4
	 */
	public FrdParameterRule4 persistFrdParameterRule4(FrdParameterRule4 frdParameterRule4);

	/**
	 * persistFrdParameterRule4List - persists a list of FrdParameterRule4
	 * 
	 * @param frdParameterRule4List
	 * @return the list of persisted FrdParameterRule4
	 */
	public ArrayList<FrdParameterRule4> persistFrdParameterRule4List(
			List<FrdParameterRule4> frdParameterRule4List);

	/**
	 * mergeFrdParameterRule4 - merges a FrdParameterRule4
	 * 
	 * @param frdParameterRule4
	 * @return the merged FrdParameterRule4
	 */
	public FrdParameterRule4 mergeFrdParameterRule4(FrdParameterRule4 frdParameterRule4);

	/**
	 * mergeFrdParameterRule4List - merges a list of FrdParameterRule4
	 * 
	 * @param frdParameterRule4List
	 * @return the merged list of FrdParameterRule4
	 */
	public ArrayList<FrdParameterRule4> mergeFrdParameterRule4List(
			List<FrdParameterRule4> frdParameterRule4List);

	/**
	 * removeFrdParameterRule4 - removes a FrdParameterRule4
	 * 
	 * @param frdParameterRule4
	 */
	public void removeFrdParameterRule4(FrdParameterRule4 frdParameterRule4);

	/**
	 * removeFrdParameterRule4List - removes a list of FrdParameterRule4
	 * 
	 * @param frdParameterRule4List
	 */
	public void removeFrdParameterRule4List(List<FrdParameterRule4> frdParameterRule4List);

	/**
	 * findByFrdParameterRule4Like - finds a list of FrdParameterRule4 Like
	 * 
	 * @param frdParameterRule4
	 * @return the list of FrdParameterRule4 found
	 */
	public List<FrdParameterRule4> findByFrdParameterRule4Like(FrdParameterRule4 frdParameterRule4,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule4>LikeFR - finds a list of FrdParameterRule4> Like with a finder return object
	 * 
	 * @param frdParameterRule4
	 * @return the list of FrdParameterRule4 found
	 */
	public FinderReturn findByFrdParameterRule4LikeFR(FrdParameterRule4 frdParameterRule4,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
