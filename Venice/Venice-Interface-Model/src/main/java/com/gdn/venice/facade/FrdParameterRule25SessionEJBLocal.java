package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule25;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule25SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule25SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule25> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule25SessionEJBRemote#persistFrdParameterRule25(com
	 * .gdn.venice.persistence.FrdParameterRule25)
	 */
	public FrdParameterRule25 persistFrdParameterRule25(FrdParameterRule25 frdParameterRule25);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule25SessionEJBRemote#persistFrdParameterRule25List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule25> persistFrdParameterRule25List(
			List<FrdParameterRule25> frdParameterRule25List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule25SessionEJBRemote#mergeFrdParameterRule25(com.
	 * gdn.venice.persistence.FrdParameterRule25)
	 */
	public FrdParameterRule25 mergeFrdParameterRule25(FrdParameterRule25 frdParameterRule25);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule25SessionEJBRemote#mergeFrdParameterRule25List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule25> mergeFrdParameterRule25List(
			List<FrdParameterRule25> frdParameterRule25List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule25SessionEJBRemote#removeFrdParameterRule25(com
	 * .gdn.venice.persistence.FrdParameterRule25)
	 */
	public void removeFrdParameterRule25(FrdParameterRule25 frdParameterRule25);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule25SessionEJBRemote#removeFrdParameterRule25List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule25List(List<FrdParameterRule25> frdParameterRule25List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule25SessionEJBRemote#findByFrdParameterRule25Like
	 * (com.gdn.venice.persistence.FrdParameterRule25, int, int)
	 */
	public List<FrdParameterRule25> findByFrdParameterRule25Like(FrdParameterRule25 frdParameterRule25,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule25SessionEJBRemote#findByFrdParameterRule25LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule25, int, int)
	 */
	public FinderReturn findByFrdParameterRule25LikeFR(FrdParameterRule25 frdParameterRule25,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
