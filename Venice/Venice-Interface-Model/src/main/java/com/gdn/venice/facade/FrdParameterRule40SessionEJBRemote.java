package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule40;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule40SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule40
	 */
	public List<FrdParameterRule40> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule40 persists a country
	 * 
	 * @param frdParameterRule40
	 * @return the persisted FrdParameterRule40
	 */
	public FrdParameterRule40 persistFrdParameterRule40(FrdParameterRule40 frdParameterRule40);

	/**
	 * persistFrdParameterRule40List - persists a list of FrdParameterRule40
	 * 
	 * @param frdParameterRule40List
	 * @return the list of persisted FrdParameterRule40
	 */
	public ArrayList<FrdParameterRule40> persistFrdParameterRule40List(
			List<FrdParameterRule40> frdParameterRule40List);

	/**
	 * mergeFrdParameterRule40 - merges a FrdParameterRule40
	 * 
	 * @param frdParameterRule40
	 * @return the merged FrdParameterRule40
	 */
	public FrdParameterRule40 mergeFrdParameterRule40(FrdParameterRule40 frdParameterRule40);

	/**
	 * mergeFrdParameterRule40List - merges a list of FrdParameterRule40
	 * 
	 * @param frdParameterRule40List
	 * @return the merged list of FrdParameterRule40
	 */
	public ArrayList<FrdParameterRule40> mergeFrdParameterRule40List(
			List<FrdParameterRule40> frdParameterRule40List);

	/**
	 * removeFrdParameterRule40 - removes a FrdParameterRule40
	 * 
	 * @param frdParameterRule40
	 */
	public void removeFrdParameterRule40(FrdParameterRule40 frdParameterRule40);

	/**
	 * removeFrdParameterRule40List - removes a list of FrdParameterRule40
	 * 
	 * @param frdParameterRule40List
	 */
	public void removeFrdParameterRule40List(List<FrdParameterRule40> frdParameterRule40List);

	/**
	 * findByFrdParameterRule40Like - finds a list of FrdParameterRule40 Like
	 * 
	 * @param frdParameterRule40
	 * @return the list of FrdParameterRule40 found
	 */
	public List<FrdParameterRule40> findByFrdParameterRule40Like(FrdParameterRule40 frdParameterRule40,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule40>LikeFR - finds a list of FrdParameterRule40> Like with a finder return object
	 * 
	 * @param frdParameterRule40
	 * @return the list of FrdParameterRule40 found
	 */
	public FinderReturn findByFrdParameterRule40LikeFR(FrdParameterRule40 frdParameterRule40,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
