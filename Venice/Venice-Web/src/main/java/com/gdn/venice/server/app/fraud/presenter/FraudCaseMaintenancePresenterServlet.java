package com.gdn.venice.server.app.fraud.presenter;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.fraud.presenter.commands.AddFraudCaseActionLogDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.AddFraudCaseHistoryLogData;
import com.gdn.venice.server.app.fraud.presenter.commands.AddFraudCaseRelatedOrderDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.DeleteFraudCaseActionLogDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.DeleteFraudCaseAttachmentDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.DeleteFraudCaseRelatedOrderDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchClaimedFraudCaseDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseActionLogDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseActionLogDetailDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseAttachmentDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseCategoryDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseCustomerAddressDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseCustomerContactDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseCustomerDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseFraudManagementDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseHistoryLogData;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseIlogRecomendationDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseMerchantDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseMoreInfoOrderDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseOrderDetailDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseOrderItemDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCasePaymentDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCasePaymentDetailDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCasePaymentSummaryDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCasePaymentTypeData;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseProductCategoryDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseProductDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseRelatedDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseRelatedOrderDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseRelatedOrderItemDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseRiskScoreDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseStatusComboBoxDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudCaseTotalRiskScoreDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudProcessingHistory;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchOrderCalculateFraud;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchOrderHistoryFilter;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchOrderItemInformation;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchPartyData;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchSameOrderHistory;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchTotalRiskPoint;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchValueToFussionChart;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchWHiteListOrderHistory;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchWhyBlackList;
import com.gdn.venice.server.app.fraud.presenter.commands.GetCaseOrderHistory;
import com.gdn.venice.server.app.fraud.presenter.commands.PrintFraudAllOrderHistoryData;
import com.gdn.venice.server.app.fraud.presenter.commands.PrintFraudOrderHistoryData;
import com.gdn.venice.server.app.fraud.presenter.commands.SaveGenuineDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateFraudCaseActionLogDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateFraudCaseAttachmentDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateFraudCaseFraudManagementDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateFraudCaseViewerFraudManagementDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateOrderStatusDataCommand;
import com.gdn.venice.server.app.fraud.report.FraudCaseReport;
import com.gdn.venice.server.app.fraud.report.FraudOrderHistoryReport;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FraudCaseMaintenancePresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FraudCaseMaintenancePresenterServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String method = request.getParameter("method");

    	if (method.equals("downloadFraudCaseReport")) {
			String filePath = System.getenv("VENICE_HOME") + "/files/report/fraud/";
			String fileName = request.getParameter("filename");

			File file = new File(filePath + fileName);
			ServletOutputStream op = response.getOutputStream();
			ServletContext context = getServletConfig().getServletContext();
			String mimetype = context.getMimeType(fileName);

			//Set the response
			response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
			response.setContentLength((int) file.length());
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			//Stream to the requester.
			int length = 0;
			byte[] bbuf = new byte[1024];
			DataInputStream in = new DataInputStream(new FileInputStream(file));

			while ((in != null) && ((length = in.read(bbuf)) != -1)) {
				op.write(bbuf, 0, length);
			}

            in.close();
            file.delete();
            
            op.flush();
            op.close();
    	}
	}
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String retVal =  "";
	
		if(type.equals("downloadFraudOrderHistory")){
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("oid", request.getParameter("oid"));
    		params.put("filterid", request.getParameter("filterid"));		
    		params.put("filter",  request.getParameter("filter"));	
    		
    		String reportFileName = request.getParameter("filename");    					
			OutputStream out = null;
			try{
					
			String outputFileName = reportFileName +System.currentTimeMillis() + ".xls";
			
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition",  "attachment; filename="+outputFileName);
		   
		    HSSFWorkbook wb = new HSSFWorkbook();
		    HSSFSheet sheet = wb.createSheet(""+reportFileName);	    
		 
		     out = response.getOutputStream();	
		     PrintFraudOrderHistoryData printFraudOrderHistoryData =new PrintFraudOrderHistoryData(wb);
		    
		     wb  =  printFraudOrderHistoryData.ExportExcel(params, sheet);     
		     wb.write(out);
			
			} catch (Exception e)   {
				throw new ServletException("Exception in Excel FraudCaseMaintenancePresenterServlet", e);
		    } finally   {
			     if (out != null)
			      out.close();		    
					
		    }
			
    	}else if (type.equals("downloadFraudAllOrderHistory")){

    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("statusId", request.getParameter("statusId"));
    		params.put("from", request.getParameter("from"));		
    		params.put("end",  request.getParameter("end"));	
    		
			OutputStream out = null;
			try{
				
			String outputFileName = "FraudAllOrderHistory" +System.currentTimeMillis() + ".xls";
			
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition",  "attachment; filename="+outputFileName);
		   
		    HSSFWorkbook wb = new HSSFWorkbook();
		    HSSFSheet sheet = wb.createSheet("FraudAllOrderHistory");	    
		 
		     out = response.getOutputStream();	
		     PrintFraudAllOrderHistoryData printFraudAllOrderHistoryData =new PrintFraudAllOrderHistoryData(wb);
		    
		     wb  =  printFraudAllOrderHistoryData.ExportExcel(params, sheet);     
		     wb.write(out);
			
			} catch (Exception e)   {
				throw new ServletException("Exception in Excel FraudCaseMaintenancePresenterServlet", e);
		    } finally   {
			     if (out != null)
			      out.close();		    
					
		    }
    	}
		
		if (type.equals(RafDsCommand.DataSource)) {
			String requestBody = Util.extractRequestBody(request);
	
			RafDsRequest rafDsRequest = null;
			try {
				rafDsRequest = RafDsRequest.convertXmltoRafDsRequest(requestBody);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID) != null) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
				
				if (request.getParameter(DataNameTokens.TASKID) != null) {
					params.put(DataNameTokens.TASKID, request.getParameter(DataNameTokens.TASKID));
				}
				if (request.getParameter(DataNameTokens.VENORDER_IPADDRESS) != null) {
					params.put(DataNameTokens.VENORDER_IPADDRESS, request.getParameter(DataNameTokens.VENORDER_IPADDRESS));
				}
				if (request.getParameter(DataNameTokens.VENORDER_WCSORDERID) != null) {
					params.put(DataNameTokens.VENORDER_WCSORDERID, request.getParameter(DataNameTokens.VENORDER_WCSORDERID));
				}
				if (request.getParameter(DataNameTokens.VENORDER_AMOUNT) != null) {
					params.put(DataNameTokens.VENORDER_AMOUNT, request.getParameter(DataNameTokens.VENORDER_AMOUNT));
				}
				if (request.getParameter(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE) != null) {
					params.put(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, request.getParameter(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE));
				}
				if (request.getParameter(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME) != null) {
					params.put(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME, request.getParameter(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME));
				}
				if (request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERMOBILEPHONE) != null) {
					params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERMOBILEPHONE, request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERMOBILEPHONE));
				}
				if (request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMEREMAIL) != null) {
					params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMEREMAIL, request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMEREMAIL));
				}
				if (request.getParameter(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY) != null) {
					params.put(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY, request.getParameter(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY));
				}
				if (request.getParameter(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME) != null) {
					params.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, request.getParameter(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME));
				}
				if (request.getParameter(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VAORIBORCCNUMBER) != null) {
					params.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VAORIBORCCNUMBER, request.getParameter(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VAORIBORCCNUMBER));
				}
				if (request.getParameter(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME) != null) {
					params.put(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME, request.getParameter(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME));
				}
				if (request.getParameter(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTHP) != null) {
					params.put(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTHP, request.getParameter(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTHP));
				}
				if (request.getParameter(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTEMAIL) != null) {
					params.put(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTEMAIL, request.getParameter(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTEMAIL));
				}
				if (request.getParameter(DataNameTokens.VENORDER_ORDERDATE) != null) {
					params.put(DataNameTokens.VENORDER_ORDERDATE, request.getParameter(DataNameTokens.VENORDER_ORDERDATE));
				}
				if (request.getParameter(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME) != null) {
					params.put(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME, request.getParameter(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME));
				}
				if (request.getParameter(DataNameTokens.VENMERCHANT_VENPARTY_FULLORLEGALNAME) != null) {
					params.put(DataNameTokens.VENMERCHANT_VENPARTY_FULLORLEGALNAME, request.getParameter(DataNameTokens.VENMERCHANT_VENPARTY_FULLORLEGALNAME));
				}
				if (request.getParameter(DataNameTokens.VENORDERITEM_VENADDRESS) != null) {
					params.put(DataNameTokens.VENORDERITEM_VENADDRESS, request.getParameter(DataNameTokens.VENORDERITEM_VENADDRESS));
				}
				if (request.getParameter(DataNameTokens.VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSERVICETYPE_LOGISTICSERVICETYPEDESC) != null) {
					params.put(DataNameTokens.VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSERVICETYPE_LOGISTICSERVICETYPEDESC, request.getParameter(DataNameTokens.VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSERVICETYPE_LOGISTICSERVICETYPEDESC));
				}
				
				rafDsRequest.setParams(params);
			} else if (request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONCASEID) != null) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONCASEID, request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONCASEID));
				rafDsRequest.setParams(params);			
			} else if (request.getParameter(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY) != null) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY, request.getParameter(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY));
				rafDsRequest.setParams(params);			
			} else if(request.getParameter(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID) != null){
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID, request.getParameter(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID));
				rafDsRequest.setParams(params);
			}
			
			String method = request.getParameter("method");
			if (method.equals("fetchFraudCaseData")) {
				String username = Util.getUserName(request);
				String password = BPMAdapter.getUserPasswordFromLDAP(username);
				
				RafDsCommand fetchFraudCaseDataCommand = new FetchFraudCaseDataCommand(rafDsRequest, username, password);
				RafDsResponse rafDsResponse = fetchFraudCaseDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchClaimedFraudCaseData")) {
				String username = Util.getUserName(request);
				String password = BPMAdapter.getUserPasswordFromLDAP(username);
				
				RafDsCommand fetchClaimedFraudCaseDataCommand = new FetchClaimedFraudCaseDataCommand(rafDsRequest, username, password);
				RafDsResponse rafDsResponse = fetchClaimedFraudCaseDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseCustomerSummaryData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchFraudCaseCustomerDataCommand = new FetchFraudCaseCustomerDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseCustomerDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchFraudCasePaymentSummaryData")) {
				RafDsCommand fetchFraudCasePaymentSummaryDataCommand = new FetchFraudCasePaymentSummaryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCasePaymentSummaryDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseCustomerData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchFraudCaseCustomerDataCommand = new FetchFraudCaseCustomerDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseCustomerDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseCustomerAddressData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));				
				rafDsRequest.setParams(params);
				RafDsCommand fetchFraudCaseCustomerAddressDataCommand = new FetchFraudCaseCustomerAddressDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseCustomerAddressDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseCustomerContactData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchFraudCaseCustomerContactDataCommand = new FetchFraudCaseCustomerContactDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseCustomerContactDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseOrderDetailData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchFraudCaseOrderDetailDataCommand = new FetchFraudCaseOrderDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseOrderDetailDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseOrderItemData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchFraudCaseOrderItemDataCommand = new FetchFraudCaseOrderItemDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseOrderItemDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseCategoryData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				rafDsRequest.setParams(params);				
				RafDsCommand fetchFraudCaseCategoryDataCommand = new FetchFraudCaseCategoryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseCategoryDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseRelatedOrderItemData")) {
				RafDsCommand fetchFraudCaseRelatedOrderItemDataCommand = new FetchFraudCaseRelatedOrderItemDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseRelatedOrderItemDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseRelatedData")) {
				RafDsCommand fetchFraudCaseRelatedDataCommand = new FetchFraudCaseRelatedDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseRelatedDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseFinancePaymentData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchFraudCasePaymentDataCommand = new FetchFraudCasePaymentDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCasePaymentDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchFraudCaseRiskScoreData")) {
				RafDsCommand fetchFraudCaseRiskScoreDataCommand = new FetchFraudCaseRiskScoreDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseRiskScoreDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchFraudCasePaymentDetailData")) {
				RafDsCommand fetchFraudCasePaymentDetailDataCommand = new FetchFraudCasePaymentDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCasePaymentDetailDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseTotalRiskScoreData")) {
				RafDsCommand fetchFraudCaseTotalRiskScoreDataCommand = new FetchFraudCaseTotalRiskScoreDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseTotalRiskScoreDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseRelatedOrderData")) {
				RafDsCommand fetchFraudCaseRelatedOrderDataCommand = new FetchFraudCaseRelatedOrderDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseRelatedOrderDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseFraudManagementData")) {
				//Dapatkan username dan password
				String username = Util.getUserName(request);
				String password = BPMAdapter.getUserPasswordFromLDAP(username);
				
				RafDsCommand fetchFraudCaseFraudManagementDataCommand = new FetchFraudCaseFraudManagementDataCommand(username, password, rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseFraudManagementDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("updateFraudCaseFraudManagementData")) {
				String action = request.getParameter("action") == null ? "" : request.getParameter("action");
				String username = Util.getUserName(request);
				String password = BPMAdapter.getUserPasswordFromLDAP(username);
				
				RafDsCommand updateFraudCaseFraudManagementDataCommand = new UpdateFraudCaseFraudManagementDataCommand(action, username, password, rafDsRequest);
				RafDsResponse rafDsResponse = updateFraudCaseFraudManagementDataCommand.execute();
				
				try {
					if (rafDsResponse.getStatus() == 0)
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					else {
						String errorMessage = "";
						if (action.equalsIgnoreCase("escalate"))
							errorMessage = "Save and escalate fraud case failed!";
						else if (action.equalsIgnoreCase("closecase"))
							errorMessage = "Save and close fraud case failed!";
						else
							errorMessage = "Save fraud case failed!";
						
						retVal = "<response><status>0</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}  else if (method.equals("updateFraudCaseViewerFraudManagementData")) {
				String username = Util.getUserName(request);
				String password = BPMAdapter.getUserPasswordFromLDAP(username);
				
				RafDsCommand updateFraudCaseViewerFraudManagementDataCommand = new UpdateFraudCaseViewerFraudManagementDataCommand(username, password, rafDsRequest);
				RafDsResponse rafDsResponse = updateFraudCaseViewerFraudManagementDataCommand.execute();
				
				try {
					if (rafDsResponse.getStatus() == 0)
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					else {
						String errorMessage = "Save fraud case failed!";	
						retVal = "<response><status>0</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseActionLogData")) {
				RafDsCommand fetchFraudCaseActionLogData = new FetchFraudCaseActionLogDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseActionLogData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("addFraudCaseActionLogData")) {
				String username = Util.getUserName(request);
				RafDsCommand addFraudCaseActionLogData = new AddFraudCaseActionLogDataCommand(rafDsRequest, username);
				RafDsResponse rafDsResponse = addFraudCaseActionLogData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}  else if (method.equals("updateFraudCaseActionLogData")) {
				String username = Util.getUserName(request);
				RafDsCommand updateFraudCaseActionLogData = new UpdateFraudCaseActionLogDataCommand(rafDsRequest, username);
				RafDsResponse rafDsResponse = updateFraudCaseActionLogData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("deleteFraudCaseActionLogData")) {
				String username = Util.getUserName(request);
				RafDsCommand deleteFraudCaseActionLogData = new DeleteFraudCaseActionLogDataCommand(rafDsRequest, username);
				RafDsResponse rafDsResponse = deleteFraudCaseActionLogData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}   else if (method.equals("fetchFraudCaseActionLogDetailData")) {
				RafDsCommand fetchFraudCaseActionLogDetailData = new FetchFraudCaseActionLogDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseActionLogDetailData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseHistoryLogData")) {
				RafDsCommand fetchFraudCaseHistoryLogData = new FetchFraudCaseHistoryLogData(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseHistoryLogData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}  else if (method.equals("addFraudCaseHistoryLogData")) {
				RafDsCommand addFraudCaseHistoryLogData = new AddFraudCaseHistoryLogData(rafDsRequest);
				RafDsResponse rafDsResponse = addFraudCaseHistoryLogData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCasePaymentTypeData")) {
				RafDsCommand fetchFraudCasePaymentTypeData = new FetchFraudCasePaymentTypeData(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCasePaymentTypeData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if(method.equals("deleteFraudCaseRelatedOrderData")){										
				RafDsCommand deleteFraudCaseRelatedOrderDataCommand = new DeleteFraudCaseRelatedOrderDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteFraudCaseRelatedOrderDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseMoreInfoOrderData")) {
				RafDsCommand fetchFraudCaseMoreInfoOrderDataCommand = new FetchFraudCaseMoreInfoOrderDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseMoreInfoOrderDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseProductCategoryData")) {
				RafDsCommand fetchFraudCaseProductCategoryDataCommand = new FetchFraudCaseProductCategoryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseProductCategoryDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseProductData")) {
				RafDsCommand fetchFraudCaseProductDataCommand = new FetchFraudCaseProductDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseProductDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFraudCaseMerchantData")) {
				RafDsCommand fetchFraudCaseMerchantDataCommand = new FetchFraudCaseMerchantDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseMerchantDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	else if (method.equals("fetchFraudCaseIlogRecomendationData")) {
				RafDsCommand fetchFraudCaseIlogRecomendationDataCommand = new FetchFraudCaseIlogRecomendationDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseIlogRecomendationDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	else if (method.equals("fetchFraudCaseAttachmentData")) {
				RafDsCommand fetchFraudCaseAttachmentDataCommand = new FetchFraudCaseAttachmentDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudCaseAttachmentDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	else if (method.equals("deleteFraudCaseAttachmentData")) {
				RafDsCommand deleteFraudCaseAttachmentDataCommand = new DeleteFraudCaseAttachmentDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteFraudCaseAttachmentDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	else if (method.equals("updateFraudCaseAttachmentData")) {
				RafDsCommand updateFraudCaseAttachmentDataCommand = new UpdateFraudCaseAttachmentDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateFraudCaseAttachmentDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	else if (method.equals("fetchPartyData")) {
				RafDsCommand fetchPartyData = new FetchPartyData(rafDsRequest);
				RafDsResponse rafDsResponse = fetchPartyData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchWHiteListOrderHistory")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDER_WCSORDERID, request.getParameter(DataNameTokens.VENORDER_WCSORDERID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchWHiteListOrderHistory = new FetchWHiteListOrderHistory(rafDsRequest);
				RafDsResponse rafDsResponse = fetchWHiteListOrderHistory.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchOrderHistoryFilter")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDER_WCSORDERID, request.getParameter(DataNameTokens.VENORDER_WCSORDERID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchOrderHistoryFilter = new FetchOrderHistoryFilter(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderHistoryFilter.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	else if (method.equals("fetchSameOrderHistory")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDER_WCSORDERID, request.getParameter(DataNameTokens.VENORDER_WCSORDERID));
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				params.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, request.getParameter(DataNameTokens.ORDERHISTORY_STRINGFILTER));
				rafDsRequest.setParams(params);
				RafDsCommand fetchSameOrderHistory = new FetchSameOrderHistory(rafDsRequest);
				RafDsResponse rafDsResponse = fetchSameOrderHistory.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	else if (method.equals("fetchOrderItemInformation")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchOrderItemInformation = new FetchOrderItemInformation(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderItemInformation.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	else if (method.equals("fetchTotalRiskPoint")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, request.getParameter(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchTotalRiskPoint = new FetchTotalRiskPoint(rafDsRequest);
				RafDsResponse rafDsResponse = fetchTotalRiskPoint.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	else if (method.equals("fetchWhyBlackList")) {
				HashMap<String, String> params = new HashMap<String, String>();				
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchWhyBlackList = new FetchWhyBlackList(rafDsRequest);
				RafDsResponse rafDsResponse = fetchWhyBlackList.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	else if (method.equals("fetchOrderCalculateFraud")) {
				HashMap<String, String> params = new HashMap<String, String>();							
				params.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERDATE, request.getParameter(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERDATE));
				rafDsRequest.setParams(params);			
				RafDsCommand fetchOrderCalculateFraud = new FetchOrderCalculateFraud(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderCalculateFraud.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}					
			
		}
		else if (type.equals(RafRpcCommand.RPC)) {
			String requestBody = Util.extractRequestBody(request);
			String method = request.getParameter("method");
			
			if (method.equalsIgnoreCase("printFraudCaseData")) {
				String fraudCaseId = request.getParameter("fraudcaseid") == null ? "" : request.getParameter("fraudcaseid");
				FraudCaseReport fraudCaseReport = new FraudCaseReport();
				fraudCaseReport.generateReport(fraudCaseId);
				
				retVal = fraudCaseReport.isFinishAndSuccess() ? fraudCaseReport.getResultFileName() : "";
			} else if (method.equals("updateOrderStatusToSF") || method.equals("updateOrderStatusToFP") ||  method.equals("updateOrderStatusToFC")) {
				String username = Util.getUserName(request);
				RafRpcCommand updateOrderStatusDataCommand = new UpdateOrderStatusDataCommand(requestBody, method, username);
				retVal = updateOrderStatusDataCommand.execute();
			} else if(method.equalsIgnoreCase("addFraudCaseRelatedOrderData")){
				RafRpcCommand addFraudCaseRelatedOrderDataCommand = new AddFraudCaseRelatedOrderDataCommand(requestBody);
				retVal = addFraudCaseRelatedOrderDataCommand.execute();
			} else if(method.equals("fetchFraudCaseStatusComboBoxData")){	
				RafRpcCommand fetchFraudCaseStatusComboBoxDataCommand = new FetchFraudCaseStatusComboBoxDataCommand();
				retVal = fetchFraudCaseStatusComboBoxDataCommand.execute();				
			}else if(method.equalsIgnoreCase("fetchValueToFussionCharToFraudCaset")){
				RafRpcCommand fetchValueToFussionChart = new FetchValueToFussionChart();
				retVal = fetchValueToFussionChart.execute();
			} else if(method.equalsIgnoreCase("onFectFraudProcessingHistory")){			
				RafRpcCommand fectFraudProcessingHistory = new FetchFraudProcessingHistory(requestBody);
				retVal = fectFraudProcessingHistory.execute();
			} else if(method.equalsIgnoreCase("getCaseid")){			
				String orderId=request.getParameter("orderId");
				RafRpcCommand getCaseOrderHistory = new GetCaseOrderHistory(orderId);
				retVal = getCaseOrderHistory.execute();
			} else if (method.equalsIgnoreCase("printFraudOrderHistoryData")) {
				String fraudCaseId = request.getParameter("fraudcaseid"),
						wcsOrderId = request.getParameter("wcsorderid");
				FraudOrderHistoryReport fraudOrderHistoryReport = new FraudOrderHistoryReport();
				fraudOrderHistoryReport.generateReport(fraudCaseId, wcsOrderId);				
				retVal = fraudOrderHistoryReport.isFinishAndSuccess() ? fraudOrderHistoryReport.getResultFileName() : "";
			} else if(method.equalsIgnoreCase("saveGenuine")){		
				RafRpcCommand saveGenuine = new SaveGenuineDataCommand(requestBody);
				retVal = saveGenuine.execute();
			} 
		}
		response.getOutputStream().println(retVal);
	}

}
