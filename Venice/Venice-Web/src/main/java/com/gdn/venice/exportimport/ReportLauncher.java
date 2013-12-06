package com.gdn.venice.exportimport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;

/**
 * A generic class to launch Jasper reports
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class ReportLauncher {
	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public ReportLauncher() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.reporting.ReportLauncher");
	}

	String VeniceHome = System.getenv("VENICE_HOME");
	String jrxmlFileName;
	String jrxmlSubReportFileName;
	String jasperFileName;
	String jasperSubReportFileName;

	String dbUrl = "jdbc:postgresql:venice";
	// String dbDriver = props.getProperty("jdbc.driver");
	String dbDriver = "org.postgresql.Driver";
	// String dbUname = props.getProperty("db.username");
	String dbUname = "venice";
	// String dbPwd = props.getProperty("db.password");
	String dbPwd = "password";

	@SuppressWarnings("rawtypes")
	Map params = new HashMap();
	String pdfFileName;

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public void outputToPDF() {
		Connection conn = null;
		// Make sure that the report template is compiled.
		try {
			JasperCompileManager.compileReportToFile(VeniceHome + "/reports/template/" + jrxmlFileName, VeniceHome + "/reports/template/" + jasperFileName);
			if(jasperSubReportFileName != null){
				JasperCompileManager.compileReportToFile(VeniceHome + "/reports/template/" + jrxmlSubReportFileName, VeniceHome + "/reports/template/" + jasperSubReportFileName);
			}

			_log.info("Connecting to venice database...");

			// Load the JDBC driver
			Class.forName(dbDriver);
			// Get the connection
			conn = DriverManager.getConnection(dbUrl, dbUname, dbPwd);
			if (conn != null) {
				_log.info("Connected!");
			} else {
				throw new Exception("Database connection was not established!");
			}
		} catch (Exception e) {
			_log.error("An exception occured while compiling the Jasper Report:" + e.getMessage());
			e.printStackTrace();
		}

		_log.info("Starting report processing...");
		// Set the file name for the report based on the merchant
		try {
			// Generate jasper print
			_log.info("Generating report...");
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(VeniceHome + "/reports/template/" + jasperFileName, params, conn);

			// Export pdf file
			_log.info("Exporting report to PDF...");
			JasperExportManager.exportReportToPdfFile(jprint, VeniceHome + "/reports/output/" + pdfFileName);
			_log.info("Report exported to " + VeniceHome + "/reports/output/" + pdfFileName);

		} catch (Exception e) {
			_log.error("An exception occured while processing a report:" + e.getMessage());
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
		_log.info("Finished report processing...");
	}
	
	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public void outputToExcel() {
		Connection conn = null;
		// Make sure that the report template is compiled.
		try {
			JasperCompileManager.compileReportToFile(VeniceHome + "/reports/template/" + jrxmlFileName, VeniceHome + "/reports/template/" + jasperFileName);
			if(jasperSubReportFileName != null){
				JasperCompileManager.compileReportToFile(VeniceHome + "/reports/template/" + jrxmlSubReportFileName, VeniceHome + "/reports/template/" + jasperSubReportFileName);
			}

			_log.info("Connecting to venice database...");

			// Load the JDBC driver
			Class.forName(dbDriver);
			// Get the connection
			conn = DriverManager.getConnection(dbUrl, dbUname, dbPwd);
			if (conn != null) {
				_log.info("Connected!");
			} else {
				throw new Exception("Database connection was not established!");
			}
		} catch (Exception e) {
			_log.error("An exception occured while compiling the Jasper Report:" + e.getMessage());
			e.printStackTrace();
		}

		_log.info("Starting report processing...");
		// Set the file name for the report based on the merchant
		try {
			// Generate jasper print
			_log.info("Generating report...");
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(VeniceHome + "/reports/template/" + jasperFileName, params, conn);

			// Export pdf file
			_log.info("Exporting report to Excel...");
			JRXlsExporter exporter= new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jprint);
			exporter.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, true);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, VeniceHome + "/reports/output/" + pdfFileName);
			exporter.exportReport();
			//JasperExportManager.exportReportToPdfFile(jprint, VeniceHome + "/reports/output/" + pdfFileName);
			//JasperExportManager.export
			_log.info("Report exported to " + VeniceHome + "/reports/output/" + "ExcelFileName");

		} catch (Exception e) {
			_log.error("An exception occured while processing a report:" + e.getMessage());
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
		_log.info("Finished report processing...");
	}

	/**
	 * @return the jrxmlFileName
	 */
	public String getJrxmlFileName() {
		return jrxmlFileName;
	}

	/**
	 * @param jrxmlFileName the jrxmlFileName to set
	 */
	public void setJrxmlFileName(String jrxmlFileName) {
		this.jrxmlFileName = jrxmlFileName;
	}

	/**
	 * @return the jrxmlSubReportFileName
	 */
	public String getJrxmlSubReportFileName() {
		return jrxmlSubReportFileName;
	}

	/**
	 * @param jrxmlSubReportFileName the jrxmlSubReportFileName to set
	 */
	public void setJrxmlSubReportFileName(String jrxmlSubReportFileName) {
		this.jrxmlSubReportFileName = jrxmlSubReportFileName;
	}

	/**
	 * @return the jasperFileName
	 */
	public String getJasperFileName() {
		return jasperFileName;
	}

	/**
	 * @param jasperFileName the jasperFileName to set
	 */
	public void setJasperFileName(String jasperFileName) {
		this.jasperFileName = jasperFileName;
	}

	/**
	 * @return the jasperSubReportFileName
	 */
	public String getJasperSubReportFileName() {
		return jasperSubReportFileName;
	}

	/**
	 * @param jasperSubReportFileName the jasperSubReportFileName to set
	 */
	public void setJasperSubReportFileName(String jasperSubReportFileName) {
		this.jasperSubReportFileName = jasperSubReportFileName;
	}

	/**
	 * @return the dbUrl
	 */
	public String getDbUrl() {
		return dbUrl;
	}

	/**
	 * @param dbUrl the dbUrl to set
	 */
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	/**
	 * @return the dbDriver
	 */
	public String getDbDriver() {
		return dbDriver;
	}

	/**
	 * @param dbDriver the dbDriver to set
	 */
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	/**
	 * @return the dbUname
	 */
	public String getDbUname() {
		return dbUname;
	}

	/**
	 * @param dbUname the dbUname to set
	 */
	public void setDbUname(String dbUname) {
		this.dbUname = dbUname;
	}

	/**
	 * @return the dbPwd
	 */
	public String getDbPwd() {
		return dbPwd;
	}

	/**
	 * @param dbPwd the dbPwd to set
	 */
	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}

	/**
	 * @return the params
	 */
	public Map getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Map params) {
		this.params = params;
	}

	/**
	 * @return the pdfFileName
	 */
	public String getPdfFileName() {
		return pdfFileName;
	}

	/**
	 * @param pdfFileName the pdfFileName to set
	 */
	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}
	
	public String getPdfFileFullPath(){
		return VeniceHome + "/reports/output/" + pdfFileName;
	}
}
