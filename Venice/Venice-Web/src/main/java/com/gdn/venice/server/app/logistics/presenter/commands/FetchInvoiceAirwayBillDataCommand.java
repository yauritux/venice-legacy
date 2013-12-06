package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogMerchantPickupInstruction;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchInvoiceAirwayBillDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public FetchInvoiceAirwayBillDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		String invoiceAirwaybillRecordId = "";
		if(request.getParams() != null)
			invoiceAirwaybillRecordId = request.getParams().get(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID);			
		
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			LogAirwayBillSessionEJBRemote sessionHome = (LogAirwayBillSessionEJBRemote) locator
			.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			LogMerchantPickupInstructionSessionEJBRemote pickupInstructionHome = (LogMerchantPickupInstructionSessionEJBRemote) locator
			.lookup(LogMerchantPickupInstructionSessionEJBRemote.class, "LogMerchantPickupInstructionSessionEJBBean");
			
			List<LogAirwayBill> airwayBillList = null;		
			String select;
			if (criteria == null) {
				 select = "select awb from LogAirwayBill awb where awb.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId = " + invoiceAirwaybillRecordId;				
				airwayBillList = sessionHome.queryByRange(select, 0, 0);				
			} else {
				LogAirwayBill airwayBill = new LogAirwayBill();
				airwayBillList = sessionHome.findByLogAirwayBillLike(airwayBill, criteria, 0, 0);
			}
			
			//Remove airway bill(s) with activity report result status is "Invalid GDN Ref", this is to avoid cluttering the invoice
			//screen from invalid airway bills from activity report
			for (int i=0;i<airwayBillList.size();i++) {
				LogAirwayBill airwayBill = airwayBillList.get(i);
				if (airwayBill.getActivityResultStatus()!=null && airwayBill.getActivityResultStatus().toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_INVALIDGDNREF.toUpperCase())) {
					airwayBillList.remove(i);
				}
			}
				
			String gdnRefNo = "";
			for (int i=0;i<airwayBillList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();				
				LogAirwayBill airwayBill = airwayBillList.get(i);				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();				
				
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, airwayBill.getAirwayBillId()!=null?airwayBill.getAirwayBillId().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID, airwayBill.getVenOrderItem().getVenOrder().getWcsOrderId());
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID, airwayBill.getVenOrderItem().getWcsOrderItemId());
				gdnRefNo = airwayBill.getVenOrderItem().getVenOrder().getRmaFlag()?"R":"O" + "-" +
						airwayBill.getVenOrderItem().getVenOrder().getWcsOrderId() + "-" +
						airwayBill.getVenOrderItem().getWcsOrderItemId() + "-" +
						airwayBill.getSequence();
				map.put(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE, gdnRefNo);
				map.put(DataNameTokens.LOGAIRWAYBILL_WEIGHT, airwayBill.getVenOrderItem().getShippingWeight()+"");
				map.put(DataNameTokens.LOGAIRWAYBILL_OTHERCHARGE_PACKAGINGKAYU, (airwayBill.getVenOrderItem().getShippingCost()).subtract(airwayBill.getVenOrderItem().getGiftWrapPrice()).subtract((airwayBill.getVenOrderItem().getShippingWeight()).multiply(airwayBill.getVenOrderItem().getLogisticsPricePerKg()))+"");
				map.put(DataNameTokens.LOGAIRWAYBILL_GIFTWRAPCHARGE, airwayBill.getVenOrderItem().getGiftWrapPrice()+"");
				map.put(DataNameTokens.LOGAIRWAYBILL_INSUREDAMOUNT, airwayBill.getInsuredAmount()!=null?airwayBill.getInsuredAmount().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_INSURANCECHARGE, airwayBill.getInsuranceCharge()!=null?airwayBill.getInsuranceCharge().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_TOTALCHARGE, airwayBill.getTotalCharge()!=null?airwayBill.getTotalCharge().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_INVOICERESULTSTATUS, airwayBill.getLogInvoiceAirwaybillRecord().getInvoiceResultStatus());
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP, formatter.format(airwayBill.getAirwayBillTimestamp()));
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrderStatus()!=null?airwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusId().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrderStatus()!=null?airwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusCode():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, 
						(airwayBill.getVenOrderItem()!=null &&
								airwayBill.getVenOrderItem().getVenMerchantProduct()!=null &&
								airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!= null &&
								airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!= null)
								?airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():"");
				
				List<LogMerchantPickupInstruction> merchantInstructionList=pickupInstructionHome.queryByRange("select o from LogMerchantPickupInstruction o where o.venOrderItem.orderItemId =" + airwayBill.getVenOrderItem().getOrderItemId(), 0, 0);
				
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS_VENCITY_CITYNAME,(merchantInstructionList!=null&&merchantInstructionList.size() > 0)?(merchantInstructionList.get(0).getVenAddress().getVenCity()!=null?merchantInstructionList.get(0).getVenAddress().getVenCity().getCityName():""):"");
				map.put(DataNameTokens.LOGAIRWAYBILL_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, airwayBill.getLogLogisticsProvider()!=null?airwayBill.getLogLogisticsProvider().getLogisticsProviderCode():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME, formatter.format(airwayBill.getAirwayBillPickupDateTime()));
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER, airwayBill.getAirwayBillNumber());
				map.put(DataNameTokens.LOGAIRWAYBILL_DESTINATION, airwayBill.getDestination()!=null?airwayBill.getDestination():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_ZIP, airwayBill.getZip()!=null?airwayBill.getZip():"");
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(airwayBillList.size());
			rafDsResponse.setEndRow(request.getStartRow()+airwayBillList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
}
