package com.gdn.venice.server.app.fraud.presenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.server.app.fraud.rule.FraudRules;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.util.VeniceConstants;

public class FraudCalculatePresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Logger _log = null;
       
    public FraudCalculatePresenterServlet() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.presenter.FraudCalculatePresenterServlet");
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String untuk return
		_log.info("start FraudCalculatePresenterServlet");
		String result="Calculate risk point success.";		
		String failedNotification="Calculate risk point success."; 
		String dateParam = request.getParameter("Date");
		boolean isSuccessCalculate=false;
		Locator<Object> locator = null;
		ArrayList<String> failedMessage = new ArrayList<String>();
		_log.debug("Date parameter: "+dateParam);
		try {
			locator = new Locator<Object>();
			VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			//Ambil order yang:
			//	- Status C (Confirmed)
			//	- Tanggal lebih besar dari tanggal berasal dari user parameter
			List<VenOrder> orderList = null;
			String selectOrder = "select o from VenOrder o where o.venOrderStatus.orderStatusId = "+VeniceConstants.VEN_ORDER_STATUS_C+" and o.orderDate >= '" + dateParam + "'";
//			String selectOrder = "select o from VenOrder o where o.wcsOrderId='654013'";

			orderList = orderSessionHome.queryByRange(selectOrder, 0, 0);
			_log.debug("order list size: "+orderList.size());
			for (int i = 0; i < orderList.size(); i++) {
				VenOrder venOrder = orderList.get(i);				
				
				//Cek apakah payment CC atau bukan, jika salah satu paymentnya adalah Credit Card, pastikan MIGS sudah diupload terlebih dahulu
				//Jika payment CC dan MIGS sudah diupload, baru calculate, selain itu jangan di calculate
				boolean isPaymentTypeCCandCompleteData = false;
				try {
					locator = new Locator<Object>();
					VenOrderPaymentSessionEJBRemote venOrderPaymentSessionHome = (VenOrderPaymentSessionEJBRemote) locator.lookup(VenOrderPaymentSessionEJBRemote.class, "VenOrderPaymentSessionEJBBean");
					
					List<VenOrderPayment> orderPaymentList = null;
					String queryOrderPayment = "select op from VenOrderPaymentAllocation opa inner join opa.venOrder o inner join opa.venOrderPayment op where o.orderId = " +  venOrder.getOrderId().toString();
					orderPaymentList = venOrderPaymentSessionHome.queryByRange(queryOrderPayment, 0, 0);
					for (int j = 0; j < orderPaymentList.size(); j++) {
						VenOrderPayment venOrderPayment = orderPaymentList.get(j);
						if (venOrderPayment.getVenPaymentType().getPaymentTypeId().longValue() == VeniceConstants.VEN_PAYMENT_TYPE_ID_CC) {
							if(venOrderPayment.getMaskedCreditCardNumber()!=null && !venOrderPayment.getMaskedCreditCardNumber().trim().equalsIgnoreCase("")){
								isPaymentTypeCCandCompleteData = true;
							}else{
								_log.info("wcs order id: "+ venOrder.getWcsOrderId()+" has payment type CC but has MIGS data not uploaded yet");
							}
						}else{
							_log.info("wcs order id: "+ venOrder.getWcsOrderId()+" has payment type not CC");
						}
					}
				} catch (Exception e) {
					String errMsg="Calculate risk point failed, problem occured when checking payment CC list";								
					_log.error(errMsg);					
					e.printStackTrace();
					result = errMsg;	
				} 
											
				if (isPaymentTypeCCandCompleteData) {
					_log.info("wcs order id: "+ venOrder.getWcsOrderId()+" has payment type CC and has MIGS data uploaded, proceed to calculate fraud rules");
					//Cek apakah sudah ada di fraud case					
					try {
						locator = new Locator<Object>();
						FrdFraudSuspicionCaseSessionEJBRemote fraudCaseSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
						
						List<FrdFraudSuspicionCase> fraudCaseList = null;
						String selectFraudCase = "select o from FrdFraudSuspicionCase o where o.venOrder.orderId = " + venOrder.getOrderId();
						fraudCaseList = fraudCaseSessionHome.queryByRange(selectFraudCase, 0, 0);
						
						//Jika belum ada di fraud case
						if (fraudCaseList.size() == 0) {										
							_log.info("Start calling fraud rules to calculate wcs order id: "+venOrder.getWcsOrderId());
							FraudRules fraudRules = new FraudRules();
							isSuccessCalculate = fraudRules.calculateFraudRules(venOrder);
							
							if(isSuccessCalculate==false){
								_log.error("Calculate risk point failed, problem occured when executing fraud rules for wcs order id: "+venOrder.getWcsOrderId());
								failedMessage.add(venOrder.getWcsOrderId());
							}else if(isSuccessCalculate==true){
								_log.info("Calculate success");
								//Mendapatkan fraud suspicious case id dari hasil hitungan fraud rules
								selectFraudCase = "select o from FrdFraudSuspicionCase o where o.venOrder.orderId = " + venOrder.getOrderId();
								fraudCaseList = fraudCaseSessionHome.queryByRange(selectFraudCase, 0, 1);
								FrdFraudSuspicionCase fraudSuspicionCase = fraudCaseList.get(0);
								_log.debug("fraud case id is: "+fraudSuspicionCase.getFraudSuspicionCaseId()+", Create task di BPM");
								String username = Util.getUserName(request);
								String password = BPMAdapter.getUserPasswordFromLDAP(username);
								BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(username, password);
								bpmAdapter.synchronize();
								
								HashMap<String, String> taskData = new HashMap<String, String>();
								taskData.put(ProcessNameTokens.FRAUDCASEID, fraudSuspicionCase.getFraudSuspicionCaseId().toString());
								taskData.put(ProcessNameTokens.ORDERID, venOrder.getOrderId().toString());
								taskData.put(ProcessNameTokens.WCSORDERID, venOrder.getWcsOrderId());
								taskData.put(ProcessNameTokens.FRAUDSTATUSID, fraudSuspicionCase.getFrdFraudCaseStatus().getFraudCaseStatusId().toString());
								
								try {
									_log.debug("Starting process in bpm");
									bpmAdapter.startBusinessProcess(ProcessNameTokens.FRAUDMANAGEMENTPROCESS, taskData);
								} catch (Exception e) {
									result = "Calculate risk point failed, problem occured when starting business process";
									_log.error(result);
									e.printStackTrace();
								}
							}
						}else{
							_log.info("Wcs order id: "+venOrder.getWcsOrderId()+" has already been calculated before");
						}
					} catch (Exception e) {						
						String errMsg="Calculate risk point failed, problem occured when getting fraud case list";								
						_log.error(errMsg);					
						e.printStackTrace();
						result = errMsg;	
					} 
				}
			}
			failedNotification += failedMessage.size() > 0 ? failedMessage.size() + " record(s) was not calculated, Order ID:" + failedMessage.toString():"";
			result = failedNotification;
		} catch (Exception e) {
			String errMsg="Calculate risk point failed, problem occured when getting order and payment list";								
			_log.error(errMsg);					
			e.printStackTrace();
			result = errMsg;	
		} finally {
			try {
				if (locator != null) {
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		_log.info("Calculate fraud points done!");				
		response.getOutputStream().println(result);
	}
}