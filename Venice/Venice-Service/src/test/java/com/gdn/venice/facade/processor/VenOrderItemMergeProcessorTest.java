package com.gdn.venice.facade.processor;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gdn.venice.dao.VenOrderItemDAO;
import com.gdn.venice.facade.spring.LogAirwayBillService;
import com.gdn.venice.facade.spring.PublisherService;
import com.gdn.venice.facade.spring.VenOrderItemStatusHistoryService;
import com.gdn.venice.factory.VenOrderStatusC;
import com.gdn.venice.factory.VenOrderStatusCX;
import com.gdn.venice.factory.VenOrderStatusD;
import com.gdn.venice.factory.VenOrderStatusES;
import com.gdn.venice.factory.VenOrderStatusFP;
import com.gdn.venice.factory.VenOrderStatusPF;
import com.gdn.venice.factory.VenOrderStatusPP;
import com.gdn.venice.factory.VenOrderStatusPU;
import com.gdn.venice.factory.VenOrderStatusRT;
import com.gdn.venice.factory.VenOrderStatusX;
import com.gdn.venice.persistence.VenOrderItem;

@RunWith(MockitoJUnitRunner.class)
public class VenOrderItemMergeProcessorTest {
	// SUT
	private VenOrderItemMergeProcessor processor; 
	@Mock
	private VenOrderItemDAO venOrderItemDAO;
	@Mock
	private VenOrderItemStatusHistoryService venOrderItemStatusHistoryService;
	@Mock
	private LogAirwayBillService logAirwayBillService;
	@Mock
	private PublisherService publisherService;

	private VenOrderItem orderItemWithStatusFP;
	private VenOrderItem orderItemWithStatusPF;
	private VenOrderItem orderItemWithStatusPU;
	private VenOrderItem orderItemWithStatusES;
	private VenOrderItem orderItemWithStatusCX;
	private VenOrderItem orderItemWithStatusD;
	private VenOrderItem orderItemWithStatusPP;
	private VenOrderItem orderItemWithStatusRT;
	private VenOrderItem orderItemWithStatusX;
	private VenOrderItem orderItemWithStatusC;
	
	@Before
	public void setup(){
		
		processor = new VenOrderItemMergeProcessor(); // SUT
		
		processor.venOrderItemDAO = venOrderItemDAO;
		processor.venOrderItemStatusHistoryService = venOrderItemStatusHistoryService;
		processor.logAirwayBillService = logAirwayBillService;
		processor.publisherService = publisherService;
		
		orderItemWithStatusC = new VenOrderItem();
		orderItemWithStatusC.setVenOrderStatus(VenOrderStatusC.createVenOrderStatus());
		
		orderItemWithStatusFP = new VenOrderItem();
		orderItemWithStatusFP.setVenOrderStatus(VenOrderStatusFP.createVenOrderStatus());
		
		orderItemWithStatusPF = new VenOrderItem();
		orderItemWithStatusPF.setVenOrderStatus(VenOrderStatusPF.createVenOrderStatus());
		
		orderItemWithStatusPU = new VenOrderItem();
		orderItemWithStatusPU.setVenOrderStatus(VenOrderStatusPU.createVenOrderStatus());

		orderItemWithStatusES = new VenOrderItem();
		orderItemWithStatusES.setVenOrderStatus(VenOrderStatusES.createVenOrderStatus());

		orderItemWithStatusPP = new VenOrderItem();
		orderItemWithStatusPP.setVenOrderStatus(VenOrderStatusPP.createVenOrderStatus());
		
		orderItemWithStatusCX = new VenOrderItem();
		orderItemWithStatusCX.setVenOrderStatus(VenOrderStatusCX.createVenOrderStatus());
		
		orderItemWithStatusRT = new VenOrderItem();
		orderItemWithStatusRT.setVenOrderStatus(VenOrderStatusRT.createVenOrderStatus());
		
		orderItemWithStatusD = new VenOrderItem();
		orderItemWithStatusD.setVenOrderStatus(VenOrderStatusD.createVenOrderStatus());
		
		orderItemWithStatusX = new VenOrderItem();
		orderItemWithStatusX.setVenOrderStatus(VenOrderStatusX.createVenOrderStatus());
		
	}
	
	private void commonPreMergeTest(VenOrderItem existingOrderItem, VenOrderItem newOrderItem){
		when(venOrderItemDAO
				.findWithVenOrderStatusAndLogAirwayBillByVenOrderItem(newOrderItem))
					.thenReturn(existingOrderItem);
		
		processor.preMerge(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoES_DOESNOTPublishUpdateOrderItemStatusMessage(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusES;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(publisherService, never()).publishUpdateOrderItemStatus(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoES_DOESNOTAddOrderItemStatusHistory(){
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusES;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(venOrderItemStatusHistoryService, never()).saveVenOrderItemStatusHistory(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoES_DOESNOTAddDummyLogAirwayBill(){
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusES;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(logAirwayBillService, never()).addDummyLogAirwayBillForNewlyFPOrderItem(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromCtoFP_DOESNOTPublishUpdateOrderItemStatusMessage(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusC;
		VenOrderItem newOrderItem = orderItemWithStatusFP;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(publisherService, never()).publishUpdateOrderItemStatus(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromCtoFP_DOESNOTAddOrderItemStatusHistory(){
		VenOrderItem existingOrderItem = orderItemWithStatusC;
		VenOrderItem newOrderItem = orderItemWithStatusFP;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(venOrderItemStatusHistoryService, never()).saveVenOrderItemStatusHistory(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromCtoFP_addDummyLogAirwayBill(){
		VenOrderItem existingOrderItem = orderItemWithStatusC;
		VenOrderItem newOrderItem = orderItemWithStatusFP;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(logAirwayBillService).addDummyLogAirwayBillForNewlyFPOrderItem(existingOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromPUtoES_publishUpdateOrderItemStatusMessage(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusPU;
		VenOrderItem newOrderItem = orderItemWithStatusES;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(publisherService).publishUpdateOrderItemStatus(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromPUtoES_addOrderItemStatusHistory(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusPU;
		VenOrderItem newOrderItem = orderItemWithStatusES;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(venOrderItemStatusHistoryService).saveVenOrderItemStatusHistory(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromPUtoES_DOESNOTAddDummyLogAirwayBill(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusPU;
		VenOrderItem newOrderItem = orderItemWithStatusES;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(logAirwayBillService, never()).addDummyLogAirwayBillForNewlyFPOrderItem(existingOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoPP_publishUpdateOrderItemStatusMessage(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusPP;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(publisherService).publishUpdateOrderItemStatus(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoPP_addOrderItemStatusHistory(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusPP;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(venOrderItemStatusHistoryService).saveVenOrderItemStatusHistory(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoPP_DOESNOTAddDummyLogAirwayBill(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusPP;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(logAirwayBillService, never()).addDummyLogAirwayBillForNewlyFPOrderItem(existingOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoCX_publishUpdateOrderItemStatusMessage(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusCX;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(publisherService).publishUpdateOrderItemStatus(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoCX_addOrderItemStatusHistory(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusCX;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(venOrderItemStatusHistoryService).saveVenOrderItemStatusHistory(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoCX_DOESNOTAddDummyLogAirwayBill(){
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusCX;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(logAirwayBillService, never()).addDummyLogAirwayBillForNewlyFPOrderItem(existingOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromPPtoCX_publishUpdateOrderItemStatusMessage(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusPP;
		VenOrderItem newOrderItem = orderItemWithStatusCX;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(publisherService).publishUpdateOrderItemStatus(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromPPtoCX_addOrderItemStatusHistory(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusPP;
		VenOrderItem newOrderItem = orderItemWithStatusCX;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(venOrderItemStatusHistoryService).saveVenOrderItemStatusHistory(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromPPtoCX_DOESNOTAddDummyLogAirwayBill(){
		VenOrderItem existingOrderItem = orderItemWithStatusPP;
		VenOrderItem newOrderItem = orderItemWithStatusCX;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(logAirwayBillService, never()).addDummyLogAirwayBillForNewlyFPOrderItem(existingOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoRT_publishUpdateOrderItemStatusMessage(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusRT;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(publisherService).publishUpdateOrderItemStatus(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoRT_addOrderItemStatusHistory(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusRT;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(venOrderItemStatusHistoryService).saveVenOrderItemStatusHistory(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoRT_DOESNOTAddDummyLogAirwayBill(){
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusRT;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(logAirwayBillService, never()).addDummyLogAirwayBillForNewlyFPOrderItem(existingOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoD_publishUpdateOrderItemStatusMessage(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusD;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(publisherService).publishUpdateOrderItemStatus(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoD_addOrderItemStatusHistory(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusD;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(venOrderItemStatusHistoryService).saveVenOrderItemStatusHistory(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromEStoD_DOESNOTAddDummyLogAirwayBill(){
		VenOrderItem existingOrderItem = orderItemWithStatusES;
		VenOrderItem newOrderItem = orderItemWithStatusD;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(logAirwayBillService, never()).addDummyLogAirwayBillForNewlyFPOrderItem(existingOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromCXtoD_publishUpdateOrderItemStatusMessage(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusCX;
		VenOrderItem newOrderItem = orderItemWithStatusD;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(publisherService).publishUpdateOrderItemStatus(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromCXtoD_addOrderItemStatusHistory(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusCX;
		VenOrderItem newOrderItem = orderItemWithStatusD;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(venOrderItemStatusHistoryService).saveVenOrderItemStatusHistory(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromCXtoD_DOESNOTAddDummyLogAirwayBill(){
		VenOrderItem existingOrderItem = orderItemWithStatusCX;
		VenOrderItem newOrderItem = orderItemWithStatusD;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(logAirwayBillService, never()).addDummyLogAirwayBillForNewlyFPOrderItem(existingOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromPFtoFP_publishUpdateOrderItemStatusMessage(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusPF;
		VenOrderItem newOrderItem = orderItemWithStatusFP;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(publisherService).publishUpdateOrderItemStatus(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromPFtoFP_DOESNOTAddOrderItemStatusHistory(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusPF;
		VenOrderItem newOrderItem = orderItemWithStatusFP;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(venOrderItemStatusHistoryService, never()).saveVenOrderItemStatusHistory(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromPFtoFP_DOESNOTAddDummyLogAirwayBill(){
		VenOrderItem existingOrderItem = orderItemWithStatusPF;
		VenOrderItem newOrderItem = orderItemWithStatusFP;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(logAirwayBillService, never()).addDummyLogAirwayBillForNewlyFPOrderItem(existingOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromFPtoX_DOESNOTPublishUpdateOrderItemStatusMessage(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusFP;
		VenOrderItem newOrderItem = orderItemWithStatusX;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(publisherService, never()).publishUpdateOrderItemStatus(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromFPtoX_addOrderItemStatusHistory(){
		
		VenOrderItem existingOrderItem = orderItemWithStatusFP;
		VenOrderItem newOrderItem = orderItemWithStatusX;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(venOrderItemStatusHistoryService).saveVenOrderItemStatusHistory(newOrderItem);
	}
	
	@Test
	public void preMerge_orderItemStatusChangeFromFPtoX_DOESNOTAddDummyLogAirwayBill(){
		VenOrderItem existingOrderItem = orderItemWithStatusFP;
		VenOrderItem newOrderItem = orderItemWithStatusX;
		
		commonPreMergeTest(existingOrderItem, newOrderItem);
		
		verify(logAirwayBillService, never()).addDummyLogAirwayBillForNewlyFPOrderItem(existingOrderItem);
	}
	
}
