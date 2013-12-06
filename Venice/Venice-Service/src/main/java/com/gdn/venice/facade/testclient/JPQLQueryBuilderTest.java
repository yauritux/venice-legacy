package com.gdn.venice.facade.testclient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLQueryStringBuilder;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.persistence.VenPartyAddress;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.facade.VenPartyAddressSessionEJBRemote;
import com.gdn.venice.facade.finder.FinderReturn;

public class JPQLQueryBuilderTest {

	/**
	 * For the sake of testing the scenarios for this component
	 * 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		HashMap<String, String> ctb = new HashMap<String, String>();

		VenOrder order = new VenOrder();
		VenOrderStatus orderStatus = new VenOrderStatus();

		orderStatus.setOrderStatusId(new Long(1));

		order.setVenOrderStatus(orderStatus);
		order.setRmaAction("test%");

		ctb.put("venOrderStatus", "venOrderStatus");
		JPQLQueryStringBuilder<VenOrder> q = new JPQLQueryStringBuilder<VenOrder>(order);

		JPQLSimpleQueryCriteria criteria1 = new JPQLSimpleQueryCriteria();
		criteria1.setFieldName("VenOrder.venOrderStatus.orderStatusId");
		criteria1.setFieldClass("java.lang.Long");
		criteria1.setValue("4");
		criteria1.setOperator("equals");
		
		JPQLSimpleQueryCriteria criteria2 = new JPQLSimpleQueryCriteria();
		criteria2.setFieldName("VenOrder.fulfillmentStatus");
		criteria2.setFieldClass("java.lang.Long");
		criteria2.setValue("0");
		criteria2.setOperator("equals");
		
		JPQLSimpleQueryCriteria criteria3 = new JPQLSimpleQueryCriteria();
		criteria3.setFieldName("VenOrder.orderTimestamp");
		criteria3.setFieldClass("java.sql.Timestamp");
		criteria3.setValue(new Long(System.currentTimeMillis()).toString());
		criteria3.setOperator("less");
		
		JPQLSimpleQueryCriteria criteria4 = new JPQLSimpleQueryCriteria();
		criteria4.setFieldName("VenOrder.rmaFlag");
		criteria4.setFieldClass("java.lang.Boolean");
		criteria4.setValue(new Boolean(false).toString());
		criteria4.setOperator("equals");
		
		JPQLAdvancedQueryCriteria advancedQueryCriteria = new JPQLAdvancedQueryCriteria();
		advancedQueryCriteria.add(criteria1);
		advancedQueryCriteria.add(criteria2);
		advancedQueryCriteria.add(criteria3);
		advancedQueryCriteria.add(criteria4);
		advancedQueryCriteria.setBooleanOperator("and");
		
		System.out.println(q.buildQueryString(null, advancedQueryCriteria));
		
		VenOrder venOrder = new VenOrder();
		
		try {
			@SuppressWarnings("rawtypes")
			Locator locator = new Locator<Object>();
			VenOrderSessionEJBRemote orderHome = (VenOrderSessionEJBRemote) locator
			.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			System.out.println("Testing List return based method...");
			List<VenOrder> orderList = orderHome.findByVenOrderLike(venOrder, advancedQueryCriteria, 0, 100);
			
			Iterator<VenOrder> orderListIterator = orderList.iterator();
			while(orderListIterator.hasNext()){
				VenOrder nextOrder = orderListIterator.next();
				System.out.println("Order wcsOrderId:" + nextOrder.getWcsOrderId());
			}
			
			System.out.println("Testing FinderReturn based method...");
			FinderReturn fr = orderHome.findByVenOrderLikeFR(venOrder, advancedQueryCriteria, 0, 0);
			
			System.out.println("Total rows returned by query:" + fr.getNumQUeryRows());
			orderListIterator = (Iterator<VenOrder>) fr.getResultList().iterator();
			while(orderListIterator.hasNext()){
				VenOrder nextOrder = orderListIterator.next();
				System.out.println("Order wcsOrderId:" + nextOrder.getWcsOrderId());
			}
			locator.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		criteria1.setFieldName("VenPartyAddress.venAddress.venCity.cityCode");
		criteria1.setFieldClass("java.lang.String");
		criteria1.setValue("JKT");
		criteria1.setOperator("equals");

		criteria2.setFieldName("VenPartyAddress.venAddressType.addressTypeDesc");
		criteria2.setFieldClass("java.lang.String");
		criteria2.setValue("Def");
		criteria2.setOperator("iContains");

		advancedQueryCriteria = new JPQLAdvancedQueryCriteria();
		advancedQueryCriteria.add(criteria1);
		advancedQueryCriteria.add(criteria2);
		advancedQueryCriteria.setBooleanOperator("and");

		try {
			@SuppressWarnings("rawtypes")
			Locator locator = new Locator<Object>();
			VenPartyAddressSessionEJBRemote partyAddressHome = (VenPartyAddressSessionEJBRemote) locator
			.lookup(VenPartyAddressSessionEJBRemote.class, "VenPartyAddressSessionEJBBean");
			VenPartyAddress venPartyAddress = new VenPartyAddress();
			
			List<VenPartyAddress> partyAddressList = partyAddressHome.findByVenPartyAddressLike(venPartyAddress, advancedQueryCriteria, 0, 100);
			
			Iterator<VenPartyAddress> partyAddressListIterator = partyAddressList.iterator();
			while(partyAddressListIterator.hasNext()){
				VenPartyAddress nextPartyAddress = partyAddressListIterator.next();
				System.out.println("VenPartyAddress.venAddress.streetAddress1:" + nextPartyAddress.getVenAddress().getStreetAddress1());
			}
			locator.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
