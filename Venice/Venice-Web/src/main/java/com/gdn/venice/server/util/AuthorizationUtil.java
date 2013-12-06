package com.gdn.venice.server.util;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.facade.authorization.RafPermission;
import com.djarum.raf.facade.authorization.RafPermissionsSessionEJBRemote;
import com.djarum.raf.utilities.Locator;

/**
 * Utility class to get the permissions for a user based on login name
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class AuthorizationUtil {
	private static HashMap<String, List<RafPermission>> rafPermissions = new HashMap<String, List<RafPermission>>();
	
	/**
	 * Returns the user permissions list
	 * @param userName is the user to use in the search
	 * @return the permission list
	 */
	public static List<RafPermission> getUserPermissionList(String userName) {
		Locator<Object> rafPermissionsLocator = null;
		
		List<RafPermission> rafPermissionList  = null;
		
		try {
			rafPermissionsLocator = new Locator<Object>();
			
			RafPermissionsSessionEJBRemote rafPermissionsHome = (RafPermissionsSessionEJBRemote) rafPermissionsLocator
			.lookup(RafPermissionsSessionEJBRemote.class, "RafPermissionsSessionEJBBean");
			
			rafPermissionList  = rafPermissionsHome.getUserPermissionList(userName);
			rafPermissions.put(userName, rafPermissionList);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rafPermissionsLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return rafPermissionList;
	}
	
	/**
	 * Test main
	 * @param args
	 */
	public static void main(String[] args) {
//		List<RafPermission> list = AuthorizationUtil.getUserPermissionList("Hendry");
	}
}
