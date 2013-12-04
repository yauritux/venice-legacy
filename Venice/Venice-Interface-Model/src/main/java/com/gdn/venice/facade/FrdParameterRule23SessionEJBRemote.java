package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule23;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule23SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule23
	 */
	public List<FrdParameterRule23> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule23 persists a country
	 * 
	 * @param frdParameterRule23
	 * @return the persisted FrdParameterRule23
	 */
	public FrdParameterRule23 persistFrdParameterRule23(FrdParameterRule23 frdParameterRule23);

	/**
	 * persistFrdParameterRule23List - persists a list of FrdParameterRule23
	 * 
	 * @param frdParameterRule23List
	 * @return the list of persisted FrdParameterRule23
	 */
	public ArrayList<FrdParameterRule23> persistFrdParameterRule23List(
			List<FrdParameterRule23> frdParameterRule23List);

	/**
	 * mergeFrdParameterRule23 - merges a FrdParameterRule23
	 * 
	 * @param frdParameterRule23
	 * @return the merged FrdParameterRule23
	 */
	public FrdParameterRule23 mergeFrdParameterRule23(FrdParameterRule23 frdParameterRule23);

	/**
	 * mergeFrdParameterRule23List - merges a list of FrdParameterRule23
	 * 
	 * @param frdParameterRule23List
	 * @return the merged list of FrdParameterRule23
	 */
	public ArrayList<FrdParameterRule23> mergeFrdParameterRule23List(
			List<FrdParameterRule23> frdParameterRule23List);

	/**
	 * removeFrdParameterRule23 - removes a FrdParameterRule23
	 * 
	 * @param frdParameterRule23
	 */
	public void removeFrdParameterRule23(FrdParameterRule23 frdParameterRule23);

	/**
	 * removeFrdParameterRule23List - removes a list of FrdParameterRule23
	 * 
	 * @param frdParameterRule23List
	 */
	public void removeFrdParameterRule23List(List<FrdParameterRule23> frdParameterRule23List);

	/**
	 * findByFrdParameterRule23Like - finds a list of FrdParameterRule23 Like
	 * 
	 * @param frdParameterRule23
	 * @return the list of FrdParameterRule23 found
	 */
	public List<FrdParameterRule23> findByFrdParameterRule23Like(FrdParameterRule23 frdParameterRule23,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule23>LikeFR - finds a list of FrdParameterRule23> Like with a finder return object
	 * 
	 * @param frdParameterRule23
	 * @return the list of FrdParameterRule23 found
	 */
	public FinderReturn findByFrdParameterRule23LikeFR(FrdParameterRule23 frdParameterRule23,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
