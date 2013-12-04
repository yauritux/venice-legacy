package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.facade.VenPartyAddressSessionEJBRemote;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenPartyAddress;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;

public class FetchOrderFinancePaymentDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchOrderFinancePaymentDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator
			.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			
			VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) locator
			.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria()!=null?request.getCriteria():new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("and");
			JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
			simpleCriteria.setFieldName(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERID);
			simpleCriteria.setOperator("equals");
			simpleCriteria.setValue(request.getParams().get(DataNameTokens.VENORDER_ORDERID));
			simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERID));
			criteria.add(simpleCriteria);
			
			VenOrderPaymentAllocation orderPaymentAllocation = new VenOrderPaymentAllocation();
			
//			List<VenOrderPaymentAllocation> venOrderPaymentAllocation = orderPaymentAllocationSessionHome.findByVenOrderPaymentAllocationLike(orderPaymentAllocation, criteria, request.getStartRow(), request.getEndRow());
			List<VenOrderPaymentAllocation> venOrderPaymentAllocation = orderPaymentAllocationSessionHome.findByVenOrderPaymentAllocationLike(orderPaymentAllocation, criteria, 0, 0);

			for (int i=0;i<venOrderPaymentAllocation.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				VenOrderPayment orderPayment = venOrderPaymentAllocation.get(i).getVenOrderPayment();
				
				if(orderPayment.getVenPaymentType().getPaymentTypeCode().equals("CS")){	
					List<VenOrder> oldVenOrderList =  orderSessionHome.queryByRange("select o from VenOrder o left join fetch o.venOrderPaymentAllocations opa left join fetch opa.venOrderPayment op left join fetch op.venWcsPaymentType where o.wcsOrderId = '" +  orderPayment.getOldVenOrder().getWcsOrderId() + "'", 0, 0);
					
					map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OLDVENORDER_WCSORDERID, oldVenOrderList.get(i).getWcsOrderId());
					map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OLDVENORDER_VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC, oldVenOrderList.get(i).getVenOrderPaymentAllocations().get(i).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeDesc());
					map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_REFERENCEID, orderPayment.getReferenceId());

				}
				
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID, orderPayment.getOrderPaymentId()!=null?orderPayment.getOrderPaymentId().toString():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID, orderPayment.getWcsPaymentId());
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, orderPayment.getVenBank()!=null?orderPayment.getVenBank().getBankShortName():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPECODE, orderPayment.getVenPaymentType()!=null?orderPayment.getVenPaymentType().getPaymentTypeCode():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC, orderPayment.getVenWcsPaymentType()!=null?orderPayment.getVenWcsPaymentType().getWcsPaymentTypeDesc():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VIRTUALACCOUNTNUMBER, orderPayment.getVirtualAccountNumber());
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT, orderPayment.getAmount()!=null? orderPayment.getAmount().toString():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSID, orderPayment.getVenPaymentStatus()!=null&&orderPayment.getVenPaymentStatus().getPaymentStatusId()!=null?orderPayment.getVenPaymentStatus().getPaymentStatusId().toString():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSDESC, orderPayment.getVenPaymentStatus()!=null?orderPayment.getVenPaymentStatus().getPaymentStatusDesc():"");
				
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(venOrderPaymentAllocation.size());
			rafDsResponse.setEndRow(request.getStartRow()+venOrderPaymentAllocation.size());
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
