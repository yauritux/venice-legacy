package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchFraudCasePaymentTypeData implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCasePaymentTypeData(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			//Catch parameter from client
			String fraudCaseId = request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
			
			//Find ven order id first
			//Lookup into EJB for fraud case
			locator = new Locator<Object>();
			FrdFraudSuspicionCaseSessionEJBRemote fraudCaseSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
			List<FrdFraudSuspicionCase> fraudCaseList = null;
			String query = "select o from FrdFraudSuspicionCase o " +
					       "where o.fraudSuspicionCaseId = " + fraudCaseId;
			
			//Calling facade for fraud case
			fraudCaseList = fraudCaseSessionHome.queryByRange(query, 0, 0);
			
			//Find ven order id
			String orderId = "";
			for (int i = 0; i < fraudCaseList.size(); i++) {
				orderId = fraudCaseList.get(i).getVenOrder().getOrderId() == null ? "" : fraudCaseList.get(i).getVenOrder().getOrderId().toString();
			}
			
			//Lookup into EJB for order payment
			VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			List<VenOrderPaymentAllocation> orderPaymentAllocationList = null;
			query = "select o from VenOrderPaymentAllocation o " +
					"where o.venOrder.orderId = " + orderId;
			
			//Calling facade for order payment
			orderPaymentAllocationList = orderPaymentAllocationSessionHome.queryByRange(query, 0, 0);
			
			VenBinCreditLimitEstimateSessionEJBRemote binSessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");
			//Set result
			for (int i = 0; i < orderPaymentAllocationList.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				VenOrderPayment orderPayment = orderPaymentAllocationList.get(i).getVenOrderPayment();
				
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID,  Util.isNull(orderPayment.getWcsPaymentId(), "").toString());
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC,  Util.isNull(orderPayment.getVenWcsPaymentType().getWcsPaymentTypeDesc(), "").toString());			
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME,  orderPayment.getVenBank()!=null && orderPayment.getVenBank().getBankShortName()!=null? orderPayment.getVenBank().getBankShortName().toString():"");
				
				String creditCardNumber = null;
				String binNumber="", bankName="",type="",limit="";
				creditCardNumber = orderPayment.getMaskedCreditCardNumber();
				if(!creditCardNumber.isEmpty() || !creditCardNumber.equals(null)){		
					//check CC limit from BIN number
					if(creditCardNumber.length()>6){
						binNumber = creditCardNumber.substring(0, 6);
					}			
				}
				List<VenBinCreditLimitEstimate> binCreditLimitList = binSessionHome.queryByRange("select o from VenBinCreditLimitEstimate o where o.isActive=true and o.binNumber like '"+binNumber+"'", 0, 1);
				if(binCreditLimitList.size()>0){
					bankName=binCreditLimitList.get(0).getBankName();
					type=binCreditLimitList.get(0).getVenCardType().getCardTypeDesc()+" - "+binCreditLimitList.get(0).getDescription();
					limit=binCreditLimitList.get(0).getCreditLimitEstimate().toString();
				}
				
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_ISSUERBANK, bankName);
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_DESCRIPTION, type);
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_LIMIT, limit);
				
				//Build other info string
				String otherInfo = "";
				if (orderPayment.getVenPaymentType().getPaymentTypeId() == 0) {
					otherInfo += ("<div style=\"float:left;width:60px\">CC Number</div>:&nbsp;" + Util.isNull(orderPayment.getMaskedCreditCardNumber(), "").toString());
					otherInfo += ("<br /><div style=\"float:left;width:60px\">Auth. Code</div>:&nbsp;" + Util.isNull(orderPayment.getPaymentConfirmationNumber(), "").toString());
					otherInfo += (orderPayment.getTenor() == null ? "" : ("<br /><div style=\"float:left;width:60px\">Tenor</div>:&nbsp;" + orderPayment.getTenor()+ "&nbsp;bulan"));
					otherInfo += (orderPayment.getInterest() == null ? "" : ("<br /><div style=\"float:left;width:60px\">Interest</div>:&nbsp;Rp.&nbsp;" + orderPayment.getInterest().toString()));
					otherInfo += (orderPayment.getInstallment() == null ? "" : ("<br /><div style=\"float:left;width:60px\">Installment</div>:&nbsp;Rp.&nbsp;" + orderPayment.getInstallment().toString()));
					otherInfo += (orderPayment.getInterestInstallment() == null ? "" : ("<br /><div style=\"float:left;width:60px\">Int. + Instl.</div>:&nbsp;Rp.&nbsp;" + orderPayment.getInterestInstallment().toString()));
					otherInfo += (orderPayment.getThreeDsSecurityLevelAuth() == null ? "" : ("<br /><div style=\"float:left;width:60px\">ECI</div>:&nbsp;" + orderPayment.getThreeDsSecurityLevelAuth().toString()));
				}
				else if (orderPayment.getVenPaymentType().getPaymentTypeId() == 1)
					otherInfo += ("<div style=\"float:left;width:60px\">iBanking ID</div>:&nbsp;" + Util.isNull(orderPayment.getInternetBankingId(), "").toString());
				else if (orderPayment.getVenPaymentType().getPaymentTypeId() == 2)
					otherInfo += ("<div style=\"float:left;width:60px\">VA Number</div>:&nbsp;" + Util.isNull(orderPayment.getVirtualAccountNumber(), "").toString());
				
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OTHERINFO, otherInfo);
				dataList.add(map);
			}
			
			//Set DSResponse's properties
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setEndRow(request.getStartRow() + dataList.size());
			rafDsResponse.setTotalRows(dataList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if (locator != null) {
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Set data and return
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}