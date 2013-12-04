package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafApplicationObjectType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafApplicationObjectTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafApplicationObjectType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectTypeSessionEJBRemote#persistRafApplicationObjectType(com
	 * .gdn.venice.persistence.RafApplicationObjectType)
	 */
	public RafApplicationObjectType persistRafApplicationObjectType(RafApplicationObjectType rafApplicationObjectType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectTypeSessionEJBRemote#persistRafApplicationObjectTypeList
	 * (java.util.List)
	 */
	public ArrayList<RafApplicationObjectType> persistRafApplicationObjectTypeList(
			List<RafApplicationObjectType> rafApplicationObjectTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectTypeSessionEJBRemote#mergeRafApplicationObjectType(com.
	 * gdn.venice.persistence.RafApplicationObjectType)
	 */
	public RafApplicationObjectType mergeRafApplicationObjectType(RafApplicationObjectType rafApplicationObjectType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectTypeSessionEJBRemote#mergeRafApplicationObjectTypeList(
	 * java.util.List)
	 */
	public ArrayList<RafApplicationObjectType> mergeRafApplicationObjectTypeList(
			List<RafApplicationObjectType> rafApplicationObjectTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectTypeSessionEJBRemote#removeRafApplicationObjectType(com
	 * .gdn.venice.persistence.RafApplicationObjectType)
	 */
	public void removeRafApplicationObjectType(RafApplicationObjectType rafApplicationObjectType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectTypeSessionEJBRemote#removeRafApplicationObjectTypeList
	 * (java.util.List)
	 */
	public void removeRafApplicationObjectTypeList(List<RafApplicationObjectType> rafApplicationObjectTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectTypeSessionEJBRemote#findByRafApplicationObjectTypeLike
	 * (com.gdn.venice.persistence.RafApplicationObjectType, int, int)
	 */
	public List<RafApplicationObjectType> findByRafApplicationObjectTypeLike(RafApplicationObjectType rafApplicationObjectType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectTypeSessionEJBRemote#findByRafApplicationObjectTypeLikeFR
	 * (com.gdn.venice.persistence.RafApplicationObjectType, int, int)
	 */
	public FinderReturn findByRafApplicationObjectTypeLikeFR(RafApplicationObjectType rafApplicationObjectType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
