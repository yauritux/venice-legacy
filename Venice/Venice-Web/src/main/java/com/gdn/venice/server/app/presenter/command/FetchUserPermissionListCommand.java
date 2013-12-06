package com.gdn.venice.server.app.presenter.command;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.facade.authorization.RafPermission;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.AuthorizationUtil;
import com.gdn.venice.server.util.Util;

/**
 * Command to fetch the user permissions list
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class FetchUserPermissionListCommand implements RafRpcCommand {
	String userName;

	/**
	 * Basic copy constructor
	 * @param userName
	 */
	public FetchUserPermissionListCommand(String userName) {
		super();
		this.userName = userName;
		if(this.userName == null || this.userName.isEmpty()){
			this.userName = "widy";
		}
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		HashMap<String,String>map = new HashMap<String,String>();
		
		List<RafPermission> rafPermissionList = AuthorizationUtil.getUserPermissionList(userName);
		
		for (int i=0;i<rafPermissionList.size();i++) {
			RafPermission permission = rafPermissionList.get(i);
			/*
			 * Add the permission bits into the single value
			 * because they will be treated as XOR (1,2,4 etc.)
			 */
			if (!map.containsKey(permission.getApplicationObjectCanonicalName())) {
				map.put(permission.getApplicationObjectCanonicalName(),permission.getPermissionTypeId().toString());
			}else{
				Integer existingPermission = new Integer(map.get(permission.getApplicationObjectCanonicalName()));
				existingPermission = existingPermission + permission.getPermissionTypeId().intValue();
				map.put(permission.getApplicationObjectCanonicalName(), existingPermission.toString());
			}
		}
		
		return Util.formXMLfromHashMap(map);
		
	}

}
