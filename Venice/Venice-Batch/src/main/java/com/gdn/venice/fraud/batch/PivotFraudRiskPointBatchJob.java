package com.gdn.venice.fraud.batch;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;

public class PivotFraudRiskPointBatchJob {
	
	protected static Logger _log = null;
	private String CONFIG_FILE = System.getenv("VENICE_HOME") + "/admin/config.properties";
	private String PIVOT_QUERY = "insert into frd_pivot_risk_point (order_id, rp1, rp2, rp3, rp4, rp5, rp6, rp7, rp8, rp9, rp10," + 
			"rp11, rp12, rp13, rp14, rp15, rp16, rp17, rp18, rp19, rp20, rp21, rp22, rp23, rp24, rp25, rp26, rp27, rp28, rp29, rp30,"+ 
			"rp31,rp32,rp33,rp34,rp35,rp36,rp37,rp38,rp39,rp40)" +
				"(SELECT * FROM crosstab(" +
					"'SELECT order_id, fraud_rule_name, risk_points FROM frd_fraud_suspicion_case c" +
					" left outer join frd_fraud_suspicion_points p on p.fraud_suspicion_case_id = c.fraud_suspicion_case_id " +
					"where order_id NOT IN (SELECT order_id FROM frd_pivot_risk_point)" +
					"order by 1,2')" +
				"AS ct (\"Order Id\" int8, \"Rule 01\" int4, \"Rule 02\" int4, \"Rule 03\" int4, \"Rule 04\" int4, \"Rule 05\" int4, \"Rule 06\" int4, \"Rule 07\" int4, " +
				"\"Rule 08\" int4, \"Rule 09\" int4, \"Rule 10\" int4, \"Rule 11\" int4, \"Rule 12\" int4, \"Rule 13\" int4, \"Rule 14\" int4, \"Rule 15\" int4, \"Rule 16\" int4, " +
				"\"Rule 17\" int4, \"Rule 18\" int4, \"Rule 19\" int4, \"Rule 20\" int4, \"Rule 21\" int4, \"Rule 22\" int4, \"Rule 23\" int4, \"Rule 24\" int4, \"Rule 25\" int4, " +
				"\"Rule 26\" int4, \"Rule 27\" int4, \"Rule 28\" int4, \"Rule 29\" int4, \"Rule 30\" int4, \"Rule 31\" int4, \"Rule 32\" int4, \"Rule 33\" int4, \"Rule 34\" int4, " +
				"\"Rule 35\" int4, \"Rule 36\" int4, \"Rule 37\" int4, \"Rule 38\" int4, \"Rule 39\" int4, \"Rule 40\" int4))";
	
	private static Connection conn;
	
	public PivotFraudRiskPointBatchJob(){
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.fraud.batch.PivotFraudRiskPointBatchJob");
		conn = setupConnection();
	}

	private Connection setupConnection() {
		_log.debug("Setting up connection....");
		Properties prop = new Properties();
		String environment = "";
		try{
			Class.forName("org.postgresql.Driver");		
			prop.load(new FileInputStream(CONFIG_FILE));
			environment = prop.getProperty("environment");
			_log.debug("Environment: " + prop.getProperty("environment"));
			_log.debug("DB Host: " + prop.getProperty(environment + ".dbHost"));
			_log.debug("DB: " + prop.getProperty(environment + ".dbName"));
			_log.debug("DB Port: " + prop.getProperty(environment + ".dbPort"));
			return DriverManager.getConnection("jdbc:postgresql://" + prop.getProperty(environment + ".dbHost") +":" + prop.getProperty(environment + ".dbPort") + 
					"/" + prop.getProperty(environment + ".dbName"), prop.getProperty(environment + ".dbUsername"), prop.getProperty(environment + ".dbPassword"));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	private void generatePivot(){
		try{
			_log.debug("Inserting fraud risk point to pivot table....");
			Long startTime = System.currentTimeMillis();
			PreparedStatement psPivotRiskPoint = conn.prepareStatement(PIVOT_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int count = psPivotRiskPoint.executeUpdate();
			_log.debug("Migrated data: " + count + " rows");
			Long endTime = System.currentTimeMillis();
			_log.debug("Pivot duration:" + (endTime - startTime) + "ms");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PivotFraudRiskPointBatchJob pivotJob = new PivotFraudRiskPointBatchJob();
		if(conn != null){
			pivotJob.generatePivot();
		}
	}	
}
