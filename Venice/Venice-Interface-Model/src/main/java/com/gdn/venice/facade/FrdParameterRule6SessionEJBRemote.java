package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule6;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule6SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule6
	 */
	public List<FrdParameterRule6> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule6 persists a country
	 * 
	 * @param frdParameterRule6
	 * @return the persisted FrdParameterRule6
	 */
	public FrdParameterRule6 persistFrdParameterRule6(FrdParameterRule6 frdParameterRule6);

	/**
	 * persistFrdParameterRule6List - persists a list of FrdParameterRule6
	 * 
	 * @param frdParameterRule6List
	 * @return the list of persisted FrdParameterRule6
	 */
	public ArrayList<FrdParameterRule6> persistFrdParameterRule6List(
			List<FrdParameterRule6> frdParameterRule6List);

	/**
	 * mergeFrdParameterRule6 - merges a FrdParameterRule6
	 * 
	 * @param frdParameterRule6
	 * @return the merged FrdParameterRule6
	 */
	public FrdParameterRule6 mergeFrdParameterRule6(FrdParameterRule6 frdParameterRule6);

	/**
	 * mergeFrdParameterRule6List - merges a list of FrdParameterRule6
	 * 
	 * @param frdParameterRule6List
	 * @return the merged list of FrdParameterRule6
	 */
	public ArrayList<FrdParameterRule6> mergeFrdParameterRule6List(
			List<FrdParameterRule6> frdParameterRule6List);

	/**
	 * removeFrdParameterRule6 - removes a FrdParameterRule6
	 * 
	 * @param frdParameterRule6
	 */
	public void removeFrdParameterRule6(FrdParameterRule6 frdParameterRule6);

	/**
	 * removeFrdParameterRule6List - removes a list of FrdParameterRule6
	 * 
	 * @param frdParameterRule6List
	 */
	public void removeFrdParameterRule6List(List<FrdParameterRule6> frdParameterRule6List);

	/**
	 * findByFrdParameterRule6Like - finds a list of FrdParameterRule6 Like
	 * 
	 * @param frdParameterRule6
	 * @return the list of FrdParameterRule6 found
	 */
	public List<FrdParameterRule6> findByFrdParameterRule6Like(FrdParameterRule6 frdParameterRule6,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule6>LikeFR - finds a list of FrdParameterRule6> Like with a finder return object
	 * 
	 * @param frdParameterRule6
	 * @return the list of FrdParameterRule6 found
	 */
	public FinderReturn findByFrdParameterRule6LikeFR(FrdParameterRule6 frdParameterRule6,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
