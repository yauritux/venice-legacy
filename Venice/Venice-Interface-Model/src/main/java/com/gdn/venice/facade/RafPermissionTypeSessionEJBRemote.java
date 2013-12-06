package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafPermissionType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafPermissionTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafPermissionType
	 */
	public List<RafPermissionType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafPermissionType persists a country
	 * 
	 * @param rafPermissionType
	 * @return the persisted RafPermissionType
	 */
	public RafPermissionType persistRafPermissionType(RafPermissionType rafPermissionType);

	/**
	 * persistRafPermissionTypeList - persists a list of RafPermissionType
	 * 
	 * @param rafPermissionTypeList
	 * @return the list of persisted RafPermissionType
	 */
	public ArrayList<RafPermissionType> persistRafPermissionTypeList(
			List<RafPermissionType> rafPermissionTypeList);

	/**
	 * mergeRafPermissionType - merges a RafPermissionType
	 * 
	 * @param rafPermissionType
	 * @return the merged RafPermissionType
	 */
	public RafPermissionType mergeRafPermissionType(RafPermissionType rafPermissionType);

	/**
	 * mergeRafPermissionTypeList - merges a list of RafPermissionType
	 * 
	 * @param rafPermissionTypeList
	 * @return the merged list of RafPermissionType
	 */
	public ArrayList<RafPermissionType> mergeRafPermissionTypeList(
			List<RafPermissionType> rafPermissionTypeList);

	/**
	 * removeRafPermissionType - removes a RafPermissionType
	 * 
	 * @param rafPermissionType
	 */
	public void removeRafPermissionType(RafPermissionType rafPermissionType);

	/**
	 * removeRafPermissionTypeList - removes a list of RafPermissionType
	 * 
	 * @param rafPermissionTypeList
	 */
	public void removeRafPermissionTypeList(List<RafPermissionType> rafPermissionTypeList);

	/**
	 * findByRafPermissionTypeLike - finds a list of RafPermissionType Like
	 * 
	 * @param rafPermissionType
	 * @return the list of RafPermissionType found
	 */
	public List<RafPermissionType> findByRafPermissionTypeLike(RafPermissionType rafPermissionType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafPermissionType>LikeFR - finds a list of RafPermissionType> Like with a finder return object
	 * 
	 * @param rafPermissionType
	 * @return the list of RafPermissionType found
	 */
	public FinderReturn findByRafPermissionTypeLikeFR(RafPermissionType rafPermissionType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
