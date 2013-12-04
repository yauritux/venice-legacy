package com.djarum.raf.facade.authorization;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface RafPermissionsSessionEJBRemote {

    /**
     * Returns a list of all the permissions a user has for all objects.
     * @param userLogonName
     * @return the permission list
     */
    public List<RafPermission> getUserPermissionList(String userLoginName);

    /**
     * Returns a list of the permissions a user has on a specific object
     * @param userLoginName
     * @param canonicalName
     * @return the permission list
     */
    public List<RafPermission> getUserPermissionList(String userLoginName, String canonicalName);

}
