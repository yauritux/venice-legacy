package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;

public class FetchOrderDetailDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchOrderDetailDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			VenOrderSessionEJBRemote sessionHome = (VenOrderSessionEJBRemote) locator
			.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria()!=null?request.getCriteria():new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("and");
			JPQLSimpleQueryCriteria orderIdCriteria = new JPQLSimpleQueryCriteria();
			orderIdCriteria.setFieldName(DataNameTokens.VENORDER_ORDERID);
			orderIdCriteria.setOperator("equals");
			orderIdCriteria.setValue(request.getParams().get(DataNameTokens.VENORDER_ORDERID));
			orderIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENORDER_ORDERID));
			criteria.add(orderIdCriteria);
			
			
			VenOrder order = new VenOrder();

			//Find Order by Order Id			
			List<VenOrder> orderList = sessionHome.findByVenOrderLike(order, criteria, 0, 1);

			HashMap<String, String> map = new HashMap<String, String>();
			
			order = orderList.get(0);
			
			DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
			
			map.put(DataNameTokens.VENORDER_ORDERID, Util.isNull(order.getOrderId(), "").toString());
			map.put(DataNameTokens.VENORDER_WCSORDERID, Util.isNull(order.getWcsOrderId(), "").toString());
			map.put(DataNameTokens.VENORDER_ORDERDATE, formatter.format(order.getOrderDate()));
			map.put(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, order.getVenOrderStatus() != null && order.getVenOrderStatus().getOrderStatusCode() != null?order.getVenOrderStatus().getOrderStatusCode():"");
			map.put(DataNameTokens.VENORDER_RMAFLAG, Util.isNull(order.getRmaFlag(), "false").toString());
			map.put(DataNameTokens.VENORDER_RMAACTION, Util.isNull(order.getRmaAction(), "").toString());
			map.put(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME, order.getVenCustomer() != null && order.getVenCustomer().getCustomerUserName() != null?order.getVenCustomer().getCustomerUserName():"");
			map.put(DataNameTokens.VENORDER_AMOUNT, Util.isNull(order.getAmount(), "").toString());
			map.put(DataNameTokens.VENORDER_DELIVEREDDATETIME, formatter.format(order.getDeliveredDateTime()));
			map.put(DataNameTokens.VENORDER_IPADDRESS, Util.isNull(order.getIpAddress(), "").toString());
			map.put(DataNameTokens.VENORDER_FULFILLMENTSTATUS, Util.isNull(order.getFulfillmentStatus(), "").toString());
			map.put(DataNameTokens.VENORDER_BLOCKEDFLAG, Util.isNull(order.getBlockedFlag(), "false").toString());
			map.put(DataNameTokens.VENORDER_BLOCKEDTIMESTAMP, formatter.format(order.getBlockedTimestamp()));
			map.put(DataNameTokens.VENORDER_BLOCKEDREASON, Util.isNull(order.getBlockedReason(), "").toString());
			map.put(DataNameTokens.VENORDER_VENFRAUDCHECKSTATUS_FRAUDCHECKSTATUSDESC, order.getVenFraudCheckStatus() != null && order.getVenFraudCheckStatus().getFraudCheckStatusDesc() != null?order.getVenFraudCheckStatus().getFraudCheckStatusDesc():"false");
			map.put(DataNameTokens.VENORDER_FINANCERECONCILEFLAG, Util.isNull(order.getFinanceReconcileFlag(), "false").toString());
			
			dataList.add(map);

			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(1);
			rafDsResponse.setEndRow(request.getStartRow()+1);
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
