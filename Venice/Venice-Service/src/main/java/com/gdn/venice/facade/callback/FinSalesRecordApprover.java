package com.gdn.venice.facade.callback;

import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinSalesRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.util.VeniceConstants;

public class FinSalesRecordApprover {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FinSalesRecordSessionEJBRemote salesRecordHome = (FinSalesRecordSessionEJBRemote) locator.lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");

			Long salesRecordId = new Long(9101);
			List<FinSalesRecord> salesRecordList = salesRecordHome.queryByRange("select o from FinSalesRecord o where o.salesRecordId = " + salesRecordId, 0, 0);
			if (!salesRecordList.isEmpty()) {
				FinSalesRecord salesRecord = salesRecordList.get(0);

				FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
				finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_APPROVED);
				salesRecord.setFinApprovalStatus(finApprovalStatus);

				salesRecordHome.mergeFinSalesRecord(salesRecord);
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
