package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote;
import com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote;
import com.gdn.venice.persistence.FinJournalApprovalGroup;
import com.gdn.venice.persistence.FinJournalTransaction;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;
/**
 * Command for deletion of manual journal approval groups
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class DeleteManualJournalApprovalGroupCommand implements RafRpcCommand{
	HashMap<String, String> manualJournalMap;
	
	/**
	 * COmmand construtor to extract the form parameters
	 * @param parameter
	 */
	public DeleteManualJournalApprovalGroupCommand(String parameter) {
		manualJournalMap = Util.formHashMapfromXML(parameter);
	}
	
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	public String execute() {
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			FinJournalApprovalGroupSessionEJBRemote finJournalApprovalGroupSessionHome = (FinJournalApprovalGroupSessionEJBRemote) locator
			.lookup(FinJournalApprovalGroupSessionEJBRemote.class, "FinJournalApprovalGroupSessionEJBBean");
			
			FinJournalTransactionSessionEJBRemote finJournalTransactionSessionHome = (FinJournalTransactionSessionEJBRemote) locator
			.lookup(FinJournalTransactionSessionEJBRemote.class, "FinJournalTransactionSessionEJBBean");
			
			
			for (int i=0;i<manualJournalMap.size();i++) {
				String journalGroupId=manualJournalMap.get("DELETEDJOURNALAPPROVALGROUPID"+i);
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

