package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule13;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule13SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule13
	 */
	public List<FrdParameterRule13> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule13 persists a country
	 * 
	 * @param frdParameterRule13
	 * @return the persisted FrdParameterRule13
	 */
	public FrdParameterRule13 persistFrdParameterRule13(FrdParameterRule13 frdParameterRule13);

	/**
	 * persistFrdParameterRule13List - persists a list of FrdParameterRule13
	 * 
	 * @param frdParameterRule13List
	 * @return the list of persisted FrdParameterRule13
	 */
	public ArrayList<FrdParameterRule13> persistFrdParameterRule13List(
			List<FrdParameterRule13> frdParameterRule13List);

	/**
	 * mergeFrdParameterRule13 - merges a FrdParameterRule13
	 * 
	 * @param frdParameterRule13
	 * @return the merged FrdParameterRule13
	 */
	public FrdParameterRule13 mergeFrdParameterRule13(FrdParameterRule13 frdParameterRule13);

	/**
	 * mergeFrdParameterRule13List - merges a list of FrdParameterRule13
	 * 
	 * @param frdParameterRule13List
	 * @return the merged list of FrdParameterRule13
	 */
	public ArrayList<FrdParameterRule13> mergeFrdParameterRule13List(
			List<FrdParameterRule13> frdParameterRule13List);

	/**
	 * removeFrdParameterRule13 - removes a FrdParameterRule13
	 * 
	 * @param frdParameterRule13
	 */
	public void removeFrdParameterRule13(FrdParameterRule13 frdParameterRule13);

	/**
	 * removeFrdParameterRule13List - removes a list of FrdParameterRule13
	 * 
	 * @param frdParameterRule13List
	 */
	public void removeFrdParameterRule13List(List<FrdParameterRule13> frdParameterRule13List);

	/**
	 * findByFrdParameterRule13Like - finds a list of FrdParameterRule13 Like
	 * 
	 * @param frdParameterRule13
	 * @return the list of FrdParameterRule13 found
	 */
	public List<FrdParameterRule13> findByFrdParameterRule13Like(FrdParameterRule13 frdParameterRule13,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule13>LikeFR - finds a list of FrdParameterRule13> Like with a finder return object
	 * 
	 * @param frdParameterRule13
	 * @return the list of FrdParameterRule13 found
	 */
	public FinderReturn findByFrdParameterRule13LikeFR(FrdParameterRule13 frdParameterRule13,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
