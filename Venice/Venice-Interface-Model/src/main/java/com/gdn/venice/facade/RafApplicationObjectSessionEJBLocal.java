package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafApplicationObject;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafApplicationObjectSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafApplicationObject> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote#persistRafApplicationObject(com
	 * .gdn.venice.persistence.RafApplicationObject)
	 */
	public RafApplicationObject persistRafApplicationObject(RafApplicationObject rafApplicationObject);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote#persistRafApplicationObjectList
	 * (java.util.List)
	 */
	public ArrayList<RafApplicationObject> persistRafApplicationObjectList(
			List<RafApplicationObject> rafApplicationObjectList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote#mergeRafApplicationObject(com.
	 * gdn.venice.persistence.RafApplicationObject)
	 */
	public RafApplicationObject mergeRafApplicationObject(RafApplicationObject rafApplicationObject);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote#mergeRafApplicationObjectList(
	 * java.util.List)
	 */
	public ArrayList<RafApplicationObject> mergeRafApplicationObjectList(
			List<RafApplicationObject> rafApplicationObjectList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote#removeRafApplicationObject(com
	 * .gdn.venice.persistence.RafApplicationObject)
	 */
	public void removeRafApplicationObject(RafApplicationObject rafApplicationObject);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote#removeRafApplicationObjectList
	 * (java.util.List)
	 */
	public void removeRafApplicationObjectList(List<RafApplicationObject> rafApplicationObjectList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote#findByRafApplicationObjectLike
	 * (com.gdn.venice.persistence.RafApplicationObject, int, int)
	 */
	public List<RafApplicationObject> findByRafApplicationObjectLike(RafApplicationObject rafApplicationObject,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote#findByRafApplicationObjectLikeFR
	 * (com.gdn.venice.persistence.RafApplicationObject, int, int)
	 */
	public FinderReturn findByRafApplicationObjectLikeFR(RafApplicationObject rafApplicationObject,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
