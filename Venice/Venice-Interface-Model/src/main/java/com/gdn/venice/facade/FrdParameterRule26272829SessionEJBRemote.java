package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule26272829;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule26272829SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule26272829
	 */
	public List<FrdParameterRule26272829> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule26272829 persists a country
	 * 
	 * @param frdParameterRule26272829
	 * @return the persisted FrdParameterRule26272829
	 */
	public FrdParameterRule26272829 persistFrdParameterRule26272829(FrdParameterRule26272829 frdParameterRule26272829);

	/**
	 * persistFrdParameterRule26272829List - persists a list of FrdParameterRule26272829
	 * 
	 * @param frdParameterRule26272829List
	 * @return the list of persisted FrdParameterRule26272829
	 */
	public ArrayList<FrdParameterRule26272829> persistFrdParameterRule26272829List(
			List<FrdParameterRule26272829> frdParameterRule26272829List);

	/**
	 * mergeFrdParameterRule26272829 - merges a FrdParameterRule26272829
	 * 
	 * @param frdParameterRule26272829
	 * @return the merged FrdParameterRule26272829
	 */
	public FrdParameterRule26272829 mergeFrdParameterRule26272829(FrdParameterRule26272829 frdParameterRule26272829);

	/**
	 * mergeFrdParameterRule26272829List - merges a list of FrdParameterRule26272829
	 * 
	 * @param frdParameterRule26272829List
	 * @return the merged list of FrdParameterRule26272829
	 */
	public ArrayList<FrdParameterRule26272829> mergeFrdParameterRule26272829List(
			List<FrdParameterRule26272829> frdParameterRule26272829List);

	/**
	 * removeFrdParameterRule26272829 - removes a FrdParameterRule26272829
	 * 
	 * @param frdParameterRule26272829
	 */
	public void removeFrdParameterRule26272829(FrdParameterRule26272829 frdParameterRule26272829);

	/**
	 * removeFrdParameterRule26272829List - removes a list of FrdParameterRule26272829
	 * 
	 * @param frdParameterRule26272829List
	 */
	public void removeFrdParameterRule26272829List(List<FrdParameterRule26272829> frdParameterRule26272829List);

	/**
	 * findByFrdParameterRule26272829Like - finds a list of FrdParameterRule26272829 Like
	 * 
	 * @param frdParameterRule26272829
	 * @return the list of FrdParameterRule26272829 found
	 */
	public List<FrdParameterRule26272829> findByFrdParameterRule26272829Like(FrdParameterRule26272829 frdParameterRule26272829,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule26272829>LikeFR - finds a list of FrdParameterRule26272829> Like with a finder return object
	 * 
	 * @param frdParameterRule26272829
	 * @return the list of FrdParameterRule26272829 found
	 */
	public FinderReturn findByFrdParameterRule26272829LikeFR(FrdParameterRule26272829 frdParameterRule26272829,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
