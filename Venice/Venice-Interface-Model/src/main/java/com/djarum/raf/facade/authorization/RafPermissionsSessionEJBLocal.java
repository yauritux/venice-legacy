package com.djarum.raf.facade.authorization;
import java.util.List;

import javax.ejb.Local;

@Local
public interface RafPermissionsSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.djarum.raf.facade.authorization.RafPermissionsSessionEJBRemote#getUserPermissionList(java.lang
	 * .String)
	 */
    public List<RafPermission> getUserPermissionList(String userLoginName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.djarum.raf.facade.authorization.RafPermissionsSessionEJBRemote#getUserPermissionList(java.lang
	 * .String, java.lang.String)
	 */
    public List<RafPermission> getUserPermissionList(String userLoginName, String canonicalName);
}
