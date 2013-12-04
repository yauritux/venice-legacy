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
import com.gdn.venice.server.app.fraud.dataimport.MandiriInstalmentReport;
import com.gdn.venice.server.app.fraud.dataimport.PojoInterface;

/**
 * Servlet implementation class MandiriInstallmentUploadPresenterServlet
 */
public class MandiriInstallmentUploadPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String notificationText = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MandiriInstallmentUploadPresenterServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "unchecked" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String retVal =  "";
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		notificationText = "<html>\n" + "<head>"
		+ "<title>Processing Report Completed</title>" + "</head>\n"
		+ "<body onload=\"alert('REPLACE')\">" + "<p>TEST</p>\n"
		+ "</body>\n" + "</html>";
		
		if (isMultipart) { // import
			String formatString = "yyyy.MM.dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(formatString);
			
			String filePath = System.getenv("VENICE_HOME") + "/files/import/migs/";
			String fileName = "MandiriInstallment-" + sdf.format(new Date()) + ".xls";
			
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
			
			ExcelToPojo x = new ExcelToPojo(System.getenv("VENICE_HOME") + "/files/template/MandiriInstalmentReport.xml", filePath + fileName, 2, 1, 16, MandiriInstalmentReport.class);
			ArrayList<PojoInterface> result = x.getPojoResult();
			
			if (!x.getErrorMessage().equalsIgnoreCase("")) {
				retVal = notificationText.replaceFirst("REPLACE", x.getErrorMessage());
			} else {
				Locator<Object> locator = null;
				try {

					locator = new Locator<Object>();
					VenMigsUploadTemporarySessionEJBRemote migsUploadHome = (VenMigsUploadTemporarySessionEJBRemote) locator.lookup(VenMigsUploadTemporarySessionEJBRemote.class, "VenMigsUploadTemporarySessionEJBBean");
					VenMigsUploadMasterSessionEJBRemote migsUploadMasterHome = (VenMigsUploadMasterSessionEJBRemote) locator.lookup(VenMigsUploadMasterSessionEJBRemote.class, "VenMigsUploadMasterSessionEJBBean");
					boolean isOrderAlreadyExist = false;
					VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
						
							
					int numberOfSuccess = 0;
					ArrayList<String> failedMessage = new ArrayList<String>();
					for (PojoInterface element : result) {
						MandiriInstalmentReport mdrInstReport = (MandiriInstalmentReport) element;
						if(mdrInstReport.getTransactionid()!=null && !mdrInstReport.getTransactionid().equals("")){	
								String autCode="";
								autCode=autCode+mdrInstReport.getAuthorizationcode();
								if(!autCode.equals("null")){
									if(autCode.length()<6 ){
										autCode="000000".substring(0, 6-autCode.length())+autCode;
									}
								}else{
									autCode=null;
								}
								
								String eci="";
								eci=eci+mdrInstReport.getEcicode();
								if(!eci.equals("null")){
									if(eci.length()<2 ){
										eci="00".substring(0, 2-eci.length())+eci;
									}
								}else{
									eci=null;
								}						
								
								//Cek apakah data MIGS sudah pernah terupload sebelumnya
								String query = "select o from VenMigsUploadMaster o where o.transactionId = '" + isNull(mdrInstReport.getTransactionid(), "") + "' and o.authorisationCode ='"+ isNull(autCode, "") +"' and o.transactionId is not null and o.action <> 'REMOVE'";
								List<VenMigsUploadMaster> migsUploadList = migsUploadMasterHome.queryByRange(query, 0, 0);
								boolean isDuplicated = (migsUploadList.size() > 0);					
								
								if (isDuplicated) {
									failedMessage.add("Already Exist transaction id : " + isNull(mdrInstReport.getTransactionid(), "") + ", Auth Code:" + isNull(mdrInstReport.getAuthorizationcode(), ""));
								} else {													
									List<VenOrder> orderList = null;
									query = "select o from VenOrderPaymentAllocation opa inner join opa.venOrder o inner join opa.venOrderPayment op" +
											" where op.amount = '" + isNull(mdrInstReport.getAmount(), "0") +
											"' and op.referenceId = '" + isNull(autCode, "") + "'";
									
									orderList = orderSessionHome.queryByRange(query, 0, 0);
									isOrderAlreadyExist = (orderList.size() > 0);			
									String wcsOrderId = isOrderAlreadyExist?orderList.get(0).getWcsOrderId():"";
									
									VenMigsUploadTemporary newMigsUploadTemporary = new VenMigsUploadTemporary();
									newMigsUploadTemporary.setTransactionId(mdrInstReport.getTransactionid());							
									newMigsUploadTemporary.setTransactionDate(SQLDateUtility.utilDateToSqlTimestamp(mdrInstReport.getTrandate()));
									newMigsUploadTemporary.setMerchantId(mdrInstReport.getMerchantaccountno());
									newMigsUploadTemporary.setOrderReference(wcsOrderId);
									newMigsUploadTemporary.setOrderId(mdrInstReport.getMerchanttransactionid());
									newMigsUploadTemporary.setMerchantTransactionReference(wcsOrderId);
									newMigsUploadTemporary.setTransactionType("");
									newMigsUploadTemporary.setAcquirerId("");
									newMigsUploadTemporary.setBatchNumber("");
									newMigsUploadTemporary.setCurrency(mdrInstReport.getCurrency());
									newMigsUploadTemporary.setAmount(mdrInstReport.getAmount());
									newMigsUploadTemporary.setRrn("");
									newMigsUploadTemporary.setResponseCode(mdrInstReport.getStatus());
									newMigsUploadTemporary.setAcquirerResponseCode(mdrInstReport.getResponsecode());
									newMigsUploadTemporary.setAuthorisationCode(autCode);
									newMigsUploadTemporary.setOperator("");
									newMigsUploadTemporary.setMerchantTransactionSource("");
									newMigsUploadTemporary.setOrderDate(null);
									newMigsUploadTemporary.setCardType(mdrInstReport.getCardtype());
									newMigsUploadTemporary.setCardNumber(mdrInstReport.getCardno());
									newMigsUploadTemporary.setCardExpiryMonth("");
									newMigsUploadTemporary.setCardExpiryYear("");
									newMigsUploadTemporary.setDialectCscResultCode("");
									newMigsUploadTemporary.setComment(mdrInstReport.getErrdesc());
									newMigsUploadTemporary.setEcommerceIndicator(eci);
									newMigsUploadTemporary.setFileName(filePath + fileName);
									
									VenMigsUploadMaster newMigsUploadMaster;
									try{
										newMigsUploadMaster = migsUploadMasterHome.queryByRange("select o from VenMigsUploadMaster o where o.transactionId = '" + isNull(mdrInstReport.getTransactionid(), "") + "' and o.authorisationCode ='"+ isNull(autCode, "") +"' and o.transactionId is not null", 0, 1).get(0);
									}catch(IndexOutOfBoundsException e){
										newMigsUploadMaster = new VenMigsUploadMaster();
									}
									newMigsUploadMaster.setTransactionId(mdrInstReport.getTransactionid());
									newMigsUploadMaster.setTransactionDate(SQLDateUtility.utilDateToSqlTimestamp(mdrInstReport.getTrandate()));
									newMigsUploadMaster.setMerchantId(mdrInstReport.getMerchantaccountno());
									newMigsUploadMaster.setOrderReference(wcsOrderId);
									newMigsUploadMaster.setOrderId(mdrInstReport.getMerchanttransactionid());
									newMigsUploadMaster.setMerchantTransactionReference(wcsOrderId);
									newMigsUploadMaster.setTransactionType("");
									newMigsUploadMaster.setAcquirerId("");
									newMigsUploadMaster.setBatchNumber("");
									newMigsUploadMaster.setCurrency(mdrInstReport.getCurrency());
									newMigsUploadMaster.setAmount(mdrInstReport.getAmount());
									newMigsUploadMaster.setRrn("");
									newMigsUploadMaster.setResponseCode(mdrInstReport.getStatus());
									newMigsUploadMaster.setAcquirerResponseCode(mdrInstReport.getResponsecode());
									newMigsUploadMaster.setAuthorisationCode(autCode);
									newMigsUploadMaster.setOperator("");
									newMigsUploadMaster.setMerchantTransactionSource("");
									newMigsUploadMaster.setOrderDate(null);
									newMigsUploadMaster.setCardType(mdrInstReport.getCardtype());
									newMigsUploadMaster.setCardNumber(mdrInstReport.getCardno());
									newMigsUploadMaster.setCardExpiryMonth("");
									newMigsUploadMaster.setCardExpiryYear("");
									newMigsUploadMaster.setDialectCscResultCode("");
									newMigsUploadMaster.setComment(mdrInstReport.getErrdesc());
									newMigsUploadMaster.setEcommerceIndicator(eci);
									newMigsUploadMaster.setFileName(filePath + fileName);
									
									//Set problem description
									if (!isNull(newMigsUploadTemporary.getResponseCode(), "").equalsIgnoreCase("S") && !isNull(newMigsUploadTemporary.getResponseCode(), "").equalsIgnoreCase("V")) {
										newMigsUploadTemporary.setProblemDescription("Not an approved transaction (Status: " + mdrInstReport.getStatus() + ")");
										newMigsUploadTemporary.setAction("REMOVE");
										newMigsUploadMaster.setProblemDescription("Not an approved transaction (Status: " + mdrInstReport.getStatus() + ")");
										newMigsUploadMaster.setAction("REMOVE");
									}	else if (isNull(newMigsUploadTemporary.getMerchantTransactionReference(), "").equalsIgnoreCase("") ||
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
										
										newMigsUploadTemporary.setProblemDescription("Incomplete data (" + StringUtils.join(errNotes.toArray(),", ") + ") (Status Type : "+mdrInstReport.getStatus()+" and Response Code : "+mdrInstReport.getResponsecode()+")");
										newMigsUploadTemporary.setAction("REMOVE");
										newMigsUploadMaster.setProblemDescription("Incomplete data (" + StringUtils.join(errNotes.toArray(),", ") + ") (Status Type : "+mdrInstReport.getStatus()+" and Response Code : "+mdrInstReport.getResponsecode()+")");
										newMigsUploadMaster.setAction("REMOVE");
									}	else {
										//Cek apakah order dari WCS sudah tersinkronisasi ke Venice
										
										
										if (!isOrderAlreadyExist) {
											newMigsUploadTemporary.setProblemDescription("Order is not found on venice (Status Type : "+mdrInstReport.getStatus()+" and Response Code : "+mdrInstReport.getResponsecode()+")");
											newMigsUploadTemporary.setAction("KEEP");
											newMigsUploadMaster.setProblemDescription("Order is not found on venice (Status Type : "+mdrInstReport.getStatus()+" and Response Code : "+mdrInstReport.getResponsecode()+")");
											newMigsUploadMaster.setAction("KEEP");
										}
										else {
											//Cek apakah data MIGS sudah pernah terupload sebelumnya
//											boolean isAlreadyUploadedBefore = false;
//											VenMigsTransactionSessionEJBRemote migsSessionHome = (VenMigsTransactionSessionEJBRemote) locator.lookup(VenMigsTransactionSessionEJBRemote.class, "VenMigsTransactionSessionEJBBean");
//												
//											List<VenMigsTransaction> migsList = null;
//											query = "select o from VenMigsTransaction o where o.merchantTransactionReference = '" + isNull(wcsOrderId, "") + "' and o.authorisationCode = '" + isNull(autCode, "") + "' and o.isActive = true";
//											migsList = migsSessionHome.queryByRange(query, 0, 0);
//											isAlreadyUploadedBefore = (migsList.size() > 0);
//											
//											if (isAlreadyUploadedBefore) {
//												newMigsUploadTemporary.setProblemDescription("Data has been uploaded before ");
//												newMigsUploadTemporary.setAction("KEEP");
//												newMigsUploadMaster.setProblemDescription("Data has been uploaded before");
//												newMigsUploadMaster.setAction("KEEP");
//											}
//											else {
												newMigsUploadTemporary.setAction("SUBMIT");
												newMigsUploadMaster.setAction("SUBMIT");
//											}
										}
									}										
									migsUploadHome.persistVenMigsUploadTemporary(newMigsUploadTemporary);
									migsUploadMasterHome.mergeVenMigsUploadMaster(newMigsUploadMaster);
									numberOfSuccess++;
								}
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
		
		response.getOutputStream().println(retVal);
	}
	
	private String isNull(Object object, String replacement) {
		return object == null ? replacement : object.toString();
	}

}
