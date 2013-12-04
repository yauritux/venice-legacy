package com.gdn.venice.server.app.fraud.report;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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
import com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote;
import com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote;
import com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.facade.VenPartyAddressSessionEJBRemote;
import com.gdn.venice.persistence.FrdBlacklistReason;
import com.gdn.venice.persistence.FrdFraudCaseHistory;
import com.gdn.venice.persistence.FrdFraudFileAttachment;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.FrdFraudSuspicionPoint;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenPartyAddress;
import com.gdn.venice.server.app.fraud.report.bean.OrderHistory;
import com.gdn.venice.server.app.fraud.report.bean.OrderHistoryAttachment;
import com.gdn.venice.server.app.fraud.report.bean.OrderHistoryContactDetail;
import com.gdn.venice.server.app.fraud.report.bean.OrderHistoryOrderItem;
import com.gdn.venice.server.app.fraud.report.bean.OrderHistoryRiskPoint;
import com.gdn.venice.server.util.Util;

public class FraudOrderHistoryReport {
	
	private boolean isFinishAndSuccess;
	private String resultFilePath = System.getenv("VENICE_HOME") + "/files/report/fraud/";
	private String resultFileName;
	
	public FraudOrderHistoryReport() {
		this.isFinishAndSuccess = false;
		this.resultFileName = "";
	}
	
	public boolean isFinishAndSuccess() {
		return this.isFinishAndSuccess;
	}
	
	public String getResultFileName() {
		return this.resultFileName;
	}
	
	private List<OrderHistory> getReportData(String fraudCaseId, String orderId) {
		//Feed bean from EJB Facade
		List<OrderHistory> reportData = new ArrayList<OrderHistory>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			FrdFraudSuspicionCaseSessionEJBRemote frdFraudSuspicionCaseSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");			
			
			List<FrdFraudSuspicionCase> fraudSuspicionCaseList = null;	
			if(!fraudCaseId.equals(""))
				fraudSuspicionCaseList = frdFraudSuspicionCaseSessionHome.queryByRange("select o from FrdFraudSuspicionCase o join fetch o.frdFraudSuspicionPoints where o.fraudSuspicionCaseId = " + fraudCaseId, 0, 0);
			
			VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			List<VenOrder> orderList = orderSessionHome.queryByRange("select o from VenOrder o  where o.wcsOrderId = '"+orderId+"'", 0, 1);
			
			if (orderList.size() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");//:ss");
				OrderHistory fraudCase = new OrderHistory();
				
				if(fraudSuspicionCaseList != null && fraudSuspicionCaseList.size() > 0){
					fraudCase.setTotalRiskPoint(fraudSuspicionCaseList.get(0).getFraudTotalPoints());
					fraudCase.setFraudDescription(fraudSuspicionCaseList.get(0).getFraudCaseDesc());
					fraudCase.setFraudReason(fraudSuspicionCaseList.get(0).getSuspicionReason());
					fraudCase.setFraudNotes(fraudSuspicionCaseList.get(0).getFraudSuspicionNotes());
					
					//Risk Points
					List<OrderHistoryRiskPoint> riskPointList = new ArrayList<OrderHistoryRiskPoint>();
					List<FrdFraudSuspicionPoint> frdRiskPointList = fraudSuspicionCaseList.get(0).getFrdFraudSuspicionPoints();
						for (int i = 0; i < frdRiskPointList.size(); i++) {
							OrderHistoryRiskPoint riskPoint = new OrderHistoryRiskPoint();
							riskPoint.setRuleName(frdRiskPointList.get(i).getFraudRuleName());
							riskPoint.setRiskPoint(frdRiskPointList.get(i).getRiskPoints());
							riskPointList.add(riskPoint);
						}
						fraudCase.setRiskPointList(riskPointList);					
				}
				
				//History (investigator info)
				FrdFraudCaseHistorySessionEJBRemote FrdFraudCaseHistorySessionHome = (FrdFraudCaseHistorySessionEJBRemote) locator.lookup(FrdFraudCaseHistorySessionEJBRemote.class, "FrdFraudCaseHistorySessionEJBBean");
				List<FrdFraudCaseHistory> frdFraudCaseHistoryList = null;
				if(!fraudCaseId.equals(""))
					frdFraudCaseHistoryList = FrdFraudCaseHistorySessionHome.queryByRange("select o from FrdFraudCaseHistory o where o.frdFraudSuspicionCase.fraudSuspicionCaseId = " + fraudCaseId +" order by o.id.fraudCaseHistoryDate desc", 0, 0);
				
				String investigator = "-";
				if(frdFraudCaseHistoryList != null && frdFraudCaseHistoryList.size()>0){
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
				VenOrder venOrder = orderList.get(0);
				fraudCase.setWcsOrderId(venOrder.getWcsOrderId());
				
				String s =  sdf.format(venOrder.getOrderTimestamp());
		        StringBuilder sb = new StringBuilder(s);
				fraudCase.setOrderTimeStamp(sb.toString());
				
				//Ven customer
				fraudCase.setCustomerName(venOrder.getVenCustomer().getVenParty().getFullOrLegalName());
				
				//Ven customer's address
				VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) locator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");
				
				List<VenOrderAddress> orderAddressList = orderAddressSessionHome.queryByRange("select o from VenOrderAddress o where o.venOrder.orderId = "+venOrder.getOrderId(), 0, 1);
				VenAddress venAddress;
				if (orderAddressList.size()>0) {
					venAddress= orderAddressList.get(0).getVenAddress();
				}else{
					VenPartyAddressSessionEJBRemote partyAddressSessionHome = (VenPartyAddressSessionEJBRemote) locator.lookup(VenPartyAddressSessionEJBRemote.class, "VenPartyAddressSessionEJBBean");
					List<VenPartyAddress> partyAddressList = partyAddressSessionHome.queryByRange("select o from VenPartyAddress o where o.venParty.partyId = (select a.venParty.partyId from VenCustomer a where a.customerId = (select b.venCustomer.customerId from VenOrder b where b.wcsOrderId ='"+venOrder.getWcsOrderId()+"')) ", 0, 1);
					venAddress = partyAddressList.get(0).getVenAddress();
				}
				fraudCase.setCustomerAddress1(venAddress.getStreetAddress1());
				fraudCase.setCustomerAddress2(venAddress.getStreetAddress2());
				fraudCase.setCustomerKecamatanKelurahan(venAddress.getKecamatan() + ", " + venAddress.getKelurahan());
				fraudCase.setCustomerCityPostalCode((venAddress.getVenCity()!=null?venAddress.getVenCity().getCityName():"") + " " + venAddress.getPostalCode());
				fraudCase.setCustomerStateCountry((venAddress.getVenState()!=null?venAddress.getVenState().getStateName():"") + ", " + (venAddress.getVenCountry()!=null?venAddress.getVenCountry().getCountryName():""));// + " " + venAddress.getVenCountry().getCountryCode());
				
				//Ven customer's contact details
				VenContactDetailSessionEJBRemote contactDetailSessionHome = (VenContactDetailSessionEJBRemote) locator.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");			
			
				//Find Order by Order Id			
				List<VenContactDetail> contactDetailList = contactDetailSessionHome.queryByRange("select o from VenContactDetail o where o.contactDetailId in (select o.venContactDetail.contactDetailId from VenOrderContactDetail o where o.venOrder.wcsOrderId = '"+venOrder.getWcsOrderId()+"')",0,0);
				if(contactDetailList.size()<1){
					contactDetailList = contactDetailSessionHome.queryByRange("select o from VenContactDetail o where o.venParty.partyId in (select a.venParty.partyId from VenCustomer a where a.customerId = (select b.venCustomer.customerId from VenOrder b where b.wcsOrderId ='"+venOrder.getWcsOrderId()+"'))",0,0);
				}
				List<OrderHistoryContactDetail> orderContactDetailList = new ArrayList<OrderHistoryContactDetail>();
				for (VenContactDetail venContactDetail : contactDetailList) {
					orderContactDetailList.add(new OrderHistoryContactDetail(venContactDetail.getVenContactDetailType().getContactDetailTypeDesc(), venContactDetail.getContactDetail()));
				}
				fraudCase.setContactDetailList(orderContactDetailList);
								
				//Ven order item
				VenOrderItemSessionEJBRemote venOrderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
				
				List<VenOrderItem> venOrderItemList = null;
				venOrderItemList = venOrderItemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId = " + venOrder.getOrderId().toString(), 0, 0);
				
				//recipient dan shipping address dikeluarkan dari detail order item dan dipindahkan ke customer detail 
				fraudCase.setShippingType(venOrderItemList.get(0).getLogLogisticService().getLogisticsServiceDesc());
				
				//Recipient
				fraudCase.setRecipientName(venOrderItemList.get(0).getVenRecipient().getVenParty().getFullOrLegalName());
				
				//Shipping address
					VenAddress shippingAddress = venOrderItemList.get(0).getVenAddress();
					fraudCase.setRecipientAddress1(shippingAddress.getStreetAddress1());
					fraudCase.setRecipientAddress2(shippingAddress.getStreetAddress2());
					fraudCase.setRecipientKecamatanKelurahan(shippingAddress.getKecamatan() + ", " + shippingAddress.getKelurahan());
					fraudCase.setRecipientCityPostalCode(shippingAddress.getVenCity().getCityName() + " " + shippingAddress.getPostalCode());
					fraudCase.setRecipientStateCountry(shippingAddress.getVenState().getStateName() + ", " + shippingAddress.getVenCountry().getCountryName());// + " " + shippingAddress.getVenCountry().getCountryCode());
				
				List<OrderHistoryOrderItem> orderItemList = new ArrayList<OrderHistoryOrderItem>();
				for (int i = 0; i < venOrderItemList.size(); i++) {
					OrderHistoryOrderItem orderItem = new OrderHistoryOrderItem();
					
					//Order item
					VenOrderItem venOrderItem = venOrderItemList.get(i);
					orderItem.setOrderItemId(venOrderItem.getWcsOrderItemId());
					orderItem.setProductSku(venOrderItem.getVenMerchantProduct().getWcsProductSku());
					orderItem.setProductName(venOrderItem.getVenMerchantProduct().getWcsProductName());
					orderItem.setQuantity(venOrderItem.getQuantity());
					orderItem.setPrice(venOrderItem.getPrice());
					orderItem.setTotalPrice(venOrderItem.getTotal());
					
					orderItemList.add(orderItem);
				}
				
				fraudCase.setOrderItemList(orderItemList);
				
				//Ven payment
				VenOrderPaymentAllocationSessionEJBRemote venOrderPaymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
				
				List<VenOrderPaymentAllocation> venOrderPaymentAllocationList = null;
				venOrderPaymentAllocationList = venOrderPaymentAllocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId = " + venOrder.getOrderId().toString(), 0, 0);
									
				//Order item
				if(!venOrderPaymentAllocationList.isEmpty()){
					VenOrderPayment venOrderPayment = venOrderPaymentAllocationList.get(0).getVenOrderPayment();
					fraudCase.setPaymentType(venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeDesc());
					
					switch ((int) venOrderPayment.getVenPaymentType().getPaymentTypeId().longValue()) {
						case 0:
							String creditCardNumber=venOrderPayment.getMaskedCreditCardNumber();
							String binNumber="";
							if(creditCardNumber.length()>6){
								binNumber = creditCardNumber.substring(0, 6);
							}
							fraudCase.setPaymentInfo(creditCardNumber);
							VenBinCreditLimitEstimateSessionEJBRemote venBinCreditLimitEstimateSessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");
							
							List<VenBinCreditLimitEstimate> venBinCreditLimitEstimateList = null;
							venBinCreditLimitEstimateList = venBinCreditLimitEstimateSessionHome.queryByRange("select o from VenBinCreditLimitEstimate o where o.binNumber = '" + binNumber+"'", 0, 0);
							fraudCase.setCardType(venBinCreditLimitEstimateList.get(venBinCreditLimitEstimateList.size()-1).getVenCardType().getCardTypeDesc());
							fraudCase.setIssuingBank(venBinCreditLimitEstimateList.get(venBinCreditLimitEstimateList.size()-1).getBankName());
							break;
						case 2:
							fraudCase.setPaymentInfo(venOrderPayment.getVirtualAccountNumber());
							break;
						case 1:
							fraudCase.setInternetBankingId(venOrderPayment.getInternetBankingId());
							break;
						default:
							fraudCase.setPaymentInfo("-");
							break;
					}					
					fraudCase.setAmount(venOrderPaymentAllocationList.get(0).getAllocationAmount());
					fraudCase.setHandlingFee(venOrderPaymentAllocationList.get(0).getVenOrderPayment().getAmount().subtract(fraudCase.getAmount()));
					
					//Billing address
					venAddress = venOrderPayment.getVenAddress();
					if(venAddress!=null){
						fraudCase.setBillingAddress1(venAddress.getStreetAddress1());
						fraudCase.setBillingAddress2(venAddress.getStreetAddress2());
						fraudCase.setBillingKecamatanKelurahan(venAddress.getKecamatan() + ", " + venAddress.getKelurahan());
						fraudCase.setBillingCityPostalCode(venAddress.getVenCity().getCityName() + " " + venAddress.getPostalCode());
						fraudCase.setBillingStateCountry(venAddress.getVenState().getStateName() + ", " + venAddress.getVenCountry().getCountryName());
					}
				}
				
				//Add blacklist reason data
				FrdBlacklistReasonSessionEJBRemote frdBlacklistReasonSessionHome = (FrdBlacklistReasonSessionEJBRemote) locator.lookup(FrdBlacklistReasonSessionEJBRemote.class, "FrdBlacklistReasonSessionEJBBean");			
				List<FrdBlacklistReason> partyList = frdBlacklistReasonSessionHome.queryByRange("select o from FrdBlacklistReason o where o.orderId="+venOrder.getOrderId(), 0, 0);
				if(!partyList.isEmpty()){
					HashMap<Long, String> map = new HashMap<Long, String>();
					for (FrdBlacklistReason frdBlacklistReason : partyList) {
						map.put(frdBlacklistReason.getOrderId(), Util.isNull(frdBlacklistReason.getBlacklistReason(), "").toString());
					}
					sb = new StringBuilder();
					Collection<String> reasonList = map.values();
					while (reasonList.iterator().hasNext()) {
						sb.append(reasonList.iterator().next());
						sb.append(", ");
					}
					fraudCase.setBlacklistReason(sb.toString());
				}
				
				//Add attachment data
				FrdFraudFileAttachmentSessionEJBRemote sessionHome = (FrdFraudFileAttachmentSessionEJBRemote) locator.lookup(FrdFraudFileAttachmentSessionEJBRemote.class, "FrdFraudFileAttachmentSessionEJBBean");
				List<FrdFraudFileAttachment> frdAttachmentList = null;
				List<OrderHistoryAttachment> attachmentList = new ArrayList<OrderHistoryAttachment>();
				if(!fraudCaseId.equals("")){
					frdAttachmentList = sessionHome.queryByRange("select o from FrdFraudFileAttachment o join fetch o.frdFraudSuspicionCase where o.frdFraudSuspicionCase.fraudSuspicionCaseId =  "+ fraudCaseId, 0,0);
					for (FrdFraudFileAttachment frdFraudFileAttachment : frdAttachmentList) {
						attachmentList.add(new OrderHistoryAttachment(frdFraudFileAttachment.getCreatedBy(), frdFraudFileAttachment.getFraudFileAttachmentDescription(), frdFraudFileAttachment.getFileName()));
					}
				}
				fraudCase.setAttachmentList(attachmentList);

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

	public void generateReport(String fraudCaseId, String orderId) {
		try {
			//Load compiled report design (.jasper file)
			InputStream inputStreamMain = getClass().getResourceAsStream("./FraudOrderHistoryReport.jasper");
			JasperReport jasperReportMain = (JasperReport) JRLoader.loadObject(inputStreamMain);
			
			InputStream inputStreamSubOrderItem = getClass().getResourceAsStream("./FraudOrderHistoryOrderItemSubReport.jasper");
			JasperReport jasperReportSubOrderItem = (JasperReport) JRLoader.loadObject(inputStreamSubOrderItem);
			
			InputStream inputStreamSubRiskPoint = getClass().getResourceAsStream("./FraudOrderHistoryRiskPointSubReport.jasper");
			JasperReport jasperReportSubRiskPoint = (JasperReport) JRLoader.loadObject(inputStreamSubRiskPoint);

			InputStream inputStreamSubContactDetail = getClass().getResourceAsStream("./FraudOrderHistoryContactDetailSubReport.jasper");
			JasperReport jasperReportSubContactDetail = (JasperReport) JRLoader.loadObject(inputStreamSubContactDetail);
			
			InputStream inputStreamSubAttachment = getClass().getResourceAsStream("./FraudOrderHistoryAttachmentSubReport.jasper");
			JasperReport jasperReportSubAttachment= (JasperReport) JRLoader.loadObject(inputStreamSubAttachment);
			
			//Put report's parameters 
			HashMap<String, Object> parameters = new HashMap<String, Object> ();
			parameters.put("SUBREPORT_ORDER_ITEM", jasperReportSubOrderItem);
			parameters.put("SUBREPORT_RISK_POINT", jasperReportSubRiskPoint);
			parameters.put("SUBREPORT_CONTACT_DETAIL", jasperReportSubContactDetail);
			parameters.put("SUBREPORT_ATTACHMENT", jasperReportSubAttachment);
			
			//Generate bean from EJB
			List<OrderHistory> fraudCaseDataCollection = getReportData(fraudCaseId, orderId);
			JRBeanCollectionDataSource dataCollection = new JRBeanCollectionDataSource(fraudCaseDataCollection);
			
			//Generate report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReportMain, parameters, dataCollection);
			
			//Export to PDF
			String wcsOrderId = fraudCaseDataCollection.size() == 1 ? ("_" + fraudCaseDataCollection.get(0).getWcsOrderId()) : "";
			String formatString = "yyyy.MM.dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(formatString);
			
			resultFileName = "Order_History_" + wcsOrderId + "_" + sdf.format(new Date()) + ".pdf";
			JasperExportManager.exportReportToPdfFile(jasperPrint, resultFilePath + resultFileName);
						
			this.isFinishAndSuccess = true;
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}