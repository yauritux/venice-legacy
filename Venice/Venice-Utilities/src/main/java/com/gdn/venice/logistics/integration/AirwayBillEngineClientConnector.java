package com.gdn.venice.logistics.integration;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.djarum.raf.utilities.SQLDateUtility;
import com.gdn.awb.client.AWBEngineClient;
import com.gdn.awb.client.ServiceTransport;
import com.gdn.awb.exchange.model.AirwayBillTransactionItemResource;
import com.gdn.awb.exchange.model.AirwayBillTransactionResource;
import com.gdn.awb.exchange.response.GetAirwayBillTransactionResponse;
import com.gdn.awb.exchange.response.GetLogisticProviderListResponse;
import com.gdn.awb.exchange.response.Response;
import com.gdn.venice.logistics.integration.bean.AirwayBillTransaction;

public class AirwayBillEngineClientConnector  implements AirwayBillEngineConnector{

	protected static Logger _log = null;
	private AWBEngineClient client;
	protected static final String AIRWAYBILL_ENGINE_PROPERTIES_FILE = System.getenv("VENICE_HOME") +  "/conf/airwaybill-engine.properties";
	
	public AirwayBillEngineClientConnector(){
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.integration.AirwayBillEngineClientConnector");
		
		ServiceTransport serviceTransport = new ServiceTransport();
		// set engine address
		serviceTransport.setEndpoint(getAirwayBillEngineProperties().getProperty("address"));
		serviceTransport.setAuthUser(getAirwayBillEngineProperties().getProperty("username"));
		serviceTransport.setAuthPass(getAirwayBillEngineProperties().getProperty("password"));
		
		client = new AWBEngineClient(serviceTransport);
	}
	
	public  Properties getAirwayBillEngineProperties() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(AIRWAYBILL_ENGINE_PROPERTIES_FILE));
		} catch (Exception e) {
			_log.error("Error getting airwaybill-engine.properties", e);
			e.printStackTrace();
			return null;
		}
		return properties;
	}
	
	@Override
	public String getGDNRefStatus(String gdnRef) throws Exception{
		AirwayBillTransaction awb = getAirwayBillTransaction(gdnRef);
		
		return awb.getStatus();
	}

	@Override
	public String getGDNRef(String wcsOrderItemId) throws Exception{
		List<AirwayBillTransaction> awbList = getAirwayBillTransactionByItem(wcsOrderItemId);
		
		return awbList.get(0).getGdnRef();
	}

	@Override
	public String getAirwayBillNumber(String wcsOrderItemId) throws Exception{
		List<AirwayBillTransaction> awbList = getAirwayBillTransactionByItem(wcsOrderItemId);
		
		return awbList.get(0).getAirwayBillNo();
	}

	@Override
	public boolean updateAirwayBillToES(String airwayBillNumber, String logisticProviderCode, String user) throws Exception{
		
		Response res =  client.updateStatusToES(airwayBillNumber, logisticProviderCode, user);
		
		if(res.isSuccess()){
			return true;
			// throw exception	
	    }else{
	    	_log.error("Fail getting airway bill transaction. Reason : " + res.getMessage());
	    	return false;
	    }
		
	}

	@Override
	public boolean updateAirwayBillToPU(String airwayBillNumber) throws Exception{
		throw new UnsupportedOperationException("updateAirwayBillToPU is not yet implemented");
	}

	@Override
	public boolean updateAirwayBillToCX(String airwayBillNumber, String logisticProviderCode, String user, Timestamp actualPickupTime, String receiver, String receiverRelation, Timestamp receivedDate) throws Exception{
		
		Response res =  client.updateStatusToCX(airwayBillNumber, logisticProviderCode, user, actualPickupTime, receiver, receiverRelation, receivedDate);
		
		if(res.isSuccess()){
			return true;
			// throw exception	
	    }else{
	    	_log.error("Fail getting airway bill transaction. Reason : " + res.getMessage());
	    	return false;
	    }
	}

	@Override
	public boolean updateAirwayBillToD(String airwayBillNumber, String logisticProviderCode, String user, Timestamp actualPickupTime, String receiver, String receiverRelation, Timestamp receivedDate) throws Exception{
		
		Response res = client.updateStatusToD(airwayBillNumber, logisticProviderCode, user, receiver, receiverRelation, receivedDate);
		
		if(res.isSuccess()){
			return true;
			// throw exception	
	    }else{
	    	_log.error("Fail getting airway bill transaction. Reason : " + res.getMessage());
	    	return false;
	    }
		
	}
	
	@Override
	public boolean overrideAirwayBillNumber(String gdnRefNo, String awbNo, String user){
		
		Response res = client.overrideAwbNo(gdnRefNo, awbNo, user);
		
		if (res.isSuccess()) {
			return true;
		}else{
			_log.error("Fail override airway bill number. Reason : " + res.getMessage());
	    	return false;
		}
		
	}

	@Override
	public List<String> getWCSOrderItemIds(String airwayBillNumber) throws Exception{
		throw new UnsupportedOperationException("getWCSOrderItemIds is not yet implemented");
	}

	@Override
	public AirwayBillTransaction getAirwayBillTransaction(String gdnRefNo) throws Exception {
		
		GetAirwayBillTransactionResponse res = client.getAirwayBillTransaction(gdnRefNo);
		
	    // check connection status
	    if(res.isSuccess()){
	    	// get airwaybill transaction
	    	List<AirwayBillTransactionResource> awbTransactionResList =  res.getList();
	    	
	    	if(awbTransactionResList.size() > 1)	
	    		_log.warn("airway bill engine return " + awbTransactionResList.size() + " airway bills, system will retrieve 1");
	    	
	    	if(awbTransactionResList.size() == 0){
	    		_log.info("GDN Ref not found : " + gdnRefNo);
	    		
	    		return null;
	    	}
	    	
	    	// only retrieve the first airwaybill
	    	AirwayBillTransactionResource airwayBillTransactionResource = awbTransactionResList.get(0);
	    	// get airwaybill transaction item
	    	AirwayBillTransactionItemResource[] awbTransactionItems =  airwayBillTransactionResource.getItems();
	    	
	    	if(awbTransactionItems.length > 1)
	    		_log.warn("airway bill engine return " + awbTransactionItems.length + " transactions, system will retrieve 1");
	    	
	    	if(awbTransactionItems.length == 0){
	    		_log.info("GDN Ref not found : " + gdnRefNo);
	    		
	    		return null;
	    	}
	    	
	    	// only retrieve the first airwaybill item
	    	AirwayBillTransaction awb = airwayBillTransactionMapper(airwayBillTransactionResource).get(0);
	    	
	    	return awb;
	    	
	    // throw exception	
	    }else{
	    	throw new Exception("Fail getting airway bill transaction. Reason : " + res.getMessage());
	    }
		
	}

	@Override
	public boolean updateAirwayBillReceiving(String gdnRef, String recipient,
			String relation, Timestamp received) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<AirwayBillTransaction> getAirwayBillReadyForES(String logisticProviderCode, Date startDate, Date endDate) throws Exception{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String statusToFetch = AirwayBillTransaction.STATUS_PICK_UP;
		
		_log.info("Get Airway Bill Ready For ES params: logisticProviderCode: " + logisticProviderCode + ", status: " + statusToFetch + ", startDate: " + dateFormat.format(startDate) + ", endDate: " + dateFormat.format(endDate));
		
		GetAirwayBillTransactionResponse res = client.getAirwayBillTransaction(logisticProviderCode, statusToFetch, startDate, endDate);
		
		// check connection status
	    if(res.isSuccess()){
	    	// get airwaybill transaction
	    	List<AirwayBillTransactionResource> awbTransactionResList =  res.getList();
	    	
	    	if(awbTransactionResList.size() == 0){
	    		_log.info("No airway bill is ready for ES");
	    		return null;
	    	}
	    	
	    	List<AirwayBillTransaction> awbList = new ArrayList<AirwayBillTransaction>();
	    	
	    	for(AirwayBillTransactionResource airwayBillTransactionResource:awbTransactionResList){
	    		// get airwaybill transaction item
		    	AirwayBillTransactionItemResource[] awbTransactionItems =  airwayBillTransactionResource.getItems();
		    	
		    	if(awbTransactionItems.length > 1)
		    		_log.warn("airway bill engine return " + awbTransactionItems.length + " transactions, system will retrieve 1");
		    	
		    	if(awbTransactionItems.length == 0){
		    		
		    		_log.info("Airway bill " + airwayBillTransactionResource.getAirwayBill().getNumber() + " has no item");
		    		
		    		continue;
		    	}
		    	
		    	awbList.addAll(airwayBillTransactionMapper(airwayBillTransactionResource));
		    	
	    	}
	    	
	    	return awbList;
	    	
	    // throw exception	
	    }else{
	    	throw new Exception("Fail getting airway bill transaction. Reason : " + res.getMessage());
	    }
	}

	@Override
	public List<AirwayBillTransaction> getAirwayBillTransactionByItem(String wcsOrderItemId) throws Exception {
		
		GetAirwayBillTransactionResponse res = client.getAirwayBillTransaction(null, wcsOrderItemId, false);
		
		// check connection status
	    if(res.isSuccess()){
	    	// get airwaybill transaction
	    	List<AirwayBillTransactionResource> awbTransactionResList =  res.getList();
	    	
	    	if(awbTransactionResList.size() > 1)	
	    		_log.warn("airway bill engine return " + awbTransactionResList.size() + " airway bills, system will retrieve 1");
	    	
	    	if(awbTransactionResList.size() == 0){
	    		_log.info("Order Item not found : " + wcsOrderItemId);
	    		
	    		return null;
	    	}
	    	
	    	// only retrieve the first airwaybill
	    	AirwayBillTransactionResource airwayBillTransactionResource = awbTransactionResList.get(0);
	    	// get airwaybill transaction item
	    	AirwayBillTransactionItemResource[] awbTransactionItems =  airwayBillTransactionResource.getItems();
	    	
	    	if(awbTransactionItems.length > 1)
	    		_log.warn("airway bill engine return " + awbTransactionItems.length + " transactions, system will retrieve 1");
	    	
	    	if(awbTransactionItems.length == 0){
	    		_log.info("Order Item not found : " + wcsOrderItemId);
	    		
	    		return null;
	    	}
	    	
	    	List<AirwayBillTransaction> awbList = airwayBillTransactionMapper(airwayBillTransactionResource);
	    	
	    	return awbList;
	    	
	    // throw exception	
	    }else{
	    	throw new Exception("Fail getting airway bill transaction. Reason : " + res.getMessage());
	    }
		
	}
	
	private List<AirwayBillTransaction> airwayBillTransactionMapper(AirwayBillTransactionResource airwayBillTransactionResource){
		
		List<AirwayBillTransaction> awbList = new ArrayList<AirwayBillTransaction>();
		
		AirwayBillTransactionItemResource[] awbTransactionItems =  airwayBillTransactionResource.getItems();
		
		for (int i = 0; i < awbTransactionItems.length; i++) {
			AirwayBillTransactionItemResource item = awbTransactionItems[i];
			
			AirwayBillTransaction awb = new AirwayBillTransaction();
			
			awb.setGdnRef(item.getGdnRefNo());
			awb.setOrderId(item.getOrderId());
			awb.setOrderItemId(item.getOrderItemId());
			awb.setAirwayBillNo(airwayBillTransactionResource.getAirwayBill().getNumber());
			awb.setKodeOrigin(airwayBillTransactionResource.getOriginCode());
			awb.setKodeDestination(airwayBillTransactionResource.getDestinationCode());
			awb.setQtyProduk(item.getQuantity());
			awb.setWeight(item.getWeight());
			awb.setBeratCetak(item.getPrintedWeight());
			awb.setPricePerKg(new  BigDecimal(item.getPricePerKg()));
			awb.setFixedPrice(new BigDecimal(item.getFixPrice()));
			awb.setGiftWrap(new BigDecimal(item.getGiftWrapPrice()));
			awb.setShippingCost(new BigDecimal(item.getShippingCost()));
			awb.setInsuranceCost(new BigDecimal(item.getInsuranceCost()));
			awb.setInsurancePercentage(item.getInsurancePercentage());
			awb.setAirwaybillInsuranceCost(new BigDecimal(airwayBillTransactionResource.getTotalInsuranceCost()));
			awb.setAirwaybillFixedPrice(new BigDecimal(airwayBillTransactionResource.getTotalFixPrice()));
			awb.setAirwaybillGiftWrap(new BigDecimal(airwayBillTransactionResource.getTotalGiftWrap()));
			awb.setAirwaybillShippingCost(new BigDecimal(airwayBillTransactionResource.getTotalShippingCost()));
			awb.setAirwaybillWeight(airwayBillTransactionResource.getTotalWeight());
			awb.setNamaPengirim(airwayBillTransactionResource.getSenderName());
			awb.setKodeLogistik(airwayBillTransactionResource.getAirwayBill().getLogisticProviderCode());
			awb.setAccountNumber(airwayBillTransactionResource.getAccountNumber());
			awb.setAlamatPenerima1(airwayBillTransactionResource.getRecipientAddress1());
			awb.setAlamatPenerima2(airwayBillTransactionResource.getRecipientAddress2());
			awb.setAlamatPengirim(airwayBillTransactionResource.getSenderAddress());
			awb.setComment(airwayBillTransactionResource.getComment());
			awb.setCreatedBy(airwayBillTransactionResource.getCreatedBy());
			awb.setCreatedDate(new Timestamp(airwayBillTransactionResource.getCreatedDate().getTime()));
			awb.setHargaProduk(item.getProductPrice());
			awb.setInstruksiPengiriman(airwayBillTransactionResource.getShipmentInstruction());
			awb.setInvoiceApproved(item.isInvoiceApproved());
			awb.setKelurahanKecamatanPenerima(airwayBillTransactionResource.getRecipientKelurahanKecamatan());
			awb.setKelurahanKecamatanPengirim(airwayBillTransactionResource.getSenderKelurahanKecamatan());
			awb.setKodeposPenerima(airwayBillTransactionResource.getRecipientZipcode());
			awb.setKodeposPengirim(airwayBillTransactionResource.getSenderZipcode());
			awb.setKotaKabupatenPenerima(airwayBillTransactionResource.getRecipientCity());
			awb.setKotaKabupatenPengirim(airwayBillTransactionResource.getSenderCity());
			awb.setLastPrintedBy(airwayBillTransactionResource.getLastPrintedBy());
			
			if(airwayBillTransactionResource.getLastPrintedDate() != null)
				awb.setLastPrintedDate(SQLDateUtility.utilDateToSqlTimestamp(airwayBillTransactionResource.getLastPrintedDate()));
			else
				awb.setLastPrintedDate(null);
			
			awb.setNamaPenerima(airwayBillTransactionResource.getRecipientName());
			awb.setNamaProduk(item.getProductName());
			awb.setNoHPPicPenerima(airwayBillTransactionResource.getRecipientPersonInChargeMobile());
			awb.setNoHPPicPengirim(airwayBillTransactionResource.getSenderPersonInChargeMobile());
			awb.setPicPenerima(airwayBillTransactionResource.getRecipientPersonInCharge());
			awb.setPicPengirim(airwayBillTransactionResource.getSenderPersonInCharge());
			awb.setPrintedCount(airwayBillTransactionResource.getPrintedCount());
			awb.setPropinsiPenerima(airwayBillTransactionResource.getRecipientProvince());
			awb.setPropinsiPengirim(airwayBillTransactionResource.getSenderProvince());
			awb.setServiceType(airwayBillTransactionResource.getServiceType());
			
			if(airwayBillTransactionResource.getPickupTime() != null)
				awb.setTanggalPickup(SQLDateUtility.utilDateToSqlTimestamp(airwayBillTransactionResource.getPickupTime()));
			else
				awb.setTanggalPickup(null);
			
			awb.setVoidBy(airwayBillTransactionResource.getVoidBy());
			
			if(airwayBillTransactionResource.getVoidDate() != null)
				awb.setVoidDate(SQLDateUtility.utilDateToSqlTimestamp(airwayBillTransactionResource.getVoidDate()));
			else
				awb.setVoidDate(null);
			
			awb.setWcsFulfillmentId(airwayBillTransactionResource.getWcsFulfillmentId());
			
			if(airwayBillTransactionResource.getActualPickupTime() != null)
				awb.setTanggalActualPickup(SQLDateUtility.utilDateToSqlTimestamp(airwayBillTransactionResource.getActualPickupTime()));
			else
				awb.setTanggalActualPickup(null);
			
			awb.setLevel(airwayBillTransactionResource.getLevel());
			awb.setStatus(airwayBillTransactionResource.getStatus());
			awb.setRecipient(airwayBillTransactionResource.getReceivedBy());
			awb.setRelation(airwayBillTransactionResource.getReceiverRelation());
			
			if(airwayBillTransactionResource.getReceivedDate() != null)
				awb.setReceived(SQLDateUtility.utilDateToSqlTimestamp(airwayBillTransactionResource.getReceivedDate()));
			else
				awb.setReceived(null);
			if(airwayBillTransactionResource.getReplacementAirwayBillId() != null)
				awb.setAirwayBillPengganti(airwayBillTransactionResource.getReplacementAirwayBillId().toString());
			else
				awb.setAirwayBillPengganti(null);
			
			awb.setPenanggungBiaya(airwayBillTransactionResource.getCostHolder());
			
			awbList.add(awb);
		}
		
		return awbList;
		
	}

	@Override
	public AirwayBillTransaction getAirwayBillTransaction(String wcsOrderId,String wcsOrderItemId, boolean isGroup) throws Exception {
		GetAirwayBillTransactionResponse res = client.getAirwayBillTransaction(wcsOrderId, wcsOrderItemId, isGroup);
		
	    // check connection status
	    if(res.isSuccess()){
	    	// get airwaybill transaction
	    	List<AirwayBillTransactionResource> awbTransactionResList =  res.getList();
	    	
	    	if(awbTransactionResList.size() > 1)	
	    		_log.warn("airway bill engine return " + awbTransactionResList.size() + " airway bills, system will retrieve 1");
	    	
	    	if(awbTransactionResList.size() == 0){
	    		_log.info("Item not found : " + wcsOrderItemId);	
	    		return null;
	    	}
	    	
	    	// only retrieve the first airwaybill
	    	AirwayBillTransactionResource airwayBillTransactionResource = awbTransactionResList.get(0);
	    	// get airwaybill transaction item
	    	AirwayBillTransactionItemResource[] awbTransactionItems =  airwayBillTransactionResource.getItems();
	    	
	    	if(awbTransactionItems.length > 1)
	    		_log.warn("airway bill engine return " + awbTransactionItems.length + " transactions, system will retrieve 1");
	    	
	    	if(awbTransactionItems.length == 0){
	    		_log.info("Item not found : " + wcsOrderItemId);
	    		
	    		return null;
	    	}
	    	
	    	// only retrieve the first airwaybill item
	    	AirwayBillTransaction awb = airwayBillTransactionMapper(airwayBillTransactionResource).get(0);
	    	
	    	return awb;
	    	
	    // throw exception	
	    }else{
	    	throw new Exception("Fail getting airway bill transaction. Reason : " + res.getMessage());
	    }
	}
	
	@Override
	public GetLogisticProviderListResponse getAllLogisticProvider(String lpCode) throws Exception{
		GetLogisticProviderListResponse logisticProviderList = client.getAllLogisticProvider(lpCode);
		
		if(logisticProviderList.getList().size()>0){
			return logisticProviderList;
		}else{
			return null;
		}
	}

	@Override
	public List<AirwayBillTransactionResource> getAirwayBillTransaction(String gdnRefOrAwbNo, boolean isAwbNo) throws Exception {
		GetAirwayBillTransactionResponse res = client.getAirwayBillTransaction(gdnRefOrAwbNo, isAwbNo);
		
	    // check connection status
	    if(res.isSuccess()){
	    	// get airwaybill transaction
	    	List<AirwayBillTransactionResource> awbTransactionResList =  res.getList();
	    	
	    	if(awbTransactionResList.size() > 1)	
	    		_log.warn("airway bill engine return " + awbTransactionResList.size() + " airway bills, system will retrieve 1 with matching provider code");
	    	
	    	if(awbTransactionResList.size() == 0){
	    		_log.info("Airwaybill not found : " + gdnRefOrAwbNo);	
	    		return null;
	    	}	    	
	    	return awbTransactionResList;
	    	
	    }else{
	    	throw new Exception("Fail getting airway bill transaction. Reason : " + res.getMessage());
	    }
	}

	@Override
	public boolean approveInvoice(String awbNumber, String logProviderCode)	throws Exception {
		Response res = client.approveInvoice(awbNumber, logProviderCode);
		
		if(res.isSuccess()){
			return true;	
	    }else{
	    	_log.error("Fail getting airway bill transaction. Reason : " + res.getMessage());
	    	return false;
	    }
	}
}
