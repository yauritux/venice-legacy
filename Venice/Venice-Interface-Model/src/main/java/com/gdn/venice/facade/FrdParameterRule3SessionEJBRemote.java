package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule3;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule3SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule3
	 */
	public List<FrdParameterRule3> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule3 persists a country
	 * 
	 * @param frdParameterRule3
	 * @return the persisted FrdParameterRule3
	 */
	public FrdParameterRule3 persistFrdParameterRule3(FrdParameterRule3 frdParameterRule3);

	/**
	 * persistFrdParameterRule3List - persists a list of FrdParameterRule3
	 * 
	 * @param frdParameterRule3List
	 * @return the list of persisted FrdParameterRule3
	 */
	public ArrayList<FrdParameterRule3> persistFrdParameterRule3List(
			List<FrdParameterRule3> frdParameterRule3List);

	/**
	 * mergeFrdParameterRule3 - merges a FrdParameterRule3
	 * 
	 * @param frdParameterRule3
	 * @return the merged FrdParameterRule3
	 */
	public FrdParameterRule3 mergeFrdParameterRule3(FrdParameterRule3 frdParameterRule3);

	/**
	 * mergeFrdParameterRule3List - merges a list of FrdParameterRule3
	 * 
	 * @param frdParameterRule3List
	 * @return the merged list of FrdParameterRule3
	 */
	public ArrayList<FrdParameterRule3> mergeFrdParameterRule3List(
			List<FrdParameterRule3> frdParameterRule3List);

	/**
	 * removeFrdParameterRule3 - removes a FrdParameterRule3
	 * 
	 * @param frdParameterRule3
	 */
	public void removeFrdParameterRule3(FrdParameterRule3 frdParameterRule3);

	/**
	 * removeFrdParameterRule3List - removes a list of FrdParameterRule3
	 * 
	 * @param frdParameterRule3List
	 */
	public void removeFrdParameterRule3List(List<FrdParameterRule3> frdParameterRule3List);

	/**
	 * findByFrdParameterRule3Like - finds a list of FrdParameterRule3 Like
	 * 
	 * @param frdParameterRule3
	 * @return the list of FrdParameterRule3 found
	 */
	public List<FrdParameterRule3> findByFrdParameterRule3Like(FrdParameterRule3 frdParameterRule3,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule3>LikeFR - finds a list of FrdParameterRule3> Like with a finder return object
	 * 
	 * @param frdParameterRule3
	 * @return the list of FrdParameterRule3 found
	 */
	public FinderReturn findByFrdParameterRule3LikeFR(FrdParameterRule3 frdParameterRule3,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
