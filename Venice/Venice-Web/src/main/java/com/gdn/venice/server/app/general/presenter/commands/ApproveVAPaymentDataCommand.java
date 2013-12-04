package com.gdn.venice.server.app.general.presenter.commands;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.persistence.VenPaymentStatus;
import com.gdn.venice.server.command.RafRpcCommand;
import com.djarum.raf.utilities.Locator;

public class ApproveVAPaymentDataCommand implements RafRpcCommand {
	
	String paymentId;
	
	public ApproveVAPaymentDataCommand(String paymentId) {
		this.paymentId = paymentId;
	}

	@Override
	public String execute() {
		VenOrderPayment orderPayment;
		
		VenPaymentStatus venPaymentStatus = new VenPaymentStatus();
		venPaymentStatus.setPaymentStatusId(DataConstantNameTokens.VENORDERPAYMENT_PAYMENTSTATUSID_APPROVED);
		
		Locator<VenOrderPayment> orderPaymentLocator = null;
		
		try {
			orderPaymentLocator = new Locator<VenOrderPayment>();
			
			VenOrderPaymentSessionEJBRemote sessionHome = (VenOrderPaymentSessionEJBRemote) orderPaymentLocator
			.lookup(VenOrderPaymentSessionEJBRemote.class, "VenOrderPaymentSessionEJBBean");
			System.out.print(paymentId);
			orderPayment = sessionHome.queryByRange("select o from VenOrderPayment o where o.orderPaymentId = " + paymentId, 0, 1).get(0);
			orderPayment.setVenPaymentStatus(venPaymentStatus);
			
			sessionHome.mergeVenOrderPayment(orderPayment);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				orderPaymentLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "-1";
		} finally {
			try {
				orderPaymentLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
		return "0";
	}

}