package com.gdn.venice.logistics.integration;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.gdn.awb.exchange.model.AirwayBillTransactionResource;
import com.gdn.awb.exchange.response.GetLogisticProviderListResponse;
import com.gdn.venice.logistics.integration.bean.AirwayBillTransaction;

public interface AirwayBillEngineConnector {
	public String getGDNRefStatus(String gdnRef) throws Exception;
	
	public String getGDNRef(String wcsOrderItemId) throws Exception;
	
	public String getAirwayBillNumber(String wcsOrderItemId) throws Exception;
	
	public boolean updateAirwayBillToES(String airwayBillNumber, String logisticProviderCode, String user) throws Exception;
	
	public boolean updateAirwayBillToPU(String airwayBillNumber) throws Exception;
	
	public boolean updateAirwayBillToCX(String airwayBillNumber, String logisticProviderCode, String user, Timestamp actualPickupTime, String receiver, String receiverRelation, Timestamp receivedDate) throws Exception;
	
	public boolean updateAirwayBillToD(String airwayBillNumber, String logisticProviderCode, String user, Timestamp actualPickupTime, String receiver, String receiverRelation, Timestamp receivedDate) throws Exception;

	public List<String> getWCSOrderItemIds(String airwayBillNumber) throws Exception;

	public AirwayBillTransaction getAirwayBillTransaction(String gdnRefNo) throws Exception;

	public boolean updateAirwayBillReceiving(String gdnRef, String recipient, String relation, Timestamp received) throws Exception;
	
	public List<AirwayBillTransaction> getAirwayBillTransactionByItem(String wcsOrderItemId) throws Exception;

	public AirwayBillTransaction getAirwayBillTransaction(String wcsOrderId,String wcsOrderItemId, boolean isGroup) throws Exception;

	public List<AirwayBillTransactionResource> getAirwayBillTransaction(String gdnRefOrAwbNo, boolean isAwbNo) throws Exception;

	public abstract boolean overrideAirwayBillNumber(String gdnRefNo, String awbNo, String user);

	public List<AirwayBillTransaction> getAirwayBillReadyForES(String logisticProviderCode, Date startDate, Date endDate) throws Exception;
	
	public GetLogisticProviderListResponse getAllLogisticProvider(String lpCode) throws Exception;
	
	public boolean approveInvoice(String awbNumber, String logProviderCode) throws Exception;
}
