package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule14;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule14SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule14
	 */
	public List<FrdParameterRule14> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule14 persists a country
	 * 
	 * @param frdParameterRule14
	 * @return the persisted FrdParameterRule14
	 */
	public FrdParameterRule14 persistFrdParameterRule14(FrdParameterRule14 frdParameterRule14);

	/**
	 * persistFrdParameterRule14List - persists a list of FrdParameterRule14
	 * 
	 * @param frdParameterRule14List
	 * @return the list of persisted FrdParameterRule14
	 */
	public ArrayList<FrdParameterRule14> persistFrdParameterRule14List(
			List<FrdParameterRule14> frdParameterRule14List);

	/**
	 * mergeFrdParameterRule14 - merges a FrdParameterRule14
	 * 
	 * @param frdParameterRule14
	 * @return the merged FrdParameterRule14
	 */
	public FrdParameterRule14 mergeFrdParameterRule14(FrdParameterRule14 frdParameterRule14);

	/**
	 * mergeFrdParameterRule14List - merges a list of FrdParameterRule14
	 * 
	 * @param frdParameterRule14List
	 * @return the merged list of FrdParameterRule14
	 */
	public ArrayList<FrdParameterRule14> mergeFrdParameterRule14List(
			List<FrdParameterRule14> frdParameterRule14List);

	/**
	 * removeFrdParameterRule14 - removes a FrdParameterRule14
	 * 
	 * @param frdParameterRule14
	 */
	public void removeFrdParameterRule14(FrdParameterRule14 frdParameterRule14);

	/**
	 * removeFrdParameterRule14List - removes a list of FrdParameterRule14
	 * 
	 * @param frdParameterRule14List
	 */
	public void removeFrdParameterRule14List(List<FrdParameterRule14> frdParameterRule14List);

	/**
	 * findByFrdParameterRule14Like - finds a list of FrdParameterRule14 Like
	 * 
	 * @param frdParameterRule14
	 * @return the list of FrdParameterRule14 found
	 */
	public List<FrdParameterRule14> findByFrdParameterRule14Like(FrdParameterRule14 frdParameterRule14,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule14>LikeFR - finds a list of FrdParameterRule14> Like with a finder return object
	 * 
	 * @param frdParameterRule14
	 * @return the list of FrdParameterRule14 found
	 */
	public FinderReturn findByFrdParameterRule14LikeFR(FrdParameterRule14 frdParameterRule14,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
