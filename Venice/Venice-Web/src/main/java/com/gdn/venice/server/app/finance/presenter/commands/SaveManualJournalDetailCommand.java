package com.gdn.venice.server.app.finance.presenter.commands;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote;
import com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote;
import com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote;
import com.gdn.venice.facade.finance.journal.ManualJournalEntryDTO;
import com.gdn.venice.persistence.FinJournalApprovalGroup;
import com.gdn.venice.persistence.FinJournalTransaction;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;

/**
 * Command for saving manual journal data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class SaveManualJournalDetailCommand implements RafRpcCommand{
	HashMap<String, String> manualJournalDataMap;
	
	/**
	 * Constructor that extracts the form data
	 * @param parameter
	 */
	public SaveManualJournalDetailCommand(String parameter) {
		manualJournalDataMap = Util.formHashMapfromXML(parameter);
	}
	
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	public String execute() {
		String journalGroupId = manualJournalDataMap.get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
		
		Date journalApprovalGroupTimestamp = null;
		if (manualJournalDataMap.get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP)!=null) {
			DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
			try {
				journalApprovalGroupTimestamp = formatter.parse(manualJournalDataMap.get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP));
			} catch (ParseException e) {
				String errorMsg = e.getMessage();
				e.printStackTrace();
				return "-1" + ":" + errorMsg;
			}
		}
		
		String journalApprovalGroupDesc = manualJournalDataMap.get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC);
		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			FinJournalApprovalGroupSessionEJBRemote finJournalApprovalGroupSessionHome = (FinJournalApprovalGroupSessionEJBRemote) locator
			.lookup(FinJournalApprovalGroupSessionEJBRemote.class, "FinJournalApprovalGroupSessionEJBBean");
			
			FinJournalTransactionSessionEJBRemote finJournalTransactionSessionHome = (FinJournalTransactionSessionEJBRemote) locator
			.lookup(FinJournalTransactionSessionEJBRemote.class, "FinJournalTransactionSessionEJBBean");
			
			//If editing, remove first			
			if (journalGroupId!=null) {
				System.out.println("Delete old journal first");
				System.out.println("Journal group id to delete: "+journalGroupId);
				
				List<FinJournalTransaction>jurnalTransactionList = finJournalTransactionSessionHome.queryByRange("select o from FinJournalTransaction o where o.finJournalApprovalGroup.journalGroupId="+journalGroupId, 0, 0);
				if(jurnalTransactionList.size()>0){
					System.out.println("Jurnal transaction size: "+jurnalTransactionList.size());
					for(int j=0;j<jurnalTransactionList.size();j++){
						System.out.println("Deleting jurnal transaction: "+jurnalTransactionList.get(j).getTransactionId());
						finJournalTransactionSessionHome.removeFinJournalTransaction(jurnalTransactionList.get(j));
					}
				}
				
				FinJournalApprovalGroup finJournalApprovalGroup = new FinJournalApprovalGroup();
				finJournalApprovalGroup.setJournalGroupId(new Long(journalGroupId));
				finJournalApprovalGroupSessionHome.removeFinJournalApprovalGroup(finJournalApprovalGroup);
			} 
		
			//Posts Manual Journal's Journal Transactions
			System.out.println("Posts Manual Journal's Journal Transactions");
			HashMap<String,String> manualJournalTransactionMapData = Util.formHashMapfromXML(manualJournalDataMap.get("MANUALJOURNALTRANSACTION"));
			
			ArrayList<ManualJournalEntryDTO> journalEntryList = new ArrayList<ManualJournalEntryDTO>();
			
			for (int i=0;i<manualJournalTransactionMapData.size();i++) {
				HashMap<String,String> manualJournalTransactionMap = Util.formHashMapfromXML(manualJournalTransactionMapData.get("MANUALJOURNALTRANSACTION" + i));
				
				ManualJournalEntryDTO manualJournalEntryDTO = new ManualJournalEntryDTO();

				if (manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC)!=null) {
					
					String accountDesc = manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC);
					String[] split = accountDesc.split("-");
					System.out.println("account number: "+split[0]);
					manualJournalEntryDTO.setFinAccountId(new Long(split[0]));
				}
				
				if (manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID)!=null) {
					manualJournalEntryDTO.setWcsOrderId(manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID));
				}

				if (manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID)!=null) {
					manualJournalEntryDTO.setVenPartyId(new Long(manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID)));
				}

				if (manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT)!=null && manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT)!="") {
					manualJournalEntryDTO.setAmount(new Double(manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT)));
					manualJournalEntryDTO.setCreditOrDebit(false);
				} else if (manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT)!=null && manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT)!="") {
					manualJournalEntryDTO.setAmount(new Double(manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT)));
					manualJournalEntryDTO.setCreditOrDebit(true);
				}
				
				if (manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS)!=null) {
					manualJournalEntryDTO.setComments(manualJournalTransactionMap.get(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS));
				}
				
				journalEntryList.add(manualJournalEntryDTO);
			}
						
			FinanceJournalPosterSessionEJBRemote financeJournalPosterSessionHome = (FinanceJournalPosterSessionEJBRemote) locator
			.lookup(FinanceJournalPosterSessionEJBRemote.class, "FinanceJournalPosterSessionEJBBean");
			
			financeJournalPosterSessionHome.postManualJournalTransaction(
					journalApprovalGroupDesc, 
					journalApprovalGroupTimestamp, 
					journalEntryList);
		} catch (Exception ex) {
			String errorMsg = Util.extractMessageFromEJBExceptionText(ex.getMessage());
			ex.printStackTrace();
			return "-1" + ":" + errorMsg;
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "0";
	}	
}

