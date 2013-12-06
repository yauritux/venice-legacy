package com.gdn.venice.integration.services;


//@RunWith(MockitoJUnitRunner.class)
public class VenInboundServiceSessionEJBBeanTest {
	
//	private Order order;
//	@Mock
//	private VenOrderDAO venOrderDAO;
//	@Mock
//	private Logger _log;
//	@Mock
//	private Log4jLoggerFactory loggerFactory;
//	
//	@InjectMocks
//	VenInboundServiceSessionEJBBean testClass;
//	
//	private String orderIdThatDoesNotExist = "12345";
//	private String orderIdWithSFStatus  = "12346";
//	private String orderIdWithFCStatus  = "12347";
//	
//	@Before
//	public void setup() throws Exception{
//		
//		//PowerMockito.whenNew(Log4jLoggerFactory.class).withNoArguments().thenReturn(loggerFactory);
//		
//		mockStatic(Log4jLoggerFactory.class);
//		Mockito.when(loggerFactory.getLog4JLogger(anyString())).thenReturn(_log);
//		
//		VenOrderStatus orderStatusSF = new VenOrderStatus();
//		orderStatusSF.setOrderStatusCode("SF");
//		orderStatusSF.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_SF);
//		
//		VenOrderStatus orderStatusFC = new VenOrderStatus();
//		orderStatusFC.setOrderStatusCode("FC");
//		orderStatusFC.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_FC);
//		
//		VenOrder orderWithSFStatus = new VenOrder();
//		orderWithSFStatus.setWcsOrderId(orderIdWithSFStatus);
//		orderWithSFStatus.setVenOrderStatus(orderStatusSF);
//		
//		VenOrder orderWithFCStatus = new VenOrder();
//		orderWithFCStatus.setWcsOrderId(orderIdWithFCStatus);
//		orderWithFCStatus.setVenOrderStatus(orderStatusFC);
//		
//		Mockito.when(venOrderDAO.findByWcsOrderId(orderIdThatDoesNotExist)).thenReturn(null);
//		Mockito.when(venOrderDAO.findByWcsOrderId(orderIdWithSFStatus)).thenReturn(orderWithSFStatus);
//		Mockito.when(venOrderDAO.findByWcsOrderId(orderIdWithFCStatus)).thenReturn(orderWithFCStatus);
//		
//	}
//	
//	@Test(expected = EJBException.class)
//	public void updateOrder_invokeWithOrderIdNull_throwsEJBException() {
//		order = mock(Order.class);
//		
//		testClass.updateOrder(order);
//	}
//
//	@Test(expected = EJBException.class)
//	public void updateOrder_invokeWithOrderId_WithOrderTimestampNull_throwsEJBException() {
//		
//		order = mock(Order.class);
//		stub(order.getOrderId()).toReturn(new MasterData());
//		
//		testClass.updateOrder(order);
//	}
//	
//	@Test(expected = EJBException.class)
//	public void updateOrder_invokeWithOrderId_WithOrderTimestamp_WithOrderStatusNull_throwsEJBException() {
//		
//		order = mock(Order.class);
//		stub(order.getOrderId()).toReturn(new MasterData());
//		stub(order.getTimestamp()).toReturn(createOrderTimestamp());
//		
//		testClass.updateOrder(order);
//	}
//	
//	@Test(expected = EJBException.class)
//	public void updateOrder_invokeWithOrderId_WithOrderTimestamp_WithOrderStatus_WithFullfillmentStatusNull_throwsEJBException() {
//		
//		order = mock(Order.class);
//		stub(order.getOrderId()).toReturn(new MasterData());
//		stub(order.getTimestamp()).toReturn(createOrderTimestamp());
//		stub(order.getStatus()).toReturn("C");
//		
//		testClass.updateOrder(order);
//	}
//	
//	@Test(expected = EJBException.class)
//	public void updateOrder_invokeWithOrderNotExist_throwsEJBException(){
//		
//		MasterData masterData = new MasterData();
//		masterData.setCode(orderIdThatDoesNotExist);
//		
//		order = mock(Order.class);
//		stub(order.getOrderId()).toReturn(masterData);
//		stub(order.getTimestamp()).toReturn(createOrderTimestamp());
//		stub(order.getStatus()).toReturn("C");
//		stub(order.getFullfillmentStatus()).toReturn(1);
//		
//		testClass.updateOrder(order);
//	}
//	
//	@Test
//	public void updateOrder_invokeWithOrderStatusFC_returnsFalse(){
//		
//		MasterData masterData = new MasterData();
//		masterData.setCode(orderIdWithFCStatus);
//		
//		order = mock(Order.class);
//		stub(order.getOrderId()).toReturn(masterData);
//		stub(order.getTimestamp()).toReturn(createOrderTimestamp());
//		stub(order.getStatus()).toReturn("C");
//		stub(order.getFullfillmentStatus()).toReturn(1);
//		
//		assertEquals(Boolean.FALSE, testClass.updateOrder(order));
//	}
//	
//	@Test
//	public void updateOrder_invokeWithOrderStatusSF_returnsFalse(){
//		
//		MasterData masterData = new MasterData();
//		masterData.setCode(orderIdWithSFStatus);
//		
//		order = mock(Order.class);
//		stub(order.getOrderId()).toReturn(masterData);
//		stub(order.getTimestamp()).toReturn(createOrderTimestamp());
//		stub(order.getStatus()).toReturn("C");
//		stub(order.getFullfillmentStatus()).toReturn(1);
//		
//		assertEquals(Boolean.FALSE, testClass.updateOrder(order));
//	}
//	
//	@After
//	public void shutdown(){
//		order = null;
//		Mockito.reset(venOrderDAO);
//	}
//	
//	private XMLGregorianCalendar createOrderTimestamp(){
//		GregorianCalendar gregory = new GregorianCalendar();
//		gregory.setTime(new Date());
//		XMLGregorianCalendar calendar = null;
//		try {
//			calendar = DatatypeFactory.newInstance()
//			        .newXMLGregorianCalendar(
//			            gregory);
//		} catch (DatatypeConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return calendar;
//	}
	
}
