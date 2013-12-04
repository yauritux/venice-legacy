package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafApplicationObjectType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafApplicationObjectTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafApplicationObjectType
	 */
	public List<RafApplicationObjectType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafApplicationObjectType persists a country
	 * 
	 * @param rafApplicationObjectType
	 * @return the persisted RafApplicationObjectType
	 */
	public RafApplicationObjectType persistRafApplicationObjectType(RafApplicationObjectType rafApplicationObjectType);

	/**
	 * persistRafApplicationObjectTypeList - persists a list of RafApplicationObjectType
	 * 
	 * @param rafApplicationObjectTypeList
	 * @return the list of persisted RafApplicationObjectType
	 */
	public ArrayList<RafApplicationObjectType> persistRafApplicationObjectTypeList(
			List<RafApplicationObjectType> rafApplicationObjectTypeList);

	/**
	 * mergeRafApplicationObjectType - merges a RafApplicationObjectType
	 * 
	 * @param rafApplicationObjectType
	 * @return the merged RafApplicationObjectType
	 */
	public RafApplicationObjectType mergeRafApplicationObjectType(RafApplicationObjectType rafApplicationObjectType);

	/**
	 * mergeRafApplicationObjectTypeList - merges a list of RafApplicationObjectType
	 * 
	 * @param rafApplicationObjectTypeList
	 * @return the merged list of RafApplicationObjectType
	 */
	public ArrayList<RafApplicationObjectType> mergeRafApplicationObjectTypeList(
			List<RafApplicationObjectType> rafApplicationObjectTypeList);

	/**
	 * removeRafApplicationObjectType - removes a RafApplicationObjectType
	 * 
	 * @param rafApplicationObjectType
	 */
	public void removeRafApplicationObjectType(RafApplicationObjectType rafApplicationObjectType);

	/**
	 * removeRafApplicationObjectTypeList - removes a list of RafApplicationObjectType
	 * 
	 * @param rafApplicationObjectTypeList
	 */
	public void removeRafApplicationObjectTypeList(List<RafApplicationObjectType> rafApplicationObjectTypeList);

	/**
	 * findByRafApplicationObjectTypeLike - finds a list of RafApplicationObjectType Like
	 * 
	 * @param rafApplicationObjectType
	 * @return the list of RafApplicationObjectType found
	 */
	public List<RafApplicationObjectType> findByRafApplicationObjectTypeLike(RafApplicationObjectType rafApplicationObjectType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafApplicationObjectType>LikeFR - finds a list of RafApplicationObjectType> Like with a finder return object
	 * 
	 * @param rafApplicationObjectType
	 * @return the list of RafApplicationObjectType found
	 */
	public FinderReturn findByRafApplicationObjectTypeLikeFR(RafApplicationObjectType rafApplicationObjectType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
