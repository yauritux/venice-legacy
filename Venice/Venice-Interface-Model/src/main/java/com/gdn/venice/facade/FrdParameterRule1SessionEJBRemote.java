package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule1;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule1SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule1
	 */
	public List<FrdParameterRule1> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule1 persists a country
	 * 
	 * @param frdParameterRule1
	 * @return the persisted FrdParameterRule1
	 */
	public FrdParameterRule1 persistFrdParameterRule1(FrdParameterRule1 frdParameterRule1);

	/**
	 * persistFrdParameterRule1List - persists a list of FrdParameterRule1
	 * 
	 * @param frdParameterRule1List
	 * @return the list of persisted FrdParameterRule1
	 */
	public ArrayList<FrdParameterRule1> persistFrdParameterRule1List(
			List<FrdParameterRule1> frdParameterRule1List);

	/**
	 * mergeFrdParameterRule1 - merges a FrdParameterRule1
	 * 
	 * @param frdParameterRule1
	 * @return the merged FrdParameterRule1
	 */
	public FrdParameterRule1 mergeFrdParameterRule1(FrdParameterRule1 frdParameterRule1);

	/**
	 * mergeFrdParameterRule1List - merges a list of FrdParameterRule1
	 * 
	 * @param frdParameterRule1List
	 * @return the merged list of FrdParameterRule1
	 */
	public ArrayList<FrdParameterRule1> mergeFrdParameterRule1List(
			List<FrdParameterRule1> frdParameterRule1List);

	/**
	 * removeFrdParameterRule1 - removes a FrdParameterRule1
	 * 
	 * @param frdParameterRule1
	 */
	public void removeFrdParameterRule1(FrdParameterRule1 frdParameterRule1);

	/**
	 * removeFrdParameterRule1List - removes a list of FrdParameterRule1
	 * 
	 * @param frdParameterRule1List
	 */
	public void removeFrdParameterRule1List(List<FrdParameterRule1> frdParameterRule1List);

	/**
	 * findByFrdParameterRule1Like - finds a list of FrdParameterRule1 Like
	 * 
	 * @param frdParameterRule1
	 * @return the list of FrdParameterRule1 found
	 */
	public List<FrdParameterRule1> findByFrdParameterRule1Like(FrdParameterRule1 frdParameterRule1,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule1>LikeFR - finds a list of FrdParameterRule1> Like with a finder return object
	 * 
	 * @param frdParameterRule1
	 * @return the list of FrdParameterRule1 found
	 */
	public FinderReturn findByFrdParameterRule1LikeFR(FrdParameterRule1 frdParameterRule1,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
