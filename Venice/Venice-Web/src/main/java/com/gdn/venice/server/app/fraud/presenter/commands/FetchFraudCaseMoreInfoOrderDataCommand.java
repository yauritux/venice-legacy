package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseMoreInfoOrderDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseMoreInfoOrderDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			//get order id
			//Lookup into EJB ven order entity
			locator = new Locator<Object>();
			FrdFraudSuspicionCaseSessionEJBRemote sessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
			
			//Build query for getting order id
			JPQLAdvancedQueryCriteria fraudCaseCriteria = request.getCriteria() != null ? request.getCriteria() : new JPQLAdvancedQueryCriteria();
			fraudCaseCriteria.setBooleanOperator("and");
			JPQLSimpleQueryCriteria caseIdCriteria = new JPQLSimpleQueryCriteria();
			caseIdCriteria.setFieldName(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
			caseIdCriteria.setOperator("equals");
			caseIdCriteria.setValue(request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
			caseIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
			fraudCaseCriteria.add(caseIdCriteria);


			FrdFraudSuspicionCase fraudCaseSuspicion = new FrdFraudSuspicionCase();
			List<FrdFraudSuspicionCase> fraudCase = sessionHome.findByFrdFraudSuspicionCaseLike(fraudCaseSuspicion, fraudCaseCriteria, 0, 1);
			fraudCaseSuspicion = fraudCase.get(0);
			String orderID = fraudCaseSuspicion.getVenOrder()!=null && fraudCaseSuspicion.getVenOrder().getOrderId()!=null?fraudCaseSuspicion.getVenOrder().getOrderId().toString():"";
			
			//build query for searching
			VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			List<VenOrder> orderList = null;			
			String select = "select o from VenOrder o ";
			
			if (request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERMOBILEPHONE) != null)
				select += "left join o.venCustomer.venParty.venContactDetails cd ";
			if (request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMEREMAIL) != null)
				select += "left join o.venCustomer.venParty.venContactDetails co ";
			if (request.getParams().get(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY) != null)
				select += "inner join o.venOrderItems oipc inner join oipc.venMerchantProduct.venProductCategories pc ";
			if (request.getParams().get(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME) != null)
				select += "inner join o.venOrderItems oipn inner join oipn.venMerchantProduct pn ";
			if (request.getParams().get(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VAORIBORCCNUMBER) != null)
				select += "inner join o.venOrderPaymentAllocations pa inner join pa.venOrderPayment op ";
			if (request.getParams().get(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME) != null)
				select += "inner join o.venOrderItems oi inner join oi.venRecipient.venParty r ";
			if (request.getParams().get(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTHP) != null)
				select += "inner join o.venOrderItems oii inner join oii.venRecipient.venParty.venContactDetails rd inner join rd.venContactDetailType cdt ";
			if (request.getParams().get(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTEMAIL) != null)
				select += "inner join o.venOrderItems oie inner join oie.venRecipient.venParty.venContactDetails re inner join re.venContactDetailType cde ";
			if (request.getParams().get(DataNameTokens.VENMERCHANT_VENPARTY_FULLORLEGALNAME) != null)
				select += "inner join o.venOrderItems oim inner join oim.venMerchantProduct.venMerchant.venParty m  ";
			if (request.getParams().get(DataNameTokens.VENORDERITEM_VENADDRESS) != null)
				select += "inner join o.venOrderItems oia ";
			if (request.getParams().get(DataNameTokens.VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSERVICETYPE_LOGISTICSERVICETYPEDESC) != null)
				select += "inner join o.venOrderItems oil inner join oil.logLogisticService oils inner join oils.logLogisticsServiceType lst ";
			select += "where o.orderId not in (select p.venOrder.orderId from FrdFraudRelatedOrderInfo p ";
			select += "where p.frdFraudSuspicionCase.fraudSuspicionCaseId = " + request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID) + " )";
			
			if (request.getParams().get(DataNameTokens.VENORDER_ORDERDATE) != null) {
				String[] orderDate;
				orderDate = request.getParams().get(DataNameTokens.VENORDER_ORDERDATE).split("~");		
				select += "and o.orderDate between " + orderDate[0] + "' and '" + orderDate[1] + "'";
			}			
			if (request.getParams().get(DataNameTokens.VENORDER_IPADDRESS) != null)
				select = select + " and o.ipAddress like '%" + request.getParams().get(DataNameTokens.VENORDER_IPADDRESS).toString() + "%'";
			if (request.getParams().get(DataNameTokens.VENORDER_WCSORDERID) != null)
				select = select + " and o.wcsOrderId like '%" + request.getParams().get(DataNameTokens.VENORDER_WCSORDERID).toString() + "%'";
			if (request.getParams().get(DataNameTokens.VENORDER_AMOUNT) != null){
				String[] amount;
				amount = request.getParams().get(DataNameTokens.VENORDER_AMOUNT).split("-");
				if (amount.length == 2)
					select = select + " and o.amount between " + Integer.parseInt(amount[0]) + " and " + Integer.parseInt(amount[1]);
				else
					select = select + " and o.amount >= " + Integer.parseInt(amount[0]);
			}
			if (request.getParams().get(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME) != null)
				select = select + " and upper(o.venCustomer.venParty.fullOrLegalName) like '%" + request.getParams().get(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME).toString().toUpperCase() + "%'";
			if (request.getParams().get(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE) != null && request.getParams().get(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE) != "")
				select = select + " and o.venOrderStatus.orderStatusCode = '" + request.getParams().get(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE).toString() + "'";
			if (request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERMOBILEPHONE) != null)
				select = select + " and (cd.venContactDetailType.contactDetailTypeId = 1 and cd.contactDetail like '%" + request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERMOBILEPHONE).toString() + "%')";
			if (request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMEREMAIL) != null)
				select = select + " and (co.venContactDetailType.contactDetailTypeId = 3 and upper(co.contactDetail) like '%" + request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMEREMAIL).toString().toUpperCase() + "%')";
			if (request.getParams().get(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY) != null)
				select = select + " and pc.level = 3 and upper(pc.productCategory) like '%" + request.getParams().get(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY).toString().toUpperCase() + "%'";
			if (request.getParams().get(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME) != null)
				select = select + " and upper(pn.wcsProductName) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME).toString().toUpperCase() + "%'";
			if (request.getParams().get(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VAORIBORCCNUMBER) != null && request.getParams().get(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VAORIBORCCNUMBER) != "") {
				String[] ccNumber;
				ccNumber = request.getParams().get(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VAORIBORCCNUMBER).split("-");
				if (ccNumber.length == 3)
					select = select + " and op.maskedCreditCardNumber like '" + ccNumber[0].trim() + "%" + ccNumber[2].trim() + "'";
				else
					select = select + " and op.maskedCreditCardNumber like '" + ccNumber[0].trim() + "%'";
			}				
			if (request.getParams().get(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME) != null)
				select = select + " and upper(r.fullOrLegalName) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME).toString().toUpperCase() + "%'";
			if (request.getParams().get(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTHP) != null)
				select = select + " and (cdt.contactDetailTypeId = 1 and rd.contactDetail like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTHP).toString().toUpperCase() + "%')";
			if (request.getParams().get(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTEMAIL) != null)
				select = select + " and (cde.contactDetailTypeId = 3 and upper(re.contactDetail) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTEMAIL).toString().toUpperCase() + "%')";
			if (request.getParams().get(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME) != null)
				select = select + " and upper(o.venCustomer.customerUserName) like '%" + request.getParams().get(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME).toString().toUpperCase() + "%'";
			if (request.getParams().get(DataNameTokens.VENMERCHANT_VENPARTY_FULLORLEGALNAME) != null)
				select = select + " and upper(m.fullOrLegalName) like '%" + request.getParams().get(DataNameTokens.VENMERCHANT_VENPARTY_FULLORLEGALNAME).toString().toUpperCase() + "%'";
			if (request.getParams().get(DataNameTokens.VENORDERITEM_VENADDRESS) != null)
				select = select + " and (upper(oia.venAddress.streetAddress1) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENADDRESS).toString().toUpperCase() + "%' " +
						"or upper(oia.venAddress.streetAddress2) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENADDRESS).toString().toUpperCase() + "%' " +
						"or upper(oia.venAddress.kelurahan) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENADDRESS).toString().toUpperCase() + "%' " +
						"or upper(oia.venAddress.kecamatan) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENADDRESS).toString().toUpperCase() + "%' " +
						"or upper(oia.venAddress.venCity.cityName) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENADDRESS).toString().toUpperCase() + "%' " +
						"or upper(oia.venAddress.postalCode) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENADDRESS).toString().toUpperCase() + "%' " +
						"or upper(oia.venAddress.venState.stateName) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENADDRESS).toString().toUpperCase() + "%' " +
						"or upper(oia.venAddress.venCountry.countryName) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_VENADDRESS).toString().toUpperCase() + "%' )";
			if (request.getParams().get(DataNameTokens.VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSERVICETYPE_LOGISTICSERVICETYPEDESC) != null)
				select = select + " and upper(lst.logisticsServiceTypeDesc) like '%" + request.getParams().get(DataNameTokens.VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSERVICETYPE_LOGISTICSERVICETYPEDESC).toString().toUpperCase() + "%'";
			
			
			//exclude current order id from searching			
			select+=" and o.orderId <> "+orderID;
			
			orderList = orderSessionHome.queryByRange(select, request.getStartRow(), request.getEndRow());
			String orderId = "";
			for (int i=0;i<orderList.size();i++) {		
				VenOrder venOrder = orderList.get(i);
				if (!orderId.equals(venOrder.getOrderId().toString())) {
					orderId = venOrder.getOrderId().toString();
					HashMap<String, String> map = new HashMap<String, String>();
					DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
					map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
					map.put(DataNameTokens.VENORDER_ORDERID, Util.isNull(venOrder.getOrderId(), "").toString());
					map.put(DataNameTokens.VENORDER_WCSORDERID, Util.isNull(venOrder.getWcsOrderId(), "").toString());
					map.put(DataNameTokens.VENORDER_AMOUNT, Util.isNull(venOrder.getAmount(), "").toString());
					map.put(DataNameTokens.VENORDER_ORDERDATE, formatter.format(venOrder.getOrderDate()));
					map.put(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, venOrder.getVenOrderStatus()!=null && venOrder.getVenOrderStatus().getOrderStatusCode()!=null?venOrder.getVenOrderStatus().getOrderStatusCode().toString():"");
					map.put(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG, venOrder.getVenCustomer()!=null && venOrder.getVenCustomer().getFirstTimeTransactionFlag()!=null?venOrder.getVenCustomer().getFirstTimeTransactionFlag().toString():"");
					map.put(DataNameTokens.VENORDER_IPADDRESS, Util.isNull(venOrder.getIpAddress(), "").toString());
					dataList.add(map);
				}
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
