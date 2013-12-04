package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule35;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule35SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule35
	 */
	public List<FrdParameterRule35> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule35 persists a country
	 * 
	 * @param frdParameterRule35
	 * @return the persisted FrdParameterRule35
	 */
	public FrdParameterRule35 persistFrdParameterRule35(FrdParameterRule35 frdParameterRule35);

	/**
	 * persistFrdParameterRule35List - persists a list of FrdParameterRule35
	 * 
	 * @param frdParameterRule35List
	 * @return the list of persisted FrdParameterRule35
	 */
	public ArrayList<FrdParameterRule35> persistFrdParameterRule35List(
			List<FrdParameterRule35> frdParameterRule35List);

	/**
	 * mergeFrdParameterRule35 - merges a FrdParameterRule35
	 * 
	 * @param frdParameterRule35
	 * @return the merged FrdParameterRule35
	 */
	public FrdParameterRule35 mergeFrdParameterRule35(FrdParameterRule35 frdParameterRule35);

	/**
	 * mergeFrdParameterRule35List - merges a list of FrdParameterRule35
	 * 
	 * @param frdParameterRule35List
	 * @return the merged list of FrdParameterRule35
	 */
	public ArrayList<FrdParameterRule35> mergeFrdParameterRule35List(
			List<FrdParameterRule35> frdParameterRule35List);

	/**
	 * removeFrdParameterRule35 - removes a FrdParameterRule35
	 * 
	 * @param frdParameterRule35
	 */
	public void removeFrdParameterRule35(FrdParameterRule35 frdParameterRule35);

	/**
	 * removeFrdParameterRule35List - removes a list of FrdParameterRule35
	 * 
	 * @param frdParameterRule35List
	 */
	public void removeFrdParameterRule35List(List<FrdParameterRule35> frdParameterRule35List);

	/**
	 * findByFrdParameterRule35Like - finds a list of FrdParameterRule35 Like
	 * 
	 * @param frdParameterRule35
	 * @return the list of FrdParameterRule35 found
	 */
	public List<FrdParameterRule35> findByFrdParameterRule35Like(FrdParameterRule35 frdParameterRule35,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule35>LikeFR - finds a list of FrdParameterRule35> Like with a finder return object
	 * 
	 * @param frdParameterRule35
	 * @return the list of FrdParameterRule35 found
	 */
	public FinderReturn findByFrdParameterRule35LikeFR(FrdParameterRule35 frdParameterRule35,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
