package com.gdn.venice.server.app.general.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenPaymentStatus;
import com.gdn.venice.server.app.finance.presenter.commands.FundInReconciliationActionCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

public class ApproveCSPaymentDataCommand implements RafRpcCommand {

	String paymentId;
	
	public ApproveCSPaymentDataCommand(String paymentId) {
		this.paymentId = paymentId;
	}

	@Override
	public String execute() {
		VenOrderPayment orderPayment;
		
		VenPaymentStatus venPaymentStatus = new VenPaymentStatus();
		venPaymentStatus.setPaymentStatusId(DataConstantNameTokens.VENORDERPAYMENT_PAYMENTSTATUSID_APPROVED);
		
		Locator<Object> locator = null;
		
		try {
			System.out.print(paymentId);
			locator = new Locator<Object>();
			
			VenOrderPaymentSessionEJBRemote sessionHome = (VenOrderPaymentSessionEJBRemote) locator
				.lookup(VenOrderPaymentSessionEJBRemote.class, "VenOrderPaymentSessionEJBBean");
			
			VenOrderSessionEJBRemote orderHome = (VenOrderSessionEJBRemote) locator
				.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			FinArFundsInReconRecordSessionEJBRemote fundInHome = (FinArFundsInReconRecordSessionEJBRemote) locator
				.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			
			// approve payment
			orderPayment = sessionHome.queryByRange("select o from VenOrderPayment o where o.orderPaymentId = " + paymentId, 0, 1).get(0);
			orderPayment.setVenPaymentStatus(venPaymentStatus);
			sessionHome.mergeVenOrderPayment(orderPayment);
			// get current order
			List<VenOrder> orderList = orderHome.queryByRange("select o from VenOrder o left join fetch o.venOrderPaymentAllocations opa left join fetch opa.venOrderPayment op left join fetch op.venWcsPaymentType left join fetch op.oldVenOrder oo where op.orderPaymentId = " + paymentId, 0, 0);
			VenOrder order = orderList.get(0);
			// get old order
			List<VenOrder> oldOrderList = orderHome.queryByRange("select o from VenOrder o left join fetch o.venOrderPaymentAllocations opa left join fetch opa.venOrderPayment op left join fetch op.venWcsPaymentType where o.wcsOrderId = '" + order.getVenOrderPaymentAllocations().get(0).getVenOrderPayment().getOldVenOrder().getWcsOrderId() + "'", 0, 0);
			VenOrder oldOrder = oldOrderList.get(0);
			// get fund in from old order
			List<FinArFundsInReconRecord> fundInList = fundInHome.queryByRange("select o from FinArFundsInReconRecord o inner join fetch o.venOrderPayment op where  o.finArReconResult.reconResultId <> 4 and op.orderPaymentId = '" + oldOrder.getVenOrderPaymentAllocations().get(0).getVenOrderPayment().getOrderPaymentId() + "'", 0, 0);
			// when payment from old order has been paid, it will be allocated to the new order
			if(fundInList.size() > 0){
				FinArFundsInReconRecord fundsIn = fundInList.get(0);
				
				HashMap<String, String> hashParam = new HashMap<String, String>();
				hashParam.put("sourceVenWCSOrderId", oldOrder.getWcsOrderId());
				hashParam.put("sourceVenOrderPaymentId", oldOrder.getVenOrderPaymentAllocations().get(0).getVenOrderPayment().getOrderPaymentId().toString());
				hashParam.put("destinationVenOrderPaymentId", paymentId);
				hashParam.put("allocationAmount", fundsIn.getProviderReportPaidAmount().toString());
				hashParam.put("destinationVenOrderId", order.getOrderId().toString());
				// allocate fund in from old order to the new order
				RafRpcCommand fundInRecon = new FundInReconciliationActionCommand(Util.formXMLfromHashMap(hashParam), null, "allocateFundIn", null);
				fundInRecon.execute();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "-1";
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
