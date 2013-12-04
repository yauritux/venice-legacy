package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule25;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule25SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule25
	 */
	public List<FrdParameterRule25> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule25 persists a country
	 * 
	 * @param frdParameterRule25
	 * @return the persisted FrdParameterRule25
	 */
	public FrdParameterRule25 persistFrdParameterRule25(FrdParameterRule25 frdParameterRule25);

	/**
	 * persistFrdParameterRule25List - persists a list of FrdParameterRule25
	 * 
	 * @param frdParameterRule25List
	 * @return the list of persisted FrdParameterRule25
	 */
	public ArrayList<FrdParameterRule25> persistFrdParameterRule25List(
			List<FrdParameterRule25> frdParameterRule25List);

	/**
	 * mergeFrdParameterRule25 - merges a FrdParameterRule25
	 * 
	 * @param frdParameterRule25
	 * @return the merged FrdParameterRule25
	 */
	public FrdParameterRule25 mergeFrdParameterRule25(FrdParameterRule25 frdParameterRule25);

	/**
	 * mergeFrdParameterRule25List - merges a list of FrdParameterRule25
	 * 
	 * @param frdParameterRule25List
	 * @return the merged list of FrdParameterRule25
	 */
	public ArrayList<FrdParameterRule25> mergeFrdParameterRule25List(
			List<FrdParameterRule25> frdParameterRule25List);

	/**
	 * removeFrdParameterRule25 - removes a FrdParameterRule25
	 * 
	 * @param frdParameterRule25
	 */
	public void removeFrdParameterRule25(FrdParameterRule25 frdParameterRule25);

	/**
	 * removeFrdParameterRule25List - removes a list of FrdParameterRule25
	 * 
	 * @param frdParameterRule25List
	 */
	public void removeFrdParameterRule25List(List<FrdParameterRule25> frdParameterRule25List);

	/**
	 * findByFrdParameterRule25Like - finds a list of FrdParameterRule25 Like
	 * 
	 * @param frdParameterRule25
	 * @return the list of FrdParameterRule25 found
	 */
	public List<FrdParameterRule25> findByFrdParameterRule25Like(FrdParameterRule25 frdParameterRule25,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule25>LikeFR - finds a list of FrdParameterRule25> Like with a finder return object
	 * 
	 * @param frdParameterRule25
	 * @return the list of FrdParameterRule25 found
	 */
	public FinderReturn findByFrdParameterRule25LikeFR(FrdParameterRule25 frdParameterRule25,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
