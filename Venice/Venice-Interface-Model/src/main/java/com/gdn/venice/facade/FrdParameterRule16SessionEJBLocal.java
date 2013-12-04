package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule16;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule16SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule16SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule16> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule16SessionEJBRemote#persistFrdParameterRule16(com
	 * .gdn.venice.persistence.FrdParameterRule16)
	 */
	public FrdParameterRule16 persistFrdParameterRule16(FrdParameterRule16 frdParameterRule16);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule16SessionEJBRemote#persistFrdParameterRule16List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule16> persistFrdParameterRule16List(
			List<FrdParameterRule16> frdParameterRule16List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule16SessionEJBRemote#mergeFrdParameterRule16(com.
	 * gdn.venice.persistence.FrdParameterRule16)
	 */
	public FrdParameterRule16 mergeFrdParameterRule16(FrdParameterRule16 frdParameterRule16);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule16SessionEJBRemote#mergeFrdParameterRule16List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule16> mergeFrdParameterRule16List(
			List<FrdParameterRule16> frdParameterRule16List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule16SessionEJBRemote#removeFrdParameterRule16(com
	 * .gdn.venice.persistence.FrdParameterRule16)
	 */
	public void removeFrdParameterRule16(FrdParameterRule16 frdParameterRule16);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule16SessionEJBRemote#removeFrdParameterRule16List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule16List(List<FrdParameterRule16> frdParameterRule16List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule16SessionEJBRemote#findByFrdParameterRule16Like
	 * (com.gdn.venice.persistence.FrdParameterRule16, int, int)
	 */
	public List<FrdParameterRule16> findByFrdParameterRule16Like(FrdParameterRule16 frdParameterRule16,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule16SessionEJBRemote#findByFrdParameterRule16LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule16, int, int)
	 */
	public FinderReturn findByFrdParameterRule16LikeFR(FrdParameterRule16 frdParameterRule16,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
