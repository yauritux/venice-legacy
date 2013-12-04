package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafObjectAttribute;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafObjectAttributeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectAttributeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafObjectAttribute> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectAttributeSessionEJBRemote#persistRafObjectAttribute(com
	 * .gdn.venice.persistence.RafObjectAttribute)
	 */
	public RafObjectAttribute persistRafObjectAttribute(RafObjectAttribute rafObjectAttribute);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectAttributeSessionEJBRemote#persistRafObjectAttributeList
	 * (java.util.List)
	 */
	public ArrayList<RafObjectAttribute> persistRafObjectAttributeList(
			List<RafObjectAttribute> rafObjectAttributeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectAttributeSessionEJBRemote#mergeRafObjectAttribute(com.
	 * gdn.venice.persistence.RafObjectAttribute)
	 */
	public RafObjectAttribute mergeRafObjectAttribute(RafObjectAttribute rafObjectAttribute);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectAttributeSessionEJBRemote#mergeRafObjectAttributeList(
	 * java.util.List)
	 */
	public ArrayList<RafObjectAttribute> mergeRafObjectAttributeList(
			List<RafObjectAttribute> rafObjectAttributeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectAttributeSessionEJBRemote#removeRafObjectAttribute(com
	 * .gdn.venice.persistence.RafObjectAttribute)
	 */
	public void removeRafObjectAttribute(RafObjectAttribute rafObjectAttribute);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectAttributeSessionEJBRemote#removeRafObjectAttributeList
	 * (java.util.List)
	 */
	public void removeRafObjectAttributeList(List<RafObjectAttribute> rafObjectAttributeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectAttributeSessionEJBRemote#findByRafObjectAttributeLike
	 * (com.gdn.venice.persistence.RafObjectAttribute, int, int)
	 */
	public List<RafObjectAttribute> findByRafObjectAttributeLike(RafObjectAttribute rafObjectAttribute,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectAttributeSessionEJBRemote#findByRafObjectAttributeLikeFR
	 * (com.gdn.venice.persistence.RafObjectAttribute, int, int)
	 */
	public FinderReturn findByRafObjectAttributeLikeFR(RafObjectAttribute rafObjectAttribute,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
