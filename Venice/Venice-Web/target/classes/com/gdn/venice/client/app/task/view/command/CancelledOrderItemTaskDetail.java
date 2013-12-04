package com.gdn.venice.client.app.task.view.command;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.general.data.GeneralData;
import com.gdn.venice.client.data.RafDataSource;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.DSOperationType;

public class CancelledOrderItemTaskDetail {
	public static DataSource getJournalsDataSource(String taskId) {
		RafDataSource orderItemData = GeneralData.getOrderItemData(null);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.TASKID, taskId);
		orderItemData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return orderItemData;
	}
}
