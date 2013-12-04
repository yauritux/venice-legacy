package com.gdn.venice.server.app.administration.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.administration.presenter.commands.AddGroupDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.AddGroupDetailDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.AddProfileDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.AddProfileDetailDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.AddRoleDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.AddRoleDetailProfileDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.AddRoleDetailUserDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.AddUserDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.AddUserDetailGroupDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.AddUserDetailRoleDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.DeleteGroupDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.DeleteGroupDetailDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.DeleteProfileDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.DeleteProfileDetailDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.DeleteRoleDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.DeleteRoleDetailProfileDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.DeleteRoleDetailUserDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.DeleteUserDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.DeleteUserDetailGroupDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.DeleteUserDetailRoleDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchGroupComboBoxDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchGroupDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchGroupDetailDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchPermissionComboBoxDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchProfileComboBoxDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchProfileDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchProfileDetailDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchRoleComboBoxDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchRoleDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchRoleDetailProfileDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchRoleDetailUserDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchScreenComboBoxDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchUserComboBoxDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchUserDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchUserDetailGroupDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchUserDetailRoleDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.UpdateGroupDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.UpdateGroupDetailDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.UpdateProfileDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.UpdateProfileDetailDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.UpdateRoleDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.UpdateRoleDetailProfileDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.UpdateRoleDetailUserDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.UpdateUserDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.UpdateUserDetailGroupDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.UpdateUserDetailRoleDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class RoleProfileUserGroupManagementPresenterServlet
 * 
 * @author Anto
 */
public class RoleProfileUserGroupManagementPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoleProfileUserGroupManagementPresenterServlet() {
        super();
       
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String retVal =  "";
		if (type.equals(RafDsCommand.DataSource)) {
			String requestBody = Util.extractRequestBody(request);
	
			RafDsRequest rafDsRequest = null;
			try {
				rafDsRequest = RafDsRequest.convertXmltoRafDsRequest(requestBody);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String method = request.getParameter("method");
			if (method.equals("fetchProfileData")) {				
				RafDsCommand fetchProfileDataCommand = new FetchProfileDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchProfileDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("addProfileData")){
				RafDsCommand addProfileDataCommand = new AddProfileDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addProfileDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("updateProfileData")){	
				RafDsCommand updateProfileDataCommand = new UpdateProfileDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateProfileDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("deleteProfileData")){										
				RafDsCommand deleteProfileDataCommand = new DeleteProfileDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteProfileDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (method.equals("fetchProfileDetailData")) {		
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFPROFILE_PROFILEID, request.getParameter(DataNameTokens.RAFPROFILE_PROFILEID));
				rafDsRequest.setParams(params);	
				RafDsCommand fetchProfileDetailDataCommand = new FetchProfileDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchProfileDetailDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("addProfileDetailData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFPROFILE_PROFILEID, request.getParameter(DataNameTokens.RAFPROFILE_PROFILEID));
				rafDsRequest.setParams(params);
				RafDsCommand addProfileDetailDataCommand = new AddProfileDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addProfileDetailDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if (rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another screen or permission.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("updateProfileDetailData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFPROFILE_PROFILEID, request.getParameter(DataNameTokens.RAFPROFILE_PROFILEID));
				rafDsRequest.setParams(params);				
				RafDsCommand updateProfileDetailDataCommand = new UpdateProfileDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateProfileDetailDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if(rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another screen or permission.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("deleteProfileDetailData")){										
				RafDsCommand deleteProfileDetailDataCommand = new DeleteProfileDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteProfileDetailDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (method.equals("fetchRoleData")) {				
				RafDsCommand fetchRoleDataCommand = new FetchRoleDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchRoleDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("addRoleData")){
				RafDsCommand addRoleDataCommand = new AddRoleDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addRoleDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("updateRoleData")){			
				RafDsCommand updateRoleDataCommand = new UpdateRoleDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateRoleDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("deleteRoleData")){										
				RafDsCommand deleteRoleDataCommand = new DeleteRoleDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteRoleDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (method.equals("fetchRoleDetailProfileData")) {		
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFROLE_ROLEID, request.getParameter(DataNameTokens.RAFROLE_ROLEID));
				rafDsRequest.setParams(params);	
				RafDsCommand fetchRoleDetailProfileDataCommand = new FetchRoleDetailProfileDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchRoleDetailProfileDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("addRoleDetailProfileData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFROLE_ROLEID, request.getParameter(DataNameTokens.RAFROLE_ROLEID));
				rafDsRequest.setParams(params);
				RafDsCommand addRoleDetailProfileDataCommand = new AddRoleDetailProfileDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addRoleDetailProfileDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if(rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another profile.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("updateRoleDetailProfileData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFROLE_ROLEID, request.getParameter(DataNameTokens.RAFROLE_ROLEID));
				rafDsRequest.setParams(params);				
				RafDsCommand updateRoleDetailProfileDataCommand = new UpdateRoleDetailProfileDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateRoleDetailProfileDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if(rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another profile.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("deleteRoleDetailProfileData")){										
				RafDsCommand deleteRoleDetailProfileDataCommand = new DeleteRoleDetailProfileDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteRoleDetailProfileDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (method.equals("fetchRoleDetailUserData")) {		
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFROLE_ROLEID, request.getParameter(DataNameTokens.RAFROLE_ROLEID));
				rafDsRequest.setParams(params);	
				RafDsCommand fetchRoleDetailUserDataCommand = new FetchRoleDetailUserDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchRoleDetailUserDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("addRoleDetailUserData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFROLE_ROLEID, request.getParameter(DataNameTokens.RAFROLE_ROLEID));
				rafDsRequest.setParams(params);
				RafDsCommand addRoleDetailUserDataCommand = new AddRoleDetailUserDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addRoleDetailUserDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if(rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another user.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("updateRoleDetailUserData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFROLE_ROLEID, request.getParameter(DataNameTokens.RAFROLE_ROLEID));
				rafDsRequest.setParams(params);				
				RafDsCommand updateRoleDetailUserDataCommand = new UpdateRoleDetailUserDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateRoleDetailUserDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if(rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another user.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("deleteRoleDetailUserData")){										
				RafDsCommand deleteRoleDetailUserDataCommand = new DeleteRoleDetailUserDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteRoleDetailUserDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (method.equals("fetchUserData")) {				
				RafDsCommand fetchUserDataCommand = new FetchUserDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchUserDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("addUserData")){
				RafDsCommand addUserDataCommand = new AddUserDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addUserDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("updateUserData")){	
				RafDsCommand updateUserDataCommand = new UpdateUserDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateUserDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("deleteUserData")){										
				RafDsCommand deleteUserDataCommand = new DeleteUserDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteUserDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (method.equals("fetchUserDetailGroupData")) {		
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFUSER_USERID, request.getParameter(DataNameTokens.RAFUSER_USERID));
				rafDsRequest.setParams(params);	
				RafDsCommand fetchUserDetailGroupDataCommand = new FetchUserDetailGroupDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchUserDetailGroupDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("addUserDetailGroupData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFUSER_USERID, request.getParameter(DataNameTokens.RAFUSER_USERID));
				rafDsRequest.setParams(params);
				RafDsCommand addUserDetailGroupDataCommand = new AddUserDetailGroupDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addUserDetailGroupDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if(rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another group.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("updateUserDetailGroupData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFUSER_USERID, request.getParameter(DataNameTokens.RAFUSER_USERID));
				rafDsRequest.setParams(params);				
				RafDsCommand updateUserDetailGroupDataCommand = new UpdateUserDetailGroupDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateUserDetailGroupDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if(rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another group.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("deleteUserDetailGroupData")){										
				RafDsCommand deleteUserDetailGroupDataCommand = new DeleteUserDetailGroupDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteUserDetailGroupDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (method.equals("fetchUserDetailRoleData")) {		
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFUSER_USERID, request.getParameter(DataNameTokens.RAFUSER_USERID));
				rafDsRequest.setParams(params);	
				RafDsCommand fetchUserDetailRoleDataCommand = new FetchUserDetailRoleDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchUserDetailRoleDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("addUserDetailRoleData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFUSER_USERID, request.getParameter(DataNameTokens.RAFUSER_USERID));
				rafDsRequest.setParams(params);
				RafDsCommand addUserDetailRoleDataCommand = new AddUserDetailRoleDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addUserDetailRoleDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if(rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another role.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("updateUserDetailRoleData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFUSER_USERID, request.getParameter(DataNameTokens.RAFUSER_USERID));
				rafDsRequest.setParams(params);				
				RafDsCommand updateUserDetailRoleDataCommand = new UpdateUserDetailRoleDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateUserDetailRoleDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if(rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another role.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("deleteUserDetailRoleData")){										
				RafDsCommand deleteUserDetailRoleDataCommand = new DeleteUserDetailRoleDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteUserDetailRoleDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (method.equals("fetchGroupData")) {				
				RafDsCommand fetchGroupDataCommand = new FetchGroupDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchGroupDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("addGroupData")){
				RafDsCommand addGroupDataCommand = new AddGroupDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addGroupDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("updateGroupData")){	
				RafDsCommand updateGroupDataCommand = new UpdateGroupDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateGroupDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("deleteGroupData")){							
				RafDsCommand deleteGroupDataCommand = new DeleteGroupDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteGroupDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (method.equals("fetchGroupDetailData")) {	
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFGROUP_GROUPID, request.getParameter(DataNameTokens.RAFGROUP_GROUPID));
				rafDsRequest.setParams(params);	
				RafDsCommand fetchGroupDetailDataCommand = new FetchGroupDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchGroupDetailDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("addGroupDetailData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFGROUP_GROUPID, request.getParameter(DataNameTokens.RAFGROUP_GROUPID));
				rafDsRequest.setParams(params);
				RafDsCommand addGroupDetailDataCommand = new AddGroupDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addGroupDetailDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if(rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another role.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("updateGroupDetailData")){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.RAFGROUP_GROUPID, request.getParameter(DataNameTokens.RAFGROUP_GROUPID));
				rafDsRequest.setParams(params);	
				RafDsCommand updateGroupDetailDataCommand = new UpdateGroupDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateGroupDetailDataCommand.execute();				
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else if(rafDsResponse.getStatus() == 2){
						String errorMessage = "Data already exist, please choose another role.";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("deleteGroupDetailData")){							
				RafDsCommand deleteGroupDetailDataCommand = new DeleteGroupDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteGroupDetailDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} 
		else if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");			
			if(method.equals("fetchGroupComboBoxData")){	
				RafRpcCommand fetchGroupComboBoxDataCommand = new FetchGroupComboBoxDataCommand();
				retVal = fetchGroupComboBoxDataCommand.execute();				
			}	else if(method.equals("fetchRoleComboBoxData")){	
				RafRpcCommand fetchRoleComboBoxDataCommand = new FetchRoleComboBoxDataCommand();
				retVal = fetchRoleComboBoxDataCommand.execute();				
			} else if(method.equals("fetchUserComboBoxData")){	
				RafRpcCommand fetchUserComboBoxDataCommand = new FetchUserComboBoxDataCommand();
				retVal = fetchUserComboBoxDataCommand.execute();				
			} else if(method.equals("fetchProfileComboBoxData")){	
				RafRpcCommand fetchProfileComboBoxDataCommand = new FetchProfileComboBoxDataCommand();
				retVal = fetchProfileComboBoxDataCommand.execute();				
			} else if(method.equals("fetchScreenComboBoxData")){	
				RafRpcCommand fetchScreenComboBoxDataCommand = new FetchScreenComboBoxDataCommand();
				retVal = fetchScreenComboBoxDataCommand.execute();				
			} else if(method.equals("fetchPermissionComboBoxData")){	
				RafRpcCommand fetchPermissionComboBoxDataCommand = new FetchPermissionComboBoxDataCommand();
				retVal = fetchPermissionComboBoxDataCommand.execute();				
			}
		}
		response.getOutputStream().println(retVal);
	}
}
