package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule24;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule24SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule24
	 */
	public List<FrdParameterRule24> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule24 persists a country
	 * 
	 * @param frdParameterRule24
	 * @return the persisted FrdParameterRule24
	 */
	public FrdParameterRule24 persistFrdParameterRule24(FrdParameterRule24 frdParameterRule24);

	/**
	 * persistFrdParameterRule24List - persists a list of FrdParameterRule24
	 * 
	 * @param frdParameterRule24List
	 * @return the list of persisted FrdParameterRule24
	 */
	public ArrayList<FrdParameterRule24> persistFrdParameterRule24List(
			List<FrdParameterRule24> frdParameterRule24List);

	/**
	 * mergeFrdParameterRule24 - merges a FrdParameterRule24
	 * 
	 * @param frdParameterRule24
	 * @return the merged FrdParameterRule24
	 */
	public FrdParameterRule24 mergeFrdParameterRule24(FrdParameterRule24 frdParameterRule24);

	/**
	 * mergeFrdParameterRule24List - merges a list of FrdParameterRule24
	 * 
	 * @param frdParameterRule24List
	 * @return the merged list of FrdParameterRule24
	 */
	public ArrayList<FrdParameterRule24> mergeFrdParameterRule24List(
			List<FrdParameterRule24> frdParameterRule24List);

	/**
	 * removeFrdParameterRule24 - removes a FrdParameterRule24
	 * 
	 * @param frdParameterRule24
	 */
	public void removeFrdParameterRule24(FrdParameterRule24 frdParameterRule24);

	/**
	 * removeFrdParameterRule24List - removes a list of FrdParameterRule24
	 * 
	 * @param frdParameterRule24List
	 */
	public void removeFrdParameterRule24List(List<FrdParameterRule24> frdParameterRule24List);

	/**
	 * findByFrdParameterRule24Like - finds a list of FrdParameterRule24 Like
	 * 
	 * @param frdParameterRule24
	 * @return the list of FrdParameterRule24 found
	 */
	public List<FrdParameterRule24> findByFrdParameterRule24Like(FrdParameterRule24 frdParameterRule24,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule24>LikeFR - finds a list of FrdParameterRule24> Like with a finder return object
	 * 
	 * @param frdParameterRule24
	 * @return the list of FrdParameterRule24 found
	 */
	public FinderReturn findByFrdParameterRule24LikeFR(FrdParameterRule24 frdParameterRule24,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
