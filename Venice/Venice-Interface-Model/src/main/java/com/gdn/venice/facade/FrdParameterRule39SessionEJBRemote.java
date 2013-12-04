package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule39;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule39SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule39
	 */
	public List<FrdParameterRule39> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule39 persists a country
	 * 
	 * @param frdParameterRule39
	 * @return the persisted FrdParameterRule39
	 */
	public FrdParameterRule39 persistFrdParameterRule39(FrdParameterRule39 frdParameterRule39);

	/**
	 * persistFrdParameterRule39List - persists a list of FrdParameterRule39
	 * 
	 * @param frdParameterRule39List
	 * @return the list of persisted FrdParameterRule39
	 */
	public ArrayList<FrdParameterRule39> persistFrdParameterRule39List(
			List<FrdParameterRule39> frdParameterRule39List);

	/**
	 * mergeFrdParameterRule39 - merges a FrdParameterRule39
	 * 
	 * @param frdParameterRule39
	 * @return the merged FrdParameterRule39
	 */
	public FrdParameterRule39 mergeFrdParameterRule39(FrdParameterRule39 frdParameterRule39);

	/**
	 * mergeFrdParameterRule39List - merges a list of FrdParameterRule39
	 * 
	 * @param frdParameterRule39List
	 * @return the merged list of FrdParameterRule39
	 */
	public ArrayList<FrdParameterRule39> mergeFrdParameterRule39List(
			List<FrdParameterRule39> frdParameterRule39List);

	/**
	 * removeFrdParameterRule39 - removes a FrdParameterRule39
	 * 
	 * @param frdParameterRule39
	 */
	public void removeFrdParameterRule39(FrdParameterRule39 frdParameterRule39);

	/**
	 * removeFrdParameterRule39List - removes a list of FrdParameterRule39
	 * 
	 * @param frdParameterRule39List
	 */
	public void removeFrdParameterRule39List(List<FrdParameterRule39> frdParameterRule39List);

	/**
	 * findByFrdParameterRule39Like - finds a list of FrdParameterRule39 Like
	 * 
	 * @param frdParameterRule39
	 * @return the list of FrdParameterRule39 found
	 */
	public List<FrdParameterRule39> findByFrdParameterRule39Like(FrdParameterRule39 frdParameterRule39,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule39>LikeFR - finds a list of FrdParameterRule39> Like with a finder return object
	 * 
	 * @param frdParameterRule39
	 * @return the list of FrdParameterRule39 found
	 */
	public FinderReturn findByFrdParameterRule39LikeFR(FrdParameterRule39 frdParameterRule39,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
