package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule37;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule37SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule37SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule37> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule37SessionEJBRemote#persistFrdParameterRule37(com
	 * .gdn.venice.persistence.FrdParameterRule37)
	 */
	public FrdParameterRule37 persistFrdParameterRule37(FrdParameterRule37 frdParameterRule37);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule37SessionEJBRemote#persistFrdParameterRule37List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule37> persistFrdParameterRule37List(
			List<FrdParameterRule37> frdParameterRule37List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule37SessionEJBRemote#mergeFrdParameterRule37(com.
	 * gdn.venice.persistence.FrdParameterRule37)
	 */
	public FrdParameterRule37 mergeFrdParameterRule37(FrdParameterRule37 frdParameterRule37);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule37SessionEJBRemote#mergeFrdParameterRule37List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule37> mergeFrdParameterRule37List(
			List<FrdParameterRule37> frdParameterRule37List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule37SessionEJBRemote#removeFrdParameterRule37(com
	 * .gdn.venice.persistence.FrdParameterRule37)
	 */
	public void removeFrdParameterRule37(FrdParameterRule37 frdParameterRule37);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule37SessionEJBRemote#removeFrdParameterRule37List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule37List(List<FrdParameterRule37> frdParameterRule37List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule37SessionEJBRemote#findByFrdParameterRule37Like
	 * (com.gdn.venice.persistence.FrdParameterRule37, int, int)
	 */
	public List<FrdParameterRule37> findByFrdParameterRule37Like(FrdParameterRule37 frdParameterRule37,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule37SessionEJBRemote#findByFrdParameterRule37LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule37, int, int)
	 */
	public FinderReturn findByFrdParameterRule37LikeFR(FrdParameterRule37 frdParameterRule37,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
