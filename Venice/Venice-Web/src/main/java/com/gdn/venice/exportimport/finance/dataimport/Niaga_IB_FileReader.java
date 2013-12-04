package com.gdn.venice.exportimport.finance.dataimport;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;

/**
 * A class to read the Niaga internet banking transaction report file.
 * 
 *@author Roland
 * 
 */
public class Niaga_IB_FileReader {
	protected static Logger _log = null;

	public Niaga_IB_FileReader() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.Niaga_IB_FileReader");
	}

	/**
	 * Reads the Niaga IB report into an ArrayList of Niaga_IB_Record objects
	 * 
	 * @param fileNameAndPath
	 * @return the array list of records
	 * @throws Exception 
	 */
	public ArrayList<Niaga_IB_Record> readFile(String fileNameAndPath) throws Exception {
		ArrayList<Niaga_IB_Record> records = new ArrayList<Niaga_IB_Record>();
		/*
		 * String to store formatted report date, used to get the year of the transaction date.
		 */
		String reportDateFormatted="";

		_log.info("fileNameAndPath:" + fileNameAndPath);
		DataInputStream in = null;
		BufferedReader br = null;
		try {
			// Open the file
			FileInputStream fstream = new FileInputStream(fileNameAndPath);
			// Get the InputStream
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine = null;
			// Read the records from the file
			int line = 0;
			while ((strLine = br.readLine()) != null) {
				_log.info("File Data:" + strLine);
				// The lines we need start on line 6
				if (line < 2) {
					line++;
					continue;
				}

				if (line == 2 && strLine.contains("Run Date")) {
				/*
				 * read the report date to get the transaction year from position 100-112
				 */
				String reportDate = strLine.substring(100,112).trim();
				_log.debug("\n Report date: "+reportDate); 
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				sdf.setCalendar(GregorianCalendar.getInstance()); 
				
				String[] parts = reportDate.split(" ");
				reportDate = parts[1].replace(",", "") + "-" + parts[0] + "-" + parts[2]; 
				
				Date transDate= sdf.parse(reportDate);	
				sdf=new SimpleDateFormat("dd/MM/yyyy");					
				
				reportDateFormatted=sdf.format(transDate);
				_log.debug("\n Report date formatted: "+reportDateFormatted);
			} else if (strLine.contains("/")) {
				_log.debug("\n Check transaction record");
					/*
					 * Assume that any line that contains a "/" from a date must
					 * be a transaction because no other lines have any "/"
					 * other than the discarded lines at the top of the file.
					 */
					Niaga_IB_Record record = new Niaga_IB_Record();
					
					/*
					 * Read account number (wcs order id) from column Payment Id#
					 * from position 57-75 and trim
					 */
					record.setAccountNumber(strLine.substring(57, 75).trim());
					_log.info("\n Wcs order id: "+strLine.substring(57, 75));
					
					/*
					 * Read the payment amount from position 77 to 96,
					 * remove dot, replace comma and trim
					 */
					String strPaymentAmount = strLine.substring(77, 96).trim();
					strPaymentAmount = strPaymentAmount.replaceAll("\\.", "");
					strPaymentAmount = strPaymentAmount.replaceAll("\\,", ".");
					record.setPaymentAmount(new Double(strPaymentAmount));
					_log.debug("\n Payment amount: "+strPaymentAmount);
					
					/*
					 * Get the month and year of the report date
					 */
					String reportMonth = reportDateFormatted.substring(3, 5);
					String reportYear = reportDateFormatted.substring(6, 10);
					
					/*
					 * Read the transaction date from position 107-111
					 */
					//ambil string date transaksi ex 6/12
					_log.debug(" get date Transaction = "+strLine.substring(107, 112).trim());
					///split /pecah string dengan pemisah "/"
					String[] tempDateTrans = strLine.substring(107, 112).trim().split("/");
					//string hasil pemisah pertama adalah bulan
					String transMonth = tempDateTrans[0];
					_log.debug(" get Month Transaction = "+transMonth);
					//string hasil pemisah pertama adalah hari
					String transDate = tempDateTrans[1];
					_log.debug(" get day Transaction = "+transDate);
					int transYear = 0;
					
					/*
					 * Compare the month of report date with transaction date to determine if the year is the same or not.
					 */
					if(new Integer(reportMonth)<new Integer(transMonth)){
						_log.debug("\n reportMonth is less than transMonth, so subtract 1 year");
						transYear = new Integer(reportYear)-1;
					}else{
						transYear = new Integer(reportYear);
					}
					String transactionDate = transDate + "/" + transMonth + "/" + transYear; 
					_log.debug("\n Transaction date: "+transactionDate);
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					sdf.setCalendar(GregorianCalendar.getInstance());
					record.setPaymentDate(sdf.parse(transactionDate));
					records.add(record);
				}
				line++;
			}
			_log.info("\n Done read file");
		} catch (Exception e) {
			String errMsg = "An exception occured while processing the selected file:" + e.getMessage() + ". Please contact the systems administrator.";
			_log.error(errMsg);
			throw e;
		} finally{
			try{
				if(in!=null){
					in.close();
				}
				if(br!=null){
					br.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		/*
		 * Go back and calculate the fees for each transaction
		 * For CIMB Clicks the commission is 3000 for each order, but the commission is not in the report 
		 * and the paid amount in the report is already nett amount, so no need to subtract the commmission from the paid amount
		 */
		if (records.size() != 0) {
			Double fee=(double) 3000;
			for (Niaga_IB_Record record : records) {
				record.setBankFee(fee);
			}
		}
		return records;
	}
	
	/**
	 * Returns the unique identifier for the report (
	 * @param fileNameAndPath
	 * @return
	 * @throws Exception
	 */
	public String getUniqueReportIdentifier(String fileNameAndPath) throws Exception{
		DataInputStream in = null;
		BufferedReader br = null;
		try {
			// Open the file
			FileInputStream fstream = new FileInputStream(fileNameAndPath);
			// Get the InputStream
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine = null;

			String uniqueId = "";
			// Read the records from the file and extract the unique id
			int line = 0;
			while ((strLine = br.readLine()) != null) {
				_log.info(line + ":File Data:" + strLine);

				/*
				 * Modified to use the file date based on user feedback from 2011-11-04
				 * because the file number will be non-unique				 
				 */
				
				if(line == 2 && strLine.contains("Run Date")){
					uniqueId = strLine.substring(100,112).trim();
					_log.debug("\n Unique id before formatted: "+uniqueId);
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
					sdf.setCalendar(GregorianCalendar.getInstance()); 
					
					String[] parts = uniqueId.split(" ");
					uniqueId = parts[1].replace(",", "") + "-" + parts[0] + "-" + parts[2]; 
					
					Date transDate= sdf.parse(uniqueId);	
					sdf=new SimpleDateFormat("dd/MM/yyyy");
					uniqueId=sdf.format(transDate);
					
					break;
				}
				line++;
				
			}
			return uniqueId;
		} catch (Exception e) {
			String errMsg = "An exception occured while finding the unique identifier for the selected file:" + e.getMessage() + ". Please contact the systems administrator.";
			_log.error(errMsg);
			throw e;
		} finally{
			try{
				if(in!=null){
					in.close();
				}
				if(br!=null){
					br.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
