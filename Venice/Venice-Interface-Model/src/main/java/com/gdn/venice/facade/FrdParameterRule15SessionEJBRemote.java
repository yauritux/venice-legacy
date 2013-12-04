package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule15;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule15SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule15
	 */
	public List<FrdParameterRule15> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule15 persists a country
	 * 
	 * @param frdParameterRule15
	 * @return the persisted FrdParameterRule15
	 */
	public FrdParameterRule15 persistFrdParameterRule15(FrdParameterRule15 frdParameterRule15);

	/**
	 * persistFrdParameterRule15List - persists a list of FrdParameterRule15
	 * 
	 * @param frdParameterRule15List
	 * @return the list of persisted FrdParameterRule15
	 */
	public ArrayList<FrdParameterRule15> persistFrdParameterRule15List(
			List<FrdParameterRule15> frdParameterRule15List);

	/**
	 * mergeFrdParameterRule15 - merges a FrdParameterRule15
	 * 
	 * @param frdParameterRule15
	 * @return the merged FrdParameterRule15
	 */
	public FrdParameterRule15 mergeFrdParameterRule15(FrdParameterRule15 frdParameterRule15);

	/**
	 * mergeFrdParameterRule15List - merges a list of FrdParameterRule15
	 * 
	 * @param frdParameterRule15List
	 * @return the merged list of FrdParameterRule15
	 */
	public ArrayList<FrdParameterRule15> mergeFrdParameterRule15List(
			List<FrdParameterRule15> frdParameterRule15List);

	/**
	 * removeFrdParameterRule15 - removes a FrdParameterRule15
	 * 
	 * @param frdParameterRule15
	 */
	public void removeFrdParameterRule15(FrdParameterRule15 frdParameterRule15);

	/**
	 * removeFrdParameterRule15List - removes a list of FrdParameterRule15
	 * 
	 * @param frdParameterRule15List
	 */
	public void removeFrdParameterRule15List(List<FrdParameterRule15> frdParameterRule15List);

	/**
	 * findByFrdParameterRule15Like - finds a list of FrdParameterRule15 Like
	 * 
	 * @param frdParameterRule15
	 * @return the list of FrdParameterRule15 found
	 */
	public List<FrdParameterRule15> findByFrdParameterRule15Like(FrdParameterRule15 frdParameterRule15,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule15>LikeFR - finds a list of FrdParameterRule15> Like with a finder return object
	 * 
	 * @param frdParameterRule15
	 * @return the list of FrdParameterRule15 found
	 */
	public FinderReturn findByFrdParameterRule15LikeFR(FrdParameterRule15 frdParameterRule15,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
