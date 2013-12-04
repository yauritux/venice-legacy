package com.gdn.venice.facade.callback;

import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.util.VeniceConstants;

/**
 * A main application for testing the approval of airway bills
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class LogAirwayBillApproverMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			LogAirwayBillSessionEJBRemote awbHome = (LogAirwayBillSessionEJBRemote) locator
					.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");

			Long airwayBillId = new Long(5960);
			List<LogAirwayBill> airwayBillList = awbHome.queryByRange("select o from LogAirwayBill o where o.airwayBillId = " + airwayBillId, 0, 0);
			if (!airwayBillList.isEmpty()) {
				LogAirwayBill logAirwayBill = airwayBillList.get(0);

				LogApprovalStatus logApprovalStatus = new LogApprovalStatus();
				logApprovalStatus.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED);
				logAirwayBill.setLogApprovalStatus2(logApprovalStatus);

				awbHome.mergeLogAirwayBill(logAirwayBill);
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
