package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.HashMap;
import java.util.regex.Pattern;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.util.VeniceConstants;

public class RefundSalesRecordActionCommand implements RafRpcCommand {
	HashMap<String, String> refundDataMap;
	
	public RefundSalesRecordActionCommand(String parameter){
		refundDataMap = Util.formHashMapfromXML(parameter);
	}
	
	@Override
	public String execute() {
		
		Locator<FinanceJournalPosterSessionEJBRemote> financeJournalPosterLocator = null;
		FinanceJournalPosterSessionEJBRemote sessionHome = null;
		
		try {
			financeJournalPosterLocator = new Locator<FinanceJournalPosterSessionEJBRemote>();
			sessionHome = (FinanceJournalPosterSessionEJBRemote) financeJournalPosterLocator
					.lookup(FinanceJournalPosterSessionEJBRemote.class,
							"FinanceJournalPosterSessionEJBBean");
		}catch (Exception e) {
			String errorMsg = Util.extractMessageFromEJBExceptionText(e.getMessage());
			e.printStackTrace();
			try {
				financeJournalPosterLocator.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return "-1" + ":" + errorMsg;
		}
		
		for (int i=0;i<refundDataMap.size();i++) {
			String refundDataString = refundDataMap.get("REFUNDDATA" + i);
			
			if (refundDataString!=null) {
				
				Pattern p1 = Pattern.compile("[\\{\\}\\=\\, ]++");
				String[] split = p1.split( refundDataString );
	
				HashMap<String,String> refundMap = new HashMap<String,String>();
				for ( int j=1; j< split.length; j+=2 ) {
					refundMap.put( split[j], split[j+1] );
				}
				
				Long salesRecordId = new Long(refundMap.get("SALESRECORDID"));
				Long wcsOrderItemId = new Long(refundMap.get("WCSORDERITEMID"));
				
				sessionHome.postRefundJournalTransaction(salesRecordId, VeniceConstants.VEN_GDN_PPN_RATE > 0);
				
			}
		}
		
		return "0";
	}

}
