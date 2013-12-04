package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule34;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule34SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule34
	 */
	public List<FrdParameterRule34> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule34 persists a country
	 * 
	 * @param frdParameterRule34
	 * @return the persisted FrdParameterRule34
	 */
	public FrdParameterRule34 persistFrdParameterRule34(FrdParameterRule34 frdParameterRule34);

	/**
	 * persistFrdParameterRule34List - persists a list of FrdParameterRule34
	 * 
	 * @param frdParameterRule34List
	 * @return the list of persisted FrdParameterRule34
	 */
	public ArrayList<FrdParameterRule34> persistFrdParameterRule34List(
			List<FrdParameterRule34> frdParameterRule34List);

	/**
	 * mergeFrdParameterRule34 - merges a FrdParameterRule34
	 * 
	 * @param frdParameterRule34
	 * @return the merged FrdParameterRule34
	 */
	public FrdParameterRule34 mergeFrdParameterRule34(FrdParameterRule34 frdParameterRule34);

	/**
	 * mergeFrdParameterRule34List - merges a list of FrdParameterRule34
	 * 
	 * @param frdParameterRule34List
	 * @return the merged list of FrdParameterRule34
	 */
	public ArrayList<FrdParameterRule34> mergeFrdParameterRule34List(
			List<FrdParameterRule34> frdParameterRule34List);

	/**
	 * removeFrdParameterRule34 - removes a FrdParameterRule34
	 * 
	 * @param frdParameterRule34
	 */
	public void removeFrdParameterRule34(FrdParameterRule34 frdParameterRule34);

	/**
	 * removeFrdParameterRule34List - removes a list of FrdParameterRule34
	 * 
	 * @param frdParameterRule34List
	 */
	public void removeFrdParameterRule34List(List<FrdParameterRule34> frdParameterRule34List);

	/**
	 * findByFrdParameterRule34Like - finds a list of FrdParameterRule34 Like
	 * 
	 * @param frdParameterRule34
	 * @return the list of FrdParameterRule34 found
	 */
	public List<FrdParameterRule34> findByFrdParameterRule34Like(FrdParameterRule34 frdParameterRule34,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule34>LikeFR - finds a list of FrdParameterRule34> Like with a finder return object
	 * 
	 * @param frdParameterRule34
	 * @return the list of FrdParameterRule34 found
	 */
	public FinderReturn findByFrdParameterRule34LikeFR(FrdParameterRule34 frdParameterRule34,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
