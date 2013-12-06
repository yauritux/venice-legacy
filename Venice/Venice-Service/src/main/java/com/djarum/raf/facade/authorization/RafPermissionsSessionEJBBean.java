package com.djarum.raf.facade.authorization;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote;
import com.gdn.venice.facade.RafProfileSessionEJBRemote;
import com.gdn.venice.facade.RafUserPermissionSessionEJBRemote;
import com.gdn.venice.persistence.RafProfile;
import com.gdn.venice.persistence.RafProfilePermission;
import com.gdn.venice.persistence.RafUserPermission;

/**
 * Session Bean implementation class RafPermissionsSessionEJBBean
 * 
 * This bean class provides aggregated methods for returning all of the
 * permissions for a user on all objects and on singular objects.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Stateless(mappedName = "RafPermissionsSessionEJBBean")
public class RafPermissionsSessionEJBBean implements RafPermissionsSessionEJBRemote, RafPermissionsSessionEJBLocal {

	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public RafPermissionsSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.djarum.raf.facade.authorization.RafPermissionsSessionEJBBean");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.djarum.raf.facade.authorization.RafPermissionsSessionEJBRemote#
	 * getUserPermissionList(java.lang .String)
	 */
	public List<RafPermission> getUserPermissionList(String userLoginName) {
		ArrayList<RafPermission> rafPermissionList = new ArrayList<RafPermission>();
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();

			// Get the list of profile id's for the user
			RafProfileSessionEJBRemote profileHome = (RafProfileSessionEJBRemote) locator.lookup(RafProfileSessionEJBRemote.class, "RafProfileSessionEJBBean");

			/*
			 * This needed to be changed to due to structure changes implemented
			 * by Roland when building the screens
			 */
//			List<RafProfile> rafProfileList = profileHome
//			.queryByRange(
//					"select pro from RafUser usr, RafProfile pro, in(pro.rafRoles) rol , in(usr.rafRoles) rol2 "
//							+ " where usr.loginName = '"
//							+ userLoginName + "'" + 
//							" and pro member of rol2.rafProfiles", 0, 0);

			/*
			 * Note that the user-specific permissions are not queried
			 */
			List<RafProfile> rafProfileList = profileHome.queryByRange(
					"select pro from RafProfile pro,  RafUser usr, RafUserRole urol, RafRoleProfile rolpro"
							+ " where usr.loginName = '"
							+ userLoginName + "'" + 
							" and urol.rafUser.userId = usr.userId" +
							" and urol.rafRole.roleId = rolpro.rafRole.roleId" +
							" and rolpro.rafProfile.profileId = pro.profileId", 0, 0);

			RafProfilePermissionSessionEJBRemote profilePermissionHome = (RafProfilePermissionSessionEJBRemote) locator
					.lookup(RafProfilePermissionSessionEJBRemote.class, "RafProfilePermissionSessionEJBBean");

			// Get the user profile associated permissions (add to list)
			for (RafProfile profile : rafProfileList) {
				List<RafProfilePermission> rafProfilePermissionList = profilePermissionHome.queryByRange(
								"select o from RafProfilePermission o where o.rafProfile.profileId = "+ profile.getProfileId(), 0, 0);
				for (RafProfilePermission rafProfilepermission : rafProfilePermissionList) {
					RafPermission permission = new RafPermission();
					permission.setApplicationObjectCanonicalName(rafProfilepermission.getRafApplicationObject().getApplicationObjectCanonicalName());
					permission.setPermissionTypeId(rafProfilepermission.getRafPermissionType().getPermissionTypeId());
					rafPermissionList.add(permission);
				}

			}

			RafUserPermissionSessionEJBRemote userPermissionHome = (RafUserPermissionSessionEJBRemote) locator
					.lookup(RafUserPermissionSessionEJBRemote.class, "RafUserPermissionSessionEJBBean");

			// Get the user associated permissions (add to list)
			List<RafUserPermission> rafUserPermissionList = userPermissionHome
					.queryByRange("select o from RafUserPermission o where o.rafUser.loginName = '" + userLoginName + "'", 0, 0);
			if(rafUserPermissionList.size()>0){
				for (RafUserPermission rafUserPermission : rafUserPermissionList) {
					RafPermission permission = new RafPermission();
					permission.setApplicationObjectCanonicalName(rafUserPermission.getRafApplicationObject().getApplicationObjectCanonicalName());
					permission.setPermissionTypeId(rafUserPermission.getRafPermissionType().getPermissionTypeId());
					rafPermissionList.add(permission);
				}
			}

		} catch (Exception e) {
			String errMsg = "An Exception occured when getting a user permission list 1:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg);
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return rafPermissionList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.djarum.raf.facade.authorization.RafPermissionsSessionEJBRemote#
	 * getUserPermissionList(java.lang .String, java.lang.String)
	 */
	public List<RafPermission> getUserPermissionList(String userLoginName, String canonicalName) {
		ArrayList<RafPermission> rafPermissionList = new ArrayList<RafPermission>();
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			// Get the list of profile id's for the user
			RafProfileSessionEJBRemote profileHome = (RafProfileSessionEJBRemote) locator
					.lookup(RafProfileSessionEJBRemote.class, "RafProfileSessionEJBBean");

			List<RafProfile> rafProfileList = profileHome.queryByRange(
					"select pro from RafUser usr, RafProfile pro, in(pro.rafRoles) rol , in(usr.rafRoles) rol2 "
							+ " where usr.loginName = '"
							+ userLoginName + "'" + 
							" and pro member of rol2.rafProfiles", 0, 0);

			RafProfilePermissionSessionEJBRemote profilePermissionHome = (RafProfilePermissionSessionEJBRemote) locator
					.lookup(RafProfilePermissionSessionEJBRemote.class, "RafProfilePermissionSessionEJBBean");

			// Get the user profile associated permissions for the object (add to list)
			for (RafProfile profile : rafProfileList) {
				List<RafProfilePermission> rafProfilePermissionList = profilePermissionHome	.queryByRange(
								"select o from RafProfilePermission o where o.rafProfile.profileId = "
										+ profile.getProfileId()
										+ " and o.rafApplicationObject.applicationObjectCanonicalName = '"
										+ canonicalName + "'", 0, 0);
				for (RafProfilePermission rafProfilepermission : rafProfilePermissionList) {
					RafPermission permission = new RafPermission();
					permission.setApplicationObjectCanonicalName(rafProfilepermission.getRafApplicationObject().getApplicationObjectCanonicalName());
					permission.setPermissionTypeId(rafProfilepermission.getRafPermissionType().getPermissionTypeId());
					rafPermissionList.add(permission);
				}
			}

			RafUserPermissionSessionEJBRemote userPermissionHome = (RafUserPermissionSessionEJBRemote) locator
					.lookup(RafUserPermissionSessionEJBRemote.class, "RafUserPermissionSessionEJBBean");

			// Get the user associated permissions for the object (add to list)
			List<RafUserPermission> rafUserPermissionList = userPermissionHome.queryByRange(
							"select o from RafUserPermission o where o.rafUser.loginName = '"
									+ userLoginName
									+ "'"
									+ " and o.rafApplicationObject.applicationObjectCanonicalName = '"
									+ canonicalName + "'", 0, 0);
			if(rafUserPermissionList.size()>0){
				for (RafUserPermission rafUserPermission : rafUserPermissionList) {
					RafPermission permission = new RafPermission();
					permission.setApplicationObjectCanonicalName(rafUserPermission.getRafApplicationObject().getApplicationObjectCanonicalName());
					permission.setPermissionTypeId(rafUserPermission.getRafPermissionType().getPermissionTypeId());
					rafPermissionList.add(permission);
				}
			}

		} catch (Exception e) {
			String errMsg = "An Exception occured when getting a user permission list 2:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg);
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return rafPermissionList;
	}
}
