package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
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

public class FetchFraudCasePaymentDataCommand implements RafDsCommand {
	RafDsRequest request;	

	public FetchFraudCasePaymentDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			//Lookup into EJB for Fraud Suspicion
			locator = new Locator<Object>();
			FrdFraudSuspicionCaseSessionEJBRemote sessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
			String orderId= request.getParams().get(DataNameTokens.VENORDER_ORDERID);
			if(!request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).equals("")){	
					//Build query
					JPQLAdvancedQueryCriteria fraudCaseCriteria = request.getCriteria() != null ? request.getCriteria() : new JPQLAdvancedQueryCriteria();
					fraudCaseCriteria.setBooleanOperator("and");
					JPQLSimpleQueryCriteria caseIdCriteria = new JPQLSimpleQueryCriteria();
					caseIdCriteria.setFieldName(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
					caseIdCriteria.setOperator("equals");
					caseIdCriteria.setValue(request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
					caseIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
					fraudCaseCriteria.add(caseIdCriteria);
		
					//Grab order id (ven order id)
					FrdFraudSuspicionCase fraudCaseSuspicion = new FrdFraudSuspicionCase();
					List<FrdFraudSuspicionCase> fraudCase = sessionHome.findByFrdFraudSuspicionCaseLike(fraudCaseSuspicion, fraudCaseCriteria, 0, 1);
					fraudCaseSuspicion = fraudCase.get(0);
					orderId = fraudCaseSuspicion.getVenOrder().getOrderId().toString();
			}
			//Lookup into EJB for order payment allocation
			VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			VenBinCreditLimitEstimateSessionEJBRemote binSessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");
			
			//Build query
			JPQLAdvancedQueryCriteria paymentCriteria = request.getCriteria() != null ? request.getCriteria() : new JPQLAdvancedQueryCriteria();
			paymentCriteria.setBooleanOperator("and");
			JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
			simpleCriteria.setFieldName(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERID);
			simpleCriteria.setOperator("equals");
			simpleCriteria.setValue(orderId);
			simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERID));
			paymentCriteria.add(simpleCriteria);
			
			//Get list of order payment allocation
			VenOrderPaymentAllocation orderPaymentAllocation = new VenOrderPaymentAllocation();
			List<VenOrderPaymentAllocation> venOrderPaymentAllocation = orderPaymentAllocationSessionHome.findByVenOrderPaymentAllocationLike(orderPaymentAllocation, paymentCriteria, request.getStartRow(), request.getEndRow());

			//Set result
			for (int i = 0; i < venOrderPaymentAllocation.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				VenOrderPayment orderPayment = venOrderPaymentAllocation.get(i).getVenOrderPayment();
				
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID, Util.isNull(orderPayment.getOrderPaymentId(), "").toString());
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID, Util.isNull(orderPayment.getWcsPaymentId(), "").toString());
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC, Util.isNull(orderPayment.getVenWcsPaymentType().getWcsPaymentTypeDesc(), "").toString());
				
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
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSCODE, orderPayment.getVenPaymentStatus()!=null && orderPayment.getVenPaymentStatus().getPaymentStatusCode()!=null?orderPayment.getVenPaymentStatus().getPaymentStatusCode().toString():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, orderPayment.getVenBank()!=null && orderPayment.getVenBank().getBankShortName()!=null?orderPayment.getVenBank().getBankShortName().toString():"");
				
				String creditCardNumber = "";
				String binNumber="", bankName="",type="",limit="";
				creditCardNumber = orderPayment.getMaskedCreditCardNumber()!=null?orderPayment.getMaskedCreditCardNumber():"";
				if(!creditCardNumber.equals("")){		
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
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT, Util.isNull(venOrderPaymentAllocation.get(i).getAllocationAmount(), "").toString());
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_HANDLINGFEE, Util.isNull(orderPayment.getHandlingFee(), "").toString());
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENADDRESS,		
				(orderPayment.getVenAddress()!=null && orderPayment.getVenAddress().getStreetAddress1()!=null?orderPayment.getVenAddress().getStreetAddress1():"") +
				(" "+ orderPayment.getVenAddress()!=null && orderPayment.getVenAddress().getStreetAddress2()!=null?orderPayment.getVenAddress().getStreetAddress2():"") + "<br />" +
				(orderPayment.getVenAddress()!=null && orderPayment.getVenAddress().getKelurahan()!=null?orderPayment.getVenAddress().getKelurahan():"") + ", " +
				(orderPayment.getVenAddress()!=null && orderPayment.getVenAddress().getKecamatan()!=null?orderPayment.getVenAddress().getKecamatan().toString():"") + "<br />" +
				(orderPayment.getVenAddress()!=null && orderPayment.getVenAddress().getVenCity()!=null ?orderPayment.getVenAddress().getVenCity().getCityName():"") + " " +
				(orderPayment.getVenAddress()!=null && orderPayment.getVenAddress().getPostalCode()!=null?orderPayment.getVenAddress().getPostalCode():"") + "<br />" +
				(orderPayment.getVenAddress()!=null && orderPayment.getVenAddress().getVenState()!=null ?orderPayment.getVenAddress().getVenState().getStateName():"") + ", " +
				(orderPayment.getVenAddress()!=null && orderPayment.getVenAddress().getVenCountry()!=null && orderPayment.getVenAddress().getVenCountry().getCountryName()!=null?orderPayment.getVenAddress().getVenCountry().getCountryName().toString():""));
						
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
		
		//Put list result and return
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
