package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule15;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule15SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule15SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule15> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule15SessionEJBRemote#persistFrdParameterRule15(com
	 * .gdn.venice.persistence.FrdParameterRule15)
	 */
	public FrdParameterRule15 persistFrdParameterRule15(FrdParameterRule15 frdParameterRule15);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule15SessionEJBRemote#persistFrdParameterRule15List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule15> persistFrdParameterRule15List(
			List<FrdParameterRule15> frdParameterRule15List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule15SessionEJBRemote#mergeFrdParameterRule15(com.
	 * gdn.venice.persistence.FrdParameterRule15)
	 */
	public FrdParameterRule15 mergeFrdParameterRule15(FrdParameterRule15 frdParameterRule15);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule15SessionEJBRemote#mergeFrdParameterRule15List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule15> mergeFrdParameterRule15List(
			List<FrdParameterRule15> frdParameterRule15List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule15SessionEJBRemote#removeFrdParameterRule15(com
	 * .gdn.venice.persistence.FrdParameterRule15)
	 */
	public void removeFrdParameterRule15(FrdParameterRule15 frdParameterRule15);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule15SessionEJBRemote#removeFrdParameterRule15List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule15List(List<FrdParameterRule15> frdParameterRule15List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule15SessionEJBRemote#findByFrdParameterRule15Like
	 * (com.gdn.venice.persistence.FrdParameterRule15, int, int)
	 */
	public List<FrdParameterRule15> findByFrdParameterRule15Like(FrdParameterRule15 frdParameterRule15,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule15SessionEJBRemote#findByFrdParameterRule15LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule15, int, int)
	 */
	public FinderReturn findByFrdParameterRule15LikeFR(FrdParameterRule15 frdParameterRule15,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
