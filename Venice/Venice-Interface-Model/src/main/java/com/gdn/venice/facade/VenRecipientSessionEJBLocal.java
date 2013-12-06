package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenRecipient;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenRecipientSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenRecipientSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenRecipient> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenRecipientSessionEJBRemote#persistVenRecipient(com
	 * .gdn.venice.persistence.VenRecipient)
	 */
	public VenRecipient persistVenRecipient(VenRecipient venRecipient);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenRecipientSessionEJBRemote#persistVenRecipientList
	 * (java.util.List)
	 */
	public ArrayList<VenRecipient> persistVenRecipientList(
			List<VenRecipient> venRecipientList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenRecipientSessionEJBRemote#mergeVenRecipient(com.
	 * gdn.venice.persistence.VenRecipient)
	 */
	public VenRecipient mergeVenRecipient(VenRecipient venRecipient);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenRecipientSessionEJBRemote#mergeVenRecipientList(
	 * java.util.List)
	 */
	public ArrayList<VenRecipient> mergeVenRecipientList(
			List<VenRecipient> venRecipientList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenRecipientSessionEJBRemote#removeVenRecipient(com
	 * .gdn.venice.persistence.VenRecipient)
	 */
	public void removeVenRecipient(VenRecipient venRecipient);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenRecipientSessionEJBRemote#removeVenRecipientList
	 * (java.util.List)
	 */
	public void removeVenRecipientList(List<VenRecipient> venRecipientList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenRecipientSessionEJBRemote#findByVenRecipientLike
	 * (com.gdn.venice.persistence.VenRecipient, int, int)
	 */
	public List<VenRecipient> findByVenRecipientLike(VenRecipient venRecipient,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenRecipientSessionEJBRemote#findByVenRecipientLikeFR
	 * (com.gdn.venice.persistence.VenRecipient, int, int)
	 */
	public FinderReturn findByVenRecipientLikeFR(VenRecipient venRecipient,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
