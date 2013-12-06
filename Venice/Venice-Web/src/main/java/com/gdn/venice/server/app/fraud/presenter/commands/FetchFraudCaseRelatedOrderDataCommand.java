package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudRelatedOrderInfo;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseRelatedOrderDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseRelatedOrderDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			FrdFraudRelatedOrderInfoSessionEJBRemote sessionHome = (FrdFraudRelatedOrderInfoSessionEJBRemote) locator.lookup(FrdFraudRelatedOrderInfoSessionEJBRemote.class, "FrdFraudRelatedOrderInfoSessionEJBBean");
			List<FrdFraudRelatedOrderInfo> relatedOrder = sessionHome.queryByRange("select o from FrdFraudRelatedOrderInfo o join fetch o.frdFraudSuspicionCase where o.frdFraudSuspicionCase.fraudSuspicionCaseId = " + request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).toString(), 0, 0);
			
			for (int i=0;i<relatedOrder.size();i++) {
				VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
				
				JPQLAdvancedQueryCriteria orderCriteria = new JPQLAdvancedQueryCriteria();
				orderCriteria.setBooleanOperator("and");
				JPQLSimpleQueryCriteria orderIdCriteria = new JPQLSimpleQueryCriteria();
				orderIdCriteria.setFieldName(DataNameTokens.VENORDER_ORDERID);
				orderIdCriteria.setOperator("equals");
				orderIdCriteria.setValue(relatedOrder.get(i).getVenOrder().getOrderId().toString());
				orderIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENORDER_ORDERID));
				orderCriteria.add(orderIdCriteria);
				
				VenOrder venOrder = new VenOrder();
				List<VenOrder> orderList = orderSessionHome.findByVenOrderLike(venOrder, orderCriteria, 0, 1);
				HashMap<String, String> map = new HashMap<String, String>();
				venOrder = orderList.get(0);
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, Util.isNull(request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID), "").toString());
				map.put(DataNameTokens.VENORDER_ORDERID, Util.isNull(venOrder.getOrderId(), "").toString());
				map.put(DataNameTokens.VENORDER_WCSORDERID, Util.isNull(venOrder.getWcsOrderId(), "").toString());
				map.put(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME, venOrder.getVenCustomer()!=null && venOrder.getVenCustomer().getVenParty()!=null && venOrder.getVenCustomer().getVenParty().getFullOrLegalName()!=null?venOrder.getVenCustomer().getVenParty().getFullOrLegalName().toString():"");
				map.put(DataNameTokens.VENORDER_AMOUNT, Util.isNull(venOrder.getAmount(), "").toString());
				map.put(DataNameTokens.VENORDER_ORDERDATE, formatter.format(venOrder.getOrderDate()));
				map.put(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, venOrder.getVenOrderStatus()!=null && venOrder.getVenOrderStatus().getOrderStatusCode()!=null?venOrder.getVenOrderStatus().getOrderStatusCode().toString():"");
				map.put(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG, venOrder.getVenCustomer()!=null && venOrder.getVenCustomer().getFirstTimeTransactionFlag()!=null?venOrder.getVenCustomer().getFirstTimeTransactionFlag().toString():"false");
				map.put(DataNameTokens.VENORDER_IPADDRESS, Util.isNull(venOrder.getIpAddress(), "").toString());			
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
				if(locator!=null){
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
