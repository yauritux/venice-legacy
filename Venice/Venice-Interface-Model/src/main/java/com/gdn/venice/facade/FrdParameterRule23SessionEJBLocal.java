package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule23;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule23SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule23SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule23> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule23SessionEJBRemote#persistFrdParameterRule23(com
	 * .gdn.venice.persistence.FrdParameterRule23)
	 */
	public FrdParameterRule23 persistFrdParameterRule23(FrdParameterRule23 frdParameterRule23);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule23SessionEJBRemote#persistFrdParameterRule23List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule23> persistFrdParameterRule23List(
			List<FrdParameterRule23> frdParameterRule23List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule23SessionEJBRemote#mergeFrdParameterRule23(com.
	 * gdn.venice.persistence.FrdParameterRule23)
	 */
	public FrdParameterRule23 mergeFrdParameterRule23(FrdParameterRule23 frdParameterRule23);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule23SessionEJBRemote#mergeFrdParameterRule23List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule23> mergeFrdParameterRule23List(
			List<FrdParameterRule23> frdParameterRule23List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule23SessionEJBRemote#removeFrdParameterRule23(com
	 * .gdn.venice.persistence.FrdParameterRule23)
	 */
	public void removeFrdParameterRule23(FrdParameterRule23 frdParameterRule23);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule23SessionEJBRemote#removeFrdParameterRule23List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule23List(List<FrdParameterRule23> frdParameterRule23List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule23SessionEJBRemote#findByFrdParameterRule23Like
	 * (com.gdn.venice.persistence.FrdParameterRule23, int, int)
	 */
	public List<FrdParameterRule23> findByFrdParameterRule23Like(FrdParameterRule23 frdParameterRule23,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule23SessionEJBRemote#findByFrdParameterRule23LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule23, int, int)
	 */
	public FinderReturn findByFrdParameterRule23LikeFR(FrdParameterRule23 frdParameterRule23,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
