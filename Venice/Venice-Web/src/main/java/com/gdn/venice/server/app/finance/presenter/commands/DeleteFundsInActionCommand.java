package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinArFundsInReportSessionEJBRemote;
import com.gdn.venice.facade.finance.fundsin.FundsInReconciliationHelperSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReport;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

public class DeleteFundsInActionCommand implements RafRpcCommand{
	HashMap<String, String> deleteDataMap;
	private static final Pattern p1 = Pattern.compile("[\\{\\}\\=\\, ]++");
	
	public DeleteFundsInActionCommand(String parameter) {
		deleteDataMap = Util.formHashMapfromXML(parameter);
	}
	
	
	public String execute() {
		
		/*
		 * Get the parameter values out of the map
		 */
//		"VA BCA", "VA Mandiri", "KlikBCA", "Mandiri Klikpay", "CIMBClicks", "Credit Card BCA", "KlikPay Internet Banking", "KlikPay Credit Card"
		List<Long> idRecord = new ArrayList<Long>();
		String idRec ="";
		for (int i=0;i<deleteDataMap.size();i++) {
			String delDataString = deleteDataMap.get("DELETEDATA" + i);			
			if (delDataString!=null) {
				String[] split = p1.split( delDataString );	
				HashMap<String,String> refundMap = new HashMap<String,String>();
				for ( int j=1; j< split.length; j+=2 ) {
					refundMap.put( split[j], split[j+1] );
				}
				boolean trueOrFalse = true;
				for(Long id : idRecord){					
				if(new Long(refundMap.get("RECONCILIATIONRECORDID")).equals(id)){
						trueOrFalse=false;
						break;
					}
				}				
				if(trueOrFalse){
						idRecord.add(new Long(refundMap.get("RECONCILIATIONRECORDID")));
						if(!idRec.equals(""))
							idRec=idRec+","+refundMap.get("RECONCILIATIONRECORDID");
						else
							idRec=refundMap.get("RECONCILIATIONRECORDID");
				}				
			}
		}
		Locator<Object> locators = null;
		try {
			locators = new Locator<Object>();
			FinArFundsInReportSessionEJBRemote finArFundsInReportRecordHome = (FinArFundsInReportSessionEJBRemote) locators.lookup(FinArFundsInReportSessionEJBRemote.class, "FinArFundsInReportSessionEJBBean");
			List<FinArFundsInReport> finArFundsInReportRecordList = 
				finArFundsInReportRecordHome.queryByRange("select u from FinArFundsInReport u where u.paymentReportId in (select o.finArFundsInReport.paymentReportId from FinArFundsInReconRecord o where o.reconciliationRecordId in ("+idRec+"))", 0, 0);

			FundsInReconciliationHelperSessionEJBRemote sessionHome = (FundsInReconciliationHelperSessionEJBRemote) locators
					.lookup(FundsInReconciliationHelperSessionEJBRemote.class,	"FundsInReconciliationHelperSessionEJBBean");
			sessionHome.deleteFundsInReport(finArFundsInReportRecordList);
			
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			e.printStackTrace();
			return "-1" + ":" + errorMsg;
		}
		
		return "0";
	}	
}
