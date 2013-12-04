package com.gdn.venice.facade.logistics.invoice;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.djarum.raf.utilities.SQLDateUtility;
import com.gdn.awb.exchange.model.AirwayBillTransactionResource;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBLocal;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote;
import com.gdn.venice.facade.util.AWBReconciliation;
import com.gdn.venice.hssf.ExcelToPojo;
import com.gdn.venice.hssf.PojoInterface;
import com.gdn.venice.logistics.dataexport.ActivityInvoiceFailedToUploadExport;
import com.gdn.venice.logistics.dataexport.FailedStatusUpdate;
import com.gdn.venice.logistics.dataimport.InvoiceJNE;
import com.gdn.venice.logistics.dataimport.InvoiceNCS;
import com.gdn.venice.logistics.dataimport.InvoiceRPX;
import com.gdn.venice.logistics.dataimport.LogisticsConstants;
import com.gdn.venice.logistics.integration.AirwayBillEngineClientConnector;
import com.gdn.venice.logistics.integration.AirwayBillEngineConnector;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.persistence.LogInvoiceAirwaybillRecord;
import com.gdn.venice.persistence.LogInvoiceReconRecord;
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.gdn.venice.persistence.LogInvoiceUploadLog;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.LogReportStatus;
import com.gdn.venice.persistence.LogReportTemplate;
import com.gdn.venice.util.VeniceConstants;

@MessageDriven(
		name = "LogInvoiceReportMDB",
		activationConfig = { 
				@ActivationConfigProperty(
						propertyName = "destinationType", propertyValue = "javax.jms.Queue"
				),
				@ActivationConfigProperty(
						propertyName = "destination", propertyValue = "InvoiceUploadQueue"
				)
		}
)
public class LogInvoiceReportMDB implements MessageListener{
	
	private static final String ACTIVITY_OR_INVOICE = "invoice";
	private static final String FILE_PATH = System.getenv("VENICE_HOME") + LogisticsConstants.INVOICE_REPORT_FOLDER;
	private SimpleDateFormat fileDateTimeFormat = new SimpleDateFormat(LogisticsConstants.FILE_DATE_TIME_FORMAT);
	
	protected static Logger _log = null;

	
	private AirwayBillEngineConnector awbConn;
	private Integer errorRowNumber = 1;
	
	/**
     * Default constructor. 
	 * @throws ParseException 
     */
    public LogInvoiceReportMDB() throws ParseException {
    	super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.logistics.invoice.LogInvoiceReportMDB");
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void onMessage(Message message) {
		try {
			awbConn = new AirwayBillEngineClientConnector();
    		ObjectMessage msg = (ObjectMessage) message;
    		LogInvoiceUploadLog fileUploadLog = (LogInvoiceUploadLog) msg.getObject();
			
			if(fileUploadLog.getFileUploadFormat().equals("JNE")){
				processJNEFormat(fileUploadLog);
			}
			
			if(fileUploadLog.getFileUploadFormat().equals("NCS")){
				processNCSFormat(fileUploadLog);
			}
			
			if(fileUploadLog.getFileUploadFormat().equals("RPX")){
				processRPXFormat(fileUploadLog);
			}
			
		} catch (JMSException e) {
			_log.error("Error on reading message", e);
			e.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
		
	private String checkTemplate(long providerId, LogInvoiceUploadLog fileUploadLog){
		String templateFile = "";
		Locator<Object> locator = null;
		LogInvoiceUploadLogSessionEJBRemote invoiceUploadLogHome = null;
		
		try{
			locator = new Locator<Object>();
			invoiceUploadLogHome = (LogInvoiceUploadLogSessionEJBRemote) locator
			.lookup(LogInvoiceUploadLogSessionEJBRemote.class, "LogInvoiceUploadLogSessionEJBBean");
			LogLogisticsProviderSessionEJBLocal LogLogisticsProviderHome = (LogLogisticsProviderSessionEJBLocal) locator.lookupLocal(LogLogisticsProviderSessionEJBLocal.class, "LogLogisticsProviderSessionEJBBeanLocal");
			List<LogLogisticsProvider>LogLogisticsProviderList = LogLogisticsProviderHome.queryByRange("select o from LogLogisticsProvider o where o.logisticsProviderId = " + providerId, 0, 0);
			if(LogLogisticsProviderList.size()>0){
				templateFile=LogLogisticsProviderList.get(0).getLogReportTemplate1().getTemplateFile();
				
				if(templateFile.equals("") || templateFile==null){
					throw new Exception(LogisticsConstants.EXCEPTION_TEXT_TEMPLATE_NOT_FOUND);
				}else{
					_log.debug("logistic provider found, templateFile: "+templateFile);
				}
			}
		}catch(Exception e){
			String errMsg = LogisticsConstants.EXCEPTION_TEXT_TEMPLATE_NOT_FOUND ;
			_log.error(errMsg);
			fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
			invoiceUploadLogHome.mergeLogInvoiceUploadLog(fileUploadLog);
			return null;
		} finally{
			if(locator!=null)
				try {
					locator.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return templateFile;
	}
	
	private AirwayBillTransactionResource checkIsDataOK(String awbNumber, HashMap<String, String> failedRecord, List<AirwayBillTransactionResource> awbList, String invoiceNumber, long providerId, String providerCode){
		boolean isDataOK = true;
		Locator<Object> locator = null;
		AirwayBillTransactionResource mtaAirwayBill = null;
		
		try{
			locator = new Locator<Object>();

			LogInvoiceAirwaybillRecordSessionEJBRemote awbRecordHome = (LogInvoiceAirwaybillRecordSessionEJBRemote) locator
				.lookup(LogInvoiceAirwaybillRecordSessionEJBRemote.class, "LogInvoiceAirwaybillRecordSessionEJBBean");
			
			
			if(awbList != null && awbList.size()>0){
				String provider = "";
				// only retrieve awb transaction with matching provider code
		    	for (AirwayBillTransactionResource airwayBillTransactionResource :  awbList) {
					if(airwayBillTransactionResource.getAirwayBill().getLogisticProviderCode().equalsIgnoreCase(providerCode)){
						 mtaAirwayBill = airwayBillTransactionResource;
					} else {
						if(!provider.equals(""))
							provider = provider.concat(" or ");
						provider = provider.concat(airwayBillTransactionResource.getAirwayBill().getLogisticProviderCode());
					}
				};
				
				if(mtaAirwayBill == null) {
					isDataOK = false;
					_log.debug("Airwaybill number originally sent by "+provider+" : "+awbNumber);
					failedRecord.put(awbNumber, "Originally sent by "+provider);
				}
			} else {
				if(mtaAirwayBill == null) {
					isDataOK = false;
					_log.debug("Airwaybill number not found in engine: "+awbNumber);
					failedRecord.put(awbNumber, "Not found in engine");
				}
			}
			
			List<LogInvoiceAirwaybillRecord> awbRecordList = awbRecordHome.queryByRange("select o from LogInvoiceAirwaybillRecord o where o.airwayBillNumber ='" + awbNumber + "' and o.logInvoiceReportUpload.logLogisticsProvider.logisticsProviderId = "+ providerId, 0, 1);

			if(isDataOK){
				for (LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord : awbRecordList) {
					if(logInvoiceAirwaybillRecord.getLogInvoiceReportUpload().getLogApprovalStatus().getApprovalStatusId() == VeniceConstants.LOG_APPROVAL_STATUS_SUBMITTED ||
							logInvoiceAirwaybillRecord.getLogInvoiceReportUpload().getLogApprovalStatus().getApprovalStatusId() == VeniceConstants.LOG_APPROVAL_STATUS_APPROVED){
						_log.debug("Airwaybil number has been submitted/approved in another invoice: "+ awbNumber);
						failedRecord.put(awbNumber, "Has been submitted/approved in another invoice: "+logInvoiceAirwaybillRecord.getLogInvoiceReportUpload().getInvoiceNumber());
						isDataOK=false;
						break;
					}
				}
			}		
			
			if(isDataOK){					
				if(!awbRecordList.isEmpty() && !awbRecordList.get(0).getLogInvoiceReportUpload().getInvoiceNumber().equals(invoiceNumber)){
					_log.debug("Airwaybil number has been uploaded in another invoice ("+ awbRecordList.get(0).getLogInvoiceReportUpload().getInvoiceNumber() +"): "+ awbNumber);
					failedRecord.put(awbNumber, "Has been uploaded in another invoice. Please check in invoice '"+awbRecordList.get(0).getLogInvoiceReportUpload().getInvoiceNumber()+"'");
					isDataOK=false;
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if(locator != null)
				try {
					locator.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return mtaAirwayBill;
	}
	
	private void uploadFailedRecord(HashMap<String, String> failedRecord, LogInvoiceUploadLog fileUploadLog) throws Exception{
		String successMsg = LogisticsConstants.UPLOAD_SUCCESS_MESSAGE;			
		if(failedRecord.size() > 0){
			_log.info(failedRecord.size() + " row(s) need more attention when being processed");				
			FileOutputStream fos = null;				
			try{							
				String outputFileName = "InvoiceReportFailedToUpload-" +fileDateTimeFormat.format(new Date()) + ".xls";				
				fos = new FileOutputStream(FILE_PATH + outputFileName);
			   
			    HSSFWorkbook wb = new HSSFWorkbook();
			    HSSFSheet sheet = wb.createSheet("InvoiceReportFailedToUpload");	    					   
				
			    ActivityInvoiceFailedToUploadExport activityInvoiceFailedToUploadExport =new ActivityInvoiceFailedToUploadExport(wb);					   
			    
			    wb  =  activityInvoiceFailedToUploadExport.ExportExcel(new HashMap<String, String>(), failedRecord, new ArrayList<FailedStatusUpdate>(),sheet, ACTIVITY_OR_INVOICE);     
			    wb.write(fos);
			    _log.debug("done export excel");
			    
			    fileUploadLog.setFailedFileUploadName(outputFileName);
			    fileUploadLog.setFailedFileUploadNameAndLoc(FILE_PATH + outputFileName) ;				    
			} catch (Exception e)   {
				_log.error("Exception in Excel InvoiceReportImportServlet", e);
				throw new ServletException("Exception in Excel InvoiceReportImportServlet", e);
		    } finally   {
			     if (fos != null)
			    	 fos.close();							
		    }  
		} else{
			_log.info(successMsg);
		}		
	}
	
	private void processRPXFormat(LogInvoiceUploadLog fileUploadLog) {
		Locator<Object> locator = null;
		String notificationText="", invoiceNumber = fileUploadLog.getInvoiceNumber(),
			templateFile = checkTemplate(VeniceConstants.VEN_LOGISTICS_PROVIDER_RPX, fileUploadLog);
		if(templateFile == null)
			return;

		LogInvoiceUploadLogSessionEJBRemote invoiceUploadLogHome = null;
		
		try {
			locator = new Locator<Object>();
			AirwayBillTransactionResource mtaAirwayBill = null;
			Map<String, AirwayBillTransactionResource> mtaAwbMap = new HashMap<String, AirwayBillTransactionResource>();

			LogInvoiceReportUploadSessionEJBRemote reportUploadHome = (LogInvoiceReportUploadSessionEJBRemote) locator
					.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");
			
			invoiceUploadLogHome  = (LogInvoiceUploadLogSessionEJBRemote) locator
			.lookup(LogInvoiceUploadLogSessionEJBRemote.class, "LogInvoiceUploadLogSessionEJBBean");
			
			ExcelToPojo x = null;
			try {
				x = new ExcelToPojo(InvoiceRPX.class, System.getenv("VENICE_HOME") + LogisticsConstants.TEMPLATE_FOLDER + templateFile, fileUploadLog.getFileUploadNameAndLoc(), 3, 0);
				x = x.getPojo();
			} catch (Exception e1) {
				String errMsg = LogisticsConstants.EXCEPTION_TEXT_FILE_PARSE + e1.getMessage() + ". Processing row number " + (x != null && x.getErrorRowNumber()!= null ?x.getErrorRowNumber():"1" + "\n");
				_log.error(errMsg);
				e1.printStackTrace();
				fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
				invoiceUploadLogHome.mergeLogInvoiceUploadLog(fileUploadLog);
				return;
			}
			List<PojoInterface> result =  x.getPojoResult();
			
			/* Store data error during processing */
			HashMap<String, String> failedRecord = new HashMap<String, String>();
			
			if(result.isEmpty()){
				throw new Exception(LogisticsConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS);
			}else{
				_log.debug("result size: "+result.size());
			}

			Boolean reportuploadEntryCreated = false;
			LogInvoiceReportUpload logInvoiceReportUpload = null;
			int length = result.size();
			
			/*
			 * Jika dalam report terdapat gdn reference with no matching airwaybill, maka di remove dari list, 
			 * airwaybill nya dicatat dan ditampilkan di pop up window setelah upload. sedangkan data lain yang match tetap di proses.
			 */
			for(int i=0;i<length;i++){
				InvoiceRPX invoiceRPX= (InvoiceRPX) result.get(i);	
				 
				List<AirwayBillTransactionResource> awbList = awbConn.getAirwayBillTransaction(invoiceRPX.getNoAirwayBill(), true);
								
				mtaAirwayBill = checkIsDataOK(invoiceRPX.getNoAirwayBill(), failedRecord, awbList, invoiceNumber, VeniceConstants.VEN_LOGISTICS_PROVIDER_RPX, "RPX");

				if(mtaAirwayBill == null){
					result.remove(result.get(i));
					_log.debug("data not ok, remove from list: "+ invoiceRPX.getNoAirwayBill());
					--i;
					--length;
					continue;
				}else{
					_log.debug("data is ok, keep in the list: "+ invoiceRPX.getNoAirwayBill());
					mtaAwbMap.put(invoiceRPX.getNoAirwayBill(), mtaAirwayBill);
				}
			}
			
			_log.debug("result size after validation: "+result.size());
			errorRowNumber = 1;
			for (PojoInterface element : result) {
				InvoiceRPX invoiceRPX = (InvoiceRPX) element;
				LogAirwayBill newLogAirwayBill = new LogAirwayBill();					
				newLogAirwayBill.setAirwayBillNumber(invoiceRPX.getNoAirwayBill());
				newLogAirwayBill.setInvoiceFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
				newLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
				newLogAirwayBill.setShipper(invoiceRPX.getNamaMerchant());
				newLogAirwayBill.setOrigin(invoiceRPX.getOrigin());
				newLogAirwayBill.setConsignee(invoiceRPX.getPenerima());
				newLogAirwayBill.setDestination(invoiceRPX.getDestination());
				newLogAirwayBill.setService(invoiceRPX.getService());
				newLogAirwayBill.setPackageWeight(invoiceRPX.getBeratYangDibebankan() != null && !invoiceRPX.getBeratYangDibebankan().isEmpty()?new BigDecimal(new Double(invoiceRPX.getBeratYangDibebankan())): new BigDecimal(0));
				newLogAirwayBill.setPricePerKg(invoiceRPX.getRateFirstKg() != null && !invoiceRPX.getRateFirstKg().isEmpty()?new BigDecimal(new Double(invoiceRPX.getRateFirstKg())): new BigDecimal(0));
				newLogAirwayBill.setInsuranceCharge(invoiceRPX.getBiayaAsuransi() != null && !invoiceRPX.getBiayaAsuransi().isEmpty()?new BigDecimal(new Double(invoiceRPX.getBiayaAsuransi())): new BigDecimal(0));
				newLogAirwayBill.setOtherCharge(invoiceRPX.getBiayaSpecialHandling() != null && !invoiceRPX.getBiayaSpecialHandling().isEmpty()?new BigDecimal(new Double(invoiceRPX.getBiayaSpecialHandling())): new BigDecimal(0));
				newLogAirwayBill.setGiftWrapCharge(invoiceRPX.getBiayaGiftWrapAndGiftCard() != null && !invoiceRPX.getBiayaGiftWrapAndGiftCard().isEmpty()?new BigDecimal(new Double(invoiceRPX.getBiayaGiftWrapAndGiftCard())): new BigDecimal(0));
				newLogAirwayBill.setTotalCharge(invoiceRPX.getTotalBiaya() != null && !invoiceRPX.getTotalBiaya().isEmpty()?new BigDecimal(new Double(invoiceRPX.getTotalBiaya())): new BigDecimal(0));
				
				if (!reportuploadEntryCreated) {
					_log.debug("report not created yet, creating new one");
					try{
						logInvoiceReportUpload = reportUploadHome.queryByRange("select o from LogInvoiceReportUpload o where o.invoiceNumber = '" + invoiceNumber + "'", 0, 1).get(0);
					} catch (IndexOutOfBoundsException e){
						logInvoiceReportUpload = new LogInvoiceReportUpload();
					}
					reportuploadEntryCreated = true;

					logInvoiceReportUpload = createInvoiceReportUpload(logInvoiceReportUpload, locator, invoiceNumber, fileUploadLog, new Long(result.size()), "RPX");
				}							
				
				/*
				 * 2011-05-23 In accordance with JIRA VENICE-16 Pickup
				 * Data Late Only reconcile the AWB data if the existing data has come from MTA or has been reconciled against data from MTA. 
				 * Reconcile and merge the airwaybill
				 * At this point there'll always MTA data provided because MTA settlement happened on PU
				 */
				// Perform the reconciliation
				_log.debug("reconcile awb");
				AWBReconciliation awbReconciliation = new AWBReconciliation();				
				awbReconciliation.performInvoiceReconciliation(logInvoiceReportUpload, newLogAirwayBill, mtaAwbMap.get(invoiceRPX.getNoAirwayBill()), failedRecord);
				errorRowNumber++;
			}
			
			uploadFailedRecord(failedRecord, fileUploadLog);
			fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_SUCCESS);
			invoiceUploadLogHome.mergeLogInvoiceUploadLog(fileUploadLog);
		} catch (Exception e) {
			String errMsg = LogisticsConstants.EXCEPTION_TEXT_GENERAL + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber ;
			_log.error(errMsg);
			e.printStackTrace();
			notificationText = notificationText.replaceFirst("REPLACE",	errMsg.replace("\"", " "));

			cleanupFailedReport(invoiceNumber);
			
			fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
			invoiceUploadLogHome.mergeLogInvoiceUploadLog(fileUploadLog);
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void processJNEFormat(LogInvoiceUploadLog fileUploadLog) {
		Locator<Object> locator = null;
		String notificationText = "", invoiceNumber = fileUploadLog.getInvoiceNumber(),
			templateFile = checkTemplate(VeniceConstants.VEN_LOGISTICS_PROVIDER_JNE, fileUploadLog);

		LogInvoiceUploadLogSessionEJBRemote invoiceUploadLogHome = null;
		try {
			locator = new Locator<Object>();
			AirwayBillTransactionResource mtaAirwayBill = null;
			Map<String, AirwayBillTransactionResource> mtaAwbMap = new HashMap<String, AirwayBillTransactionResource>();

			LogInvoiceReportUploadSessionEJBRemote reportUploadHome = (LogInvoiceReportUploadSessionEJBRemote) locator
					.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");
			
			invoiceUploadLogHome  = (LogInvoiceUploadLogSessionEJBRemote) locator
			.lookup(LogInvoiceUploadLogSessionEJBRemote.class, "LogInvoiceUploadLogSessionEJBBean");
			
			ExcelToPojo x = null;
			try {
				x = new ExcelToPojo(InvoiceJNE.class, System.getenv("VENICE_HOME") + LogisticsConstants.TEMPLATE_FOLDER + templateFile, fileUploadLog.getFileUploadNameAndLoc(), 0, 0);
				x = x.getPojo();
			} catch (Exception e1) {
				String errMsg = LogisticsConstants.EXCEPTION_TEXT_FILE_PARSE + e1.getMessage() + ". Processing row number " + (x != null && x.getErrorRowNumber()!= null ?x.getErrorRowNumber():"1" + "\n");
				_log.error(errMsg);
				e1.printStackTrace();
				fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
				invoiceUploadLogHome.mergeLogInvoiceUploadLog(fileUploadLog);
				return;
			}
			List<PojoInterface> result =  x.getPojoResult();
						
			/* Store data error during processing */
			HashMap<String, String> failedRecord = new HashMap<String, String>();
			
			if(result.isEmpty()){
				throw new Exception(LogisticsConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS);
			}else{
				_log.debug("result size: "+result.size());
			}
			
			Boolean reportuploadEntryCreated = false;
			LogInvoiceReportUpload logInvoiceReportUpload = null;
			int length = result.size();
			
			/*
			 * Jika dalam report terdapat gdn reference with no matching order item id / gdn reference, maka di remove dari list, 
			 * gdn ref nya dicatat dan ditampilkan di pop up window setelah upload. sedangkan data lain yang match tetap di proses.
			 */
			for(int i=0;i<length;i++){
				InvoiceJNE invoiceJNE = (InvoiceJNE) result.get(i);	

				List<AirwayBillTransactionResource> awbList = awbConn.getAirwayBillTransaction(invoiceJNE.getAwbNumber(), true);
				
				mtaAirwayBill = checkIsDataOK(invoiceJNE.getAwbNumber(), failedRecord, awbList, invoiceNumber, VeniceConstants.VEN_LOGISTICS_PROVIDER_JNE, "JNE");
				
				if(mtaAirwayBill == null){
					result.remove(result.get(i));
					_log.debug("data not ok, remove from list: "+invoiceJNE.getAwbNumber());
					--i;
					--length;
					continue;
				}else{
					_log.debug("data is ok, keep in the list: "+invoiceJNE.getAwbNumber());
					mtaAwbMap.put(invoiceJNE.getAwbNumber(), mtaAirwayBill);
				}
			}	
			
			_log.debug("result size after validation: "+result.size());
			errorRowNumber = 1;
			for (PojoInterface element : result) {
				InvoiceJNE invoiceJNE = (InvoiceJNE) element;
				
				LogAirwayBill newLogAirwayBill = new LogAirwayBill();
				newLogAirwayBill.setAirwayBillNumber(invoiceJNE.getAwbNumber());
				newLogAirwayBill.setInvoiceFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
				newLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
				newLogAirwayBill.setOrigin(invoiceJNE.getOrigin());
				newLogAirwayBill.setConsignee(invoiceJNE.getReceiver());
				newLogAirwayBill.setDestination(invoiceJNE.getDestination());
				newLogAirwayBill.setService(invoiceJNE.getService());
				newLogAirwayBill.setPackageWeight(invoiceJNE.getWeight() != null && !invoiceJNE.getWeight().isEmpty()?new BigDecimal(new Double(invoiceJNE.getWeight())): new BigDecimal(0));
				newLogAirwayBill.setPricePerKg(invoiceJNE.getPricePerKg() != null && !invoiceJNE.getPricePerKg().isEmpty()?new BigDecimal(new Double(invoiceJNE.getPricePerKg())): new BigDecimal(0));
				newLogAirwayBill.setInsuranceCharge(invoiceJNE.getInsMin5000() != null && !invoiceJNE.getInsMin5000().isEmpty()?new BigDecimal(new Double(invoiceJNE.getInsMin5000())): new BigDecimal(0));
				newLogAirwayBill.setOtherCharge(invoiceJNE.getAddCharge() != null && !invoiceJNE.getAddCharge().isEmpty()?new BigDecimal(new Double(invoiceJNE.getAddCharge())): new BigDecimal(0));
				newLogAirwayBill.setGiftWrapCharge(new BigDecimal(0));
				newLogAirwayBill.setTotalCharge(invoiceJNE.getTotalAmount() != null && !invoiceJNE.getTotalAmount().isEmpty()?new BigDecimal(new Double(invoiceJNE.getTotalAmount())): new BigDecimal(0));							
				
				if (!reportuploadEntryCreated) {
					_log.debug("report not created yet, creating new one");
					try{
						logInvoiceReportUpload = reportUploadHome.queryByRange("select o from LogInvoiceReportUpload o where o.invoiceNumber = '" + invoiceNumber + "'", 0, 1).get(0);
					} catch (IndexOutOfBoundsException e){
						logInvoiceReportUpload = new LogInvoiceReportUpload();
					}
					reportuploadEntryCreated = true;

					logInvoiceReportUpload = createInvoiceReportUpload(logInvoiceReportUpload, locator, invoiceNumber, fileUploadLog, new Long(result.size()), "JNE");
				}

				/*
				 * 2011-05-23 In accordance with JIRA VENICE-16 Pickup
				 * Data Late Only reconcile the AWB data if the existing data has come from MTA or has been reconciled against data from MTA. 
				 * Reconcile and merge the airwaybill
				 * At this point there'll always MTA data provided because MTA settlement happened on PU
				 */
				// Perform the reconciliation
				_log.debug("reconcile awb");
				AWBReconciliation awbReconciliation = new AWBReconciliation();
				awbReconciliation.performInvoiceReconciliation(logInvoiceReportUpload, newLogAirwayBill, mtaAwbMap.get(invoiceJNE.getAwbNumber()), failedRecord);
				errorRowNumber++;
			}				
			
			uploadFailedRecord(failedRecord, fileUploadLog);
			fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_SUCCESS);
			invoiceUploadLogHome.mergeLogInvoiceUploadLog(fileUploadLog);
		} catch (Exception e) {
			String errMsg = LogisticsConstants.EXCEPTION_TEXT_GENERAL + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber ;
			_log.error(errMsg);
			e.printStackTrace();
			notificationText = notificationText.replaceFirst("REPLACE",	errMsg.replace("\"", " "));

			cleanupFailedReport(invoiceNumber);
			
			fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
			invoiceUploadLogHome.mergeLogInvoiceUploadLog(fileUploadLog);
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	private void processNCSFormat(LogInvoiceUploadLog fileUploadLog) {
		// Merged AWB list used for reconciliation
		Locator<Object> locator = null;

		String notificationText="", invoiceNumber = fileUploadLog.getInvoiceNumber(),
			templateFile = checkTemplate(VeniceConstants.VEN_LOGISTICS_PROVIDER_NCS, fileUploadLog);

		LogInvoiceUploadLogSessionEJBRemote invoiceUploadLogHome = null;
		try {
			locator = new Locator<Object>();
			AirwayBillTransactionResource mtaAirwayBill = null;
			Map<String, AirwayBillTransactionResource> mtaAwbMap = new HashMap<String, AirwayBillTransactionResource>();
			
			LogInvoiceReportUploadSessionEJBRemote reportUploadHome = (LogInvoiceReportUploadSessionEJBRemote) locator
					.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");
			
			invoiceUploadLogHome  = (LogInvoiceUploadLogSessionEJBRemote) locator
			.lookup(LogInvoiceUploadLogSessionEJBRemote.class, "LogInvoiceUploadLogSessionEJBBean");
			
			ExcelToPojo x = null;
			try {
				x = new ExcelToPojo(InvoiceNCS.class, System.getenv("VENICE_HOME") + LogisticsConstants.TEMPLATE_FOLDER + templateFile, fileUploadLog.getFileUploadNameAndLoc(), 17, 0);
				x = x.getPojo();
			} catch (Exception e1) {
				String errMsg = LogisticsConstants.EXCEPTION_TEXT_FILE_PARSE + e1.getMessage() + ". Processing row number " + (x != null && x.getErrorRowNumber()!= null ?x.getErrorRowNumber():"1" + "\n");
				_log.error(errMsg);
				e1.printStackTrace();
				fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
				invoiceUploadLogHome.mergeLogInvoiceUploadLog(fileUploadLog);
			}
			List<PojoInterface> result =  x.getPojoResult();
			
			/* Store missing GDN Ref */
			ArrayList<String> failedMessage = new ArrayList<String>();
			/* Store data error during processing */
			HashMap<String, String> failedRecord = new HashMap<String, String>();	
			
			if(result.isEmpty()){
				throw new Exception(LogisticsConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS);
			}else{
				_log.debug("result size: "+result.size());
			}
			
			Boolean reportuploadEntryCreated = false;
			LogInvoiceReportUpload logInvoiceReportUpload = null;
			int length = result.size();
			
			/*
			 * Jika dalam report terdapat gdn reference with no matching order item id / gdn reference, maka di remove dari list, 
			 * gdn ref nya dicatat dan ditampilkan di pop up window setelah upload. sedangkan data lain yang match tetap di proses.
			 */
			for(int i=0;i<length;i++){
				InvoiceNCS invoiceNCS = (InvoiceNCS) result.get(i);
				
				List<AirwayBillTransactionResource> awbList = awbConn.getAirwayBillTransaction(invoiceNCS.getNoKonos(), true);
				
				mtaAirwayBill = checkIsDataOK(invoiceNCS.getNoKonos(), failedRecord, awbList, invoiceNumber, VeniceConstants.VEN_LOGISTICS_PROVIDER_NCS, "NCS");
				
				if(mtaAirwayBill == null){
					result.remove(result.get(i));
					failedMessage.add(invoiceNCS.getNoKonos());
					_log.debug("data not ok, remove from list: "+invoiceNCS.getNoKonos());
					--i;
					--length;
					continue;
				}else{
					_log.debug("data is ok, keep in the list: "+invoiceNCS.getNoKonos());
					mtaAwbMap.put(invoiceNCS.getNoKonos(), mtaAirwayBill);
				}
			}	
			
			_log.debug("result size after validation: "+result.size());
			errorRowNumber = 1;
			for (PojoInterface element : result) {
				InvoiceNCS invoiceNCS = (InvoiceNCS) element;
				LogAirwayBill newLogAirwayBill = new LogAirwayBill();
				newLogAirwayBill.setAirwayBillNumber(invoiceNCS.getNoKonos());	
				newLogAirwayBill.setInvoiceFileNameAndLoc(fileUploadLog.getFileUploadNameAndLoc());
				newLogAirwayBill.setAirwayBillTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
				newLogAirwayBill.setShipper(invoiceNCS.getNamaMerchant());
				newLogAirwayBill.setOrigin(invoiceNCS.getOrigin());
				newLogAirwayBill.setConsignee(invoiceNCS.getPenerima());
				newLogAirwayBill.setDestination(invoiceNCS.getDest());
				newLogAirwayBill.setService(invoiceNCS.getService());
				newLogAirwayBill.setPackageWeight(invoiceNCS.getBeratDibebankan() != null && !invoiceNCS.getBeratDibebankan().isEmpty()?new BigDecimal(new Double(invoiceNCS.getBeratDibebankan())): new BigDecimal(0));
				newLogAirwayBill.setPricePerKg(invoiceNCS.getPublishRate() != null && !invoiceNCS.getPublishRate().isEmpty()?new BigDecimal(new Double(invoiceNCS.getPublishRate())): new BigDecimal(0));
				newLogAirwayBill.setInsuranceCharge(invoiceNCS.getAsuransi() != null && !invoiceNCS.getAsuransi().isEmpty()?new BigDecimal(new Double(invoiceNCS.getAsuransi())): new BigDecimal(0));
				newLogAirwayBill.setOtherCharge(invoiceNCS.getBiayaPackaging() != null && !invoiceNCS.getBiayaPackaging().isEmpty()?new BigDecimal(new Double(invoiceNCS.getBiayaPackaging())): new BigDecimal(0));
				newLogAirwayBill.setGiftWrapCharge(invoiceNCS.getBiayaGiftWrap() != null && !invoiceNCS.getBiayaGiftWrap().isEmpty()?new BigDecimal(new Double(invoiceNCS.getBiayaGiftWrap())):new BigDecimal(0));
				newLogAirwayBill.setTotalCharge(invoiceNCS.getTotalBiaya() != null && !invoiceNCS.getTotalBiaya().isEmpty()?new BigDecimal(new Double(invoiceNCS.getTotalBiaya())): new BigDecimal(0));

				if (!reportuploadEntryCreated) {
					_log.debug("report not created yet, creating new one");
					try{
						logInvoiceReportUpload = reportUploadHome.queryByRange("select o from LogInvoiceReportUpload o where o.invoiceNumber = '" + invoiceNumber + "'", 0, 1).get(0);
					} catch (IndexOutOfBoundsException e){
						logInvoiceReportUpload = new LogInvoiceReportUpload();
					}
					reportuploadEntryCreated = true;

					logInvoiceReportUpload = createInvoiceReportUpload(logInvoiceReportUpload, locator, invoiceNumber, fileUploadLog, new Long(result.size()), "NCS");
				}
				
				/*
				 * 2011-05-23 In accordance with JIRA VENICE-16 Pickup
				 * Data Late Only reconcile the AWB data if the existing data has come from MTA or has been reconciled against data from MTA. 
				 * Reconcile and merge the airwaybill
				 * At this point there'll always MTA data provided because MTA settlement happened on PU
				 */
				// Perform the reconciliation
				_log.debug("reconcile awb");
				AWBReconciliation awbReconciliation = new AWBReconciliation();
				awbReconciliation.performInvoiceReconciliation(logInvoiceReportUpload, newLogAirwayBill, mtaAwbMap.get(invoiceNCS.getNoKonos()), failedRecord);
				errorRowNumber++;
			}			
						
			uploadFailedRecord(failedRecord, fileUploadLog);
			fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_SUCCESS);
			invoiceUploadLogHome.mergeLogInvoiceUploadLog(fileUploadLog);
		} catch (Exception e) {
			String errMsg = LogisticsConstants.EXCEPTION_TEXT_GENERAL + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber ;
			_log.error(errMsg);
			e.printStackTrace();
			notificationText = notificationText.replaceFirst("REPLACE",	errMsg.replace("\"", " "));
			
			cleanupFailedReport(invoiceNumber);
			
			fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
			invoiceUploadLogHome.mergeLogInvoiceUploadLog(fileUploadLog);
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	private LogInvoiceReportUpload createInvoiceReportUpload(LogInvoiceReportUpload logInvoiceReportUpload, Locator<Object> locator,
			String invoiceNumber, LogInvoiceUploadLog fileUploadLog, long size, String providerCode) throws Exception{

		LogLogisticsProviderSessionEJBRemote logisticsProviderHome;
		try {
			logisticsProviderHome = (LogLogisticsProviderSessionEJBRemote) locator
					.lookup(LogLogisticsProviderSessionEJBRemote.class, "LogLogisticsProviderSessionEJBBean");
			LogInvoiceReportUploadSessionEJBRemote reportUploadHome = (LogInvoiceReportUploadSessionEJBRemote) locator
			.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");
	
			LogLogisticsProvider logisticsProvider = logisticsProviderHome.queryByRange("select o from LogLogisticsProvider o where o.logisticsProviderCode = '"+ providerCode +"'", 0, 1).get(0);
			
			logInvoiceReportUpload.setInvoiceNumber(invoiceNumber);
			logInvoiceReportUpload.setFileNameAndLocation(fileUploadLog.getFileUploadNameAndLoc());
			logInvoiceReportUpload.setLogLogisticsProvider(logisticsProvider);
		
			// Hard code this for now... fix later
			LogReportStatus logReportStatus = new LogReportStatus();
			logReportStatus.setReportStatusId(VeniceConstants.LOG_REPORT_STATUS_NEW);
			logInvoiceReportUpload.setLogReportStatus(logReportStatus);
			
			LogReportTemplate logReportTemplate = new LogReportTemplate();
			logReportTemplate.setTemplateId(new Long(5));
			logInvoiceReportUpload.setLogReportTemplate(logReportTemplate);
		
			LogApprovalStatus logApprovalStatus = new LogApprovalStatus();
			logApprovalStatus.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_NEW);
			logInvoiceReportUpload.setLogApprovalStatus(logApprovalStatus);
			
			logInvoiceReportUpload.setNumberOfRecords(size);
			logInvoiceReportUpload.setReportDesc(fileUploadLog.getActualFileUploadName() + ": Logistics invoice report");
			logInvoiceReportUpload.setReportTimestamp(SQLDateUtility.utilDateToSqlTimestamp(new Date()));
			logInvoiceReportUpload.setUserLogonName(fileUploadLog.getUploadedBy());
			
			return reportUploadHome.mergeLogInvoiceReportUpload(logInvoiceReportUpload);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	private void cleanupFailedReport(String invoiceNumber){
		Locator<Object> locator = null;
		boolean cleanUpReport = false, cleanUpAWB = false;
		try{
			locator = new Locator<Object>();
			LogInvoiceReportUploadSessionEJBRemote reportUploadHome = (LogInvoiceReportUploadSessionEJBRemote) locator
			.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");
			
			LogInvoiceAirwaybillRecordSessionEJBRemote awbRecordHome = (LogInvoiceAirwaybillRecordSessionEJBRemote) locator
			.lookup(LogInvoiceAirwaybillRecordSessionEJBRemote.class, "LogInvoiceAirwaybillRecordSessionEJBBean");

			LogInvoiceReconRecordSessionEJBRemote reconRecordHome = (LogInvoiceReconRecordSessionEJBRemote) locator
			.lookup(LogInvoiceReconRecordSessionEJBRemote.class, "LogInvoiceReconRecordSessionEJBBean");
			
			LogAirwayBillSessionEJBRemote awbHome = (LogAirwayBillSessionEJBRemote) locator
			.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			
			List<LogInvoiceReportUpload> logInvoiceReportUploadList = reportUploadHome.queryByRange("select o from LogInvoiceReportUpload o where o.invoiceNumber = '" + invoiceNumber + "'", 0, 0);
			List<LogInvoiceReportUpload> logInvoiceReportUploadDeleteList = new ArrayList<LogInvoiceReportUpload>(); 
			if(!logInvoiceReportUploadList.isEmpty()){
				for (LogInvoiceReportUpload logInvoiceReportUpload : logInvoiceReportUploadList) {
					cleanUpReport = false;
					List<LogInvoiceAirwaybillRecord> logInvoiceAirwaybillRecordList = awbRecordHome.queryByRange("select o from LogInvoiceAirwaybillRecord o where o.logInvoiceReportUpload.invoiceReportUploadId = " + logInvoiceReportUpload.getInvoiceReportUploadId(), 0, 0);
					List<LogInvoiceAirwaybillRecord> logInvoiceAirwaybillRecordDeleteList = new ArrayList<LogInvoiceAirwaybillRecord>();
					if(!logInvoiceAirwaybillRecordList.isEmpty()){
						for (LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord : logInvoiceAirwaybillRecordList) {
							cleanUpAWB = false;
							List<LogInvoiceReconRecord> logInvoiceReconRecordList = reconRecordHome.queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId = " + logInvoiceAirwaybillRecord.getInvoiceAirwaybillRecordId(), 0, 0);
							List<LogAirwayBill> logAirwayBillList = awbHome.queryByRange("select o from LogAirwayBill o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId = " + logInvoiceAirwaybillRecord.getInvoiceAirwaybillRecordId(), 0, 0);
							if(logInvoiceReconRecordList.isEmpty() && !logInvoiceAirwaybillRecord.getInvoiceResultStatus().equals("OK") || logAirwayBillList.isEmpty()){
								cleanUpAWB = true;
								if(!logAirwayBillList.isEmpty()){
									for (LogAirwayBill logAirwayBill : logAirwayBillList) {
										logAirwayBill.setLogInvoiceAirwaybillRecord(null);
										awbHome.mergeLogAirwayBill(logAirwayBill);
									}
								}
							}
							if(cleanUpAWB)
								logInvoiceAirwaybillRecordDeleteList.add(logInvoiceAirwaybillRecord);
						}
						awbRecordHome.removeLogInvoiceAirwaybillRecordList(logInvoiceAirwaybillRecordDeleteList);
					} else {
						cleanUpReport = true;
						logInvoiceReportUploadDeleteList.add(logInvoiceReportUpload);
					}
				}
				if(cleanUpReport)
					reportUploadHome.removeLogInvoiceReportUploadList(logInvoiceReportUploadList);
			}
		}catch(Exception e){
			try {
				locator.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			_log.error(e.getMessage());
		}			
	}
}
