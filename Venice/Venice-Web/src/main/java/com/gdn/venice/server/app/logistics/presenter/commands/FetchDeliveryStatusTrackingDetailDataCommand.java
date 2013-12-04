package com.gdn.venice.server.app.logistics.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote;
import com.gdn.venice.facade.VenAddressSessionEJBRemote;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.facade.VenPartyAddressSessionEJBRemote;
import com.gdn.venice.logistics.integration.AirwayBillEngineClientConnector;
import com.gdn.venice.logistics.integration.AirwayBillEngineConnector;
import com.gdn.venice.logistics.integration.bean.AirwayBillTransaction;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogMerchantPickupInstruction;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenOrderItemAddress;
import com.gdn.venice.persistence.VenOrderItemContactDetail;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenPartyAddress;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.util.VeniceConstants;

public class FetchDeliveryStatusTrackingDetailDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	protected static Logger _log = null;
	
	public FetchDeliveryStatusTrackingDetailDataCommand(RafDsRequest request) {
		this.request = request;
		
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
		.getLog4JLogger("com.gdn.venice.server.app.logistics.presenter.commands.FetchDeliveryStatusTrackingDetailDataCommand");
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<Object> locator = null;
		
		String airwayBillId = request.getParams().get(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);
		try {
			locator = new Locator<Object>();
			
			LogAirwayBillSessionEJBRemote sessionHome = (LogAirwayBillSessionEJBRemote) locator
			.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			
			VenPartyAddressSessionEJBRemote partyAddressSessionHome = (VenPartyAddressSessionEJBRemote) locator
			.lookup(VenPartyAddressSessionEJBRemote.class, "VenPartyAddressSessionEJBBean");
			
			VenContactDetailSessionEJBRemote contactDetailSessionHome = (VenContactDetailSessionEJBRemote) locator
			.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");
			
			LogMerchantPickupInstructionSessionEJBRemote pickupSessionHome = (LogMerchantPickupInstructionSessionEJBRemote) locator
			.lookup(LogMerchantPickupInstructionSessionEJBRemote.class, "LogMerchantPickupInstructionSessionEJBBean");
			
			VenAddressSessionEJBRemote addressSessionHome = (VenAddressSessionEJBRemote) locator
			.lookup(VenAddressSessionEJBRemote.class, "VenAddressSessionEJBBean");
			
			VenOrderItemStatusHistorySessionEJBRemote orderItemStatusHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) locator
			.lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");		
			
			VenOrderAddressSessionEJBRemote orderAddressHome = (VenOrderAddressSessionEJBRemote) locator
			.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");
			
			VenOrderContactDetailSessionEJBRemote orderContactDetailHome = (VenOrderContactDetailSessionEJBRemote) locator
			.lookup(VenOrderContactDetailSessionEJBRemote.class, "VenOrderContactDetailSessionEJBBean");
			
			VenOrderItemAddressSessionEJBRemote orderItemAddressHome = (VenOrderItemAddressSessionEJBRemote) locator
			.lookup(VenOrderItemAddressSessionEJBRemote.class, "VenOrderItemAddressSessionEJBBean");
			
			VenOrderItemContactDetailSessionEJBRemote orderItemContactDetailHome = (VenOrderItemContactDetailSessionEJBRemote) locator
			.lookup(VenOrderItemContactDetailSessionEJBRemote.class, "VenOrderItemContactDetailSessionEJBBean");
						
			List<LogAirwayBill> airwayBillList = null;
			List<VenPartyAddress> partyMerchantAddressList = null;
			List<VenContactDetail> contactDetailListRecipient = null;
			List<VenContactDetail> contactDetailListSender = null;
			List<VenContactDetail> contactDetailListMerchant = null;
			List<LogMerchantPickupInstruction> pickupList = null;
			List<VenAddress> pickupAddressList = null;
			List<VenOrderItemStatusHistory> orderItemStatusHistoryList = null;
			
			String select = "select o from LogAirwayBill o where o.airwayBillId="+airwayBillId;				
			airwayBillList = sessionHome.queryByRange(select, 0, 1);
			
			DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
			
			AirwayBillEngineConnector awbConn = new AirwayBillEngineClientConnector();
			
			HashMap<String, String> map = new HashMap<String, String>();				
			LogAirwayBill airwayBill = airwayBillList.get(0);				
											
			String airwayBillNo = "N/A";
			String gdnRef = "N/A";
			String weight = "N/A";
			String quantity = "N/A";
			String insuredAmount = "0";
			String recipientAddress="N/A";
			String recipientName="N/A";
			String recipientMobileNumber="N/A";
			String totalCharge = "0";
			String shippingCharge = "0";
			String insuranceCharge = "0";
			String otherCharge = "0";
			String giftWrapCharge = "0";
			
			if (airwayBill.getGdnReference() != null && !airwayBill.getGdnReference().equals("")) {
				gdnRef = airwayBill.getGdnReference();
				airwayBillNo = airwayBill.getAirwayBillNumber();
				weight = airwayBill.getVenDistributionCart()!=null?airwayBill.getVenDistributionCart().getPackageWeight().toPlainString():"";
				quantity = airwayBill.getVenDistributionCart()!=null && airwayBill.getVenDistributionCart().getQuantity()!=null?airwayBill.getVenDistributionCart().getQuantity().toString():"";
				insuredAmount = airwayBill.getInsuredAmount()!=null?airwayBill.getInsuredAmount().toString():"0";
				recipientName = airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenRecipient()!=null && airwayBill.getVenOrderItem().getVenRecipient().getVenParty()!=null && airwayBill.getVenOrderItem().getVenRecipient().getVenParty().getFullOrLegalName()!=null?airwayBill.getVenOrderItem().getVenRecipient().getVenParty().getFullOrLegalName():"";
				
				String queryAddressRecipient = "select o from VenOrderItemAddress o where o.venOrderItem.orderItemId = " + airwayBill.getVenOrderItem().getOrderItemId();
				
				List<VenOrderItemAddress> orderItemAddressList = orderItemAddressHome.queryByRange(queryAddressRecipient, 0, 0);
								
				if(orderItemAddressList.size()>0){					
					recipientAddress=(Util.isNull(orderItemAddressList.get(0).getVenAddress().getStreetAddress1(), "").toString()) +
												(" "+Util.isNull(orderItemAddressList.get(0).getVenAddress().getStreetAddress2(), "").toString()) + "<br />" + 
												(Util.isNull(orderItemAddressList.get(0).getVenAddress().getKelurahan(), "").toString()) + " - " +
												(Util.isNull(orderItemAddressList.get(0).getVenAddress().getKecamatan(), "").toString()) + "<br />" +
												(orderItemAddressList.get(0).getVenAddress().getVenCity()!=null ?orderItemAddressList.get(0).getVenAddress().getVenCity().getCityName()+"":"") + "<br />" +							
												(orderItemAddressList.get(0).getVenAddress().getVenState()!=null ?orderItemAddressList.get(0).getVenAddress().getVenState().getStateName()+"":"") + " " +(Util.isNull(orderItemAddressList.get(0).getVenAddress().getPostalCode(), "").toString());						
				}
				
				totalCharge = airwayBill.getTotalCharge()!=null?airwayBill.getTotalCharge().toString():"0";
				shippingCharge = airwayBill.getVenOrderItem().getShippingCost()!=null? airwayBill.getVenOrderItem().getShippingCost().toString():"0";
				insuranceCharge = airwayBill.getInsuranceCharge()!=null?airwayBill.getInsuranceCharge().toString():(airwayBill.getVenOrderItem().getInsuranceCost()!=null?airwayBill.getVenOrderItem().getInsuranceCost().toString():"0");
				otherCharge = airwayBill.getOtherCharge()!=null?airwayBill.getOtherCharge().toString():"0";
				giftWrapCharge = airwayBill.getGiftWrapCharge()!=null?airwayBill.getGiftWrapCharge().toString():(airwayBill.getVenOrderItem().getGiftWrapPrice()!=null?airwayBill.getVenOrderItem().getGiftWrapPrice().toString():"0");
				
			}else{
				
				AirwayBillTransaction awbTransaction = awbConn.getAirwayBillTransaction(airwayBill.getVenOrderItem().getVenOrder().getWcsOrderId(), airwayBill.getVenOrderItem().getWcsOrderItemId(), false);
				
				try{
					gdnRef = awbTransaction.getGdnRef();
					airwayBillNo = awbTransaction.getAirwayBillNo();
					weight = awbTransaction.getAirwaybillWeight().toString();
					quantity = String.valueOf(awbTransaction.getQtyProduk());
					Double insured = awbTransaction.getQtyProduk() * awbTransaction.getHargaProduk() + (awbTransaction.getShippingCost()==null?Double.valueOf("0"):awbTransaction.getShippingCost().doubleValue()) + (awbTransaction.getGiftWrap()==null?Double.valueOf("0"):awbTransaction.getGiftWrap().doubleValue()) + (awbTransaction.getFixedPrice()==null?Double.valueOf("0"):awbTransaction.getFixedPrice().doubleValue());
					insuredAmount = insured.toString();
					recipientName = awbTransaction.getNamaPenerima();
					awbTransaction.getNoHPPicPenerima();
					recipientAddress = awbTransaction.getAlamatPenerima1() + " " + awbTransaction.getAlamatPenerima2() + "<br />" +
					                               awbTransaction.getKelurahanKecamatanPenerima() + " " + awbTransaction.getKodeposPenerima() + "<br />" + 
					                               awbTransaction.getKotaKabupatenPenerima() + " " + awbTransaction.getPropinsiPenerima() + "<br />" +
					                               awbTransaction.getKodeposPenerima();
					
					recipientMobileNumber = awbTransaction.getNoHPPicPenerima();
					
					totalCharge = awbTransaction.getShippingCost().add(awbTransaction.getInsuranceCost()).add(awbTransaction.getFixedPrice()).add(awbTransaction.getGiftWrap()).toString();
					shippingCharge = awbTransaction.getShippingCost().toString();
					insuranceCharge = awbTransaction.getInsuranceCost().toString();
					otherCharge = awbTransaction.getFixedPrice().toString();
					giftWrapCharge = awbTransaction.getGiftWrap().toString();
					
					
				}catch (Exception e) {
					_log.error("Error getting airway bill transaction detail", e);
				}
			}
			
			//airway bill & order detail
			map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, airwayBill.getAirwayBillId()!=null?airwayBill.getAirwayBillId().toString():"");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrder()!=null && airwayBill.getVenOrderItem().getVenOrder().getWcsOrderId()!=null?airwayBill.getVenOrderItem().getVenOrder().getWcsOrderId():"");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getWcsOrderItemId()!=null?airwayBill.getVenOrderItem().getWcsOrderItemId():"");
			map.put(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE, gdnRef);
			map.put(DataNameTokens.LOGAIRWAYBILL_VENDISTRIBUTIONCART_PACKAGEWEIGHT, weight);
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE, 
					airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrder()!=null && airwayBill.getVenOrderItem().getVenOrder().getOrderDate()!=null?formatter.format(airwayBill.getVenOrderItem().getVenOrder().getOrderDate()):"");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, 
					airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrderStatus()!=null && airwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusCode()!=null?airwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusCode():"");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, 
					airwayBill.getVenOrderItem()!=null &&
					airwayBill.getVenOrderItem().getVenMerchantProduct()!=null &&
					airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!= null &&
					airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!= null &&
					airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName()!=null?
					airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():"");
			map.put(DataNameTokens.LOGAIRWAYBILL_DESTINATION, airwayBill.getDestination()!=null?airwayBill.getDestination():"N/A");
			map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP, airwayBill.getAirwayBillTimestamp()!=null?formatter.format(airwayBill.getAirwayBillTimestamp()):"");
			map.put(DataNameTokens.LOGAIRWAYBILL_SERVICEDESC, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getLogLogisticService()!=null && airwayBill.getVenOrderItem().getLogLogisticService().getLogisticsServiceDesc()!=null?airwayBill.getVenOrderItem().getLogLogisticService().getLogisticsServiceDesc().toString():"");
			
			if(airwayBill.getLogLogisticsProvider().getLogisticsProviderId().equals(VeniceConstants.VEN_LOGISTICS_PROVIDER_BIGPRODUCT)){
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER, airwayBill.getVenOrderItem().getTrackingNumber()!=null?airwayBill.getVenOrderItem().getTrackingNumber():"N/A");
				map.put(DataNameTokens.LOGAIRWAYBILL_RECEIVED, airwayBill.getVenOrderItem().getDeliveryReceivedDate()!=null?formatter.format(airwayBill.getVenOrderItem().getDeliveryReceivedDate()):"N/A");
				map.put(DataNameTokens.LOGAIRWAYBILL_RECIPIENT, airwayBill.getVenOrderItem().getDeliveryRecipientName()!=null?airwayBill.getVenOrderItem().getDeliveryRecipientName():"N/A");
				map.put(DataNameTokens.LOGAIRWAYBILL_RELATION, airwayBill.getVenOrderItem().getDeliveryRecipientStatus()!=null?airwayBill.getVenOrderItem().getDeliveryRecipientStatus():"N/A");
			}else{				
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER, airwayBillNo);
				map.put(DataNameTokens.LOGAIRWAYBILL_RECEIVED, airwayBill.getReceived()!=null?formatter.format(airwayBill.getReceived()):"N/A");
				map.put(DataNameTokens.LOGAIRWAYBILL_RECIPIENT, airwayBill.getRecipient()!=null?airwayBill.getRecipient():"N/A");
				map.put(DataNameTokens.LOGAIRWAYBILL_RELATION, airwayBill.getRelation()!=null?airwayBill.getRelation():"N/A");
			}
			
			//order item detail
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenMerchantProduct()!=null && airwayBill.getVenOrderItem().getVenMerchantProduct().getWcsProductSku()!=null?airwayBill.getVenOrderItem().getVenMerchantProduct().getWcsProductSku():"");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenMerchantProduct()!=null && airwayBill.getVenOrderItem().getVenMerchantProduct().getWcsProductName()!=null?airwayBill.getVenOrderItem().getVenMerchantProduct().getWcsProductName():"");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENDISTRIBUTIONCART_QUANTITY, quantity);
			map.put(DataNameTokens.LOGAIRWAYBILL_INSUREDAMOUNT, insuredAmount);
			
			//recipient detail
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME, recipientName);
			//get party address
			String partyId=airwayBill.getVenOrderItem().getVenRecipient().getVenParty().getPartyId().toString();
			
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENPARTYADDRESS_VENADDRESS, recipientAddress);
			
			//get recipient contact detail
			String queryContactDetailRecipient = "select o from VenOrderItemContactDetail o join fetch o.venContactDetail where o.venOrderItem.orderItemId="+airwayBill.getVenOrderItem().getOrderItemId();		
			List<VenOrderItemContactDetail> venOrderItemContactDetailList = orderItemContactDetailHome.queryByRange(queryContactDetailRecipient, 0, 0);
			
			contactDetailListRecipient = new ArrayList<VenContactDetail>();
			
			for(VenOrderItemContactDetail orderItemContactDetail:venOrderItemContactDetailList){
				contactDetailListRecipient.add(orderItemContactDetail.getVenContactDetail());
			}
			
			String recipientPhone = "N/A";
			if(contactDetailListRecipient.size()>0){
				for(int j=0;j<contactDetailListRecipient.size();j++){
					/**
					 * Get mobile no from venice when data is before airway bill automation
					 */
					if (airwayBill.getGdnReference() != null && !airwayBill.getGdnReference().equals("")) {
						if (contactDetailListRecipient.get(j).getVenContactDetailType().getContactDetailTypeId().equals(com.gdn.venice.util.VeniceConstants.VEN_CONTACT_TYPE_MOBILE)) {
							recipientMobileNumber = contactDetailListRecipient.get(j).getContactDetail();
						}
					}
					
					if (contactDetailListRecipient.get(j).getVenContactDetailType().getContactDetailTypeId().equals(com.gdn.venice.util.VeniceConstants.VEN_CONTACT_TYPE_PHONE)) {
						recipientPhone = contactDetailListRecipient.get(j).getContactDetail();
					}
				}
			}
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_PHONE, recipientPhone);
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_MOBILE, recipientMobileNumber);
			
			//sender detail
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_FULLORLEGALNAME, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrder()!=null && airwayBill.getVenOrderItem().getVenOrder().getVenCustomer()!=null && airwayBill.getVenOrderItem().getVenOrder().getVenCustomer().getVenParty()!=null && airwayBill.getVenOrderItem().getVenOrder().getVenCustomer().getVenParty().getFullOrLegalName()!=null?airwayBill.getVenOrderItem().getVenOrder().getVenCustomer().getVenParty().getFullOrLegalName():"");
			//get party address
			partyId=airwayBill.getVenOrderItem().getVenOrder().getVenCustomer().getVenParty().getPartyId().toString();
			String queryPartyAddressSender = "select o from VenOrderAddress o join fetch o.venAddress where o.venOrder.orderId="+airwayBill.getVenOrderItem().getVenOrder().getOrderId();				
			
			List<VenOrderAddress> orderAddressList = orderAddressHome.queryByRange(queryPartyAddressSender, 0, 0);		
			
			String senderAddress="N/A";
			if(orderAddressList.size()>0){					
				senderAddress=(Util.isNull(orderAddressList.get(0).getVenAddress().getStreetAddress1(), "").toString()) +
						(" "+Util.isNull(orderAddressList.get(0).getVenAddress().getStreetAddress2(), "").toString()) + "<br />" + 
						(Util.isNull(orderAddressList.get(0).getVenAddress().getKelurahan(), "").toString()) + " - " +
						(Util.isNull(orderAddressList.get(0).getVenAddress().getKecamatan(), "").toString()) + "<br />" +
						(orderAddressList.get(0).getVenAddress().getVenCity()!=null && orderAddressList.get(0).getVenAddress().getVenCity().getCityName()!=null?orderAddressList.get(0).getVenAddress().getVenCity().getCityName().toString():"") + "<br />" +							
						(orderAddressList.get(0).getVenAddress().getVenState()!=null && orderAddressList.get(0).getVenAddress().getVenState().getStateName()!=null?orderAddressList.get(0).getVenAddress().getVenState().getStateName().toString():"") + " " +(Util.isNull(orderAddressList.get(0).getVenAddress().getPostalCode(), "").toString());							
			}
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENPARTYADDRESS_VENADDRESS, senderAddress);
			
			//get sender contact detail
			String queryContactDetailSender = "select o from VenOrderContactDetail o join fetch o.venContactDetail where o.venOrder.orderId="+airwayBill.getVenOrderItem().getVenOrder().getOrderId();	
			
			List<VenOrderContactDetail> orderContactDetailList = orderContactDetailHome.queryByRange(queryContactDetailSender, 0, 0);
			
			contactDetailListSender = new ArrayList<VenContactDetail>();
			
			for(VenOrderContactDetail contactDetail:orderContactDetailList){
				contactDetailListSender.add(contactDetail.getVenContactDetail());
			}
			
			String senderMobile = "N/A";
			String senderPhone = "N/A";
			String senderEmail = "N/A";
			if(contactDetailListSender.size()>0){
				for(int j=0;j<contactDetailListSender.size();j++){
					if (contactDetailListSender.get(j).getVenContactDetailType().getContactDetailTypeId().equals(com.gdn.venice.util.VeniceConstants.VEN_CONTACT_TYPE_MOBILE)) {
						senderMobile = contactDetailListSender.get(j).getContactDetail();
					}
					if (contactDetailListSender.get(j).getVenContactDetailType().getContactDetailTypeId().equals(com.gdn.venice.util.VeniceConstants.VEN_CONTACT_TYPE_PHONE)) {
						senderPhone = contactDetailListSender.get(j).getContactDetail();
					}
					if (contactDetailListSender.get(j).getVenContactDetailType().getContactDetailTypeId().equals(com.gdn.venice.util.VeniceConstants.VEN_CONTACT_TYPE_EMAIL)) {
						senderEmail = contactDetailListSender.get(j).getContactDetail();
						}
				}
			}
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_PHONE, senderPhone);
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_MOBILE, senderMobile);
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_EMAIL, senderEmail);
			
			//service detail
			String pickupDate="N/A";
			String queryPickupOrder = "select o from LogMerchantPickupInstruction o where o.venOrderItem.orderItemId="+airwayBill.getVenOrderItem().getOrderItemId()+" order by o.merchantPickupDetailsId desc";				
			pickupList = pickupSessionHome.queryByRange(queryPickupOrder, 0, 0);
			if(pickupList.size()>0){
				pickupDate=pickupList.get(0).getPickupDateTime()!=null?formatter.format(pickupList.get(0).getPickupDateTime()):"";
			}
			
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_PICKUPDATE, pickupDate);
			map.put(DataNameTokens.LOGAIRWAYBILL_ACTUALPICKUPDATE, airwayBill.getActualPickupDate()!=null?formatter.format(airwayBill.getActualPickupDate()):"");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MIN_EST_DATE, airwayBill.getVenOrderItem().getMinEstDate()!=null?formatter.format(airwayBill.getVenOrderItem().getMinEstDate()):"");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MAX_EST_DATE, airwayBill.getVenOrderItem().getMaxEstDate()!=null?formatter.format(airwayBill.getVenOrderItem().getMaxEstDate()):"");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_PICKUPDATE, pickupDate);
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_GIFTWRAP, airwayBill.getVenOrderItem().getGiftWrapFlag().equals(Boolean.FALSE)?"No":"Yes");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_GIFTCARD,  airwayBill.getVenOrderItem().getGiftCardFlag().equals(Boolean.FALSE)?"No":"Yes");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_GIFTNOTE, airwayBill.getVenOrderItem().getGiftCardNote()!=null?airwayBill.getVenOrderItem().getGiftCardNote():"N/A");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_SPECIALHANDLING, airwayBill.getVenOrderItem().getSpecialHandlingInstructions()!=null?airwayBill.getVenOrderItem().getSpecialHandlingInstructions():"N/A");
			
			//for big product
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String deliveredDate = "N/A", instalationDate = "N/A";
			if(airwayBill.getVenOrderItem().getMerchantDeliveredDateStart()!=null) deliveredDate = sdf.format(airwayBill.getVenOrderItem().getMerchantDeliveredDateStart());
			if(airwayBill.getVenOrderItem().getMerchantDeliveredDateEnd()!=null) deliveredDate += " - " + sdf.format(airwayBill.getVenOrderItem().getMerchantDeliveredDateEnd());
			if(airwayBill.getVenOrderItem().getMerchantInstallationDateStart()!=null) instalationDate = sdf.format(airwayBill.getVenOrderItem().getMerchantInstallationDateStart());
			if(airwayBill.getVenOrderItem().getMerchantInstallationDateEnd()!=null) instalationDate +=  " - " + sdf.format(airwayBill.getVenOrderItem().getMerchantInstallationDateEnd());

			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTCOURIER, airwayBill.getVenOrderItem().getMerchantCourier()!=null?airwayBill.getVenOrderItem().getMerchantCourier():"N/A");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTDELIVEREDDATESTARTEND, deliveredDate);
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALATIONDATESTARTEND, instalationDate);
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLOFFICER, airwayBill.getVenOrderItem().getMerchantInstallOfficer()!=null?airwayBill.getVenOrderItem().getMerchantInstallOfficer():"N/A");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLMOBILE, airwayBill.getVenOrderItem().getMerchantInstallMobile()!=null?airwayBill.getVenOrderItem().getMerchantInstallMobile():"N/A");
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLNOTE, airwayBill.getVenOrderItem().getMerchantInstallNote()!=null?airwayBill.getVenOrderItem().getMerchantInstallNote():"N/A");
			
			//charge detail						
			map.put(DataNameTokens.LOGAIRWAYBILL_TOTALCHARGE, totalCharge);
			map.put(DataNameTokens.LOGAIRWAYBILL_SHIPPINGCHARGE, shippingCharge);
			map.put(DataNameTokens.LOGAIRWAYBILL_INSURANCECHARGE, insuranceCharge);
			map.put(DataNameTokens.LOGAIRWAYBILL_OTHERCHARGE_PACKAGINGKAYU, otherCharge);
			map.put(DataNameTokens.LOGAIRWAYBILL_GIFTWRAPCHARGE, giftWrapCharge);
			
			//merchant detail				
			partyId=airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getPartyId().toString();
			//get merchant address
			String queryMerchantAddress = "select o from VenPartyAddress o where o.id.partyId="+partyId+" order by o.id.addressId desc";			
			partyMerchantAddressList = partyAddressSessionHome.queryByRange(queryMerchantAddress, 0, 0);
			String merchantAddress="N/A";
			if(partyMerchantAddressList.size()>0){					
				merchantAddress=(Util.isNull(partyMerchantAddressList.get(0).getVenAddress().getStreetAddress1(), "").toString()) +
						(" "+Util.isNull(partyMerchantAddressList.get(0).getVenAddress().getStreetAddress2(), "").toString()) + "<br />" + 
						(Util.isNull(partyMerchantAddressList.get(0).getVenAddress().getKelurahan(), "").toString()) + " - " +
						(Util.isNull(partyMerchantAddressList.get(0).getVenAddress().getKecamatan(), "").toString()) + "<br />" +
						(partyMerchantAddressList.get(0).getVenAddress().getVenCity()!=null && partyMerchantAddressList.get(0).getVenAddress().getVenCity().getCityName()!=null?partyMerchantAddressList.get(0).getVenAddress().getVenCity().getCityName().toString():"") + "<br />" +							
						(partyMerchantAddressList.get(0).getVenAddress().getVenState()!=null && partyMerchantAddressList.get(0).getVenAddress().getVenState().getStateName()!=null?partyMerchantAddressList.get(0).getVenAddress().getVenState().getStateName().toString():"") + " " +(Util.isNull(partyMerchantAddressList.get(0).getVenAddress().getPostalCode(), "").toString());							
			}
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_VENPARTYADDRESS_VENADDRESS, merchantAddress);
			
			//get merchant contact
			String merchantPhone = "N/A";
			if(pickupList.size()>0){
				partyId=pickupList.get(0).getVenMerchant().getVenParty().getPartyId().toString();

				String queryMerchantContact = "select o from VenContactDetail o where o.venParty.partyId="+partyId+" order by o.contactDetailId asc";				
				contactDetailListMerchant = contactDetailSessionHome.queryByRange(queryMerchantContact, 0, 0);
								
				if(contactDetailListMerchant.size()>0){
					for(int j=0;j<contactDetailListMerchant.size();j++){
						if (contactDetailListMerchant.get(j).getVenContactDetailType().getContactDetailTypeId().equals(com.gdn.venice.util.VeniceConstants.VEN_CONTACT_TYPE_PHONE)) {
							merchantPhone = contactDetailListMerchant.get(j).getContactDetail();
						}
					}
				}
			}				
			
			map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_VENCONTACTDETAIL_PHONE, merchantPhone);				
			
			//get pickup point address
			String addressId="";
			String pickupAddress="N/A";
			String merchantPic="N/A";
			String merchantPicPhone="N/A";
			
			if(pickupList.size()>0){
				addressId=pickupList.get(0).getVenAddress().getAddressId().toString();
			
				String queryPickupAddress = "select o from VenAddress o where o.addressId="+addressId+" order by o.addressId desc";				
				pickupAddressList = addressSessionHome.queryByRange(queryPickupAddress, 0, 0);
	
				if(pickupAddressList.size()>0){					
					pickupAddress=(Util.isNull(pickupAddressList.get(0).getStreetAddress1(), "").toString()) +
							(" "+Util.isNull(pickupAddressList.get(0).getStreetAddress2(), "").toString()) + "<br />" + 
							(Util.isNull(pickupAddressList.get(0).getKelurahan(), "").toString()) + " - " +
							(Util.isNull(pickupAddressList.get(0).getKecamatan(), "").toString()) + "<br />" +
							(pickupAddressList.get(0).getVenCity()!=null ?pickupAddressList.get(0).getVenCity().getCityName():"") + "<br />" +							
							(pickupAddressList.get(0).getVenState()!=null?pickupAddressList.get(0).getVenState().getStateName():"") + " " +(Util.isNull(pickupAddressList.get(0).getPostalCode(), "").toString());							
				}
				
				merchantPic=pickupList.get(0).getMerchantPic();
				merchantPicPhone=pickupList.get(0).getMerchantPicPhone();
			}
			
			map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS, pickupAddress);				
			map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTIONS_MERCHANTPIC, merchantPic);
			map.put(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTIONS_MERCHANTPICPHONE, merchantPicPhone);
		
			//get order item status detail
			String queryHistory = "select o from VenOrderItemStatusHistory o where o.venOrderItem.orderItemId="+airwayBill.getVenOrderItem().getOrderItemId()+" order by o.id.historyTimestamp desc";				
			orderItemStatusHistoryList = orderItemStatusHistorySessionHome.queryByRange(queryHistory, 0, 0);
			
			String orderStatusDesc="";
			String timestamp="";
			if(orderItemStatusHistoryList.size()>0){
				orderStatusDesc=orderItemStatusHistoryList.get(0).getVenOrderStatus().getOrderStatusShortDesc()+ " ("+orderItemStatusHistoryList.get(0).getVenOrderStatus().getOrderStatusCode()+")";
				timestamp=orderItemStatusHistoryList.get(0).getId().getHistoryTimestamp()!=null?formatter.format(orderItemStatusHistoryList.get(0).getId().getHistoryTimestamp()):"";
			}
			map.put(DataNameTokens.VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSDESC, orderStatusDesc);
			map.put(DataNameTokens.VENORDERITEMSTATUSHISTORY_HISTORYTIMESTAMP, timestamp);
			
			dataList.add(map);
			
			rafDsResponse.setStatus(0);
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
