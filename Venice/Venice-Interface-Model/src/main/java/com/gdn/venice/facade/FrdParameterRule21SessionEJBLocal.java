package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule21;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule21SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule21SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule21> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule21SessionEJBRemote#persistFrdParameterRule21(com
	 * .gdn.venice.persistence.FrdParameterRule21)
	 */
	public FrdParameterRule21 persistFrdParameterRule21(FrdParameterRule21 frdParameterRule21);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule21SessionEJBRemote#persistFrdParameterRule21List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule21> persistFrdParameterRule21List(
			List<FrdParameterRule21> frdParameterRule21List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule21SessionEJBRemote#mergeFrdParameterRule21(com.
	 * gdn.venice.persistence.FrdParameterRule21)
	 */
	public FrdParameterRule21 mergeFrdParameterRule21(FrdParameterRule21 frdParameterRule21);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule21SessionEJBRemote#mergeFrdParameterRule21List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule21> mergeFrdParameterRule21List(
			List<FrdParameterRule21> frdParameterRule21List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule21SessionEJBRemote#removeFrdParameterRule21(com
	 * .gdn.venice.persistence.FrdParameterRule21)
	 */
	public void removeFrdParameterRule21(FrdParameterRule21 frdParameterRule21);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule21SessionEJBRemote#removeFrdParameterRule21List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule21List(List<FrdParameterRule21> frdParameterRule21List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule21SessionEJBRemote#findByFrdParameterRule21Like
	 * (com.gdn.venice.persistence.FrdParameterRule21, int, int)
	 */
	public List<FrdParameterRule21> findByFrdParameterRule21Like(FrdParameterRule21 frdParameterRule21,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule21SessionEJBRemote#findByFrdParameterRule21LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule21, int, int)
	 */
	public FinderReturn findByFrdParameterRule21LikeFR(FrdParameterRule21 frdParameterRule21,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
