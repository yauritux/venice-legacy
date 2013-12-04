package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.EJBException;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote;
import com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote;
import com.gdn.venice.persistence.FinJournalTransaction;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.util.VeniceConstants;

public class CreateJournalForVAProcessCommand implements RafRpcCommand {

	HashMap<String, String> fundInReconRecordIds;
	String parameter;
	

	public CreateJournalForVAProcessCommand(String parameter) {
		Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
		String[] split = p.split(parameter);
		fundInReconRecordIds = new HashMap<String, String>();
		for (int i = 1; i < split.length; i += 2) {
			fundInReconRecordIds.put(split[i], split[i + 1]);		
		}		
		String param = fundInReconRecordIds.toString();
		this.parameter = param;		
	}
	@Override
	public String execute() {
		ArrayList<Long> approvedFundInReconRecordList = new ArrayList<Long>();
		Locator<Object> object = null;
		String errorMsg="";
		try {
			object = new Locator<Object>();

			FinJournalTransactionSessionEJBRemote sessionHome = (FinJournalTransactionSessionEJBRemote) object
					.lookup(FinJournalTransactionSessionEJBRemote.class, "FinJournalTransactionSessionEJBBean");
			FinanceJournalPosterSessionEJBRemote JournalSessionHome = (FinanceJournalPosterSessionEJBRemote) object
			.lookup(FinanceJournalPosterSessionEJBRemote.class,  "FinanceJournalPosterSessionEJBBean");

			for (int i = 0; i < fundInReconRecordIds.size(); i++) {
				String fundInReconRecordId = fundInReconRecordIds.get(ProcessNameTokens.FUNDINRECONRECORDID + (i + 1));

				List<FinJournalTransaction> finJournalTransaction=null;		
				finJournalTransaction = sessionHome.queryByRange("select o from FinJournalTransaction o where o.transactionId in "+
						"(select u.finJournalTransactions.transactionId from FinArFundsInJournalTransaction u where u.finArFundsInReconRecords.reconciliationRecordId ="+fundInReconRecordId+")"+
						" and o.finJournalApprovalGroup.finApprovalStatus.approvalStatusId="+VeniceConstants.FIN_APPROVAL_STATUS_APPROVED, 0, 0);
				if(!finJournalTransaction.isEmpty()){					
					throw new EJBException("Error! Journal Sudah Pernah Terbentuk!");
				}
				approvedFundInReconRecordList.add(new Long(fundInReconRecordId));		
			}		
			
			if(approvedFundInReconRecordList.size()>0)
				JournalSessionHome.postVirtualAccountH1Journal(approvedFundInReconRecordList);
			
		} catch (Exception ex) {
			errorMsg =errorMsg+"  "+ ex.getMessage();
			ex.printStackTrace();
			try {
				object.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "-1" + ":" + errorMsg;
		} finally {
			try {
				object.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "0";
	}
}
