package com.gdn.venice.integration.outbound;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.integration.jaxb.AirwayBill;
import com.gdn.integration.jaxb.BlockInfo;
import com.gdn.integration.jaxb.DistributionCart;
import com.gdn.integration.jaxb.LogisticsInfo;
import com.gdn.integration.jaxb.MasterData;
import com.gdn.integration.jaxb.Order;
import com.gdn.integration.jaxb.OrderItem;
import com.gdn.integration.jaxb.Party;
import com.gdn.integration.jaxb.Payment;
import com.gdn.integration.jaxb.Receiver;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderPayment;
import com.pws.integration.pathway.OutboundServiceSessionEJBPortTypeProxy;
import com.pws.integration.pathway.TextMessage;
import com.pws.integration.pathway.utils.StringEscapeUtils;

/**
 * Publisher.java
 * 
 * A publisher class for Venice to publish outbound messages to the integration infrastructure
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class Publisher {
    protected static Logger _log = null;
    //The properties file to get the local publisher configuration from
    protected static final String VENICE_PUBLISHER_PROPERTIES_FILE = System.getenv("VENICE_HOME") +  "/conf/Publisher.properties";

    private static final String AIRWAYBILL_BY_ORDER_ITEM_ID_SQL = "select * from log_airway_bill where order_item_id = ?";
    
	OutboundServiceSessionEJBPortTypeProxy proxy = null;
	
	public Publisher() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
        _log = loggerFactory.getLog4JLogger("com.gdn.venice.integration.outbound.Publisher");
        proxy = new OutboundServiceSessionEJBPortTypeProxy(getVenPublishPort_addressProperty());
        _log.debug("Publisher proxy created...");
	}

	
	/**
	 * publishes an order status update to the integration infrastructure
	 * 
	 * @param venOrder is the order that has changed status
	 * @param blockingSource can be null but must be provided if the order is being blocked
	 * @return true if the operation succeeds else false
	 */
	public Boolean publishUpdateOrderStatus(VenOrder venOrder, String blockingSource){
		_log.debug("publishUpdateOrderStatus entered...");
		//Instantiate the JAXB object and stuff it with the data from the venOrder object
		Order order = new Order();
		
		//OrderId
		MasterData orderId = new MasterData();
		orderId.setCode(venOrder.getWcsOrderId());
		orderId.setSource("Venice");
		orderId.setType("Order.orderId");
		order.setOrderId(orderId);
		
		Long orderStatus = venOrder.getVenOrderStatus().getOrderStatusId();
		
		//Order status		
		if(orderStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_SF){
			order.setStatus("SF");
		}else if(orderStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_FP){
			order.setStatus("FP");			
		}else if(orderStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_FC){
			order.setStatus("FC");
		}
		_log.debug("set status to "+order.getStatus()+" for wcs order id: "+venOrder.getWcsOrderId());
		
		//Order timestamp
		GregorianCalendar c = new GregorianCalendar();
		XMLGregorianCalendar xmlgc = null;
		try {
			xmlgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			_log.error("A DatatypeConfigurationException occured when creating a new XMLGregorianCalendar:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		order.setTimestamp(xmlgc);
		
		//If the order is to be blocked then include the blocking information
		if(blockingSource != null){
			_log.debug("block info is not null");
			_log.debug("block reason: "+venOrder.getBlockedReason());
			_log.debug("block source: "+blockingSource);
			BlockInfo blockInfo = new BlockInfo();
			blockInfo.setFlag(venOrder.getBlockedFlag());
			blockInfo.setReasonText(venOrder.getBlockedReason());
			blockInfo.setSource(blockingSource);
			blockInfo.setTimestamp(xmlgc);
			order.setBlockInfo(blockInfo);
		}
		
		//Marshall the order object out to a stream
		ByteArrayOutputStream jaxbOutputStream = null;

		// Create a JAXBContext
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(com.gdn.integration.jaxb.Order.class);
		} catch (JAXBException e) {
			_log.error("A JXBException occured when creating the JAXBContext" + e.getMessage());
			e.printStackTrace();
		}

		try {
			// Create a marshaller
			Marshaller m = jc.createMarshaller();

			// Create a new OutputStream with a decent buffer
			jaxbOutputStream = new ByteArrayOutputStream();
			
			// Marshal the JAXBElement object into stream.
			m.marshal(order, jaxbOutputStream); 
		} catch (JAXBException jaxbe) {
			_log.error("A JAXBException occured when marshalling the message:" + jaxbe.getMessage());
			jaxbe.printStackTrace();
			return Boolean.FALSE;
		}
		String orderXML = jaxbOutputStream.toString();
		
		//Escape the string in prep to publish it
		String escapedOrderXML = StringEscapeUtils.escapeXML(orderXML);
		
		//Publish the message		
		TextMessage outboundMessage = new TextMessage();
		
		outboundMessage.setMsgType("updateOrderStatus");
		outboundMessage.setSourceAdapter("veniceAdapter");
		outboundMessage.setMsgPayload(escapedOrderXML);
		
		try {
			proxy.publish(outboundMessage);
		} catch (RemoteException e) {
			_log.error("An error occured when sending an outbound message" + e.getMessage());
			e.printStackTrace();
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * publishes an order item status update to the integration infrastructure
	 * @param venOrder contains the order item that has changed status
	 * @return true if the operation succeeds else false
	 */
	public Boolean publishUpdateOrderItemStatus(VenOrderItem venOrderItem){
		_log.debug("publishUpdateOrderItemStatus entered..." + venOrderItem.getWcsOrderItemId());
		//Instantiate the JAXB object and stuff it with the data from the venOrderItem object
		Order order = new Order();
		
		//OrderId
		MasterData orderId = new MasterData();
		orderId.setCode(venOrderItem.getVenOrder().getWcsOrderId());
		orderId.setSource("Venice");
		orderId.setType("Order.orderId");
		order.setOrderId(orderId);
				
		//Order timestamp
		GregorianCalendar c = new GregorianCalendar();
		XMLGregorianCalendar xmlgc = null;
		try {
			xmlgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			_log.error("A DatatypeConfigurationException occured when creating a new XMLGregorianCalendar:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		order.setTimestamp(xmlgc);
		
		OrderItem orderItem = new OrderItem();
		MasterData orderItemId = new MasterData();
		orderItemId.setType("OrderItem.orderItemId");
		orderItemId.setSource("Venice");
		orderItemId.setCode(venOrderItem.getWcsOrderItemId());
		
		orderItem.setItemId(orderItemId);
		orderItem.setTimestamp(xmlgc);
		
		Long orderItemStatus = venOrderItem.getVenOrderStatus().getOrderStatusId();
		if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_ES){
			orderItem.setStatus("ES");
		}else if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_D){
			orderItem.setStatus("D");
			//Include the LogisticsInfo record with the recipeint name, occupation and received date
			LogAirwayBill logAirwayBill = venOrderItem.getLogAirwayBills().get(0);
			
			LogisticsInfo logisticsInfo = new LogisticsInfo();
			
			logisticsInfo.setDeliveredDate(com.djarum.raf.utilities.XMLGregorianCalendarConverter.asXMLGregorianCalendar(logAirwayBill.getReceived()));
			Receiver receiver = new Receiver();
			receiver.setOccupation(logAirwayBill.getRelation());
			Party party = new Party();
			party.setFullOrLegalName(logAirwayBill.getRecipient());
			receiver.setParty(party);
			logisticsInfo.setReceiver(receiver);
			orderItem.setLogisticsInfo(logisticsInfo);
			
		}else if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_PP){
			orderItem.setStatus("PP");			
		}else if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_RT){
			orderItem.setStatus("RT");
		}else if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_CX){
			orderItem.setStatus("CX");
		}else if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_FP){
			orderItem.setStatus("FP");
		}
		
		//Add the order item to the list
		order.getOrderItems().add(orderItem);
		
		//Marshall the order object out to a stream
		ByteArrayOutputStream jaxbOutputStream = null;

		// Create a JAXBContext
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(com.gdn.integration.jaxb.Order.class);
		} catch (JAXBException e) {
			_log.error("A JXBException occured when creating the JAXBContext" + e.getMessage());
			e.printStackTrace();
		}

		try {
			// Create a marshaller
			Marshaller m = jc.createMarshaller();

			// Create a new OutputStream with a decent buffer
			jaxbOutputStream = new ByteArrayOutputStream();
			
			// Marshal the JAXBElement object into stream.
			m.marshal(order, jaxbOutputStream); 
		} catch (JAXBException jaxbe) {
			_log.error("A JAXBException occured when marshalling the message:" + jaxbe.getMessage());
			jaxbe.printStackTrace();
			return Boolean.FALSE;
		}
		String orderXML = jaxbOutputStream.toString();
		
		//Escape the string in prep to publish it
		String escapedOrderXML = StringEscapeUtils.escapeXML(orderXML);
		
		//Publish the message
		TextMessage outboundMessage = new TextMessage();
		
		outboundMessage.setMsgType("updateOrderItemStatus");
		outboundMessage.setSourceAdapter("veniceAdapter");
		outboundMessage.setMsgPayload(escapedOrderXML);
		
		try {
			_log.debug("Publishing message..." + outboundMessage);
			proxy.publish(outboundMessage);
		} catch (RemoteException e) {
			_log.error("An error occured when sending an outbound message" + e.getMessage());
			e.printStackTrace();
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * publishes an order item status update to the integration infrastructure
	 * @param venOrder contains the order item that has changed status
	 * @return true if the operation succeeds else false
	 */
	public Boolean publishUpdateOrderItemStatus(VenOrderItem venOrderItem, Connection conn){
		_log.debug("publishUpdateOrderItemStatus entered..." + venOrderItem.getWcsOrderItemId());
		//Instantiate the JAXB object and stuff it with the data from the venOrderItem object
		Order order = new Order();
		
		//OrderId
		MasterData orderId = new MasterData();
		orderId.setCode(venOrderItem.getVenOrder().getWcsOrderId());
		orderId.setSource("Venice");
		orderId.setType("Order.orderId");
		order.setOrderId(orderId);
				
		//Order timestamp
		GregorianCalendar c = new GregorianCalendar();
		XMLGregorianCalendar xmlgc = null;
		try {
			xmlgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			_log.error("A DatatypeConfigurationException occured when creating a new XMLGregorianCalendar:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		order.setTimestamp(xmlgc);
		
		OrderItem orderItem = new OrderItem();
		MasterData orderItemId = new MasterData();
		orderItemId.setType("OrderItem.orderItemId");
		orderItemId.setSource("Venice");
		orderItemId.setCode(venOrderItem.getWcsOrderItemId());
		
		orderItem.setItemId(orderItemId);
		orderItem.setTimestamp(xmlgc);
		
		Long orderItemStatus = venOrderItem.getVenOrderStatus().getOrderStatusId();
		if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_ES){
			orderItem.setStatus("ES");
			if(venOrderItem.getVenOrder().getRmaFlag()!=null && venOrderItem.getVenOrder().getRmaFlag()==true){
				order.setRmaFlag(venOrderItem.getVenOrder().getRmaFlag());
			}
		}else if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_D){
			orderItem.setStatus("D");
			//Include the LogisticsInfo record with the recipeint name, occupation and received date
			try{
//				List<LogAirwayBill> logAirwayBillList = airwayBillHome.queryByRange("select o from LogAirwayBill o where o.venOrderItem.orderItemId = " + venOrderItem.getOrderItemId(), 0, 0);
				
				PreparedStatement psAirwayBill = conn.prepareStatement(AIRWAYBILL_BY_ORDER_ITEM_ID_SQL);
				psAirwayBill.setLong(1, venOrderItem.getOrderItemId());
				
				ResultSet rsAirwayBill = psAirwayBill.executeQuery();
				rsAirwayBill.next();

				LogAirwayBill logAirwayBill = new LogAirwayBill();
				logAirwayBill.setReceived(rsAirwayBill.getDate("received"));
				logAirwayBill.setRelation(rsAirwayBill.getString("relation"));
				logAirwayBill.setRecipient(rsAirwayBill.getString("recipient"));
				
				rsAirwayBill.close();
				psAirwayBill.close();
				
				LogisticsInfo logisticsInfo = new LogisticsInfo();
				
				logisticsInfo.setDeliveredDate(com.djarum.raf.utilities.XMLGregorianCalendarConverter.asXMLGregorianCalendar(logAirwayBill.getReceived()));
				Receiver receiver = new Receiver();
				receiver.setOccupation(logAirwayBill.getRelation());
				Party party = new Party();
				party.setFullOrLegalName(logAirwayBill.getRecipient());
				receiver.setParty(party);
				logisticsInfo.setReceiver(receiver);
				orderItem.setLogisticsInfo(logisticsInfo);
			} catch(Exception e){
				_log.error("A JAXBException occured when marshalling the message:" + e.getMessage());
				e.printStackTrace();
				return Boolean.FALSE;
			}
			
		}else if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_PP){
			orderItem.setStatus("PP");			
		}else if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_RT){
			orderItem.setStatus("RT");
		}else if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_CX){
			orderItem.setStatus("CX");
			LogisticsInfo logisticsInfo = new LogisticsInfo();
			logisticsInfo.setPackageCount(venOrderItem.getPackageCount());
			
//			for(VenDistributionCart cart:venOrderItem.getVenDistributionCarts()){
//				_log.debug("Getting distribution carts..." + cart.getDcSequence());
				DistributionCart distributionCart = new DistributionCart();
				distributionCart.setPackageWeight(1);
				distributionCart.setQuantity(1);
				distributionCart.setSequence(1);
				ArrayList<AirwayBill> airwayBillList = new ArrayList<AirwayBill>();
				for(LogAirwayBill awb:venOrderItem.getLogAirwayBills()){
					_log.debug("Getting airway bills..." + awb.getAirwayBillNumber());
					AirwayBill airwayBill = new AirwayBill();
					airwayBill.setTimestamp(xmlgc);
					/*
					 * 2011-05-27
					 * In accordance with JIRA number VENICE -16 Ambil Nomor AirwayBill dari 3PL Activity Report
					 * Modified here to always send the airway bill number - DF
					 */
					airwayBill.setTrackingNumber(awb.getAirwayBillNumber());
					airwayBillList.add(airwayBill);
				}
				distributionCart.setAirwayBill(airwayBillList.get(0));				
				logisticsInfo.getDistributionCarts().add(distributionCart);
//			}
			orderItem.setLogisticsInfo(logisticsInfo);
		}else if(orderItemStatus == com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_FP){
			orderItem.setStatus("FP");
		}
		
		
		//Add the order item to the list
		order.getOrderItems().add(orderItem);
		
		//Marshall the order object out to a stream
		ByteArrayOutputStream jaxbOutputStream = null;

		// Create a JAXBContext
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(com.gdn.integration.jaxb.Order.class);
		} catch (JAXBException e) {
			_log.error("A JXBException occured when creating the JAXBContext" + e.getMessage());
			e.printStackTrace();
		}

		try {
			// Create a marshaller
			Marshaller m = jc.createMarshaller();

			// Create a new OutputStream with a decent buffer
			jaxbOutputStream = new ByteArrayOutputStream();
			
			// Marshal the JAXBElement object into stream.
			m.marshal(order, jaxbOutputStream); 
		} catch (JAXBException jaxbe) {
			_log.error("A JAXBException occured when marshalling the message:" + jaxbe.getMessage());
			jaxbe.printStackTrace();
			return Boolean.FALSE;
		}
		String orderXML = jaxbOutputStream.toString();
		
		//Escape the string in prep to publish it
		String escapedOrderXML = StringEscapeUtils.escapeXML(orderXML);
		
		//Publish the message
		TextMessage outboundMessage = new TextMessage();
		
		outboundMessage.setMsgType("updateOrderItemStatus");
		outboundMessage.setSourceAdapter("veniceAdapter");
		outboundMessage.setMsgPayload(escapedOrderXML);
		
		try {
			_log.debug("Publishing message..." + outboundMessage);
			proxy.publish(outboundMessage);
		} catch (RemoteException e) {
			_log.error("An error occured when sending an outbound message" + e.getMessage());
			e.printStackTrace();
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * @param venOrderPayment
	 * @return
	 */
	public Boolean publishUpdateOrderVAPaymentStatus(String wcsOrderId, VenOrderPayment venOrderPayment){
		//Instantiate the JAXB object and stuff it with the data from the venOrderItem object
		Order order = new Order();
		
		//OrderId
		MasterData orderId = new MasterData();
		orderId.setCode(wcsOrderId);
		orderId.setSource("Venice");
		orderId.setType("Order.orderId");
		order.setOrderId(orderId);
				
		//Order timestamp
		GregorianCalendar c = new GregorianCalendar();
		XMLGregorianCalendar xmlgc = null;
		try {
			xmlgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			_log.error("A DatatypeConfigurationException occured when creating a new XMLGregorianCalendar:" + e.getMessage());
			return null;
		}
		
		order.setTimestamp(xmlgc);
		
		Payment payment = new Payment();
		MasterData paymentId = new MasterData();
		paymentId.setType("Payment.paymentId");
		paymentId.setSource("Venice");
		paymentId.setCode(venOrderPayment.getWcsPaymentId());
		
		payment.setPaymentId(paymentId);
		payment.setTimestamp(xmlgc);
				
		payment.setPaymentStatus("APPROVED");
		
		order.getPayments().add(payment);
		
		//Marshall the order object out to a stream
		ByteArrayOutputStream jaxbOutputStream = null;

		// Create a JAXBContext
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(com.gdn.integration.jaxb.Order.class);
		} catch (JAXBException e) {
			_log.error("A JXBException occured when creating the JAXBContext" + e.getMessage());
		}

		try {
			// Create a marshaller
			Marshaller m = jc.createMarshaller();

			// Create a new OutputStream with a decent buffer
			jaxbOutputStream = new ByteArrayOutputStream();
			
			// Marshal the JAXBElement object into stream.
			m.marshal(order, jaxbOutputStream); 
		} catch (JAXBException jaxbe) {
			_log.error("A JAXBException occured when marshalling the message:" + jaxbe.getMessage());
			return Boolean.FALSE;
		}
		String orderXML = jaxbOutputStream.toString();
		
		//Escape the string in prep to publish it
		String escapedOrderXML = StringEscapeUtils.escapeXML(orderXML);
		
		//Publish the message		
		TextMessage outboundMessage = new TextMessage();
		
		outboundMessage.setMsgType("updateOrderVAPaymentStatus");
		outboundMessage.setSourceAdapter("veniceAdapter");
		outboundMessage.setMsgPayload(escapedOrderXML);
		
		try {
			proxy.publish(outboundMessage);
		} catch (RemoteException e) {
			_log.error("An error occured when sending an outbound message" + e.getMessage());
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * @param venOrderPayment
	 * @return
	 */
	public Boolean publishUpdateOrderCSPaymentStatus(String wcsOrderId, VenOrderPayment venOrderPayment){
		//Instantiate the JAXB object and stuff it with the data from the venOrderItem object
		Order order = new Order();
		
		//OrderId
		MasterData orderId = new MasterData();
		orderId.setCode(wcsOrderId);
		orderId.setSource("Venice");
		orderId.setType("Order.orderId");
		order.setOrderId(orderId);
				
		//Order timestamp
		GregorianCalendar c = new GregorianCalendar();
		XMLGregorianCalendar xmlgc = null;
		try {
			xmlgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			_log.error("A DatatypeConfigurationException occured when creating a new XMLGregorianCalendar:" + e.getMessage());
			return null;
		}
		
		order.setTimestamp(xmlgc);
		
		Payment payment = new Payment();
		MasterData paymentId = new MasterData();
		paymentId.setType("Payment.paymentId");
		paymentId.setSource("Venice");
		paymentId.setCode(venOrderPayment.getWcsPaymentId());
		
		payment.setPaymentId(paymentId);
		payment.setTimestamp(xmlgc);
				
		payment.setPaymentStatus("APPROVED");
		
		order.getPayments().add(payment);
		
		//Marshall the order object out to a stream
		ByteArrayOutputStream jaxbOutputStream = null;

		// Create a JAXBContext
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(com.gdn.integration.jaxb.Order.class);
		} catch (JAXBException e) {
			_log.error("A JXBException occured when creating the JAXBContext" + e.getMessage());
		}

		try {
			// Create a marshaller
			Marshaller m = jc.createMarshaller();

			// Create a new OutputStream with a decent buffer
			jaxbOutputStream = new ByteArrayOutputStream();
			
			// Marshal the JAXBElement object into stream.
			m.marshal(order, jaxbOutputStream); 
		} catch (JAXBException jaxbe) {
			_log.error("A JAXBException occured when marshalling the message:" + jaxbe.getMessage());
			return Boolean.FALSE;
		}
		String orderXML = jaxbOutputStream.toString();
		
		//Escape the string in prep to publish it
		String escapedOrderXML = StringEscapeUtils.escapeXML(orderXML);
		
		//Publish the message		
		TextMessage outboundMessage = new TextMessage();
		
		outboundMessage.setMsgType("updateOrderVAPaymentStatus");
		outboundMessage.setSourceAdapter("veniceAdapter");
		outboundMessage.setMsgPayload(escapedOrderXML);
		
		try {
			proxy.publish(outboundMessage);
		} catch (RemoteException e) {
			_log.error("An error occured when sending an outbound message" + e.getMessage());
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * @param venOrderPayment
	 * @return
	 */
	public Boolean publishPendingOrderCSPaymentStatus(String wcsOrderId, VenOrderPayment venOrderPayment){
		//Instantiate the JAXB object and stuff it with the data from the venOrderItem object
		Order order = new Order();
		
		//OrderId
		MasterData orderId = new MasterData();
		orderId.setCode(wcsOrderId);
		orderId.setSource("Venice");
		orderId.setType("Order.orderId");
		order.setOrderId(orderId);
				
		//Order timestamp
		GregorianCalendar c = new GregorianCalendar();
		XMLGregorianCalendar xmlgc = null;
		try {
			xmlgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			_log.error("A DatatypeConfigurationException occured when creating a new XMLGregorianCalendar:" + e.getMessage());
			return null;
		}
		
		order.setTimestamp(xmlgc);
		
		Payment payment = new Payment();
		MasterData paymentId = new MasterData();
		paymentId.setType("Payment.paymentId");
		paymentId.setSource("Venice");
		paymentId.setCode(venOrderPayment.getWcsPaymentId());
		
		payment.setPaymentId(paymentId);
		payment.setTimestamp(xmlgc);
				
		payment.setPaymentStatus("PENDING");
		
		order.getPayments().add(payment);
		
		//Marshall the order object out to a stream
		ByteArrayOutputStream jaxbOutputStream = null;

		// Create a JAXBContext
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(com.gdn.integration.jaxb.Order.class);
		} catch (JAXBException e) {
			_log.error("A JXBException occured when creating the JAXBContext" + e.getMessage());
		}

		try {
			// Create a marshaller
			Marshaller m = jc.createMarshaller();

			// Create a new OutputStream with a decent buffer
			jaxbOutputStream = new ByteArrayOutputStream();
			
			// Marshal the JAXBElement object into stream.
			m.marshal(order, jaxbOutputStream); 
		} catch (JAXBException jaxbe) {
			_log.error("A JAXBException occured when marshalling the message:" + jaxbe.getMessage());
			return Boolean.FALSE;
		}
		String orderXML = jaxbOutputStream.toString();
		
		//Escape the string in prep to publish it
		String escapedOrderXML = StringEscapeUtils.escapeXML(orderXML);
		
		//Publish the message		
		TextMessage outboundMessage = new TextMessage();
		
		outboundMessage.setMsgType("updateOrderVAPaymentStatus");
		outboundMessage.setSourceAdapter("veniceAdapter");
		outboundMessage.setMsgPayload(escapedOrderXML);
		
		try {
			proxy.publish(outboundMessage);
		} catch (RemoteException e) {
			_log.error("An error occured when sending an outbound message" + e.getMessage());
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * @param venOrderPayment
	 * @return
	 */
	public Boolean publishRejectOrderCSPaymentStatus(String wcsOrderId, VenOrderPayment venOrderPayment){
		//Instantiate the JAXB object and stuff it with the data from the venOrderItem object
		Order order = new Order();
		
		//OrderId
		MasterData orderId = new MasterData();
		orderId.setCode(wcsOrderId);
		orderId.setSource("Venice");
		orderId.setType("Order.orderId");
		order.setOrderId(orderId);
				
		//Order timestamp
		GregorianCalendar c = new GregorianCalendar();
		XMLGregorianCalendar xmlgc = null;
		try {
			xmlgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			_log.error("A DatatypeConfigurationException occured when creating a new XMLGregorianCalendar:" + e.getMessage());
			return null;
		}
		
		order.setTimestamp(xmlgc);
		
		Payment payment = new Payment();
		MasterData paymentId = new MasterData();
		paymentId.setType("Payment.paymentId");
		paymentId.setSource("Venice");
		paymentId.setCode(venOrderPayment.getWcsPaymentId());
		
		payment.setPaymentId(paymentId);
		payment.setTimestamp(xmlgc);
				
		payment.setPaymentStatus("REJECTED");
		
		order.getPayments().add(payment);
		
		//Marshall the order object out to a stream
		ByteArrayOutputStream jaxbOutputStream = null;

		// Create a JAXBContext
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(com.gdn.integration.jaxb.Order.class);
		} catch (JAXBException e) {
			_log.error("A JXBException occured when creating the JAXBContext" + e.getMessage());
		}

		try {
			// Create a marshaller
			Marshaller m = jc.createMarshaller();

			// Create a new OutputStream with a decent buffer
			jaxbOutputStream = new ByteArrayOutputStream();
			
			// Marshal the JAXBElement object into stream.
			m.marshal(order, jaxbOutputStream); 
		} catch (JAXBException jaxbe) {
			_log.error("A JAXBException occured when marshalling the message:" + jaxbe.getMessage());
			return Boolean.FALSE;
		}
		String orderXML = jaxbOutputStream.toString();
		
		//Escape the string in prep to publish it
		String escapedOrderXML = StringEscapeUtils.escapeXML(orderXML);
		
		//Publish the message		
		TextMessage outboundMessage = new TextMessage();
		
		outboundMessage.setMsgType("updateOrderVAPaymentStatus");
		outboundMessage.setSourceAdapter("veniceAdapter");
		outboundMessage.setMsgPayload(escapedOrderXML);
		
		try {
			proxy.publish(outboundMessage);
		} catch (RemoteException e) {
			_log.error("An error occured when sending an outbound message" + e.getMessage());
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * Gets the port address for the publisher web service to call from the properties file.
	 * @return a string with the publish port address property
	 */
	private String getVenPublishPort_addressProperty(){
		String p = null;
	    try{
	          File f = new File(VENICE_PUBLISHER_PROPERTIES_FILE);
	          if(f.exists()){
	            Properties pro = new Properties();
	            FileInputStream in = new FileInputStream(f);
	            pro.load(in);
	            p = pro.getProperty("com.gdn.venice.publisher.PublisherPort_address");
	            _log.debug("com.gdn.venice.publisher.PublisherPort_address Property read:" + p);
	          }
	          else{
	            _log.error(VENICE_PUBLISHER_PROPERTIES_FILE + "File not found!");
	          }
	      }
	      catch(IOException e){
	      System.out.println(e.getMessage());
	      }
		return p;
	}
}

