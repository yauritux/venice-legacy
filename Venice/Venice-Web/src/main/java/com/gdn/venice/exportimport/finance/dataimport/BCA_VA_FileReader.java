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
 * A class to read the BCA virtual account transaction report file. 
 *  
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class BCA_VA_FileReader {
	protected static Logger _log = null;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public BCA_VA_FileReader() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.BCA_VA_FileReader");
	}
	
	/**
	 * Reads the BCA IB report into an ArrayList of BCA_VA_IB_Record objects
	 * @param fileNameAndPath
	 * @return the array list of records
	 * @throws Exception 
	 */
	public ArrayList<BCA_VA_IB_Record> readFile(String fileNameAndPath) throws Exception{
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
				_log.info(line + ":File Data:" + strLine);
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
					 * Read account number from nomor penggenal
					 * from position 18-28 and trim
					 */
					String[] varS=strLine.split("\\s+");
					//record.setAccountNumber(strLine.substring(18, 28).trim());
					record.setAccountNumber(varS[2].trim());
					/*
					 * Read the payment amount from position 72 to 93,
					 * remove dot, replace comma and trim
					 */
					//String strPaymentAmount = strLine.substring(72, 93).trim();
					
					for(int i=1; i< varS.length;i++){
						if(varS[i].matches("\\d+/+\\d+/+\\d+")){
							/*
							 * Read the payment amount 
							 * remove dot, replace comma and trim
							 */
							String strPaymentAmount = varS[i-1].trim();
							strPaymentAmount = strPaymentAmount.replaceAll("\\.", "");
							strPaymentAmount = strPaymentAmount.replaceAll("\\,", ".");
							record.setPaymentAmount(new Double(strPaymentAmount));
							/*
							 * Read the transaction date from position 100-107
							 */
							
							sdf.setCalendar(GregorianCalendar.getInstance());
							String strPaymentDate = varS[i].trim();//strLine.substring(100, 107);
							String[] parts = strPaymentDate.split("/");
							strPaymentDate = "20" +parts[2] + "-" + parts[1] + "-"+ parts[0]+" "+varS[i+1].trim(); 
							Date tgl=sdf.parse(strPaymentDate);
							record.setPaymentDate(new java.sql.Timestamp(tgl.getTime()));
							break;
						}
						
					}								
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
	
	public ArrayList<BCA_VA_IB_Record> readNewFile(String fileNameAndPath) throws Exception{
		ArrayList<BCA_VA_IB_Record> records = new ArrayList<BCA_VA_IB_Record>();
		/*
		 * This is a single value read from the file that needs to be
		 * apprortioned evenly across the records in the file.
		 */
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
				_log.info(line + ":File Data:" + strLine);
				// The lines we need start on line 7
				if (line < 7) {
					line++;
					continue;
				}				
				if (strLine.contains("/")) {
					/*
					 * Assume that any line that contains a "/" from a date must
					 * be a transaction because no other lines have any "/"
					 * other than the discarded lines at the top of the file.
					 */
					BCA_VA_IB_Record record = new BCA_VA_IB_Record();
					/*
					 * Read account number from nomor penggenal 
					 */
					String[] varS=strLine.split("\\s+");
					record.setAccountNumber("00481"+varS[2].replaceAll("\\s",""));

					for(int i=1; i< varS.length;i++){
						if(varS[i].matches("\\d+/+\\d+/+\\d+")){
							/*
							 * Read the payment amount 
							 * remove dot, replace comma and trim
							 */
							String strPaymentAmount = varS[i-1].trim();
							strPaymentAmount = strPaymentAmount.replaceAll("\\,", "");
							record.setPaymentAmount(new Double(strPaymentAmount));

							/*
							 * Read the transaction date 
							 */
							sdf.setCalendar(GregorianCalendar.getInstance());
							String strPaymentDate = varS[i];
							String[] parts = strPaymentDate.split("/");
							strPaymentDate =  "20" + parts[2] +"-"+parts[1] + "-" + parts[0] +" "+varS[i+1]; 
							Date tgl=sdf.parse(strPaymentDate);
							record.setPaymentDate(new java.sql.Timestamp(tgl.getTime()));
							break;
						}
						
					}					
				
					
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
			Double fee = new Double("3000");

			for (BCA_VA_IB_Record record : records) {
				record.setBankFee(fee);
			}
		}
		return records;
	}
	public ArrayList<BCA_VA_IB_Record> readNewFile2(String fileNameAndPath) throws Exception{
		ArrayList<BCA_VA_IB_Record> records = new ArrayList<BCA_VA_IB_Record>();
		/*
		 * This is a single value read from the file that needs to be
		 * apprortioned evenly across the records in the file.
		 */
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
				_log.info(line + ":File Data:" + strLine);
				// The lines we need start on line 7
				if (line < 6) {
					line++;
					continue;
				}				
				if (strLine.contains("/")) {
					/*
					 * Assume that any line that contains a "/" from a date must
					 * be a transaction because no other lines have any "/"
					 * other than the discarded lines at the top of the file.
					 */
					BCA_VA_IB_Record record = new BCA_VA_IB_Record();
					/*
					 * Read account number from nomor penggenal 
					 */
					String[] varS=strLine.split("\\s+");
					record.setAccountNumber(varS[1].replace("-", "").replaceAll("\\s","").trim());
					
					for(int i=1; i< varS.length;i++){
						if(varS[i].matches("\\d+/+\\d+/+\\d+")){
							/*
							 * Read the payment amount 
							 * remove dot, replace comma and trim
							 */
							String strPaymentAmount = varS[i-1].trim();
							strPaymentAmount = strPaymentAmount.replaceAll("\\,", "");
							record.setPaymentAmount(new Double(strPaymentAmount));

							/*
							 * Read the transaction date 
							 */
							SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							sdff.setCalendar(GregorianCalendar.getInstance());
							String strPaymentDate =  varS[i].trim()+" "+varS[i+1].trim(); 
							Date tgl=sdff.parse(strPaymentDate);
							record.setPaymentDate(new java.sql.Timestamp(tgl.getTime()));
							break;
						}
						
					}					
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
			Double fee = new Double("3000");

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
			boolean start=false;
			while ((strLine = br.readLine()) != null) {
				_log.info(line + ":File Data:" + strLine);

				/*
				 * Modified to use the file date and time based on user feedback from 2011-11-04
				 * because the file number will be non-unique
				 * DF
				 */

				if(line == 1 && strLine.contains("TANGGAL   :")){
					//uniqueId = strLine.substring(strLine.lastIndexOf("TANGGAL") + 14).trim();
					String[] temp=strLine.split(":");
					uniqueId=temp[2].replaceAll("\\s","");
					start=true;
				}else if(start && strLine.contains("HALAMAN")){
					Date date=new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					String nowTime=sdf.format(date);
					uniqueId = uniqueId +"-"+nowTime+"&new";
					break;
				}else{
					start=false;
				}
				
				if(line == 2 && strLine.contains("JAM       :")){
					uniqueId = uniqueId + "-" + strLine.substring(strLine.lastIndexOf("JAM") + 14).trim() +"&old";
					break;
				}else if(line == 2){
					Date date=new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String nowTime=sdf.format(date);
					uniqueId = uniqueId +"-"+nowTime+"&new2";
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
