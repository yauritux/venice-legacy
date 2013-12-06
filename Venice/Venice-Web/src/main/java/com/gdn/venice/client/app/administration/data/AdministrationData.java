package com.gdn.venice.client.app.administration.data;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.administration.presenter.ModuleConfigurationPresenter;
import com.gdn.venice.client.app.administration.presenter.RoleProfileUserGroupManagementPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * Defines the data sources for the user, group, role and profile data for testing purposes.
 * @author David Forden
 *
 */
public class AdministrationData {
	private static DataSource profileScreenDataDs= null;
	private static DataSource moduleDataDs = null;
	
	public static RafDataSource getRoleData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RAFROLE_ROLEID, "Role ID"),
				new DataSourceTextField(DataNameTokens.RAFROLE_ROLENAME, "Role Name"),
				new DataSourceTextField(DataNameTokens.RAFROLE_ROLEDESC, "Description"),
				new DataSourceTextField(DataNameTokens.RAFROLE_PARENTROLE, "Reports to")
		};
		dataSourceFields[0].setPrimaryKey(true);
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=fetchRoleData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=addRoleData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=updateRoleData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=deleteRoleData&type=DataSource",
				dataSourceFields); 
				
		return retVal;
	}
	
	public static DataSource getRoleDetailProfileData(String roleId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RAFROLEPROFILE_RAFROLEPROFILEID, "Role Profile ID"),
				new DataSourceTextField(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID, "Role Name"),
				new DataSourceTextField(DataNameTokens.RAFROLE_RAFROLEPROFILES_PROFILEID, "Assigned Profile")
		};
		dataSourceFields[0].setPrimaryKey(true);
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=fetchRoleDetailProfileData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=addRoleDetailProfileData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=updateRoleDetailProfileData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=deleteRoleDetailProfileData&type=DataSource",
				dataSourceFields); 
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.RAFROLE_ROLEID, roleId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.ADD).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.UPDATE).setDefaultParams(params);
		return retVal;
	}
	
	public static DataSource getRoleDetailUserData(String roleId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RAFUSERROLE_RAFUSERROLEID, "User Role ID"),
				new DataSourceTextField(DataNameTokens.RAFROLE_RAFUSERROLES_ROLEID, "Role Name"),
				new DataSourceTextField(DataNameTokens.RAFROLE_RAFUSERROLES_USERID, "Assigned User")
		};
		dataSourceFields[0].setPrimaryKey(true);
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=fetchRoleDetailUserData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=addRoleDetailUserData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=updateRoleDetailUserData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=deleteRoleDetailUserData&type=DataSource",
				dataSourceFields); 
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.RAFROLE_ROLEID, roleId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.ADD).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.UPDATE).setDefaultParams(params);
		return retVal;
	}
	
	public static RafDataSource getProfileData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RAFPROFILE_PROFILEID, "Profile ID"),
				new DataSourceTextField(DataNameTokens.RAFPROFILE_PROFILENAME, "Profile Name"),
				new DataSourceTextField(DataNameTokens.RAFPROFILE_PROFILEDESC, "Description")
		};
		dataSourceFields[0].setPrimaryKey(true);
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=fetchProfileData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=addProfileData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=updateProfileData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=deleteProfileData&type=DataSource",
				dataSourceFields); 
				
		return retVal;
	}
	
	public static DataSource getProfileDetailData(String profileId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RAFPROFILEPERMISSION_RAFPROFILEPERMISSIONID, "Profile Permission ID"),
				new DataSourceTextField(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID, "Profile Name"),
				new DataSourceTextField(DataNameTokens.RAFPROFILEPERMISSION_APPLICATIONOBJECTID, "Screen Name"),
				new DataSourceTextField(DataNameTokens.RAFPROFILEPERMISSION_PERMISSIONTYPEID, "Permission Type")
		};
		dataSourceFields[0].setPrimaryKey(true);
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=fetchProfileDetailData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=addProfileDetailData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=updateProfileDetailData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=deleteProfileDetailData&type=DataSource",
				dataSourceFields); 
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.RAFPROFILE_PROFILEID, profileId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.ADD).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.UPDATE).setDefaultParams(params);
		return retVal;
	}
	
//	public static DataSource getProfileScreenData() {
//		if (profileScreenDataDs != null) {
//			return profileScreenDataDs;
//		} else {
//			profileScreenDataDs = new DataSource();
//			profileScreenDataDs.setRecordXPath("/admin/screens/screen");
//			
//			DataSourceTextField screenNameField = new DataSourceTextField("name", "Screen");
//			DataSourceTextField fieldNameField = new DataSourceTextField("field", "Field");
//			DataSourceBooleanField editField = new DataSourceBooleanField("edit", "Edit");
//			DataSourceBooleanField viewField = new DataSourceBooleanField("view", "View");
//			DataSourceBooleanField deleteField = new DataSourceBooleanField("delete", "Delete");
//			
//			profileScreenDataDs.setFields(screenNameField, fieldNameField, editField, viewField, deleteField);
//			profileScreenDataDs.setDataURL("ds/test_data/admin.data.xml");
//			profileScreenDataDs.setClientOnly(true);
//	        
//	        return profileScreenDataDs;
//		}    
//		
//	}
	
	public static RafDataSource getUserData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RAFUSER_USERID, "User ID"),
				new DataSourceTextField(DataNameTokens.RAFUSER_LOGINNAME, "User Name")
		};
		dataSourceFields[0].setPrimaryKey(true);
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=fetchUserData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=addUserData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=updateUserData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=deleteUserData&type=DataSource",
				dataSourceFields); 
				
		return retVal;
	}
	
	public static DataSource getUserDetailGroupData(String userId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RAFUSERGROUP_RAFUSERGROUPID, "User Group ID"),
				new DataSourceTextField(DataNameTokens.RAFUSER_RAFUSERGROUP_USERID, "User Name"),
				new DataSourceTextField(DataNameTokens.RAFUSER_RAFUSERGROUP_GROUPID, "Assigned Group")
		};
		dataSourceFields[0].setPrimaryKey(true);
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=fetchUserDetailGroupData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=addUserDetailGroupData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=updateUserDetailGroupData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=deleteUserDetailGroupData&type=DataSource",
				dataSourceFields); 
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.RAFUSER_USERID, userId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.ADD).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.UPDATE).setDefaultParams(params);
		return retVal;
	}
	
	public static DataSource getUserDetailRoleData(String userId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RAFUSERROLE_RAFUSERROLEID, "User Role ID"),
				new DataSourceTextField(DataNameTokens.RAFUSER_RAFUSERROLES_USERID, "User Name"),
				new DataSourceTextField(DataNameTokens.RAFUSER_RAFUSERROLES_ROLEID, "Assigned Role")
		};
		dataSourceFields[0].setPrimaryKey(true);
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=fetchUserDetailRoleData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=addUserDetailRoleData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=updateUserDetailRoleData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=deleteUserDetailRoleData&type=DataSource",
				dataSourceFields); 
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.RAFUSER_USERID, userId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.ADD).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.UPDATE).setDefaultParams(params);
		return retVal;
	}
	
	public static RafDataSource getGroupData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RAFGROUP_GROUPID, "Group ID"),
				new DataSourceTextField(DataNameTokens.RAFGROUP_GROUPNAME, "Group Name"),
				new DataSourceTextField(DataNameTokens.RAFGROUP_GROUPDESC, "Description")
		};
		dataSourceFields[0].setPrimaryKey(true);
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=fetchGroupData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=addGroupData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=updateGroupData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=deleteGroupData&type=DataSource",
				dataSourceFields); 
				
		return retVal;
	}
	
	public static RafDataSource getGroupDetailData(String groupId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RAFGROUPROLE_RAFGROUPROLEID, "Group Role ID"),
				new DataSourceTextField(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID, "Group"),
				new DataSourceTextField(DataNameTokens.RAFGROUP_RAFGROUPROLES_ROLEID, "Assigned Role")
		};
		dataSourceFields[0].setPrimaryKey(true);
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=fetchGroupDetailData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=addGroupDetailData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=updateGroupDetailData&type=DataSource",
				GWT.getHostPageBaseURL() + RoleProfileUserGroupManagementPresenter.roleProfileUserGroupManagementPresenterServlet + "?method=deleteGroupDetailData&type=DataSource",
				dataSourceFields); 
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.RAFGROUP_GROUPID, groupId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.ADD).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.UPDATE).setDefaultParams(params);
		return retVal;
	}
	
	public static RafDataSource getModuleConfigurationData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTID, "Module ID"),
				new DataSourceTextField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTUUID, "Module UUID"),
				new DataSourceTextField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTTYPEID, "Module Type"),
				new DataSourceTextField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTCANONICALNAME, "Module Name"),
				new DataSourceTextField(DataNameTokens.RAFAPPLICATIONOBJECT_PARENTAPPLICATIONOBJECTID, "Parent Module")
		};
		dataSourceFields[0].setPrimaryKey(true);
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ModuleConfigurationPresenter.moduleConfigurationPresenterServlet + "?method=fetchModuleConfigurationData&type=DataSource",
				GWT.getHostPageBaseURL() + ModuleConfigurationPresenter.moduleConfigurationPresenterServlet + "?method=addModuleConfigurationData&type=DataSource",
				GWT.getHostPageBaseURL() + ModuleConfigurationPresenter.moduleConfigurationPresenterServlet + "?method=updateModuleConfigurationData&type=DataSource",
				GWT.getHostPageBaseURL() + ModuleConfigurationPresenter.moduleConfigurationPresenterServlet + "?method=deleteModuleConfigurationData&type=DataSource",
				dataSourceFields); 
				
		return retVal;
	}
	
	public static TreeNode[] getRoleTreeData() {
		return new RoleTreeNode[]{
			  new RoleTreeNode("2", "1", "CEO"),
			  new RoleTreeNode("3", "2", "Operations Head"),
			  new RoleTreeNode("4", "3", "Operations Officer"),
			  new RoleTreeNode("5", "4", "Operations Staff"),
			  new RoleTreeNode("6", "2", "Finance Head"),
			  new RoleTreeNode("7", "6", "Finance Officer"),
			  new RoleTreeNode("8", "7", "Finance Staff")
			};
	}
	
	private static class RoleTreeNode extends TreeNode{
		public RoleTreeNode(String roleId, String roleParent, String roleName) {
			setNavigationId(roleId);
			setNavigationParent(roleParent);
			setNavigationName(roleName);
		}

		public void setNavigationId(String id) {
			setAttribute("RoleId", id);
		}

		public void setNavigationParent(String navigationParent) {
			setAttribute("RoleParent", navigationParent);
		}

		public void setNavigationName(String navigationName) {
			setAttribute("RoleName", navigationName);
		}
	}
	
//	public static DataSource getModuleData() {
//		if (moduleDataDs != null) {
//			return moduleDataDs;
//		} else {
//			moduleDataDs = new DataSource();
//			moduleDataDs.setRecordXPath("/admin/modules/module");
//			
//			DataSourceTextField moduleIdField = new DataSourceTextField("moduleid", "Module Id");
//			DataSourceTextField moduleUuidField = new DataSourceTextField("uuid", "UUID");
//			DataSourceTextField moduleTypeField = new DataSourceTextField("type", "Module Type");
//			DataSourceTextField moduleNameField = new DataSourceTextField("name", "Module Name");
//			
//			moduleDataDs.setFields(moduleIdField, moduleUuidField,moduleTypeField, moduleNameField);
//			moduleDataDs.setDataURL("ds/test_data/admin.data.xml");
//			moduleDataDs.setClientOnly(true);
//	        
//	        return moduleDataDs;
//		}    
//		
//	}


}
