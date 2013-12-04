package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote;
import com.gdn.venice.persistence.LogInvoiceAirwaybillRecord;
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.gdn.venice.server.command.RafRpcCommand;

public class GetInvoiceReportUploadIdToApproveRejectCommand implements RafRpcCommand{
	private String ids, action;
	
	public GetInvoiceReportUploadIdToApproveRejectCommand(String ids, String action){
		this.ids = ids;
		this.action = action;
	}
	
	@Override
	public String execute() {
		Locator<Object> locator=null;
		HashMap<String, String> map = new HashMap<String, String>();
		String invoiceReportUploadId="";
		
		try{
			boolean isOK = true;
			locator = new Locator<Object>();
			LogInvoiceReportUploadSessionEJBRemote sessionHome = (LogInvoiceReportUploadSessionEJBRemote) locator
			.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");
			
			List<LogInvoiceReportUpload> invoiceReportUploadList = sessionHome.queryByRange("select distinct o from LogInvoiceReportUpload o left join fetch o.logInvoiceAirwaybillRecords " +
					"where o.invoiceNumber in ("+ids+")", 0, 0);
						
			for (int i=0;i<invoiceReportUploadList.size();i++) {
				if(action.equals("approve"))
					isOK = true;
				else
					isOK = false;
				for (LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord : invoiceReportUploadList.get(i).getLogInvoiceAirwaybillRecords()) {
					if(action.equals("approve") && logInvoiceAirwaybillRecord.getInvoiceResultStatus().equalsIgnoreCase(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_PROBLEMEXISTS)){
						isOK = false;
						break;						
					} else if(action.equals("reject") && logInvoiceAirwaybillRecord.getInvoiceResultStatus().equalsIgnoreCase(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_PROBLEMEXISTS)){
						isOK = true;
						break;						
					}
				}
				if(isOK)
					map.put(ProcessNameTokens.INVOICERENUMBER+(i+1), invoiceReportUploadList.get(i).getInvoiceNumber());
			}
			invoiceReportUploadId = map.toString();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return invoiceReportUploadId;
	}
}
