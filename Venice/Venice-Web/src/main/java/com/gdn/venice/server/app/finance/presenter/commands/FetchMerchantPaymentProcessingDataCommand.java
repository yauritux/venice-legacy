package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinSalesRecordSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.util.VeniceConstants;

public class FetchMerchantPaymentProcessingDataCommand implements RafDsCommand {
	RafDsRequest request;
	protected static Logger _log = null;
	
	public FetchMerchantPaymentProcessingDataCommand(RafDsRequest request) {
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.finance.presenter.commands.FetchMerchantPaymentProcessingDataCommand");
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();		
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			FinSalesRecordSessionEJBRemote finSalesRecordSessionHome = (FinSalesRecordSessionEJBRemote) locator
			.lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");
						
			VenOrderItemStatusHistorySessionEJBRemote venOrderItemStatusHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) locator
			.lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");
						
			List<FinSalesRecord> finSalesRecordList = null;
			List<VenOrderItemStatusHistory> historyList = null;
			
			FinSalesRecord salesRecord = new FinSalesRecord();

			if (criteria!=null) {
				JPQLSimpleQueryCriteria approvedCriteria = new JPQLSimpleQueryCriteria();
				approvedCriteria.setFieldName(DataNameTokens.FINSALESRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC);
				approvedCriteria.setOperator("equals");
				approvedCriteria.setValue("Approved");
				approvedCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINSALESRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC));						
				criteria.add(approvedCriteria);				
				
				finSalesRecordList = finSalesRecordSessionHome.findByFinSalesRecordLike(salesRecord, criteria, 0, 0);
				_log.debug("query with criteria, finSalesRecordList awal size: "+finSalesRecordList.size());
				
				int length = finSalesRecordList.size();
				for (int i=0;i<length;i++) {
					salesRecord = finSalesRecordList.get(i);
					
					historyList=venOrderItemStatusHistorySessionHome.queryByRange("Select o from VenOrderItemStatusHistory o where o.venOrderItem.orderItemId=" + salesRecord.getVenOrderItem().getOrderItemId()  +" and o.venOrderStatus.orderStatusId="+VeniceConstants.VEN_ORDER_STATUS_CX+" and o.statusChangeReason like '%CX Finance%'", 0, 1);					
					if(historyList.size()==0){
						_log.info("not cx finance yet");
						finSalesRecordList.remove(salesRecord);
						--i;
						--length;
						continue;
					}
					
					if(salesRecord.getVenOrderItem().getVenSettlementRecords()!=null && salesRecord.getVenOrderItem().getVenSettlementRecords().size()>0){
						if(!salesRecord.getVenOrderItem().getVenSettlementRecords().get(salesRecord.getVenOrderItem().getVenSettlementRecords().size()-1).getCommissionType().equals("CM") && 
								!salesRecord.getVenOrderItem().getVenSettlementRecords().get(salesRecord.getVenOrderItem().getVenSettlementRecords().size()-1).getCommissionType().equals("RB")){
							_log.info("commission type is not (CM or RB)");
							finSalesRecordList.remove(salesRecord);
							--i;
							--length;
							continue;
						}
					}									
					
					if(salesRecord.getFinApPayment()!=null){
						_log.info("ap payment is not null");
						finSalesRecordList.remove(salesRecord);
						--i;
						--length;
						continue;
					}
				}								
			} else {
				String select = "select o from FinSalesRecord o join fetch o.venOrderItem oi join fetch o.venOrderItem.venOrderItemStatusHistories h " +
						"where o.finApprovalStatus.approvalStatusDesc='Approved' and o.finApPayment is null and h.venOrderStatus.orderStatusId="+VeniceConstants.VEN_ORDER_STATUS_CX+" and h.statusChangeReason like '%CX Finance%'";
				finSalesRecordList = finSalesRecordSessionHome.queryByRange(select, 0, 0);
				
				_log.debug("query without criteria, finSalesRecordList awal size: "+finSalesRecordList.size());
				
				int length = finSalesRecordList.size();
				for (int i=0;i<length;i++) {
					salesRecord = finSalesRecordList.get(i);
					if(salesRecord.getVenOrderItem().getVenSettlementRecords()!=null && salesRecord.getVenOrderItem().getVenSettlementRecords().size()>0){
						if(!salesRecord.getVenOrderItem().getVenSettlementRecords().get(salesRecord.getVenOrderItem().getVenSettlementRecords().size()-1).getCommissionType().equals("CM") && 
								!salesRecord.getVenOrderItem().getVenSettlementRecords().get(salesRecord.getVenOrderItem().getVenSettlementRecords().size()-1).getCommissionType().equals("RB")){
							_log.info("commission type is not (CM or RB)");
							finSalesRecordList.remove(salesRecord);
							--i;
							--length;
							continue;
						}
					}			
				}
			}
			
			_log.debug("finSalesRecordList akhir size: "+finSalesRecordList.size());
			for (int i=0;i<finSalesRecordList.size();i++) {
				salesRecord = finSalesRecordList.get(i);
				
				//only show Sales Records that do not have FinApPayment -> means not settled
				if (salesRecord.getFinApPayment()==null) {
					HashMap<String, String> map = new HashMap<String, String>();
				
					DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
					
					map.put(DataNameTokens.FINSALESRECORD_SALESRECORDID, salesRecord.getSalesRecordId().toString());
					map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_WCSMERCHANTID, 
							(salesRecord.getVenOrderItem()!=null && 
							salesRecord.getVenOrderItem().getVenMerchantProduct()!=null &&
							salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!=null)?
							salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getWcsMerchantId():"");
					map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, 
							(salesRecord.getVenOrderItem()!=null && 
							salesRecord.getVenOrderItem().getVenMerchantProduct()!=null &&
							salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!=null &&
							salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!=null)?
							salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():"");
					map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID, salesRecord.getVenOrderItem().getWcsOrderItemId());
					map.put(DataNameTokens.FINSALESRECORD_MCX_DATE, formatter.format(salesRecord.getCxMtaDate()));
					map.put(DataNameTokens.FINSALESRECORD_CXF_DATE, formatter.format(salesRecord.getCxFinanceDate()));
					map.put(DataNameTokens.FINSALESRECORD_MERCHANTPAYMENTAMOUNT, (salesRecord.getMerchantPaymentAmount()!=null)?salesRecord.getMerchantPaymentAmount().toString():"");
					
					map.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_PPH23_FLAG, (salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getVenSettlementRecords()!=null && salesRecord.getVenOrderItem().getVenSettlementRecords().get(0).getPph23()!=null)? (salesRecord.getVenOrderItem().getVenSettlementRecords().get(0).getPph23()==true?"Yes":"No"):"");
					map.put(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT, salesRecord.getPph23Amount() != null?salesRecord.getPph23Amount().toString():"");
					dataList.add(map);
				}
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+finSalesRecordList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
}
