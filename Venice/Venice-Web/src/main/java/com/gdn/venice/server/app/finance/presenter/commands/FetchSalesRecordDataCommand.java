package com.gdn.venice.server.app.finance.presenter.commands;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.FinSalesRecordSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchSalesRecordDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public FetchSalesRecordDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();		
		JPQLAdvancedQueryCriteria criteriaAndTaskCriteria = null;
						
		
		Locator<Object> locator = null;
		
		//check for taskid parameter, it shall be there if this screen is called from ToDoList for approval purpose
		if (request.getParams()!=null && request.getParams().get(DataNameTokens.TASKID)!=null) {
			String taskId = request.getParams().get(DataNameTokens.TASKID);
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
			
			bpmAdapter.synchronize();
			HashMap<String,String> wcsOrderIds = bpmAdapter.getExternalDataVariableAsHashMap(new Long(taskId), ProcessNameTokens.WCSORDERID);
		
			criteriaAndTaskCriteria = new JPQLAdvancedQueryCriteria("and");
			if (criteria!=null) {
				criteriaAndTaskCriteria.add(criteria);
			}	
			request.setCriteria(criteriaAndTaskCriteria);
						
			/*
			 * Build a new simple criteria as an IN() list
			 */
			JPQLSimpleQueryCriteria inCriteria = new JPQLSimpleQueryCriteria();
			inCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_WCSORDERID));
			inCriteria.setFieldName(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_WCSORDERID);
			
			String wcsOrderIdList = "";
			for(String value:wcsOrderIds.values()){
				if(wcsOrderIdList.isEmpty()){
					wcsOrderIdList = value;
				}
				else{
					wcsOrderIdList = wcsOrderIdList + "," + value;
				}
			}
			inCriteria.setValue(wcsOrderIdList);
			inCriteria.setOperator("IN");
			
			criteriaAndTaskCriteria.add(inCriteria);
		}
				
		//Check for journal Group Id, it will be there if the screen is called from Journal Screen
		String journalGroupId = null;
		if (request.getParams()!=null && request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID)!=null) {
			journalGroupId = request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
		}		
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		try {
			locator = new Locator<Object>();
			FinSalesRecordSessionEJBRemote sessionHome = (FinSalesRecordSessionEJBRemote) locator
			.lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");
			
			List<FinSalesRecord> finSalesRecordList = null;
			
			//Check for journal Group Id, it will be there if the screen is called from Journal Screen
			//This section queries for finJournalTransactions that are part of the finJournalApprovalGroup
			if (journalGroupId!=null) {				
				String select = "select distinct o from FinSalesRecord o join fetch o.finJournalTransactions t where t.finJournalApprovalGroup.journalGroupId = " + journalGroupId;
				finSalesRecordList = sessionHome.queryByRange(select, 0, 50);

			} else {		
				if ((criteria == null && criteriaAndTaskCriteria == null)
						|| (criteria!=null && !criteria.getListIterator().hasNext())
						|| (criteriaAndTaskCriteria!=null && !criteriaAndTaskCriteria.getListIterator().hasNext())) {
					String select = "select o from FinSalesRecord o left join o.venOrderItem.venOrder.venOrderPaymentAllocations p";

					finSalesRecordList = sessionHome.queryByRange(select, 0, 50);				
				} else {
					FinSalesRecord salesRecord = new FinSalesRecord();
					if (criteriaAndTaskCriteria!=null) {
						finSalesRecordList = sessionHome.findByFinSalesRecordLike(salesRecord, criteriaAndTaskCriteria, 0, 0);
					} else {
						finSalesRecordList = sessionHome.findByFinSalesRecordLike(salesRecord, criteria, 0, 0);
					}
				}		
			}
			
			for (int i=0;i<finSalesRecordList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();

				FinSalesRecord salesRecord = finSalesRecordList.get(i);
								
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				map.put(DataNameTokens.FINSALESRECORD_SALESRECORDID, (salesRecord.getSalesRecordId()!=null)? salesRecord.getSalesRecordId().toString():"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_ORDERDATE, (salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getVenOrder()!=null && salesRecord.getVenOrderItem().getVenOrder().getOrderTimestamp()!=null)?formatter.format(new Timestamp(salesRecord.getVenOrderItem().getVenOrder().getOrderDate().getTime()-25200000)):"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_WCSORDERID, (salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getVenOrder()!=null)? salesRecord.getVenOrderItem().getVenOrder().getWcsOrderId():"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_WCSMERCHANTID, (salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!=null)? salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getWcsMerchantId():"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, (salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!=null)? salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID, (salesRecord.getVenOrderItem()!=null)? salesRecord.getVenOrderItem().getWcsOrderItemId():"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, (salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct()!=null)? salesRecord.getVenOrderItem().getVenMerchantProduct().getWcsProductName():"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_QUANTITY,  (salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getQuantity()!=null)? salesRecord.getVenOrderItem().getQuantity().toString():"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_PRICE, (salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getPrice()!=null)? salesRecord.getVenOrderItem().getPrice().toString():"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_TOTAL, (salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getTotal()!=null)? salesRecord.getVenOrderItem().getTotal().toString():"");
				map.put(DataNameTokens.FINSALESRECORD_GDNCOMMISIONAMOUNT, (salesRecord.getGdnCommissionAmount()!=null)? salesRecord.getGdnCommissionAmount().toString():"");
				map.put(DataNameTokens.FINSALESRECORD_GDNTRANSACTIONFEEAMOUNT, (salesRecord.getGdnTransactionFeeAmount()!=null)? salesRecord.getGdnTransactionFeeAmount().toString():"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_PROVIDER_CODE, (salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getLogLogisticService() != null && salesRecord.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider() != null)? salesRecord.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getLogisticsProviderCode():"");	
				map.put(DataNameTokens.FINSALESRECORD_PAYMENT_STATUS, salesRecord.getPaymentStatus() != null?salesRecord.getPaymentStatus():"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_PPH23_FLAG, salesRecord.getVenOrderItem().getVenSettlementRecords()!=null? (salesRecord.getVenOrderItem().getVenSettlementRecords().get(0).getPph23()==true?"Yes":"No"):"");
				map.put(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT, salesRecord.getPph23Amount() != null?salesRecord.getPph23Amount().toString():"");
				map.put(DataNameTokens.FINSALESRECORD_RECONCILEDATE, (salesRecord.getReconcileDate() != null)?formatter.format(new Timestamp(salesRecord.getReconcileDate().getTime()-25200000)):"");
				map.put(DataNameTokens.FINSALESRECORD_MCX_DATE, formatter.format(salesRecord.getCxMtaDate()));
				map.put(DataNameTokens.FINSALESRECORD_CXF_DATE, formatter.format(salesRecord.getCxFinanceDate()));
	
				BigDecimal shippingCost = new BigDecimal(0);
				BigDecimal insuranceCost = new BigDecimal(0);
				BigDecimal shippingAndInsuranceCost = new BigDecimal(0);				
					
				if(salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getShippingCost()!=null){
					shippingCost =  salesRecord.getVenOrderItem().getShippingCost();
				}
				
				if(salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getInsuranceCost()!=null){
					insuranceCost = salesRecord.getVenOrderItem().getInsuranceCost();
				}
				
				shippingAndInsuranceCost = shippingCost.add(insuranceCost);
				
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_SHIPPINGCOST, (!shippingCost.toString().equals(0)?shippingCost.toString():""));
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_INSURANCECOST,(!insuranceCost.toString().equals(0)?insuranceCost.toString():""));
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_SHIPPINGCOST_INSURANCECOST,(!shippingAndInsuranceCost.toString().equals(0)?shippingAndInsuranceCost.toString():""));
				map.put(DataNameTokens.FINSALESRECORD_GDNHANDLINGFEEAMOUNT, (salesRecord.getGdnHandlingFeeAmount()!=null)? salesRecord.getGdnHandlingFeeAmount().toString():"");
				
				String commissionType = (salesRecord.getVenOrderItem().getVenSettlementRecords()!=null && salesRecord.getVenOrderItem().getVenSettlementRecords().size() > 0)?salesRecord.getVenOrderItem().getVenSettlementRecords().get(0).getCommissionType():"";
				if(commissionType.equals("CM")){
					commissionType="Commission";
				}else if(commissionType.equals("RB")){
					commissionType="Rebate";
				}else if(commissionType.equals("TD")){
					commissionType="Trading";
				}else if(commissionType.equals("MP")){
					commissionType="Trading MP";
				}else if(commissionType.equals("KS")){
					commissionType="Consignment";
				}else{
					commissionType="";
				}
				
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_COMMISIONTYPE, commissionType);
				
				//get payment data
				VenOrderPaymentAllocationSessionEJBRemote paymentSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
				String paymentQuery = "select o from VenOrderPaymentAllocation o where o.venOrder.orderId = " + salesRecord.getVenOrderItem().getVenOrder().getOrderId();
				
				List<VenOrderPaymentAllocation> paymentList = paymentSessionHome.queryByRange(paymentQuery, 0, 1);
				String paymentType="";
				if(paymentList.size()>0){
					paymentType = paymentList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode();
				}

				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_PAYMENTTYPECODE, paymentType);
				
				//get adjustment data
				VenOrderItemAdjustmentSessionEJBRemote adjustmentSessionHome = (VenOrderItemAdjustmentSessionEJBRemote) locator.lookup(VenOrderItemAdjustmentSessionEJBRemote.class, "VenOrderItemAdjustmentSessionEJBBean");
				String adjustmentQuery = "select o from VenOrderItemAdjustment o where o.venOrderItem.orderItemId = " + salesRecord.getVenOrderItem().getOrderItemId();
				
				List<VenOrderItemAdjustment> orderItemAdjustmentList = adjustmentSessionHome.queryByRange(adjustmentQuery, 0, 0);

				BigDecimal orderItemAdjustment = new BigDecimal(0);	
				StringBuilder orderItemAdjustmentDetail = new StringBuilder();	
				if (salesRecord.getVenOrderItem()!=null && orderItemAdjustmentList!=null) {
					for (int j=0;j<orderItemAdjustmentList.size();j++) {
						orderItemAdjustment = orderItemAdjustment.add(orderItemAdjustmentList.get(j).getAmount());
						orderItemAdjustmentDetail = orderItemAdjustmentDetail.append(orderItemAdjustmentList.get(j).getVenPromotion().getPromotionName());
						orderItemAdjustmentDetail = orderItemAdjustmentDetail.append(", Rp ");
						orderItemAdjustmentDetail = orderItemAdjustmentDetail.append(orderItemAdjustmentList.get(j).getAmount());
						if (j < orderItemAdjustmentList.size()-1){
							orderItemAdjustmentDetail = orderItemAdjustmentDetail.append(", ");
						}
					}
				}
				
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_AMOUNT,orderItemAdjustment.toString());
				map.put(DataNameTokens.FINSALESRECORD_GDNGIFTWRAPCHARGEAMOUNT, (salesRecord.getGdnGiftWrapChargeAmount()!=null)? salesRecord.getGdnGiftWrapChargeAmount().toString():"");
				map.put(DataNameTokens.FINSALESRECORD_CUSTOMERDOWNPAYMENT, (salesRecord.getCustomerDownpayment()!=null)? salesRecord.getCustomerDownpayment().toString():"");
				map.put(DataNameTokens.FINSALESRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC, (salesRecord.getFinApprovalStatus()!=null)? salesRecord.getFinApprovalStatus().getApprovalStatusDesc():"");
				map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_DETAIL,orderItemAdjustmentDetail.toString());

				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if (locator!=null) {
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}	
}
