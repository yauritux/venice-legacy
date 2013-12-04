package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote;
import com.gdn.venice.persistence.LogInvoiceAirwaybillRecord;
import com.gdn.venice.persistence.LogInvoiceReconRecord;
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.gdn.venice.server.command.RafRpcCommand;

public class GetInvoiceReportUploadIdToSubmitCommand implements RafRpcCommand{
	private String ids;
	
	public GetInvoiceReportUploadIdToSubmitCommand(String ids){
		this.ids = ids;
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
			LogInvoiceReconRecordSessionEJBRemote reconRecordSessionHome = (LogInvoiceReconRecordSessionEJBRemote) locator
			.lookup(LogInvoiceReconRecordSessionEJBRemote.class, "LogInvoiceReconRecordSessionEJBBean");
			
			List<LogInvoiceReportUpload> invoiceReportUploadList = sessionHome.queryByRange("select distinct o from LogInvoiceReportUpload o left join fetch o.logInvoiceAirwaybillRecords " +
					"where o.invoiceNumber in ("+ids+")", 0, 0);
						
			for (int i=0;i<invoiceReportUploadList.size();i++) {
				isOK = true;
				for (LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord : invoiceReportUploadList.get(i).getLogInvoiceAirwaybillRecords()) {
					if(!logInvoiceAirwaybillRecord.getInvoiceResultStatus().equalsIgnoreCase(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_OK)){
						List<LogInvoiceReconRecord> reconRecordList = reconRecordSessionHome.queryByRange("select o from LogInvoiceReconRecord o " +
								"where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId = "+ logInvoiceAirwaybillRecord.getInvoiceAirwaybillRecordId(), 0, 0);
						for (LogInvoiceReconRecord logInvoiceReconRecord : reconRecordList) {
							if(logInvoiceReconRecord.getLogActionApplied() == null || logInvoiceReconRecord.getLogActionApplied().getActionAppliedId() == null || logInvoiceReconRecord.getLogActionApplied().getActionAppliedDesc().isEmpty()){
								isOK = false;
								break;
							}
						}
					}
					if(!isOK)
						break;
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
