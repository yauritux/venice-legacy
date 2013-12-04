package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafObjectParameter;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafObjectParameterSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafObjectParameter
	 */
	public List<RafObjectParameter> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafObjectParameter persists a country
	 * 
	 * @param rafObjectParameter
	 * @return the persisted RafObjectParameter
	 */
	public RafObjectParameter persistRafObjectParameter(RafObjectParameter rafObjectParameter);

	/**
	 * persistRafObjectParameterList - persists a list of RafObjectParameter
	 * 
	 * @param rafObjectParameterList
	 * @return the list of persisted RafObjectParameter
	 */
	public ArrayList<RafObjectParameter> persistRafObjectParameterList(
			List<RafObjectParameter> rafObjectParameterList);

	/**
	 * mergeRafObjectParameter - merges a RafObjectParameter
	 * 
	 * @param rafObjectParameter
	 * @return the merged RafObjectParameter
	 */
	public RafObjectParameter mergeRafObjectParameter(RafObjectParameter rafObjectParameter);

	/**
	 * mergeRafObjectParameterList - merges a list of RafObjectParameter
	 * 
	 * @param rafObjectParameterList
	 * @return the merged list of RafObjectParameter
	 */
	public ArrayList<RafObjectParameter> mergeRafObjectParameterList(
			List<RafObjectParameter> rafObjectParameterList);

	/**
	 * removeRafObjectParameter - removes a RafObjectParameter
	 * 
	 * @param rafObjectParameter
	 */
	public void removeRafObjectParameter(RafObjectParameter rafObjectParameter);

	/**
	 * removeRafObjectParameterList - removes a list of RafObjectParameter
	 * 
	 * @param rafObjectParameterList
	 */
	public void removeRafObjectParameterList(List<RafObjectParameter> rafObjectParameterList);

	/**
	 * findByRafObjectParameterLike - finds a list of RafObjectParameter Like
	 * 
	 * @param rafObjectParameter
	 * @return the list of RafObjectParameter found
	 */
	public List<RafObjectParameter> findByRafObjectParameterLike(RafObjectParameter rafObjectParameter,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafObjectParameter>LikeFR - finds a list of RafObjectParameter> Like with a finder return object
	 * 
	 * @param rafObjectParameter
	 * @return the list of RafObjectParameter found
	 */
	public FinderReturn findByRafObjectParameterLikeFR(RafObjectParameter rafObjectParameter,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
