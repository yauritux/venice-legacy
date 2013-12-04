package com.gdn.venice.server.app.fraud.presenter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.SQLDateUtility;
import com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote;
import com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenMigsUploadMaster;
import com.gdn.venice.persistence.VenMigsUploadTemporary;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.app.fraud.dataimport.ExcelToPojo;
import com.gdn.venice.server.app.fraud.dataimport.MigsReport;
import com.gdn.venice.server.app.fraud.dataimport.PojoInterface;
import com.gdn.venice.server.app.fraud.presenter.commands.AddMigsDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchMasterMigsDataDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchMigsUploadTemporaryDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateMigsUploadTemporaryDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;


public class MigsUploadPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String notificationText = "";
    
	public MigsUploadPresenterServlet() {
        super();
    }

	@SuppressWarnings({ "unchecked" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		String retVal =  "";
		if (type.equals(RafDsCommand.DataSource)) {
			String requestBody = Util.extractRequestBody(request);
	
			RafDsRequest rafDsRequest = null;
			try {
				rafDsRequest = RafDsRequest.convertXmltoRafDsRequest(requestBody);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String method = request.getParameter("method");
			if (method.equals("fetchMigsUploadData")) {
				RafDsCommand fetchMigsUploadTemporaryDataCommand = new FetchMigsUploadTemporaryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchMigsUploadTemporaryDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchMasterMigsData")) {				
				RafDsCommand fetchMasterMigsDataDataCommand = new FetchMasterMigsDataDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchMasterMigsDataDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("updateMigsUploadData")) {
				RafDsCommand updateMigsUploadTemporaryDataCommand = new UpdateMigsUploadTemporaryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateMigsUploadTemporaryDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (type.equalsIgnoreCase("rpc")) {
			AddMigsDataCommand addMigsDataCommand = new AddMigsDataCommand(Util.getUserName(request));
			retVal = addMigsDataCommand.execute();
		} else {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			
			notificationText = "<html>\n" + "<head>"
			+ "<title>Processing Report Completed</title>" + "</head>\n"
			+ "<body onload=\"alert('REPLACE')\">" + "<p>TEST</p>\n"
			+ "</body>\n" + "</html>";
			
			if (isMultipart) { // import
				String formatString = "yyyy.MM.dd HH:mm:ss";
				SimpleDateFormat sdf = new SimpleDateFormat(formatString);
				
				String filePath = System.getenv("VENICE_HOME") + "/files/import/migs/";
				String fileName = "MIGS-" + sdf.format(new Date()) + ".xls";
				
				File uploadedFile = new File(filePath + fileName);
				ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
				List<FileItem> fileItemsList = null;

				try {
					fileItemsList = servletFileUpload.parseRequest(request);
				} catch (FileUploadException e) {
					String errMsg = "An exception occured when parsing the sevlet file upload:" + e.getMessage();
					e.printStackTrace();
					notificationText = notificationText.replaceFirst("REPLACE", errMsg);
				}
				
				Iterator<FileItem> iter = fileItemsList.iterator();
				while (iter.hasNext()) {
					FileItem fileItem = (FileItem) iter.next();

					if (!fileItem.isFormField()) {
						try {
							fileItem.write(uploadedFile);
						} catch (Exception e) {
							String errMsg = "An exception occured when writing to file:" + fileName + " :" + e.getMessage();
							e.printStackTrace();
							notificationText = notificationText.replaceFirst("REPLACE", errMsg);
						}
					}
				}
				
				ExcelToPojo x = new ExcelToPojo(System.getenv("VENICE_HOME") + "/files/template/MIGSReport.xml", filePath + fileName, 5, 1, 23, MigsReport.class);
				ArrayList<PojoInterface> result = x.getPojoResult();
				
				if (!x.getErrorMessage().equalsIgnoreCase("")) {
					retVal = notificationText.replaceFirst("REPLACE", x.getErrorMessage());
				} else {
					Locator<Object> locator = null;
					try {
	
						locator = new Locator<Object>();
						VenMigsUploadTemporarySessionEJBRemote migsUploadHome = (VenMigsUploadTemporarySessionEJBRemote) locator.lookup(VenMigsUploadTemporarySessionEJBRemote.class, "VenMigsUploadTemporarySessionEJBBean");
						VenMigsUploadMasterSessionEJBRemote migsUploadMasterHome = (VenMigsUploadMasterSessionEJBRemote) locator.lookup(VenMigsUploadMasterSessionEJBRemote.class, "VenMigsUploadMasterSessionEJBBean");
						
						int numberOfSuccess = 0;
						ArrayList<String> failedMessage = new ArrayList<String>();
						for (PojoInterface element : result) {
							MigsReport migsReport = (MigsReport) element;
							
							String autCode="";
							autCode=autCode+migsReport.getAuthorisationCode();
							if(!autCode.equals("null")){
								if(autCode.length()<6 ){
									autCode="000000".substring(0, 6-autCode.length())+autCode;
								}
							}else{
								autCode=null;
							}
							
							//Cek apakah data MIGS sudah pernah terupload sebelumnya
							String query = "select o from VenMigsUploadMaster o where o.transactionId = '" + isNull(migsReport.getTransactionId(), "") + "' and o.authorisationCode = '" + isNull(autCode, "") + "' and o.transactionId is not null and o.action <> 'REMOVE'";
							List<VenMigsUploadMaster> migsUploadList = migsUploadMasterHome.queryByRange(query, 0, 0);
							boolean isDuplicated = (migsUploadList.size() > 0);
							
							if (isDuplicated) {
								failedMessage.add("Already Exist Order ID: " + isNull(migsReport.getMerchantTransactionReference(), "") + ", Auth Code:" + isNull(migsReport.getAuthorisationCode(), ""));
							} else {								
								VenMigsUploadTemporary newMigsUploadTemporary = new VenMigsUploadTemporary();
								newMigsUploadTemporary.setTransactionId(migsReport.getTransactionId());
								newMigsUploadTemporary.setTransactionDate(SQLDateUtility.utilDateToSqlTimestamp(migsReport.getDate()));
								newMigsUploadTemporary.setMerchantId(migsReport.getMerchantId());
								newMigsUploadTemporary.setOrderReference(migsReport.getOrderReference());
								newMigsUploadTemporary.setOrderId(migsReport.getOrderId());
								newMigsUploadTemporary.setMerchantTransactionReference(migsReport.getMerchantTransactionReference());
								newMigsUploadTemporary.setTransactionType(migsReport.getTransactionType());
								newMigsUploadTemporary.setAcquirerId(migsReport.getAcquirerId());
								newMigsUploadTemporary.setBatchNumber(migsReport.getBatchNumber());
								newMigsUploadTemporary.setCurrency(migsReport.getCurrency());
								newMigsUploadTemporary.setAmount(migsReport.getAmount());
								newMigsUploadTemporary.setRrn(migsReport.getRrn());
								newMigsUploadTemporary.setResponseCode(migsReport.getResponseCode());
								newMigsUploadTemporary.setAcquirerResponseCode(migsReport.getAcquirerResponseCode());
								newMigsUploadTemporary.setAuthorisationCode(autCode);
								newMigsUploadTemporary.setOperator(migsReport.getOperatorId());
								newMigsUploadTemporary.setMerchantTransactionSource(migsReport.getMerchantTransactionSource());
								newMigsUploadTemporary.setOrderDate(SQLDateUtility.utilDateToSqlTimestamp(migsReport.getOrderDate()));
								newMigsUploadTemporary.setCardType(migsReport.getCardType());
								newMigsUploadTemporary.setCardNumber(migsReport.getCardNumber());
								newMigsUploadTemporary.setCardExpiryMonth(migsReport.getCardExpiryMonth());
								newMigsUploadTemporary.setCardExpiryYear(migsReport.getCardExpiryYear());
								newMigsUploadTemporary.setDialectCscResultCode(migsReport.getDialectCSCResultCode());
								newMigsUploadTemporary.setComment(migsReport.getComment());
								newMigsUploadTemporary.setEcommerceIndicator(migsReport.geteCommerceIndicator());
								newMigsUploadTemporary.setFileName(filePath + fileName);
								
								VenMigsUploadMaster newMigsUploadMaster;
								try{
									newMigsUploadMaster = migsUploadMasterHome.queryByRange("select o from VenMigsUploadMaster o where o.transactionId = '" + isNull(migsReport.getTransactionId(), "") + "' and o.authorisationCode = '" + isNull(autCode, "") + "' and o.transactionId is not null", 0, 1).get(0);
								}catch(IndexOutOfBoundsException e){
									newMigsUploadMaster = new VenMigsUploadMaster();
								}
								newMigsUploadMaster.setTransactionId(migsReport.getTransactionId());
								newMigsUploadMaster.setTransactionDate(SQLDateUtility.utilDateToSqlTimestamp(migsReport.getDate()));
								newMigsUploadMaster.setMerchantId(migsReport.getMerchantId());
								newMigsUploadMaster.setOrderReference(migsReport.getOrderReference());
								newMigsUploadMaster.setOrderId(migsReport.getOrderId());
								newMigsUploadMaster.setMerchantTransactionReference(migsReport.getMerchantTransactionReference());
								newMigsUploadMaster.setTransactionType(migsReport.getTransactionType());
								newMigsUploadMaster.setAcquirerId(migsReport.getAcquirerId());
								newMigsUploadMaster.setBatchNumber(migsReport.getBatchNumber());
								newMigsUploadMaster.setCurrency(migsReport.getCurrency());
								newMigsUploadMaster.setAmount(migsReport.getAmount());
								newMigsUploadMaster.setRrn(migsReport.getRrn());
								newMigsUploadMaster.setResponseCode(migsReport.getResponseCode());
								newMigsUploadMaster.setAcquirerResponseCode(migsReport.getAcquirerResponseCode());
								newMigsUploadMaster.setAuthorisationCode(autCode);
								newMigsUploadMaster.setOperator(migsReport.getOperatorId());
								newMigsUploadMaster.setMerchantTransactionSource(migsReport.getMerchantTransactionSource());
								newMigsUploadMaster.setOrderDate(SQLDateUtility.utilDateToSqlTimestamp(migsReport.getOrderDate()));
								newMigsUploadMaster.setCardType(migsReport.getCardType());
								newMigsUploadMaster.setCardNumber(migsReport.getCardNumber());
								newMigsUploadMaster.setCardExpiryMonth(migsReport.getCardExpiryMonth());
								newMigsUploadMaster.setCardExpiryYear(migsReport.getCardExpiryYear());
								newMigsUploadMaster.setDialectCscResultCode(migsReport.getDialectCSCResultCode());
								newMigsUploadMaster.setComment(migsReport.getComment());
								newMigsUploadMaster.setEcommerceIndicator(migsReport.geteCommerceIndicator());
								newMigsUploadMaster.setFileName(filePath + fileName);
								
								//Set problem description
								if (!isNull(newMigsUploadTemporary.getResponseCode(), "").equalsIgnoreCase("0 - Approved")) {
									newMigsUploadTemporary.setProblemDescription("Not an approved transaction (Status: " + migsReport.getResponseCode() + ")");
									newMigsUploadTemporary.setAction("REMOVE");
									newMigsUploadMaster.setProblemDescription("Not an approved transaction (Status: " + migsReport.getResponseCode() + ")");
									newMigsUploadMaster.setAction("REMOVE");
								}
								else if (isNull(newMigsUploadTemporary.getMerchantTransactionReference(), "").equalsIgnoreCase("") ||
										isNull(newMigsUploadTemporary.getAuthorisationCode(), "").equalsIgnoreCase("") ||
										isNull(newMigsUploadTemporary.getCardNumber(), "").equalsIgnoreCase("")) {
									
									ArrayList<String> errNotes = new ArrayList<String>();
									
									if (isNull(newMigsUploadTemporary.getMerchantTransactionReference(), "").equalsIgnoreCase("")) {
										errNotes.add("Order ID");
									}
									
									if (isNull(newMigsUploadTemporary.getAuthorisationCode(), "").equalsIgnoreCase("")) {
										errNotes.add("Authorization Code");
									}
									
									if (isNull(newMigsUploadTemporary.getCardNumber(), "").equalsIgnoreCase("")) {
										errNotes.add("Credit Card No.");
									}
									
									newMigsUploadTemporary.setProblemDescription("Incomplete data (" + StringUtils.join(errNotes.toArray(),", ") + ") (Transaction Type : "+migsReport.getTransactionType()+" and Response Code : "+migsReport.getResponseCode()+")");
									newMigsUploadTemporary.setAction("REMOVE");
									newMigsUploadMaster.setProblemDescription("Incomplete data (" + StringUtils.join(errNotes.toArray(),", ") + ") (Transaction Type : "+migsReport.getTransactionType()+" and Response Code : "+migsReport.getResponseCode()+")");
									newMigsUploadMaster.setAction("REMOVE");
								}	else {
									//Cek apakah order dari WCS sudah tersinkronisasi ke Venice
									boolean isOrderAlreadyExist = false;
									VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
										
									List<VenOrder> orderList = null;
									//query = "select o from VenOrder o where o.wcsOrderId = '" + isNull(migsReport.getMerchantTransactionReference(), "").replaceAll("-.*$", "") + "' and o.authorisationCode = '" + isNull(migsReport.getAuthorisationCode(), "") + "'";
									query = "select o from VenOrderPaymentAllocation opa inner join opa.venOrder o inner join opa.venOrderPayment op" +
											" where o.wcsOrderId = '" + isNull(migsReport.getMerchantTransactionReference(), "").replaceAll("-.*$", "") +
											"' and op.referenceId = '" + isNull(autCode, "") + "'";
									
									orderList = orderSessionHome.queryByRange(query, 0, 0);
									isOrderAlreadyExist = (orderList.size() > 0);
									
									if (!isOrderAlreadyExist) {
										newMigsUploadTemporary.setProblemDescription("Order is not found on venice (Transaction Type : "+migsReport.getTransactionType()+" and Response Code : "+migsReport.getResponseCode()+")");
										newMigsUploadTemporary.setAction("KEEP");
										newMigsUploadMaster.setProblemDescription("Order is not found on venice (Transaction Type : "+migsReport.getTransactionType()+" and Response Code : "+migsReport.getResponseCode()+")");
										newMigsUploadMaster.setAction("KEEP");
									}	else {
										//Cek apakah data MIGS sudah pernah terupload sebelumnya
//										boolean isAlreadyUploadedBefore = false;
//										VenMigsTransactionSessionEJBRemote migsSessionHome = (VenMigsTransactionSessionEJBRemote) locator.lookup(VenMigsTransactionSessionEJBRemote.class, "VenMigsTransactionSessionEJBBean");
//											
//										List<VenMigsTransaction> migsList = null;
//										query = "select o from VenMigsTransaction o where o.merchantTransactionReference = '" + isNull(migsReport.getMerchantTransactionReference(), "") + "' and o.authorisationCode = '" + isNull(autCode, "") + "' and o.isActive = true";
//										migsList = migsSessionHome.queryByRange(query, 0, 0);
//										isAlreadyUploadedBefore = (migsList.size() > 0);
//										
//										if (isAlreadyUploadedBefore) {
//											newMigsUploadTemporary.setProblemDescription("Data has been uploaded before");
//											newMigsUploadTemporary.setAction("KEEP");
//											newMigsUploadMaster.setProblemDescription("Data has been uploaded before");
//											newMigsUploadMaster.setAction("KEEP");
//										}
//										else {
											newMigsUploadTemporary.setAction("SUBMIT");
											newMigsUploadMaster.setAction("SUBMIT");
//										}
									}
								}
								
								migsUploadHome.persistVenMigsUploadTemporary(newMigsUploadTemporary);
								migsUploadMasterHome.mergeVenMigsUploadMaster(newMigsUploadMaster);
								numberOfSuccess++;
							}
						}
						
						String successMsg = "Report uploaded successfully:";
						successMsg += "\\\\n   " + numberOfSuccess + " new rows(s) have been uploaded.";
						successMsg += failedMessage.size() > 0 ? "\\\\n   " + failedMessage.size() + " row(s) were found as duplicate entry on upload list.\\\\n" + StringUtils.join(failedMessage.toArray(), "\\\\n") : "";
						successMsg += "\\\\n\\\\nPlease refresh upload list...";
						retVal = notificationText.replaceFirst("REPLACE",	successMsg);
					} catch (Exception e) {
						String errMsg = "An exception occured when uploading the invoice report. Please contact the systems administrator.";
						e.printStackTrace();
						notificationText = notificationText.replaceFirst("REPLACE",	errMsg);
					} finally {
						try {
							if (locator != null) {
								locator.close();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		response.getOutputStream().println(retVal);
	}
	
	private String isNull(Object object, String replacement) {
		return object == null ? replacement : object.toString();
	}
}