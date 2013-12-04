package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule5;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule5SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule5
	 */
	public List<FrdParameterRule5> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule5 persists a country
	 * 
	 * @param frdParameterRule5
	 * @return the persisted FrdParameterRule5
	 */
	public FrdParameterRule5 persistFrdParameterRule5(FrdParameterRule5 frdParameterRule5);

	/**
	 * persistFrdParameterRule5List - persists a list of FrdParameterRule5
	 * 
	 * @param frdParameterRule5List
	 * @return the list of persisted FrdParameterRule5
	 */
	public ArrayList<FrdParameterRule5> persistFrdParameterRule5List(
			List<FrdParameterRule5> frdParameterRule5List);

	/**
	 * mergeFrdParameterRule5 - merges a FrdParameterRule5
	 * 
	 * @param frdParameterRule5
	 * @return the merged FrdParameterRule5
	 */
	public FrdParameterRule5 mergeFrdParameterRule5(FrdParameterRule5 frdParameterRule5);

	/**
	 * mergeFrdParameterRule5List - merges a list of FrdParameterRule5
	 * 
	 * @param frdParameterRule5List
	 * @return the merged list of FrdParameterRule5
	 */
	public ArrayList<FrdParameterRule5> mergeFrdParameterRule5List(
			List<FrdParameterRule5> frdParameterRule5List);

	/**
	 * removeFrdParameterRule5 - removes a FrdParameterRule5
	 * 
	 * @param frdParameterRule5
	 */
	public void removeFrdParameterRule5(FrdParameterRule5 frdParameterRule5);

	/**
	 * removeFrdParameterRule5List - removes a list of FrdParameterRule5
	 * 
	 * @param frdParameterRule5List
	 */
	public void removeFrdParameterRule5List(List<FrdParameterRule5> frdParameterRule5List);

	/**
	 * findByFrdParameterRule5Like - finds a list of FrdParameterRule5 Like
	 * 
	 * @param frdParameterRule5
	 * @return the list of FrdParameterRule5 found
	 */
	public List<FrdParameterRule5> findByFrdParameterRule5Like(FrdParameterRule5 frdParameterRule5,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule5>LikeFR - finds a list of FrdParameterRule5> Like with a finder return object
	 * 
	 * @param frdParameterRule5
	 * @return the list of FrdParameterRule5 found
	 */
	public FinderReturn findByFrdParameterRule5LikeFR(FrdParameterRule5 frdParameterRule5,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
