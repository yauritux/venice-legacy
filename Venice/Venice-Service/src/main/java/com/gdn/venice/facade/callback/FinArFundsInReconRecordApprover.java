package com.gdn.venice.facade.callback;

import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.util.VeniceConstants;

public class FinArFundsInReconRecordApprover {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FinArFundsInReconRecordSessionEJBRemote fundsInReconRecordHome = (FinArFundsInReconRecordSessionEJBRemote) locator
					.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");

			Long fundsInReconRecordId = new Long(5607);
			List<FinArFundsInReconRecord> reconRecordList = fundsInReconRecordHome.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId = " + fundsInReconRecordId +" and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED, 0, 0);
			if (!reconRecordList.isEmpty()) {
				FinArFundsInReconRecord fundsInReconRecord = reconRecordList.get(0);

				FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
				finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_APPROVED);
				fundsInReconRecord.setFinApprovalStatus(finApprovalStatus);

				fundsInReconRecordHome.mergeFinArFundsInReconRecord(fundsInReconRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
