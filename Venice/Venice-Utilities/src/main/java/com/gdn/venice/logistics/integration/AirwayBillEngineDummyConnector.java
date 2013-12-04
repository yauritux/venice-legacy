package com.gdn.venice.logistics.integration;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.djarum.raf.utilities.SQLDateUtility;
import com.gdn.awb.exchange.model.AirwayBillTransactionResource;
import com.gdn.awb.exchange.response.GetLogisticProviderListResponse;
import com.gdn.venice.logistics.integration.bean.AirwayBillTransaction;

public class AirwayBillEngineDummyConnector implements AirwayBillEngineConnector{
	
	private static final String RESPONSE_CONFIG = System.getenv("VENICE_HOME") + "/conf/airwaybill-engine-dummy-config.xml";
	
	private static final SimpleDateFormat timestampSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	protected static Logger _log = null;
	
	private XMLConfiguration config;
	
	public AirwayBillEngineDummyConnector(){
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.logistics.integration.AirwayBillEngineDummyConnector");
		
		readResponseConfig();
	}
	
	private void readResponseConfig(){
		_log.debug("Dummy Response Configuration File:" + RESPONSE_CONFIG);
		
		try {
			config = new XMLConfiguration(RESPONSE_CONFIG);
			
		} catch (ConfigurationException e) {
			_log.error("A ConfigurationException occured when processing the configuration file", e);
		}
	}
	
	private void refreshResponseConfig(){
		readResponseConfig();
	}
	
	
	
	@Override
	public String getGDNRefStatus(String gdnRef){
		
		List responses  =  config.configurationsAt("methods.getGDNRefStatus.response");
		
		for (Iterator it = responses.iterator(); it.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
			
			String param = sub.getString("param");
			
			if(gdnRef.equals(param)){
				refreshResponseConfig();
				return sub.getString("return");
			}
						
		}
		
		return null;
	}
	
	@Override
	public String getGDNRef(String wcsOrderItemId){
		List responses  =  config.configurationsAt("methods.getGDNRef.response");
		
		for (Iterator it = responses.iterator(); it.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
			
			String param = sub.getString("param");
			
			if(wcsOrderItemId.equals(param)){
				refreshResponseConfig();
				return sub.getString("return");
			}
						
		}
		
		return null;
	}
	
	@Override
	public String getAirwayBillNumber(String wcsOrderItemId){
		List responses  =  config.configurationsAt("methods.getAirwayBillNumber.response");
		
		for (Iterator it = responses.iterator(); it.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
			
			String param = sub.getString("param");
			
			if(wcsOrderItemId.equals(param)){
				refreshResponseConfig();
				return sub.getString("return");
			}
						
		}
		
		return null;
	}
	
	/**
	 * client.updateStatusToES(airwayBillNumber, logisticProviderCode, user)
	 */
	public boolean updateAirwayBillToES(String airwayBillNumber) {
		List responses  =  config.configurationsAt("methods.updateAirwayBillToES.response");
		
		for (Iterator it = responses.iterator(); it.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
			
			String param = sub.getString("param");
			
			if(airwayBillNumber.equals(param)){
				refreshResponseConfig();
				return sub.getBoolean("return");
			}
						
		}
		
		return false;
	}

	@Override
	public boolean updateAirwayBillToPU(String airwayBillNumber) {
		List responses  =  config.configurationsAt("methods.updateAirwayBillToPU.response");
		
		for (Iterator it = responses.iterator(); it.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
			
			String param = sub.getString("param");
			
			if(airwayBillNumber.equals(param)){
				refreshResponseConfig();
				return sub.getBoolean("return");
			}
						
		}
		
		return false;
	}

	/**
	 * client.updateStatusToCX(airwayBillNumber, logisticProviderCode, user, actualPickupTime, receiver, receiverRelation, receivedDate)
	 */
	public boolean updateAirwayBillToCX(String airwayBillNumber) {
		List responses  =  config.configurationsAt("methods.updateAirwayBillToCX.response");
		
		for (Iterator it = responses.iterator(); it.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
			
			String param = sub.getString("param");
			
			if(airwayBillNumber.equals(param)){
				refreshResponseConfig();
				return sub.getBoolean("return");
			}
						
		}
		
		return false;
	}
	
	/**
	 * client.updateStatusToD(airwayBillNumber, logisticProviderCode, user, receiver, receiverRelation, receivedDate)
	 */
	public boolean updateAirwayBillToD(String airwayBillNumber) {
		List responses  =  config.configurationsAt("methods.updateAirwayBillToD.response");
		
		for (Iterator it = responses.iterator(); it.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
			
			String param = sub.getString("param");
			
			if(airwayBillNumber.equals(param)){
				refreshResponseConfig();
				return sub.getBoolean("return");
			}
						
		}
		
		return false;
	}
	
	 
	@Override
	public List<String> getWCSOrderItemIds(String airwayBillNumber){
		List responses  =  config.configurationsAt("methods.getWCSOrderItemIds.response");
		
		for (Iterator it = responses.iterator(); it.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
			
			String param = sub.getString("param");
			
			if(airwayBillNumber.equals(param)){
				refreshResponseConfig();
				return sub.getList("return.id");
			}
						
		}
		
		return null;
	}
	
	@Override
	public AirwayBillTransaction getAirwayBillTransaction(String gdnRefNo){
		List responses  =  config.configurationsAt("methods.getAirwayBillTransaction.response");
		
		for (Iterator it = responses.iterator(); it.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
			
			String param = sub.getString("param");
			
			if(gdnRefNo.equals(param)){
				
				AirwayBillTransaction awb = new AirwayBillTransaction();
				
				awb.setGdnRef(sub.getString("return." + AirwayBillTransaction.GDN_REF));
				awb.setOrderId(sub.getString("return." + AirwayBillTransaction.ORDER_ID));
				awb.setOrderItemId(sub.getString("return." + AirwayBillTransaction.ORDER_ITEM_ID));
				awb.setAirwayBillNo(sub.getString("return." + AirwayBillTransaction.AIRWAY_BILL_NO));
				awb.setKodeOrigin(sub.getString("return." + AirwayBillTransaction.KODE_ORIGIN));
				awb.setKodeDestination(sub.getString("return." + AirwayBillTransaction.KODE_DESTINATION));
				awb.setQtyProduk(sub.getInt("return." + AirwayBillTransaction.QTY_PRODUK));
				awb.setWeight(sub.getDouble("return." + AirwayBillTransaction.WEIGHT));
				awb.setBeratCetak(sub.getDouble("return." + AirwayBillTransaction.BERAT_CETAK));
				awb.setPricePerKg(sub.getBigDecimal("return." + AirwayBillTransaction.PRICE_PER_KG));
				awb.setFixedPrice(sub.getBigDecimal("return." + AirwayBillTransaction.FIXED_PRICE));
				awb.setGiftWrap(sub.getBigDecimal("return." + AirwayBillTransaction.GIFT_WRAP));
				awb.setShippingCost(sub.getBigDecimal("return." + AirwayBillTransaction.SHIPPING_COST));
				awb.setInsuranceCost(sub.getBigDecimal("return." + AirwayBillTransaction.INSURANCE_COST));
				awb.setAirwaybillInsuranceCost(sub.getBigDecimal("return." + AirwayBillTransaction.AIRWAYBILL_INSURANCE_COST));
				awb.setAirwaybillFixedPrice(sub.getBigDecimal("return." + AirwayBillTransaction.AIRWAYBILL_FIXED_PRICE));
				awb.setAirwaybillGiftWrap(sub.getBigDecimal("return." + AirwayBillTransaction.AIRWAYBILL_GIFT_WRAP));
				awb.setAirwaybillShippingCost(sub.getBigDecimal("return." + AirwayBillTransaction.AIRWAYBILL_SHIPPING_COST));
				awb.setAirwaybillWeight(sub.getDouble("return." + AirwayBillTransaction.AIRWAYBILL_WEIGHT));
				awb.setNamaPengirim(sub.getString("return." + AirwayBillTransaction.NAMA_PENGIRIM));
				awb.setKodeLogistik(sub.getString("return." + AirwayBillTransaction.KODE_LOGISTIK));
				try {
					awb.setTanggalActualPickup(SQLDateUtility.utilDateToSqlTimestamp(timestampSDF.parse(sub.getString("return." + AirwayBillTransaction.TANGGAL_ACTUAL_PICKUP))));
				} catch (ParseException e) {
					_log.error("unable to parse " + AirwayBillTransaction.TANGGAL_ACTUAL_PICKUP, e);
				}
				
				awb.setLevel(sub.getString("return." + AirwayBillTransaction.LEVEL));
				awb.setStatus(sub.getString("return." + AirwayBillTransaction.STATUS));
				awb.setRecipient(sub.getString("return." + AirwayBillTransaction.RECIPIENT));
				awb.setRelation(sub.getString("return." + AirwayBillTransaction.RELATION));
				try {
					awb.setReceived(SQLDateUtility.utilDateToSqlTimestamp(timestampSDF.parse(sub.getString("return." + AirwayBillTransaction.RECEIVED))));
				} catch (ParseException e) {
					_log.error("unable to parse " + AirwayBillTransaction.RECEIVED, e);
				}
				
				awb.setAirwayBillPengganti(sub.getString("return." + AirwayBillTransaction.AIRWAYBILL_PENGGANTI));
				awb.setPenanggungBiaya(sub.getString("return." + AirwayBillTransaction.PENANGGUNG_BIAYA));
				
				refreshResponseConfig();
				
				return awb;
			}
						
		}
		
		return null;
	}

	@Override
	public boolean updateAirwayBillReceiving(String gdnRef, String recipient, String relation, Timestamp received){
		List responses  =  config.configurationsAt("methods.updateAirwayBillReceiving.response");
		
		for (Iterator it = responses.iterator(); it.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
			
			String param = sub.getString("param");
			
			if(gdnRef.equals(param)){
				refreshResponseConfig();
				return sub.getBoolean("return");
			}
						
		}
		
		return false;
	}
	
	@Override
	public List<AirwayBillTransaction> getAirwayBillTransactionByItem(String wcsOrderItemId){
		List responses  =  config.configurationsAt("methods.getAirwayBillTransactionByItem.response");		
		List<AirwayBillTransaction> awbList = new ArrayList<AirwayBillTransaction>();
		int i=0;
		
		for (Iterator it = responses.iterator(); it.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
			String param = sub.getString("param");
				if(wcsOrderItemId.equals(param)){														
					List<HierarchicalConfiguration> subs = config.configurationsAt("methods.getAirwayBillTransactionByItem.response("+i+").return.list");					
					for(HierarchicalConfiguration sub2 : subs){
						AirwayBillTransaction awb = new AirwayBillTransaction();
						awb.setGdnRef(sub2.getString(AirwayBillTransaction.GDN_REF));
						awb.setOrderId(sub2.getString(AirwayBillTransaction.ORDER_ID));
						awb.setOrderItemId(sub2.getString(AirwayBillTransaction.ORDER_ITEM_ID));
						awb.setAirwayBillNo(sub2.getString(AirwayBillTransaction.AIRWAY_BILL_NO));
						awb.setKodeOrigin(sub2.getString(AirwayBillTransaction.KODE_ORIGIN));
						awb.setKodeDestination(sub2.getString(AirwayBillTransaction.KODE_DESTINATION));
						awb.setQtyProduk(sub2.getInt(AirwayBillTransaction.QTY_PRODUK));
						awb.setWeight(sub2.getDouble(AirwayBillTransaction.WEIGHT));
						awb.setBeratCetak(sub2.getDouble(AirwayBillTransaction.BERAT_CETAK));
						awb.setPricePerKg(sub2.getBigDecimal(AirwayBillTransaction.PRICE_PER_KG));
						awb.setGiftWrap(sub2.getBigDecimal(AirwayBillTransaction.GIFT_WRAP));
						awb.setShippingCost(sub2.getBigDecimal(AirwayBillTransaction.SHIPPING_COST));
						awb.setInsuranceCost(sub2.getBigDecimal(AirwayBillTransaction.INSURANCE_COST));
						awb.setAirwaybillInsuranceCost(sub2.getBigDecimal(AirwayBillTransaction.AIRWAYBILL_INSURANCE_COST));
						awb.setNamaPengirim(sub2.getString(AirwayBillTransaction.NAMA_PENGIRIM));
						awb.setKodeLogistik(sub2.getString(AirwayBillTransaction.KODE_LOGISTIK));
						try {
								awb.setTanggalActualPickup(SQLDateUtility.utilDateToSqlTimestamp(timestampSDF.parse(sub2.getString(AirwayBillTransaction.TANGGAL_ACTUAL_PICKUP))));
						} catch (ParseException e) {
							_log.error("unable to parse " + AirwayBillTransaction.TANGGAL_ACTUAL_PICKUP, e);
						}
						
						awb.setLevel(sub2.getString(AirwayBillTransaction.LEVEL));
						awb.setStatus(sub2.getString(AirwayBillTransaction.STATUS));
						awb.setRecipient(sub2.getString(AirwayBillTransaction.RECIPIENT));
						awb.setRelation(sub2.getString(AirwayBillTransaction.RELATION));
						try {
								awb.setReceived(SQLDateUtility.utilDateToSqlTimestamp(timestampSDF.parse(sub2.getString(AirwayBillTransaction.RECEIVED))));
						} catch (ParseException e) {
							_log.error("unable to parse " + AirwayBillTransaction.RECEIVED, e);
						}
						
						awb.setAirwayBillPengganti(sub2.getString(AirwayBillTransaction.AIRWAYBILL_PENGGANTI));
						awb.setPenanggungBiaya(sub2.getString(AirwayBillTransaction.PENANGGUNG_BIAYA));
						awb.setVoidBy(sub2.getString(AirwayBillTransaction.VOID_BY));
						try {
							if(!sub2.getString(AirwayBillTransaction.VOID_DATE).equals("")){
								awb.setVoidDate(SQLDateUtility.utilDateToSqlTimestamp(timestampSDF.parse(sub2.getString(AirwayBillTransaction.VOID_DATE))));
							}
						} catch (ParseException e) {
							_log.error("unable to parse " + AirwayBillTransaction.VOID_DATE, e);
						}
						awbList.add(awb);							
					}
			
					refreshResponseConfig();
					return awbList;
					
				}
				i++;
		}
		return null;
	}

	public boolean overrideAirwayBillNumber(String gdnRefNo, String awbNo,
			String user) {
		return false;
	}

	@Override
	public boolean updateAirwayBillToES(String airwayBillNumber,
			String logisticProviderCode, String user) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateAirwayBillToCX(String airwayBillNumber,
			String logisticProviderCode, String user,
			Timestamp actualPickupTime, String receiver,
			String receiverRelation, Timestamp receivedDate) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateAirwayBillToD(String airwayBillNumber,
			String logisticProviderCode, String user,
			Timestamp actualPickupTime, String receiver,
			String receiverRelation, Timestamp receivedDate) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AirwayBillTransaction getAirwayBillTransaction(String wcsOrderId,
			String wcsOrderItemId, boolean isGroup) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetLogisticProviderListResponse getAllLogisticProvider(String lpCode)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AirwayBillTransactionResource> getAirwayBillTransaction(String gdnRefOrAwbNo,	boolean isAwbNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AirwayBillTransaction> getAirwayBillReadyForES(
			String logisticProviderCode, Date startDate, Date endDate)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean approveInvoice(String awbNumber, String logProviderCode)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	
	
}
