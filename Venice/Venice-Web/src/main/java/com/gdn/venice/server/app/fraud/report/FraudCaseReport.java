package com.gdn.venice.server.app.fraud.report;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote;
import com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote;
import com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote;
import com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote;
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.facade.VenMerchantProductSessionEJBRemote;
import com.gdn.venice.facade.VenMigsTransactionSessionEJBRemote;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudActionLog;
import com.gdn.venice.persistence.FrdFraudCaseHistory;
import com.gdn.venice.persistence.FrdFraudRelatedOrderInfo;
import com.gdn.venice.persistence.FrdFraudSuspicionPoint;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.persistence.VenCustomer;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.persistence.VenMigsTransaction;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAddress;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.gdn.venice.persistence.VenProductCategory;
import com.gdn.venice.server.app.fraud.report.bean.ActionLog;
import com.gdn.venice.server.app.fraud.report.bean.FraudCase;
import com.gdn.venice.server.app.fraud.report.bean.Migs;
import com.gdn.venice.server.app.fraud.report.bean.OrderItem;
import com.gdn.venice.server.app.fraud.report.bean.Payment;
import com.gdn.venice.server.app.fraud.report.bean.RelatedOrder;
import com.gdn.venice.server.app.fraud.report.bean.RiskPoint;
import com.gdn.venice.server.util.Util;

public class FraudCaseReport {
	
	private boolean isFinishAndSuccess;
	private String resultFilePath = System.getenv("VENICE_HOME") + "/files/report/fraud/";
	private String resultFileName;
	
	public FraudCaseReport() {
		this.isFinishAndSuccess = false;
		this.resultFileName = "";
	}
	
	public boolean isFinishAndSuccess() {
		return this.isFinishAndSuccess;
	}
	
	public String getResultFileName() {
		return this.resultFileName;
	}
	
	private List<FraudCase> getReportData(String fraudCaseId) {
		//Feed bean from EJB Facade
		List<FraudCase> reportData = new ArrayList<FraudCase>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			FrdFraudSuspicionPointSessionEJBRemote frdFraudSuspicionPointSessionHome = (FrdFraudSuspicionPointSessionEJBRemote) locator.lookup(FrdFraudSuspicionPointSessionEJBRemote.class, "FrdFraudSuspicionPointSessionEJBBean");			
			
			List<FrdFraudSuspicionPoint> fraudSuspicionPointList = null;	
			fraudSuspicionPointList = frdFraudSuspicionPointSessionHome.queryByRange("select o from FrdFraudSuspicionPoint o where o.frdFraudSuspicionCase.fraudSuspicionCaseId = " + fraudCaseId+" order by o.fraudRuleName", 0, 0);
			
			if (fraudSuspicionPointList.get(0).getFrdFraudSuspicionCase().getFraudSuspicionCaseId() !=null) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				FraudCase fraudCase = new FraudCase();
				
				//Fraud suspicion case
				fraudCase.setFraudStatus(fraudSuspicionPointList.get(0).getFrdFraudSuspicionCase().getIlogFraudStatus());
				fraudCase.setOrderStatus(fraudSuspicionPointList.get(0).getFrdFraudSuspicionCase().getVenOrder().getVenOrderStatus().getOrderStatusCode());
				
				//cek auto atau manual FP
				VenOrderStatusHistorySessionEJBRemote historySessionHome = (VenOrderStatusHistorySessionEJBRemote) locator.lookup(VenOrderStatusHistorySessionEJBRemote.class, "VenOrderStatusHistorySessionEJBBean");
				List<VenOrderStatusHistory> historyList = null;	
				historyList = historySessionHome.queryByRange("select o from VenOrderStatusHistory o where o.id.orderId = "+fraudSuspicionPointList.get(0).getFrdFraudSuspicionCase().getVenOrder().getOrderId()+" and o.venOrderStatus.orderStatusId="+DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FP+" order by o.id.historyTimestamp asc", 0, 1);
				
				String reason="Status not FP";				
				if(historyList.size()==1){
					reason = historyList.get(0).getStatusChangeReason();
					if(reason.equalsIgnoreCase("Updated by System")){
						reason="Auto FP by System";
					}else{
						reason="Manual FP, "+historyList.get(0).getStatusChangeReason();
					}
				}
				fraudCase.setAutoOrManualFP(reason);
				fraudCase.setTotalRiskPoint(fraudSuspicionPointList.get(0).getFrdFraudSuspicionCase().getFraudTotalPoints());
				fraudCase.setFraudDescription(fraudSuspicionPointList.get(0).getFrdFraudSuspicionCase().getFraudCaseDesc());
				fraudCase.setFraudReason(fraudSuspicionPointList.get(0).getFrdFraudSuspicionCase().getSuspicionReason());
				fraudCase.setFraudNotes(fraudSuspicionPointList.get(0).getFrdFraudSuspicionCase().getFraudSuspicionNotes());
				
				//History (investigator info)
				FrdFraudCaseHistorySessionEJBRemote FrdFraudCaseHistorySessionHome = (FrdFraudCaseHistorySessionEJBRemote) locator.lookup(FrdFraudCaseHistorySessionEJBRemote.class, "FrdFraudCaseHistorySessionEJBBean");
				List<FrdFraudCaseHistory> frdFraudCaseHistoryList = null;
				frdFraudCaseHistoryList = FrdFraudCaseHistorySessionHome.queryByRange("select o from FrdFraudCaseHistory o where o.frdFraudSuspicionCase.fraudSuspicionCaseId = " + fraudCaseId +" order by o.id.fraudCaseHistoryDate desc", 0, 0);
				
				String investigator = "-";
				if(frdFraudCaseHistoryList.size()>0){
					FrdFraudCaseHistory history = frdFraudCaseHistoryList.get(0);
					String[] splitString=history.getFraudCaseHistoryNotes().split(" ");
					for(int i=0;i<splitString.length;i++){
						if(!splitString[i].equalsIgnoreCase("executed") && !splitString[i].equalsIgnoreCase("modified") && !splitString[i].equalsIgnoreCase("claimed") && !splitString[i].equalsIgnoreCase("unclaimed") && 
								!splitString[i].equalsIgnoreCase("escalated") && !splitString[i].equalsIgnoreCase("closed") && !splitString[i].equalsIgnoreCase("to") && !splitString[i].equalsIgnoreCase("by") &&
								!splitString[i].equalsIgnoreCase("ilog") && !splitString[i].contains("Fraud") && !splitString[i].equalsIgnoreCase("operation") && !splitString[i].contains("Staff") && !splitString[i].contains("Officer") && !splitString[i].contains("Manager")){
							investigator=splitString[i];
						}
					}
				}

				fraudCase.setInvestigator(investigator);
				
				//Ven order
				VenOrder venOrder = fraudSuspicionPointList.get(0).getFrdFraudSuspicionCase().getVenOrder();
				fraudCase.setWcsOrderId(venOrder.getWcsOrderId());
				
				String s =  sdf.format(venOrder.getOrderDate());
		        StringBuilder sb = new StringBuilder(s);
				fraudCase.setOrderTimeStamp(sb.toString());
				fraudCase.setOrderAmount(venOrder.getAmount());
				fraudCase.setIpAddress(venOrder.getIpAddress());
				
				//Ven customer
				VenCustomer venCustomer = venOrder.getVenCustomer();
				fraudCase.setCustomerId(venCustomer.getCustomerUserName());
				fraudCase.setCustomerType(Util.isNull(venCustomer.getUserType(), "").equals("R") ? "Registered" : "Unregistered");
				fraudCase.setCustomerBirthDate(venCustomer.getDateOfBirth());
				fraudCase.setCustomerName(venCustomer.getVenParty().getFullOrLegalName());
				
				//Ven customer's address
				VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) locator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");
				
				List<VenOrderAddress> orderAddressList = orderAddressSessionHome.queryByRange("select o from VenOrderAddress o where o.venOrder.orderId ="+venOrder.getOrderId(), 0, 1);

				if (orderAddressList.size()>0) {
					VenAddress venAddress = orderAddressList.get(0).getVenAddress();
					
					fraudCase.setCustomerAddress1(venAddress.getStreetAddress1());
					fraudCase.setCustomerAddress2(venAddress.getStreetAddress2());
					fraudCase.setCustomerKecamatanKelurahan(venAddress.getKecamatan() + ", " + venAddress.getKelurahan());
					fraudCase.setCustomerCityPostalCode((venAddress.getVenCity()!=null?venAddress.getVenCity().getCityName():"") + " " + venAddress.getPostalCode());
					fraudCase.setCustomerStateCountry((venAddress.getVenState()!=null?venAddress.getVenState().getStateName():"") + ", " + (venAddress.getVenCountry()!=null?venAddress.getVenCountry().getCountryName():"")+" "+ (venAddress.getVenCountry()!=null?venAddress.getVenCountry().getCountryCode():""));
				}
				
				//Ven customer's contact details
				VenOrderContactDetailSessionEJBRemote orderContactDetailSessionHome = (VenOrderContactDetailSessionEJBRemote) locator.lookup(VenOrderContactDetailSessionEJBRemote.class, "VenOrderContactDetailSessionEJBBean");
				
				List<VenOrderContactDetail> venContactDetailList = orderContactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where o.venOrder.orderId = "+venOrder.getOrderId(), 0, 0);
				
				if(venContactDetailList.size()>0){
					for (int i = 0; i < venContactDetailList.size(); i++) {
						switch ((int) venContactDetailList.get(i).getVenContactDetail().getVenContactDetailType().getContactDetailTypeId().longValue()) {
							case 0:
								fraudCase.setCustomerContact((fraudCase.getCustomerContact() != null && !fraudCase.getCustomerContact().equalsIgnoreCase("") ? fraudCase.getCustomerContact() + ", " : "") + venContactDetailList.get(i).getVenContactDetail().getContactDetail() + " (Phone)");
								break;
							case 1:
								fraudCase.setCustomerContact((fraudCase.getCustomerContact() != null && !fraudCase.getCustomerContact().equalsIgnoreCase("") ? fraudCase.getCustomerContact() + ", " : "") + venContactDetailList.get(i).getVenContactDetail().getContactDetail() + " (Mobile)");
								break;
							case 2:
								fraudCase.setCustomerContact((fraudCase.getCustomerContact() != null && !fraudCase.getCustomerContact().equalsIgnoreCase("") ? fraudCase.getCustomerContact() + ", " : "") + venContactDetailList.get(i).getVenContactDetail().getContactDetail() + " (Fax)");
								break;
							case 3:
								fraudCase.setCustomerEmail(venContactDetailList.get(i).getVenContactDetail().getContactDetail());
								break;
							default:
								break;
						}
					}
				}
				
				//Ven order item
				VenOrderItemSessionEJBRemote venOrderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
				
				List<VenOrderItem> venOrderItemList = null;
				venOrderItemList = venOrderItemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId = " + venOrder.getOrderId().toString(), 0, 0);
				
				//recipient dan shipping address dikeluarkan dari detail order item dan dipindahkan ke customer detail 
				fraudCase.setShippingType(venOrderItemList.get(0).getLogLogisticService().getLogisticsServiceDesc());
				
				//Recipient
				fraudCase.setRecipientName(venOrderItemList.get(0).getVenRecipient().getVenParty().getFullOrLegalName());
				
				//Shipping address
				VenOrderItemAddressSessionEJBRemote orderItemAddressSessionHome = (VenOrderItemAddressSessionEJBRemote) locator.lookup(VenOrderItemAddressSessionEJBRemote.class, "VenOrderItemAddressSessionEJBBean");
				List<VenOrderItemAddress> itemAddressList = orderItemAddressSessionHome.queryByRange("select o from VenOrderItemAddress o where o.venOrderItem.orderItemId="+venOrderItemList.get(0).getOrderItemId(), 0, 1);
				
				if(itemAddressList.size()>0){
					VenAddress shippingAddress = itemAddressList.get(0).getVenAddress();
					fraudCase.setRecipientAddress1(shippingAddress.getStreetAddress1());
					fraudCase.setRecipientAddress2(shippingAddress.getStreetAddress2());
					fraudCase.setRecipientKecamatanKelurahan(shippingAddress.getKecamatan() + ", " + shippingAddress.getKelurahan());
					fraudCase.setRecipientCityPostalCode(shippingAddress.getVenCity().getCityName() + " " + shippingAddress.getPostalCode());
					fraudCase.setRecipientStateCountry(shippingAddress.getVenState().getStateName() + ", " + shippingAddress.getVenCountry().getCountryName() + " " + shippingAddress.getVenCountry().getCountryCode());
				}
				
				List<OrderItem> orderItemList = new ArrayList<OrderItem>();
				for (int i = 0; i < venOrderItemList.size(); i++) {
					OrderItem orderItem = new OrderItem();
					
					//Order item
					VenOrderItem venOrderItem = venOrderItemList.get(i);
					orderItem.setOrderItemId(venOrderItem.getWcsOrderItemId());
					orderItem.setProductSku(venOrderItem.getVenMerchantProduct().getWcsProductSku());
					orderItem.setProductName(venOrderItem.getVenMerchantProduct().getWcsProductName());
					orderItem.setQuantity(venOrderItem.getQuantity());
					orderItem.setPrice(venOrderItem.getPrice());
					orderItem.setTotalPrice(venOrderItem.getTotal());
					
					//Lookup into EJB for getting product category				
					VenMerchantProductSessionEJBRemote merchantProductSessionHome = (VenMerchantProductSessionEJBRemote) locator.lookup(VenMerchantProductSessionEJBRemote.class, "VenMerchantProductSessionEJBBean");
					List<VenMerchantProduct> venMerchantProductList = merchantProductSessionHome.queryByRange("select o from VenMerchantProduct o join fetch o.venProductCategories where o.productId = " + venOrderItem.getVenMerchantProduct().getProductId(), 0, 0);
					
					List<VenProductCategory> productCategoryList = null;
					if(venMerchantProductList.size()>0){
						productCategoryList = venMerchantProductList.get(0).getVenProductCategories();
					}
					String category1 = "", category2="", category3="";
					if (productCategoryList != null) {
						for (int k = 0; k < productCategoryList.size(); k++) {
							if (productCategoryList.get(k).getLevel() == 1){
								category1 = productCategoryList.get(k).getProductCategory();
							}else if(productCategoryList.get(k).getLevel() == 2){
								category2 = productCategoryList.get(k).getProductCategory();
							}else if(productCategoryList.get(k).getLevel() == 3){
								category3 = productCategoryList.get(k).getProductCategory();
							}
						}
					}
					orderItem.setProductCategory1(category1);
					orderItem.setProductCategory2(category2);
					orderItem.setProductCategory3(category3);
					
					//dikeluarkan dari detail order item
//					orderItem.setShippingType(venOrderItem.getLogLogisticService().getLogisticsServiceDesc());
//					
//					//Recipient
//					orderItem.setRecipientName(venOrderItem.getVenRecipient().getVenParty().getFullOrLegalName());
//					
//					//Shipping address
//					VenAddress venAddress = venOrderItem.getVenAddress();
//					orderItem.setRecipientAddress1(venAddress.getStreetAddress1());
//					orderItem.setRecipientAddress2(venAddress.getStreetAddress2());
//					orderItem.setRecipientKecamatanKelurahan(venAddress.getKecamatan() + ", " + venAddress.getKelurahan());
//					orderItem.setRecipientCityPostalCode(venAddress.getVenCity().getCityName() + " " + venAddress.getPostalCode());
//					orderItem.setRecipientStateCountry(venAddress.getVenState().getStateName() + ", " + venAddress.getVenCountry().getCountryName() + " " + venAddress.getVenCountry().getCountryCode());
					
					orderItemList.add(orderItem);
				}
				
				fraudCase.setOrderItemList(orderItemList);
				
				//Ven payment
				VenOrderPaymentAllocationSessionEJBRemote venOrderPaymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
				
				List<VenOrderPaymentAllocation> venOrderPaymentAllocationList = null;
				venOrderPaymentAllocationList = venOrderPaymentAllocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId = " + venOrder.getOrderId().toString(), 0, 0);
				
				List<Payment> paymentList = new ArrayList<Payment>();
				List<Migs> migsList = new ArrayList<Migs>();
				
				for (int i = 0; i < venOrderPaymentAllocationList.size(); i++) {
					Payment payment = new Payment();
					
					//Order item
					VenOrderPayment venOrderPayment = venOrderPaymentAllocationList.get(i).getVenOrderPayment();
					payment.setWcsPaymentId(venOrderPayment.getWcsPaymentId());
					payment.setPaymentType(venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeDesc());
					
					
					switch ((int) venOrderPayment.getVenPaymentType().getPaymentTypeId().longValue()) {
						case 0:
							String creditCardNumber=venOrderPayment.getMaskedCreditCardNumber();
							String binNumber="";
							if(creditCardNumber.length()>6){
								binNumber = creditCardNumber.substring(0, 6);
							}
							payment.setPaymentInfo(creditCardNumber);
							payment.setEci(venOrderPayment.getThreeDsSecurityLevelAuth()!=null?venOrderPayment.getThreeDsSecurityLevelAuth():"-");
							payment.setTenor(venOrderPayment.getTenor()!=null?venOrderPayment.getTenor():0);
							//Ven payment
							VenBinCreditLimitEstimateSessionEJBRemote venBinCreditLimitEstimateSessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");
							
							List<VenBinCreditLimitEstimate> venBinCreditLimitEstimateList = null;
							venBinCreditLimitEstimateList = venBinCreditLimitEstimateSessionHome.queryByRange("select o from VenBinCreditLimitEstimate o where o.binNumber = '" + binNumber+"'", 0, 0);
							
							for (int j = 0; j < venBinCreditLimitEstimateList.size(); j++) {
								payment.setCardType(venBinCreditLimitEstimateList.get(j).getVenCardType().getCardTypeDesc());
								payment.setCardLimit(venBinCreditLimitEstimateList.get(j).getCreditLimitEstimate());
								payment.setCardDescription(venBinCreditLimitEstimateList.get(j).getDescription());
								payment.setIssuingBank(venBinCreditLimitEstimateList.get(j).getBankName());
							}
							
							//Ven migs
							VenMigsTransactionSessionEJBRemote venMigsTransactionSessionHome = (VenMigsTransactionSessionEJBRemote) locator.lookup(VenMigsTransactionSessionEJBRemote.class, "VenMigsTransactionSessionEJBBean");
							
							List<VenMigsTransaction> venMigsTransactionList = null;
							venMigsTransactionList = venMigsTransactionSessionHome.queryByRange("select o from VenMigsTransaction o where o.venOrderPayment.orderPaymentId = " + venOrderPayment.getOrderPaymentId().toString(), 0, 0);
							
							for (int j = 0; j < venMigsTransactionList.size(); j++) {
								Migs migs = new Migs();
								
								migs.setTransactionId(venMigsTransactionList.get(j).getTransactionId());
								migs.setDate(venMigsTransactionList.get(j).getTransactionDate());
								migs.setMerchantId(venMigsTransactionList.get(j).getMerchantId());
								migs.setOrderReference(venMigsTransactionList.get(j).getOrderReference());
								migs.setOrderId(venMigsTransactionList.get(j).getOrderId());
								migs.setMerchantTransactionReference(venMigsTransactionList.get(j).getMerchantTransactionReference());
								migs.setTransactionType(venMigsTransactionList.get(j).getTransactionType());
								migs.setAcquirerId(venMigsTransactionList.get(j).getAcquirerId());
								migs.setBatchNumber(venMigsTransactionList.get(j).getBatchNumber());
								migs.setCurrency(venMigsTransactionList.get(j).getCurrency());
								migs.setAmount(venMigsTransactionList.get(j).getAmount());
								migs.setRrn(venMigsTransactionList.get(j).getRrn());
								migs.setResponseCode(venMigsTransactionList.get(j).getResponseCode());
								migs.setAcquirerResponseCode(venMigsTransactionList.get(j).getAcquirerResponseCode());
								migs.setAuthorisationCode(venMigsTransactionList.get(j).getAuthorisationCode());
								migs.setOperatorId(venMigsTransactionList.get(j).getOperator());
								migs.setMerchantTransactionSource(venMigsTransactionList.get(j).getMerchantTransactionSource());
								migs.setOrderDate(venMigsTransactionList.get(j).getOrderDate());
								migs.setCardType(venMigsTransactionList.get(j).getCardType());
								migs.setCardNumber(venMigsTransactionList.get(j).getCardNumber());
								migs.setCardExpiryMonth(venMigsTransactionList.get(j).getCardExpiryMonth());
								migs.setCardExpiryYear(venMigsTransactionList.get(j).getCardExpiryYear());
								migs.setDialectCscResultCode(venMigsTransactionList.get(j).getDialectCscResultCode());
								
								migsList.add(migs);
							}
							break;
						case 2:
							payment.setPaymentInfo(venOrderPayment.getVirtualAccountNumber());
							break;
						case 1:
							payment.setInternetBankingId(venOrderPayment.getInternetBankingId());
							break;
						default:
							payment.setPaymentInfo("-");
							break;
					}
					
					payment.setAmount(venOrderPaymentAllocationList.get(i).getAllocationAmount());
					
					//Billing address
					VenAddress venAddress = venOrderPayment.getVenAddress();
					if(venAddress!=null){
						payment.setBillingAddress1(venAddress.getStreetAddress1());
						payment.setBillingAddress2(venAddress.getStreetAddress2());
						payment.setBillingKecamatanKelurahan(venAddress.getKecamatan() + ", " + venAddress.getKelurahan());
						payment.setBillingCityPostalCode(venAddress.getVenCity().getCityName() + " " + venAddress.getPostalCode());
						payment.setBillingStateCountry(venAddress.getVenState().getStateName() + ", " + venAddress.getVenCountry().getCountryName());
					}
					paymentList.add(payment);
				}
				
				fraudCase.setPaymentList(paymentList);
				fraudCase.setMigsList(migsList);
				
				//Risk Points
				List<RiskPoint> riskPointList = new ArrayList<RiskPoint>();
				for (int i = 0; i < fraudSuspicionPointList.size(); i++) {
					RiskPoint riskPoint = new RiskPoint();
					riskPoint.setRuleName(fraudSuspicionPointList.get(i).getFraudRuleName());
					riskPoint.setRiskPoint(fraudSuspicionPointList.get(i).getRiskPoints());
					riskPointList.add(riskPoint);
				}

				fraudCase.setRiskPointList(riskPointList);
				
				//Related Order
				FrdFraudRelatedOrderInfoSessionEJBRemote relatedOrderSessionHome = (FrdFraudRelatedOrderInfoSessionEJBRemote) locator.lookup(FrdFraudRelatedOrderInfoSessionEJBRemote.class, "FrdFraudRelatedOrderInfoSessionEJBBean");
				List<FrdFraudRelatedOrderInfo> frdRelatedOrderList = relatedOrderSessionHome.queryByRange("select o from FrdFraudRelatedOrderInfo o join fetch o.frdFraudSuspicionCase where o.frdFraudSuspicionCase.fraudSuspicionCaseId = " + fraudCaseId, 0, 0);
				List<RelatedOrder> relatedOrderList = new ArrayList<RelatedOrder>();
				
				for (int i=0;i<frdRelatedOrderList.size();i++) {	
					RelatedOrder relatedOrder = new RelatedOrder();
			
					relatedOrder.setWcsOrderId(frdRelatedOrderList.get(i).getVenOrder().getWcsOrderId());
					relatedOrder.setCustomerName((frdRelatedOrderList.get(i).getVenOrder().getVenCustomer().getVenParty().getFullOrLegalName()));
					relatedOrder.setAmount(frdRelatedOrderList.get(i).getVenOrder().getAmount());
					relatedOrder.setOrderDate(frdRelatedOrderList.get(i).getVenOrder().getOrderDate());
					relatedOrder.setOrderStatus(frdRelatedOrderList.get(i).getVenOrder().getVenOrderStatus().getOrderStatusCode());
					relatedOrder.setFirstTimeFlag(frdRelatedOrderList.get(i).getVenOrder().getVenCustomer().getFirstTimeTransactionFlag());
					relatedOrder.setIpAddress(frdRelatedOrderList.get(i).getVenOrder().getIpAddress());
					relatedOrderList.add(relatedOrder);
				}
				
				fraudCase.setRelatedOrderList(relatedOrderList);
				
				//Action Log
				FrdFraudActionLogSessionEJBRemote actionLogSessionHome = (FrdFraudActionLogSessionEJBRemote) locator.lookup(FrdFraudActionLogSessionEJBRemote.class, "FrdFraudActionLogSessionEJBBean");
				List<FrdFraudActionLog> frdActionLogList = actionLogSessionHome.queryByRange("select o from FrdFraudActionLog o join fetch o.frdFraudSuspicionCase where o.isActive=true and o.frdFraudSuspicionCase.fraudSuspicionCaseId =  "+ fraudCaseId, 0, 0);
				List<ActionLog> actionLogList = new ArrayList<ActionLog>();
				
				for (int i=0;i<frdActionLogList.size();i++) {	
					ActionLog actionLog = new ActionLog();
					String actionType = frdActionLogList.get(i).getFraudActionLogType();
					if(actionType.equals("CALL")) actionType="Call";
					else if(actionType.equals("MEET")) actionType="Meet";
					else if(actionType.equals("SENDDOCUMENT")) actionType="Send Document";
					else if(actionType.equals("OTHER")) actionType="Other";
					
					actionLog.setRowNumber(i+1+".");
					actionLog.setActionType(actionType);
					actionLog.setPartyName(frdActionLogList.get(i).getVenParty().getFullOrLegalName());
					actionLog.setActionLogDate(frdActionLogList.get(i).getFraudActionLogDate());
					actionLog.setNotes(frdActionLogList.get(i).getFraudActionLogNotes());
					actionLogList.add(actionLog);
				}
				
				fraudCase.setActionLogList(actionLogList);
				
				//Add to report data object
				reportData.add(fraudCase);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (locator != null) {
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		return reportData;
	}

	public void generateReport(String fraudCaseId) {
		try {
			//Load compiled report design (.jasper file)
			InputStream inputStreamMain = getClass().getResourceAsStream("./FraudCaseReport.jasper");
			JasperReport jasperReportMain = (JasperReport) JRLoader.loadObject(inputStreamMain);
			
			InputStream inputStreamSubOrderItem = getClass().getResourceAsStream("./FraudCaseReportSubOrderItem.jasper");
			JasperReport jasperReportSubOrderItem = (JasperReport) JRLoader.loadObject(inputStreamSubOrderItem);
			
			InputStream inputStreamSubPayment = getClass().getResourceAsStream("./FraudCaseReportSubPayment.jasper");
			JasperReport jasperReportSubPayment = (JasperReport) JRLoader.loadObject(inputStreamSubPayment);
			
			InputStream inputStreamSubMigs = getClass().getResourceAsStream("./FraudCaseReportSubMigs.jasper");
			JasperReport jasperReportSubMigs = (JasperReport) JRLoader.loadObject(inputStreamSubMigs);
			
			InputStream inputStreamSubRiskPoint = getClass().getResourceAsStream("./FraudCaseReportSubRiskPoint.jasper");
			JasperReport jasperReportSubRiskPoint = (JasperReport) JRLoader.loadObject(inputStreamSubRiskPoint);
			
			InputStream inputStreamSubRelatedOrder = getClass().getResourceAsStream("./FraudCaseReportSubRelatedOrder.jasper");
			JasperReport jasperReportSubRelatedOrder = (JasperReport) JRLoader.loadObject(inputStreamSubRelatedOrder);

			
			InputStream inputStreamSubActionLog = getClass().getResourceAsStream("./FraudCaseReportSubActionLog.jasper");
			JasperReport jasperReportSubActionLog = (JasperReport) JRLoader.loadObject(inputStreamSubActionLog);
			
			//Put report's parameters 
			HashMap<String, Object> parameters = new HashMap<String, Object> ();
			parameters.put("SUBREPORT_ORDER_ITEM", jasperReportSubOrderItem);
			parameters.put("SUBREPORT_PAYMENT", jasperReportSubPayment);
			parameters.put("SUBREPORT_MIGS", jasperReportSubMigs);
			parameters.put("SUBREPORT_RISK_POINT", jasperReportSubRiskPoint);
			parameters.put("SUBREPORT_RELATED_ORDER", jasperReportSubRelatedOrder);
			parameters.put("SUBREPORT_ACTION_LOG", jasperReportSubActionLog);
			
			//Generate bean from EJB
			List<FraudCase> fraudCaseDataCollection = getReportData(fraudCaseId);
			JRBeanCollectionDataSource dataCollection = new JRBeanCollectionDataSource(fraudCaseDataCollection);
			
			//Generate report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReportMain, parameters, dataCollection);
			
			//Export to PDF
			String wcsOrderId = fraudCaseDataCollection.size() == 1 ? ("_" + fraudCaseDataCollection.get(0).getWcsOrderId()) : "";
			String formatString = "yyyy.MM.dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(formatString);
			
			resultFileName = "Order_Detail" + wcsOrderId + "_" + sdf.format(new Date()) + ".pdf";
			JasperExportManager.exportReportToPdfFile(jasperPrint, resultFilePath + resultFileName);
			
			//this is example to export to excel
//			resultFileName = "Order_Detail" + wcsOrderId + "_" + sdf.format(new Date()) + ".xls";
//			JasperExportManager.exportReportToPdfFile(jasperPrint, resultFilePath + resultFileName);
//			JRXlsExporter exporter= new JRXlsExporter();			
//			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);			
//			exporter.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, true);			
//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, resultFilePath + resultFileName);			
//			exporter.exportReport();
			
			this.isFinishAndSuccess = true;
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}