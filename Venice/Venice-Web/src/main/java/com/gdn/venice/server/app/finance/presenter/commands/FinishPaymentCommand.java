package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinSalesRecordSessionEJBRemote;
import com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.server.command.RafRpcCommand;

public class FinishPaymentCommand implements RafRpcCommand{
	String apPaymentIds;
	
	/**
	 * Constructor to extract the form parameters
	 * @param parameter
	 */
	public FinishPaymentCommand(String parameter) {
		apPaymentIds = parameter;
	}
	
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	public String execute() {
		Locator<Object> locator = null;
		try{
			locator = new Locator<Object>();
			FinanceJournalPosterSessionEJBRemote financeJournalPoster = (FinanceJournalPosterSessionEJBRemote) locator
			.lookup(FinanceJournalPosterSessionEJBRemote.class, "FinanceJournalPosterSessionEJBBean");
			
			FinSalesRecordSessionEJBRemote salesRecordHome = (FinSalesRecordSessionEJBRemote) locator
			.lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");
	
			List<FinSalesRecord> salesRecordList;
			
			String apPaymentId[] = apPaymentIds.split(",");
			for (String apId : apPaymentId) {
				financeJournalPoster.postPaymentJournalTransaction(new Long(apId));
				salesRecordList = salesRecordHome.queryByRange("select o from FinSalesRecord o where o.finApPayment.apPaymentId="+apId, 0, 0);
				for (FinSalesRecord salesRecord : salesRecordList) {
					salesRecord.setPaymentStatus("Paid");
					salesRecordHome.mergeFinSalesRecord(salesRecord);
				}
			}		

			return "0";
		} catch(Exception ex) {
			return "-1";
		}
	}	
}
