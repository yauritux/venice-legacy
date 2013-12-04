package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogMerchantPickupInstruction;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.djarum.raf.utilities.Locator;

public class FetchMerchantPickUpInstructionDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchMerchantPickUpInstructionDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		String airwayBillId = request.getParams().get(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

		Locator<LogAirwayBillSessionEJBRemote> logAirwayBillLocator = null;
		Locator<LogMerchantPickupInstructionSessionEJBRemote> logMerchantPickupInstructionLocator = null;
		
		try {
			logAirwayBillLocator = new Locator<LogAirwayBillSessionEJBRemote>();
			
			LogAirwayBillSessionEJBRemote logAirwayBillSessionHome = (LogAirwayBillSessionEJBRemote) logAirwayBillLocator
			.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			
			String select = "select awb from LogAirwayBill awb where awb.airwayBillId = " + airwayBillId;
			
			//Find AirwayBill by AirwayBillId			
			List<LogAirwayBill> airwayBillList = logAirwayBillSessionHome.queryByRange(select, 0, 1);
			
			if (airwayBillList.size()>0) {
				//Check if airwaybill exists in the query result, there should only be one, take the fist from the list
				LogAirwayBill airwayBill = airwayBillList.get(0);
				
				if (airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getOrderItemId()!=null) {
					//Check if the logMerchantPickUpInstructions exists inside airwayBill, there should only be one (one-to-one relationship)
					
					logMerchantPickupInstructionLocator = new Locator<LogMerchantPickupInstructionSessionEJBRemote>();
					
					LogMerchantPickupInstructionSessionEJBRemote logMerchantPickupInstructionSessionHome = (LogMerchantPickupInstructionSessionEJBRemote) logMerchantPickupInstructionLocator
					.lookup(LogMerchantPickupInstructionSessionEJBRemote.class, "LogMerchantPickupInstructionSessionEJBBean");
					
					select = "select pi from LogMerchantPickupInstruction pi where pi.venOrderItem.orderItemId = " + airwayBill.getVenOrderItem().getOrderItemId().toString();
					
					//Find Order by Order Id			
					List<LogMerchantPickupInstruction> merchantPickupInstructionList = logMerchantPickupInstructionSessionHome.queryByRange(select, 0, 0);
					
					HashMap<String, String> map = new HashMap<String, String>();
					
					if (merchantPickupInstructionList.size()>0) {
						
						LogMerchantPickupInstruction merchantPickupInstruction = merchantPickupInstructionList.get(0);
						
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPICKUPDETAILSID, merchantPickupInstruction.getMerchantPickupDetailsId()!=null?merchantPickupInstruction.getMerchantPickupDetailsId().toString():"");
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENMERCHANT_VENPARTY_FULLORLEGALNAME, merchantPickupInstruction.getVenMerchant()!=null &&
								 merchantPickupInstruction.getVenMerchant().getVenParty()!=null? 
								 merchantPickupInstruction.getVenMerchant().getVenParty().getFullOrLegalName():"");
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_STREETADDRESS1,  merchantPickupInstruction.getVenAddress()!=null?merchantPickupInstruction.getVenAddress().getStreetAddress1():"");
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_STREETADDRESS2, merchantPickupInstruction.getVenAddress()!=null?merchantPickupInstruction.getVenAddress().getStreetAddress2():"");
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_KECAMATAN, merchantPickupInstruction.getVenAddress()!=null?merchantPickupInstruction.getVenAddress().getKecamatan():"");
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_KELURAHAN, merchantPickupInstruction.getVenAddress()!=null?merchantPickupInstruction.getVenAddress().getKelurahan():"");
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENCITY_CITYNAME, merchantPickupInstruction.getVenAddress()!=null && merchantPickupInstruction.getVenAddress().getVenCity()!=null?merchantPickupInstruction.getVenAddress().getVenCity().getCityName():"");
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENSTATE_STATENAME, merchantPickupInstruction.getVenAddress()!=null && merchantPickupInstruction.getVenAddress().getVenState()!=null?merchantPickupInstruction.getVenAddress().getVenState().getStateName():"");
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_POSTALCODE, merchantPickupInstruction.getVenAddress()!=null?merchantPickupInstruction.getVenAddress().getPostalCode():"");
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENCOUNTRY_COUNTRYNAME, merchantPickupInstruction.getVenAddress()!=null && merchantPickupInstruction.getVenAddress().getVenCountry()!=null?merchantPickupInstruction.getVenAddress().getVenCountry().getCountryName():"");
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPIC, merchantPickupInstruction.getMerchantPic());
						map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPICPHONE, merchantPickupInstruction.getMerchantPicPhone());
						
						dataList.add(map);
						
						rafDsResponse.setStatus(0);
						rafDsResponse.setStartRow(request.getStartRow());
						rafDsResponse.setTotalRows(1);
						rafDsResponse.setEndRow(request.getStartRow()+1);
					} else {
						rafDsResponse.setStatus(0);
						rafDsResponse.setStartRow(request.getStartRow());
						rafDsResponse.setTotalRows(0);
						rafDsResponse.setEndRow(request.getStartRow());
					}

					
				}
				
			}	
			
			
			

			
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				logAirwayBillLocator.close();
				logMerchantPickupInstructionLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}

}
