package com.gdn.venice.reporting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.util.HolidayUtil;
import com.gdn.venice.persistence.VenMerchant;

/**
 * A main application for running the sales settlement report batch.
 * This will produce a sales settlement report for every merchant
 * that has settlements within the period immediately prior to the
 * date
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class SalesSettlementBatchJob {
	protected static Logger _log = null;

	Properties prop = new Properties();
	private static String CONFIG_FILE = System.getenv("VENICE_HOME") + "/admin/config.properties";
	private String dbHost = "";
	private String dbPort = "";
	private String dbUsername = "";
	private String dbPassword = "";
	private String environment = "";
	private String dbName = "";
	private static Connection conn;

	/**
	 * Default constructor.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public SalesSettlementBatchJob() throws Exception {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.reporting.SalesSettlementBatchJob");
		
		prop.load(new FileInputStream(CONFIG_FILE));
		environment = prop.getProperty("environment");
		dbHost = prop.getProperty(environment + ".dbHost");
		dbPort = prop.getProperty(environment + ".dbPort");
		dbUsername = prop.getProperty(environment + ".dbUsername");
		dbPassword = prop.getProperty(environment + ".dbPassword");
		dbName = prop.getProperty(environment + ".dbName");
		
		System.out.println("environment: "+environment);
		System.out.println("dbHost: "+dbHost);
		System.out.println("dbPort: "+dbPort);
		
		setupDBConnection();
	}
	
	private void setupDBConnection() throws Exception{
		Class.forName("org.postgresql.Driver");		
		conn = DriverManager.getConnection("jdbc:postgresql://" + dbHost +":" + dbPort + "/" + dbName, dbUsername, dbPassword);
		
		if (conn != null) {
			_log.info("Connected");
		} else {
			throw new Exception("Database connection was not established!");
		}
	}
	
	/**
	 * Considers holidays etc. and returns the report period cut off date.
	 *     o for example if the current date is 30th (28th in the case of February)
	 *       then the cut off date will be 2 days prior to include all transactions
	 *       up and until midnight on the day that is 3 days prior
	 * @param effectiveDate
	 * @return the cut off date
	 */
	private Date getPeriodCutOffDate(Date effectiveDate){
		Date periodCutOffDate = effectiveDate;
		
		Calendar effectiveDateCalendar = Calendar.getInstance();
		effectiveDateCalendar.setTime(effectiveDate);
		
		Calendar periodCutOffCalendar = Calendar.getInstance();
		periodCutOffCalendar.setTime(periodCutOffDate);
		
		/*
		 * See if the cutoff date is the first period of the month
		 * We determine this by the evaluating the effectiveDate
		 *    o if it is before 30th of the month and not Feb
		 *      then it must be the first period
		 *    o if it is on or after 30th
		 *      then it must be the second period
		 *    o for February including leap year
		 *       
		 */
		if(effectiveDateCalendar.get(Calendar.DAY_OF_MONTH) < 30 && effectiveDateCalendar.get(Calendar.MONTH) != Calendar.FEBRUARY){
			periodCutOffCalendar.set(Calendar.DAY_OF_MONTH, 18);
		}
		
		if(effectiveDateCalendar.get(Calendar.DAY_OF_MONTH) >= 30 ){
			periodCutOffCalendar.set(Calendar.DAY_OF_MONTH, 28);
		}
		
		if(effectiveDateCalendar.get(Calendar.DAY_OF_MONTH) < 27 && effectiveDateCalendar.get(Calendar.MONTH) == Calendar.FEBRUARY){
			periodCutOffCalendar.set(Calendar.DAY_OF_MONTH, 18);
		}

		if(effectiveDateCalendar.get(Calendar.DAY_OF_MONTH) >= 27 && effectiveDateCalendar.get(Calendar.MONTH) == Calendar.FEBRUARY){
			periodCutOffCalendar.set(Calendar.DAY_OF_MONTH, effectiveDateCalendar.get(Calendar.DAY_OF_MONTH) - 2);
		}
		
		/*
		 * Subtract a day for each day that is a holiday
		 */
		while(HolidayUtil.isHoliday(periodCutOffCalendar.getTime())){
			periodCutOffCalendar.set(Calendar.DAY_OF_YEAR, periodCutOffCalendar.get(Calendar.DAY_OF_YEAR) - 1);
		}
		return periodCutOffCalendar.getTime();
	}
	
	/**
	 * Returns the period start date based on the cutoff date
	 * @param periodCutOffDate
	 * @return the period start date
	 */
	private Date getPeriodStartDate(Date periodCutOffDate){
		Calendar periodStartCal = Calendar.getInstance();
		periodStartCal.setTime(periodCutOffDate);

		/*
		 * Before the 20th assume that it is period 1
		 */
		Calendar periodCutOffCal = Calendar.getInstance();
		periodCutOffCal.setTime(periodCutOffDate);
		if(periodCutOffCal.get(Calendar.DAY_OF_MONTH) < 20){
			periodStartCal.set(Calendar.DAY_OF_MONTH, 1);
		}else{
			periodStartCal.set(Calendar.DAY_OF_MONTH, 18);
		}
		return periodStartCal.getTime();
	}
	
	public void generateSalesSettlementReport(VenMerchant merchant, String salesRecordIdList){
		System.out.println("salesRecordId di report: "+salesRecordIdList);		
		try {
			SalesSettlementBatchJob salesSettlementBatchJob= new SalesSettlementBatchJob();
			_log.info("start generateSalesSettlementReport");
			
			_log.debug("get period");
			Calendar runDate = Calendar.getInstance();	
			Date periodCutOffDate = salesSettlementBatchJob.getPeriodCutOffDate(runDate.getTime());
			Date periodStartDate = salesSettlementBatchJob.getPeriodStartDate(periodCutOffDate);
			
			_log.info("periodStartDate: "+periodStartDate.toString());
			_log.info("periodCutOffDate: "+periodCutOffDate.toString());
						
			String VeniceHome = System.getenv("VENICE_HOME");
			String jrxmlFileName = VeniceHome + "/reports/template/" + "SalesSettlementReport.jrxml";
			String jasperFileName = VeniceHome + "/reports/template/" + "SalesSettlementReport.jasper";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");					
			
	 	    /*
	 	     * Generate the sales settlement reports for the prior period
	 	     * for each merchant in the list 
	 	     */
			_log.debug("compile report");
			JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
			_log.debug("done compile report");
			
			_log.info("processing merchant: "+merchant.getMerchantId()+" - "+merchant.getVenParty().getFullOrLegalName());
			Calendar periodCalendar = Calendar.getInstance();
			periodCalendar.setTime(periodCutOffDate);
			periodCalendar.set(Calendar.DAY_OF_MONTH, periodCalendar.get(Calendar.DAY_OF_MONTH) - 1);
			   
			//Set the file name for the report based on the merchant
			String pdfFileName = VeniceHome + "/reports/output/merchant/SalesSettlementReport-" + sdf.format(periodCalendar.getTime()) + "-" + merchant.getWcsMerchantId() + ".pdf";
			   
			try {										
				//Set up the report parameter(s)
			   Map<String, Object> params = new HashMap<String, Object>();
			   params.put("merchant_id", merchant.getMerchantId());
			   params.put("period_date_to", periodCutOffDate);
			   params.put("sales_record_Id_list", salesRecordIdList);
				   
			   // Generate jasper print
			   _log.info("Generating Sales Settlement Report for " + merchant.getWcsMerchantId());
			   JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, params, conn);
			   
			   // Export pdf file
			   _log.info("Exporting Sales Settlement Report for " + merchant.getWcsMerchantId() + " to PDF...");
			   JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
			   _log.info("Sales Settlement Report for " + merchant.getWcsMerchantId() + " exported to " + pdfFileName);
			} catch (Exception e) {
				_log.error("An exception occured while processing a Sales Settlement Report:" + e.getMessage());
				e.printStackTrace();
				throw new Exception();
			}			
			_log.info("Done generate sales settlement report.");			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(conn!=null){
					conn.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
