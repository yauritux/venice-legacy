package com.gdn.venice.facade.callback;

import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinApPaymentSessionEJBRemote;
import com.gdn.venice.persistence.FinApPayment;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.util.VeniceConstants;

public class FinApPaymentApprover {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FinApPaymentSessionEJBRemote finApPaymentHome = (FinApPaymentSessionEJBRemote) locator.lookup(FinApPaymentSessionEJBRemote.class, "FinApPaymentSessionEJBBean");

			Long apPaymentId = new Long(43902);
			List<FinApPayment> paymentList = finApPaymentHome.queryByRange("select o from FinApPayment o where o.apPaymentId = " + apPaymentId, 0, 0);
			if (!paymentList.isEmpty()) {
				FinApPayment payment = paymentList.get(0);

				FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
				finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_APPROVED);
				payment.setFinApprovalStatus(finApprovalStatus);

				finApPaymentHome.mergeFinApPayment(payment);
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
