package com.gdn.venice.inbound.services;

import static org.mockito.Mockito.stub;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.gdn.integration.jaxb.Customer;
import com.gdn.integration.jaxb.MasterData;
import com.gdn.integration.jaxb.Order;
import com.gdn.integration.jaxb.OrderItem;
import com.gdn.integration.jaxb.Payment;
import com.gdn.venice.constants.OrderConstants;
import com.gdn.venice.exception.InvalidOrderException;
import com.gdn.venice.exception.InvalidOrderFulfillmentStatusException;
import com.gdn.venice.exception.InvalidOrderStatusException;
import com.gdn.venice.exception.InvalidOrderTimestampException;

/**
 * 
 * @author yauritux
 *
 */
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:OrderServiceTest-context.xml"})
public class OrderCreationTest {
	
	/*
	static {
		ClassPathXmlApplicationContext appContext
		    =new ClassPathXmlApplicationContext(
		    		new String[]{"classpath:spring/application-context.xml","classpath:spring/model-context.xml"}
		     );		
	}
	*/
	
	@Autowired
	@InjectMocks
	OrderService orderService;
	
	@Mock
	private Order order;
	
	@Mock
	private List<OrderItem> items;
	
	@Mock
	private List<Payment> payments;
	
	@Before	
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		stub(order.getAmount()).toReturn(new Double("100000"));
		stub(order.isRmaFlag()).toReturn(false); // Not an "Order Returned" 
		stub(order.getCustomer()).toReturn(new Customer());		
		stub(order.getOrderId()).toReturn(new MasterData());
		stub(order.getTimestamp()).toReturn(createOrderTimestamp());
		stub(order.getStatus()).toReturn("C");
		stub(order.getOrderItems()).toReturn(items);
		stub(items.isEmpty()).toReturn(false);
		stub(order.getPayments()).toReturn(payments);
		stub(payments.isEmpty()).toReturn(false);
	}

	@Test(expected = InvalidOrderException.class, timeout = 1000)
	public void createOrder_NoOrderReceived_VENEX000002Caught() throws Exception {
		orderService.createOrder(null);
	}
	
	@Test(expected = InvalidOrderException.class, timeout = 1000)
	public void createOrder_NoOrderAmount_VENEX000003Caught() throws Exception {		
		stub(order.getAmount()).toReturn(null);
		orderService.createOrder(order);
	}
	
	@Test(expected = InvalidOrderException.class, timeout = 1000)
	public void createOrder_NoCustomerRecord_VENEX000004Caught() throws Exception {
		stub(order.getCustomer()).toReturn(null);
		orderService.createOrder(order);
	}	
	
	@Test(expected = InvalidOrderException.class, timeout = 1000)
	public void createOrder_NoOrderID_VENEX000005Caught() throws Exception {
		stub(order.getOrderId()).toReturn(null);
		orderService.createOrder(order);
	}	
	
	@Test(expected = InvalidOrderException.class, timeout = 1000)
	public void createOrder_OrderItemsNull_VENEX000006Caught() throws Exception {
		stub(order.getOrderItems()).toReturn(null);
		orderService.createOrder(order);
	}
	
	@Test(expected = InvalidOrderException.class, timeout = 1000)
	public void createOrder_OrderItemsEmpty_VENEX000006Caught() throws Exception {
		stub(items.isEmpty()).toReturn(true);
		orderService.createOrder(order);
	}
	
	@Test(expected = InvalidOrderException.class, timeout = 1000)
	public void createOrder_NullPayment_VENEX000007Caught() throws Exception {
		stub(payments.isEmpty()).toReturn(true);
		orderService.createOrder(order);
	}
	
	@Test(expected = InvalidOrderTimestampException.class, timeout = 1000) 
	public void createOrder_NoTimestamp_VENEX000008Caught() throws Exception {
		stub(order.getTimestamp()).toReturn(null);
		orderService.createOrder(order);
	}
	
	@Test(expected = InvalidOrderStatusException.class, timeout = 1000)
	public void createOrder_NoOrderStatus_VENEX000009Caught() throws Exception {
		stub(order.getStatus()).toReturn(null);
		orderService.createOrder(order);
	}
	
	@Test(expected = InvalidOrderStatusException.class, timeout = 1000)
	public void createOrder_InvalidOrderStatus_VENEX000009Caught() throws Exception {
		stub(order.getStatus()).toReturn("F");
		orderService.createOrder(order);
	}
	
	@Test(expected = InvalidOrderFulfillmentStatusException.class, timeout = 1000) 
	public void createOrder_InvalidOrderFulfillmentStatus_VENEX000010Caught() throws Exception {
		stub(order.getFullfillmentStatus()).toReturn(OrderConstants.VEN_FULFILLMENT_STATUS_ONE.code());
		orderService.createOrder(order);
	}
	
	@After
	public void shutdown() {
		order = null;
		items = null;
		payments = null;
	}
	
	private XMLGregorianCalendar createOrderTimestamp(){
        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(new Date());
        XMLGregorianCalendar calendar = null;
        try {
                calendar = DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(
                            gregory);
        } catch (DatatypeConfigurationException e) {
                e.printStackTrace();
        }
        
        return calendar;
	}
}
