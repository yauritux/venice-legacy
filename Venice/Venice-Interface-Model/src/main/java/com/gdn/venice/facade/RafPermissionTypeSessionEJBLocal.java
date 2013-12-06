package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafPermissionType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafPermissionTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafPermissionTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafPermissionType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafPermissionTypeSessionEJBRemote#persistRafPermissionType(com
	 * .gdn.venice.persistence.RafPermissionType)
	 */
	public RafPermissionType persistRafPermissionType(RafPermissionType rafPermissionType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafPermissionTypeSessionEJBRemote#persistRafPermissionTypeList
	 * (java.util.List)
	 */
	public ArrayList<RafPermissionType> persistRafPermissionTypeList(
			List<RafPermissionType> rafPermissionTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafPermissionTypeSessionEJBRemote#mergeRafPermissionType(com.
	 * gdn.venice.persistence.RafPermissionType)
	 */
	public RafPermissionType mergeRafPermissionType(RafPermissionType rafPermissionType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafPermissionTypeSessionEJBRemote#mergeRafPermissionTypeList(
	 * java.util.List)
	 */
	public ArrayList<RafPermissionType> mergeRafPermissionTypeList(
			List<RafPermissionType> rafPermissionTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafPermissionTypeSessionEJBRemote#removeRafPermissionType(com
	 * .gdn.venice.persistence.RafPermissionType)
	 */
	public void removeRafPermissionType(RafPermissionType rafPermissionType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafPermissionTypeSessionEJBRemote#removeRafPermissionTypeList
	 * (java.util.List)
	 */
	public void removeRafPermissionTypeList(List<RafPermissionType> rafPermissionTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafPermissionTypeSessionEJBRemote#findByRafPermissionTypeLike
	 * (com.gdn.venice.persistence.RafPermissionType, int, int)
	 */
	public List<RafPermissionType> findByRafPermissionTypeLike(RafPermissionType rafPermissionType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafPermissionTypeSessionEJBRemote#findByRafPermissionTypeLikeFR
	 * (com.gdn.venice.persistence.RafPermissionType, int, int)
	 */
	public FinderReturn findByRafPermissionTypeLikeFR(RafPermissionType rafPermissionType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
