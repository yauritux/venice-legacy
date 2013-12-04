package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule22;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule22SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule22
	 */
	public List<FrdParameterRule22> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule22 persists a country
	 * 
	 * @param frdParameterRule22
	 * @return the persisted FrdParameterRule22
	 */
	public FrdParameterRule22 persistFrdParameterRule22(FrdParameterRule22 frdParameterRule22);

	/**
	 * persistFrdParameterRule22List - persists a list of FrdParameterRule22
	 * 
	 * @param frdParameterRule22List
	 * @return the list of persisted FrdParameterRule22
	 */
	public ArrayList<FrdParameterRule22> persistFrdParameterRule22List(
			List<FrdParameterRule22> frdParameterRule22List);

	/**
	 * mergeFrdParameterRule22 - merges a FrdParameterRule22
	 * 
	 * @param frdParameterRule22
	 * @return the merged FrdParameterRule22
	 */
	public FrdParameterRule22 mergeFrdParameterRule22(FrdParameterRule22 frdParameterRule22);

	/**
	 * mergeFrdParameterRule22List - merges a list of FrdParameterRule22
	 * 
	 * @param frdParameterRule22List
	 * @return the merged list of FrdParameterRule22
	 */
	public ArrayList<FrdParameterRule22> mergeFrdParameterRule22List(
			List<FrdParameterRule22> frdParameterRule22List);

	/**
	 * removeFrdParameterRule22 - removes a FrdParameterRule22
	 * 
	 * @param frdParameterRule22
	 */
	public void removeFrdParameterRule22(FrdParameterRule22 frdParameterRule22);

	/**
	 * removeFrdParameterRule22List - removes a list of FrdParameterRule22
	 * 
	 * @param frdParameterRule22List
	 */
	public void removeFrdParameterRule22List(List<FrdParameterRule22> frdParameterRule22List);

	/**
	 * findByFrdParameterRule22Like - finds a list of FrdParameterRule22 Like
	 * 
	 * @param frdParameterRule22
	 * @return the list of FrdParameterRule22 found
	 */
	public List<FrdParameterRule22> findByFrdParameterRule22Like(FrdParameterRule22 frdParameterRule22,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule22>LikeFR - finds a list of FrdParameterRule22> Like with a finder return object
	 * 
	 * @param frdParameterRule22
	 * @return the list of FrdParameterRule22 found
	 */
	public FinderReturn findByFrdParameterRule22LikeFR(FrdParameterRule22 frdParameterRule22,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
