package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInRefund;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchRefundPaymentProcessingDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public FetchRefundPaymentProcessingDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<FinArFundsInRefund> finArFundsInRefundLocator = null;
		Locator<VenOrder> venOrderLocator = null;

		try {
			finArFundsInRefundLocator = new Locator<FinArFundsInRefund>();
			
			FinArFundsInRefundSessionEJBRemote fimArFundsInRefundsessionHome = (FinArFundsInRefundSessionEJBRemote) finArFundsInRefundLocator
			.lookup(FinArFundsInRefundSessionEJBRemote.class, "FinArFundsInRefundSessionEJBBean");
			
			venOrderLocator = new Locator<VenOrder>();
			
			VenOrderSessionEJBRemote venOrderSessionHome = (VenOrderSessionEJBRemote) venOrderLocator
			.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			List<FinArFundsInRefund> finArFundsInRefundList = null;
			
			FinArFundsInRefund finArFundsInRefund = new FinArFundsInRefund();

			if (criteria!=null) {
				finArFundsInRefundList = fimArFundsInRefundsessionHome.findByFinArFundsInRefundLike(finArFundsInRefund, criteria, 0, 0);
			} else {
				String select = "select o from FinArFundsInRefund o where o.finApPayment is null";
				
//				finApInvoiceList = sessionHome.queryByRange(select, request.getStartRow(), request.getEndRow());
				finArFundsInRefundList = fimArFundsInRefundsessionHome.queryByRange(select, 0, 0);
			}
						
			for (int i=0;i<finArFundsInRefundList.size();i++) {
			
				finArFundsInRefund = finArFundsInRefundList.get(i);
				
				
				//only show Ar Funds In Refunds that do not have FinApPayment -> means not settled
				if (finArFundsInRefund.getFinApPayment()==null) {
					
					HashMap<String, String> map = new HashMap<String, String>();
					
					DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
					
					map.put(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID, 
							(finArFundsInRefund.getRefundRecordId()!=null)?
							finArFundsInRefund.getRefundRecordId().toString():"");
					
					map.put(DataNameTokens.FINARFUNDSINREFUND_ACTION_TAKEN, 
							(finArFundsInRefund.getRefundType()!=null)?
							finArFundsInRefund.getRefundType().toString():"");
					
					if (finArFundsInRefund.getFinArFundsInReconRecord()!=null && finArFundsInRefund.getFinArFundsInReconRecord().getWcsOrderId()!=null) {
						String query = "Select o from VenOrder o where o.wcsOrderId='" + finArFundsInRefund.getFinArFundsInReconRecord().getWcsOrderId() + "'";
						List<VenOrder> venOrderList = venOrderSessionHome.queryByRange(query, 0, 0);
						if (venOrderList.size()>0 &&
								venOrderList.get(0).getVenCustomer()!=null) {
							map.put(DataNameTokens.FINARFUNDSINREFUND_VENORDER_VENCUSTOMER_VENPARTY_FULLORLEGALNAME, venOrderList.get(0).getVenCustomer().getVenParty().getFullOrLegalName());
						}
					}
					
					map.put(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_WCSORDERID, 
							(finArFundsInRefund.getFinArFundsInReconRecord()!=null)?
									finArFundsInRefund.getFinArFundsInReconRecord().getWcsOrderId():"");
					map.put(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_ORDERDATE, 
							(finArFundsInRefund.getFinArFundsInReconRecord()!=null &&
									finArFundsInRefund.getFinArFundsInReconRecord().getOrderDate()!=null)?
							formatter.format(finArFundsInRefund.getFinArFundsInReconRecord().getOrderDate()):"");
					map.put(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_BANKFEE, 
							(finArFundsInRefund.getFinArFundsInReconRecord()!=null ?
									finArFundsInRefund.getFinArFundsInReconRecord().getProviderReportFeeAmount()+"":""));
					map.put(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_REASON, 
							(finArFundsInRefund.getFinArFundsInReconRecord()!=null )?
							finArFundsInRefund.getFinArFundsInReconRecord().getComment():"");	
					map.put(DataNameTokens.FINARFUNDSINREFUND_APAMOUNT, 
							(finArFundsInRefund.getApAmount()!=null)?
							finArFundsInRefund.getApAmount().toString():"");
					map.put(DataNameTokens.FINARFUNDSINREFUND_REFUNDTIMESTAMP, 
							(finArFundsInRefund.getRefundTimestamp()!=null)?
									formatter.format(finArFundsInRefund.getRefundTimestamp()):"");
					
					dataList.add(map);
				}
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+finArFundsInRefundList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				finArFundsInRefundLocator.close();
				venOrderLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
	
}
