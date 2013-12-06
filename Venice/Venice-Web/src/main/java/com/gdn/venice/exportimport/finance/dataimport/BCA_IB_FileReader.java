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
 * A class to read the BCA internet banking transaction report file.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class BCA_IB_FileReader {
	protected static Logger _log = null;

	public BCA_IB_FileReader() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.BCA_IB_FileReader");
	}

	/**
	 * Reads the BCA IB report into an ArrayList of BCA_VA_IB_Record objects
	 * 
	 * @param fileNameAndPath
	 * @return the array list of records
	 * @throws Exception 
	 */
	public ArrayList<BCA_VA_IB_Record> readFile(String fileNameAndPath) throws Exception {
		ArrayList<BCA_VA_IB_Record> records = new ArrayList<BCA_VA_IB_Record>();

		/*
		 * This is a single value read from the file that needs to be
		 * apprortioned evenly across the records in the file.
		 */
		Double commission = new Double(0);
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
				// The lines we need start on line 7
				if (line < 7) {
					line++;
					continue;
				}
				/*
				 * If it is the total/commission line then read the commission
				 * else read it as a transaction
				 */
				if (strLine.contains("KOMISI :")) {
					/*
					 * read the commission from position 132 back to the last
					 * colon
					 */
					String[] varS=strLine.split("\\s+");
					String strCommission = varS[11].trim();//strLine.substring(strLine.lastIndexOf(':')+1, 132);
					strCommission = strCommission.replaceAll("\\.", "");
					strCommission = strCommission.replaceAll("\\,", ".");
					commission = new Double(strCommission.trim());
				} else if (strLine.contains("/")) {
					/*
					 * Assume that any line that contains a "/" from a date must
					 * be a transaction because no other lines have any "/"
					 * other than the discarded lines at the top of the file.
					 */
					BCA_VA_IB_Record record = new BCA_VA_IB_Record();
					/*
					 * Read account number from nama/identitas 
					 * from position 43-70 and trim
					 */
					String[] varS=strLine.split("\\s+");
					record.setAccountNumber(varS[3].trim());//strLine.substring(43, 70).trim());
					/*
					 * Read the payment amount from position 72 to 93,
					 * remove dot, replace comma and trim
					 */
					String strPaymentAmount = varS[4].trim();//strLine.substring(72, 93).trim();
					strPaymentAmount = strPaymentAmount.replaceAll("\\.", "");
					strPaymentAmount = strPaymentAmount.replaceAll("\\,", ".");
					record.setPaymentAmount(new Double(strPaymentAmount));

					/*
					 * Read the transaction date from position 100-107
					 */
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					sdf.setCalendar(GregorianCalendar.getInstance());
					String strPaymentDate =varS[5].trim(); //strLine.substring(100, 107);
					String[] parts = strPaymentDate.split("/");
					strPaymentDate = parts[0] + "/" + parts[1] + "/20" + parts[2] +" "+varS[6].trim(); 
					Date tgl=sdf.parse(strPaymentDate);
					record.setPaymentDate(new java.sql.Timestamp(tgl.getTime()));
					records.add(record);
				}
				line++;
			}
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
		 */
		if (records.size() != 0) {
			Double fee = commission / records.size();

			for (BCA_VA_IB_Record record : records) {
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
				 * DF
				 */
				
				if(line == 1 && strLine.contains("TANGGAL   :")){
					uniqueId = strLine.substring(strLine.lastIndexOf("TANGGAL") + 12).trim();
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
