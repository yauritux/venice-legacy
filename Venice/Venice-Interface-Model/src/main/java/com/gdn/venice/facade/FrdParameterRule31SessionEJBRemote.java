package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule31;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule31SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule31
	 */
	public List<FrdParameterRule31> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule31 persists a country
	 * 
	 * @param frdParameterRule31
	 * @return the persisted FrdParameterRule31
	 */
	public FrdParameterRule31 persistFrdParameterRule31(FrdParameterRule31 frdParameterRule31);

	/**
	 * persistFrdParameterRule31List - persists a list of FrdParameterRule31
	 * 
	 * @param frdParameterRule31List
	 * @return the list of persisted FrdParameterRule31
	 */
	public ArrayList<FrdParameterRule31> persistFrdParameterRule31List(
			List<FrdParameterRule31> frdParameterRule31List);

	/**
	 * mergeFrdParameterRule31 - merges a FrdParameterRule31
	 * 
	 * @param frdParameterRule31
	 * @return the merged FrdParameterRule31
	 */
	public FrdParameterRule31 mergeFrdParameterRule31(FrdParameterRule31 frdParameterRule31);

	/**
	 * mergeFrdParameterRule31List - merges a list of FrdParameterRule31
	 * 
	 * @param frdParameterRule31List
	 * @return the merged list of FrdParameterRule31
	 */
	public ArrayList<FrdParameterRule31> mergeFrdParameterRule31List(
			List<FrdParameterRule31> frdParameterRule31List);

	/**
	 * removeFrdParameterRule31 - removes a FrdParameterRule31
	 * 
	 * @param frdParameterRule31
	 */
	public void removeFrdParameterRule31(FrdParameterRule31 frdParameterRule31);

	/**
	 * removeFrdParameterRule31List - removes a list of FrdParameterRule31
	 * 
	 * @param frdParameterRule31List
	 */
	public void removeFrdParameterRule31List(List<FrdParameterRule31> frdParameterRule31List);

	/**
	 * findByFrdParameterRule31Like - finds a list of FrdParameterRule31 Like
	 * 
	 * @param frdParameterRule31
	 * @return the list of FrdParameterRule31 found
	 */
	public List<FrdParameterRule31> findByFrdParameterRule31Like(FrdParameterRule31 frdParameterRule31,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule31>LikeFR - finds a list of FrdParameterRule31> Like with a finder return object
	 * 
	 * @param frdParameterRule31
	 * @return the list of FrdParameterRule31 found
	 */
	public FinderReturn findByFrdParameterRule31LikeFR(FrdParameterRule31 frdParameterRule31,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
