package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule19;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule19SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule19
	 */
	public List<FrdParameterRule19> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule19 persists a country
	 * 
	 * @param frdParameterRule19
	 * @return the persisted FrdParameterRule19
	 */
	public FrdParameterRule19 persistFrdParameterRule19(FrdParameterRule19 frdParameterRule19);

	/**
	 * persistFrdParameterRule19List - persists a list of FrdParameterRule19
	 * 
	 * @param frdParameterRule19List
	 * @return the list of persisted FrdParameterRule19
	 */
	public ArrayList<FrdParameterRule19> persistFrdParameterRule19List(
			List<FrdParameterRule19> frdParameterRule19List);

	/**
	 * mergeFrdParameterRule19 - merges a FrdParameterRule19
	 * 
	 * @param frdParameterRule19
	 * @return the merged FrdParameterRule19
	 */
	public FrdParameterRule19 mergeFrdParameterRule19(FrdParameterRule19 frdParameterRule19);

	/**
	 * mergeFrdParameterRule19List - merges a list of FrdParameterRule19
	 * 
	 * @param frdParameterRule19List
	 * @return the merged list of FrdParameterRule19
	 */
	public ArrayList<FrdParameterRule19> mergeFrdParameterRule19List(
			List<FrdParameterRule19> frdParameterRule19List);

	/**
	 * removeFrdParameterRule19 - removes a FrdParameterRule19
	 * 
	 * @param frdParameterRule19
	 */
	public void removeFrdParameterRule19(FrdParameterRule19 frdParameterRule19);

	/**
	 * removeFrdParameterRule19List - removes a list of FrdParameterRule19
	 * 
	 * @param frdParameterRule19List
	 */
	public void removeFrdParameterRule19List(List<FrdParameterRule19> frdParameterRule19List);

	/**
	 * findByFrdParameterRule19Like - finds a list of FrdParameterRule19 Like
	 * 
	 * @param frdParameterRule19
	 * @return the list of FrdParameterRule19 found
	 */
	public List<FrdParameterRule19> findByFrdParameterRule19Like(FrdParameterRule19 frdParameterRule19,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule19>LikeFR - finds a list of FrdParameterRule19> Like with a finder return object
	 * 
	 * @param frdParameterRule19
	 * @return the list of FrdParameterRule19 found
	 */
	public FinderReturn findByFrdParameterRule19LikeFR(FrdParameterRule19 frdParameterRule19,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
