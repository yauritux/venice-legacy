package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule7;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule7SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule7
	 */
	public List<FrdParameterRule7> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule7 persists a country
	 * 
	 * @param frdParameterRule7
	 * @return the persisted FrdParameterRule7
	 */
	public FrdParameterRule7 persistFrdParameterRule7(FrdParameterRule7 frdParameterRule7);

	/**
	 * persistFrdParameterRule7List - persists a list of FrdParameterRule7
	 * 
	 * @param frdParameterRule7List
	 * @return the list of persisted FrdParameterRule7
	 */
	public ArrayList<FrdParameterRule7> persistFrdParameterRule7List(
			List<FrdParameterRule7> frdParameterRule7List);

	/**
	 * mergeFrdParameterRule7 - merges a FrdParameterRule7
	 * 
	 * @param frdParameterRule7
	 * @return the merged FrdParameterRule7
	 */
	public FrdParameterRule7 mergeFrdParameterRule7(FrdParameterRule7 frdParameterRule7);

	/**
	 * mergeFrdParameterRule7List - merges a list of FrdParameterRule7
	 * 
	 * @param frdParameterRule7List
	 * @return the merged list of FrdParameterRule7
	 */
	public ArrayList<FrdParameterRule7> mergeFrdParameterRule7List(
			List<FrdParameterRule7> frdParameterRule7List);

	/**
	 * removeFrdParameterRule7 - removes a FrdParameterRule7
	 * 
	 * @param frdParameterRule7
	 */
	public void removeFrdParameterRule7(FrdParameterRule7 frdParameterRule7);

	/**
	 * removeFrdParameterRule7List - removes a list of FrdParameterRule7
	 * 
	 * @param frdParameterRule7List
	 */
	public void removeFrdParameterRule7List(List<FrdParameterRule7> frdParameterRule7List);

	/**
	 * findByFrdParameterRule7Like - finds a list of FrdParameterRule7 Like
	 * 
	 * @param frdParameterRule7
	 * @return the list of FrdParameterRule7 found
	 */
	public List<FrdParameterRule7> findByFrdParameterRule7Like(FrdParameterRule7 frdParameterRule7,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule7>LikeFR - finds a list of FrdParameterRule7> Like with a finder return object
	 * 
	 * @param frdParameterRule7
	 * @return the list of FrdParameterRule7 found
	 */
	public FinderReturn findByFrdParameterRule7LikeFR(FrdParameterRule7 frdParameterRule7,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
