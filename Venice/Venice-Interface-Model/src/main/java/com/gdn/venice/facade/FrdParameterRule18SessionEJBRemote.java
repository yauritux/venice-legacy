package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule18;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule18SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule18
	 */
	public List<FrdParameterRule18> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule18 persists a country
	 * 
	 * @param frdParameterRule18
	 * @return the persisted FrdParameterRule18
	 */
	public FrdParameterRule18 persistFrdParameterRule18(FrdParameterRule18 frdParameterRule18);

	/**
	 * persistFrdParameterRule18List - persists a list of FrdParameterRule18
	 * 
	 * @param frdParameterRule18List
	 * @return the list of persisted FrdParameterRule18
	 */
	public ArrayList<FrdParameterRule18> persistFrdParameterRule18List(
			List<FrdParameterRule18> frdParameterRule18List);

	/**
	 * mergeFrdParameterRule18 - merges a FrdParameterRule18
	 * 
	 * @param frdParameterRule18
	 * @return the merged FrdParameterRule18
	 */
	public FrdParameterRule18 mergeFrdParameterRule18(FrdParameterRule18 frdParameterRule18);

	/**
	 * mergeFrdParameterRule18List - merges a list of FrdParameterRule18
	 * 
	 * @param frdParameterRule18List
	 * @return the merged list of FrdParameterRule18
	 */
	public ArrayList<FrdParameterRule18> mergeFrdParameterRule18List(
			List<FrdParameterRule18> frdParameterRule18List);

	/**
	 * removeFrdParameterRule18 - removes a FrdParameterRule18
	 * 
	 * @param frdParameterRule18
	 */
	public void removeFrdParameterRule18(FrdParameterRule18 frdParameterRule18);

	/**
	 * removeFrdParameterRule18List - removes a list of FrdParameterRule18
	 * 
	 * @param frdParameterRule18List
	 */
	public void removeFrdParameterRule18List(List<FrdParameterRule18> frdParameterRule18List);

	/**
	 * findByFrdParameterRule18Like - finds a list of FrdParameterRule18 Like
	 * 
	 * @param frdParameterRule18
	 * @return the list of FrdParameterRule18 found
	 */
	public List<FrdParameterRule18> findByFrdParameterRule18Like(FrdParameterRule18 frdParameterRule18,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule18>LikeFR - finds a list of FrdParameterRule18> Like with a finder return object
	 * 
	 * @param frdParameterRule18
	 * @return the list of FrdParameterRule18 found
	 */
	public FinderReturn findByFrdParameterRule18LikeFR(FrdParameterRule18 frdParameterRule18,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
