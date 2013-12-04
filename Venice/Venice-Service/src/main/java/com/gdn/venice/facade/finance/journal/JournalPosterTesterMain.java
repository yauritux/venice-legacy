package com.gdn.venice.facade.finance.journal;

import java.util.ArrayList;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinJournalTransaction;

public class JournalPosterTesterMain {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		try {
			Locator<Object> locator = new Locator<Object>();
			FinanceJournalPosterSessionEJBRemote financeJournalPosterHome = (FinanceJournalPosterSessionEJBRemote) locator
			.lookup(FinanceJournalPosterSessionEJBRemote.class, "FinanceJournalPosterSessionEJBBean");
			
			FinArFundsInReconRecordSessionEJBRemote reconRecordHome = (FinArFundsInReconRecordSessionEJBRemote) locator
			.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");

			FinJournalTransactionSessionEJBRemote journalTransactionHome = (FinJournalTransactionSessionEJBRemote) locator
			.lookup(FinJournalTransactionSessionEJBRemote.class, "FinJournalTransactionSessionEJBBean");

			
			ArrayList<Long> finArFundsInReconRecordIdList = new ArrayList<Long>();
			finArFundsInReconRecordIdList.add(new Long(5607));
			finArFundsInReconRecordIdList.add(new Long(5608));
			finArFundsInReconRecordIdList.add(new Long(5609));
			finArFundsInReconRecordIdList.add(new Long(5610));

			//financeJournalPosterHome.postCashReceiveJournalTransactions(finArFundsInReconRecordIdList);
			
			List<FinArFundsInReconRecord> recordRecordList = reconRecordHome.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId in( 5607, 5608, 5609, 5610)", 0, 0);
			for(FinArFundsInReconRecord record:recordRecordList){
				System.out.println("Record:" + record.getReconciliationRecordId());
				for(FinJournalTransaction transaction:record.getFinJournalTransactions()){
					System.out.println("Transaction ID:" + transaction.getTransactionId());
					System.out.println("Transaction Comments:" + transaction.getComments());
				}
				System.out.println("--------------------------------------------");
			}
			
			List<FinJournalTransaction> journalTransactionList = journalTransactionHome.queryByRange("select o from FinJournalTransaction o", 0, 0);
			
			for(FinJournalTransaction transaction:journalTransactionList){
				System.out.println("Transaction:" + transaction.getTransactionId());
				for(FinArFundsInReconRecord record:transaction.getFinArFundsInReconRecords()){
					System.out.println("Record ID:" + record.getReconciliationRecordId());
				}
				System.out.println("--------------------------------------------");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
