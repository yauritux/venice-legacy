package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule38;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule38SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule38SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule38> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule38SessionEJBRemote#persistFrdParameterRule38(com
	 * .gdn.venice.persistence.FrdParameterRule38)
	 */
	public FrdParameterRule38 persistFrdParameterRule38(FrdParameterRule38 frdParameterRule38);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule38SessionEJBRemote#persistFrdParameterRule38List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule38> persistFrdParameterRule38List(
			List<FrdParameterRule38> frdParameterRule38List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule38SessionEJBRemote#mergeFrdParameterRule38(com.
	 * gdn.venice.persistence.FrdParameterRule38)
	 */
	public FrdParameterRule38 mergeFrdParameterRule38(FrdParameterRule38 frdParameterRule38);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule38SessionEJBRemote#mergeFrdParameterRule38List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule38> mergeFrdParameterRule38List(
			List<FrdParameterRule38> frdParameterRule38List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule38SessionEJBRemote#removeFrdParameterRule38(com
	 * .gdn.venice.persistence.FrdParameterRule38)
	 */
	public void removeFrdParameterRule38(FrdParameterRule38 frdParameterRule38);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule38SessionEJBRemote#removeFrdParameterRule38List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule38List(List<FrdParameterRule38> frdParameterRule38List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule38SessionEJBRemote#findByFrdParameterRule38Like
	 * (com.gdn.venice.persistence.FrdParameterRule38, int, int)
	 */
	public List<FrdParameterRule38> findByFrdParameterRule38Like(FrdParameterRule38 frdParameterRule38,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule38SessionEJBRemote#findByFrdParameterRule38LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule38, int, int)
	 */
	public FinderReturn findByFrdParameterRule38LikeFR(FrdParameterRule38 frdParameterRule38,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
