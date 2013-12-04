package com.gdn.venice.logistics.integration;

import java.util.Iterator;
import java.util.List;

import com.gdn.awb.client.AWBEngineClient;
import com.gdn.awb.client.ServiceTransport;
import com.gdn.awb.exchange.model.AirwayBillTransactionItemResource;
import com.gdn.awb.exchange.model.AirwayBillTransactionResource;
import com.gdn.awb.exchange.response.GetAirwayBillListResponse;
import com.gdn.awb.exchange.response.GetAirwayBillTransactionResponse;
import com.gdn.venice.logistics.integration.bean.AirwayBillTransaction;

public class AirwayBillEngineConnectorTest {
	
	public static void main(String args[]){
		AirwayBillEngineConnector awbConn = new AirwayBillEngineDummyConnector();
		
//		System.out.println(awbConn.getGDNRefStatus("O-1234-1234"));
//		
//		System.out.println(awbConn.getGDNRef("1234"));
//		
//		System.out.println(awbConn.getAirwayBillNumber("1234"));
//		
//		System.out.println(awbConn.updateAirwayBillToPU("2223334"));
//		
//		System.out.println(awbConn.updateAirwayBillToES("2223334"));
//		
//		System.out.println(awbConn.updateAirwayBillToCX("2223334"));
//		
//		System.out.println(awbConn.updateAirwayBillToD("2223334"));
//		
//		List list = awbConn.getWCSOrderItemIds("2223334");
//		
//		for(Object ob : list){
//			System.out.println(ob.toString());
//		}
//		
//		AirwayBillTransaction awb = awbConn.getAirwayBillTransaction("O-1234-1234");
//		System.out.println(awb.getGdnRef());
//		
//		List<AirwayBillTransaction> awbList = (List<AirwayBillTransaction>) awbConn.getAirwayBillTransactionByItem("1234");
//		System.out.println("size: "+awbList.size());
//		for(AirwayBillTransaction ob : awbList){
//			System.out.println(ob.getAirwayBillNo()); 
//			System.out.println(ob.getVoidDate());
//		}
		
		
		/**
		 * Access actual AWB Engine
		 */
		ServiceTransport serviceTransport = new ServiceTransport();
		// set engine address
		serviceTransport.setEndpoint("http://10.175.70.246:8080/awb-web");
		
		AWBEngineClient client = new AWBEngineClient(serviceTransport);
		
	    GetAirwayBillTransactionResponse res =  client.getAirwayBillTransaction("1234", "1234", false);
	    // check connection status
	    if(res.isSuccess()){
	    	
	    	List<AirwayBillTransactionResource> awbTransactionResList =  res.getList();
	    	
	    	for (AirwayBillTransactionResource airwayBillTransactionResource : awbTransactionResList) {
	    		AirwayBillTransactionItemResource[] awbTransactionItems =  airwayBillTransactionResource.getItems();
	    		
	    		for (int i = 0; i < awbTransactionItems.length; i++) {
					AirwayBillTransactionItemResource item = awbTransactionItems[i];
					
					item.getGdnRefNo();
					
				}
	    		
			}
	    	
	    }
		
	}
	
}
