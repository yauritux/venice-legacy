package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenDeliveryDocket;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenDeliveryDocketSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDeliveryDocketSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenDeliveryDocket> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDeliveryDocketSessionEJBRemote#persistVenDeliveryDocket(com
	 * .gdn.venice.persistence.VenDeliveryDocket)
	 */
	public VenDeliveryDocket persistVenDeliveryDocket(VenDeliveryDocket venDeliveryDocket);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDeliveryDocketSessionEJBRemote#persistVenDeliveryDocketList
	 * (java.util.List)
	 */
	public ArrayList<VenDeliveryDocket> persistVenDeliveryDocketList(
			List<VenDeliveryDocket> venDeliveryDocketList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDeliveryDocketSessionEJBRemote#mergeVenDeliveryDocket(com.
	 * gdn.venice.persistence.VenDeliveryDocket)
	 */
	public VenDeliveryDocket mergeVenDeliveryDocket(VenDeliveryDocket venDeliveryDocket);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDeliveryDocketSessionEJBRemote#mergeVenDeliveryDocketList(
	 * java.util.List)
	 */
	public ArrayList<VenDeliveryDocket> mergeVenDeliveryDocketList(
			List<VenDeliveryDocket> venDeliveryDocketList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDeliveryDocketSessionEJBRemote#removeVenDeliveryDocket(com
	 * .gdn.venice.persistence.VenDeliveryDocket)
	 */
	public void removeVenDeliveryDocket(VenDeliveryDocket venDeliveryDocket);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDeliveryDocketSessionEJBRemote#removeVenDeliveryDocketList
	 * (java.util.List)
	 */
	public void removeVenDeliveryDocketList(List<VenDeliveryDocket> venDeliveryDocketList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDeliveryDocketSessionEJBRemote#findByVenDeliveryDocketLike
	 * (com.gdn.venice.persistence.VenDeliveryDocket, int, int)
	 */
	public List<VenDeliveryDocket> findByVenDeliveryDocketLike(VenDeliveryDocket venDeliveryDocket,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDeliveryDocketSessionEJBRemote#findByVenDeliveryDocketLikeFR
	 * (com.gdn.venice.persistence.VenDeliveryDocket, int, int)
	 */
	public FinderReturn findByVenDeliveryDocketLikeFR(VenDeliveryDocket venDeliveryDocket,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
