package com.gdn.venice.exportimport.finance.dataimport;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;

/**
 * A class the read the Mandiri MT942 formatted transaction file.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class MT942_FileReader {
	protected static Logger _log = null;

	public MT942_FileReader() {
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.MT942_FileReader");
	}
	
	/**
	 * Reads an MT942 formatted file into an ArrayList of records
	 * @param fileNameAndPath
	 * @return the ArrayList of MT942_Record
	 * @throws Exception 
	 */
	public ArrayList<MT942_Record> readFile(String fileNameAndPath) throws Exception{
		ArrayList<MT942_Record> records = new ArrayList<MT942_Record>();

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
				
				/*
				 * Ignore any lines that are not part of a transaction
				 */
				if(!strLine.startsWith(":61:") && !strLine.startsWith(":86:")){
					continue;
				}
				MT942_Record record = new MT942_Record();
				
				/*
				 * If the line begins with :61: read the date and amount
				 * else if it begins with :81: read the digits after FFFFFF
				 * as the account information
				 */
				if (strLine.startsWith(":61:")) {					
					/*
					 * Read the transaction date from position 4-10 and prepend "20"
					 */
					String strPaymentDate = strLine.substring(4, 10);
					strPaymentDate = "20" + strPaymentDate; // Will be ok until 2100
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					sdf.setCalendar(GregorianCalendar.getInstance());
					record.setPaymentDate(sdf.parse(strPaymentDate));
					
					String transactionType = strLine.substring(10,11);// Debit or Credit (D, C)
					if(!transactionType.equals("C")){
						_log.info("A transaction type that is not a credit was encountered in the MT942 file");
						/*
						 * Need to read an extra line here to remove the corresponding 86 
						 */
						strLine = br.readLine();
						_log.info("File Data:" + strLine); 
						continue;
					}
					
					/*
					 * Read the payment amount from position 11 until the
					 * transaction code is encountered (all of them start with N)
					 * and drop/remove any decimal point and fraction
					 */
					String strPaymentAmount = strLine.substring(11, strLine.indexOf("N", 11));
					
					if(strPaymentAmount.contains(".")){
						String[] tokens = strPaymentAmount.split(".");
						strPaymentAmount = tokens[0];
					}
					
					if(strPaymentAmount.contains(",")){
						String[] tokens = strPaymentAmount.split(",");
						strPaymentAmount = tokens[0];
					}
					
					record.setPaymentAmount(new Double(strPaymentAmount));
					/*
					 * Read the corresponding 86 for account information
					 */
					strLine = br.readLine();
					_log.info("File Data:" + strLine);
					if (strLine.startsWith(":86:")) {
						/*
						 * Note that there may be :86: records that are not payments
						 * in this case we need to skip the line and move on.
						 * 
						 * For VA Mandiri, all the lines that have credits for payments are prefixed with "UBP"
						 * For IB Mandiri credits for payment are not prefixed with "UBP"
						 */
						if(fileNameAndPath.contains("Mandiri_VA")){
							_log.info("Processing Mandiri VA payments");
								if(!strLine.substring(4, 7).equals("UBP")){
									_log.info("A corresponding tag :86: for Mandiri VA not prefixed with UBP (not have credits for payments)");
									continue;
								}else{
									/*
									 * Read account information from the characters from 
									 * position 24 (21 digits after the 86 tag)
									 */
									String vaNumber=strLine.substring(24).trim();									
									record.setAccountNumber(vaNumber);
									_log.info("A corresponding tag :86: is prefixed with UBP (have credits for payments), VA number: "+vaNumber);
								}
						}else if(fileNameAndPath.contains("Mandiri_IB")){
							_log.info("Processing Mandiri IB payments");
							/*
							 * tag :86: which contain BLIBLI.COM is skipped, it is not contain account information
							 */
							if(strLine.substring(10, 19).equals("BLIBLI.COM")){
								_log.info("A corresponding tag :86: Mandiri IB does not have account information (order ID)");
								continue;
							}else{
								/*
								 * In Mandiri IB, the account information is taken from order id, it is from position 4 until "-"
								 */
								String orderId = strLine.substring(4, strLine.indexOf("-", 4));								
								record.setAccountNumber(orderId.trim());
								_log.info("A corresponding tag :86: Mandiri IB does have account information (order ID): "+orderId);
								//Untuk mencari bank fee Mandiri_IB by arifin
								record.setBankFee(new Double(0));
								boolean isTrue= true;
								double amountbankFee= 0;
								int count=0;								
									while (isTrue){
										strLine = br.readLine();							
										_log.info("File Data loop ke dua:" + strLine);							
										if (strLine.startsWith(":61:")){
											String tempType = strLine.substring(10,11);
											_log.info("tempType = "+tempType);
											if(tempType.equals("D")){
												amountbankFee=amountbankFee + ( new Double (strLine.substring(strLine.indexOf("D", 1)+1,strLine.indexOf("N", strLine.indexOf("D", 1)+1))));
												_log.info("amountbankFee = "+amountbankFee+" + "+strLine.substring(strLine.indexOf("D", 1)+1,strLine.indexOf("N", strLine.indexOf("D", 1)+1)));												
											}
											count++;
											
										}	else continue;		
										if (count==2) {
											record.setBankFee(amountbankFee);
											_log.info("setBankFee = "+amountbankFee);
											
											isTrue=false;
										}
									}								
							}			
						}
					}else{
						_log.error("A corresponding tag :86: was not found after tag 61 in the MT942 file. Please contact the systems administrator");
					}					
					records.add(record);
				}
				
				//records.add(record);				
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
			/*
			 *  Read the records from the file and extract the unique id
			 *  as tag 25 + '-' + tag 28C
			 */
			int line = 0;
			while ((strLine = br.readLine()) != null) {
				_log.info(line + ":File Data:" + strLine);
				//The line with tag 25 and 28C combined contains the uniqueId
				if (strLine.contains(":20:")) {
					uniqueId = strLine.substring(strLine.lastIndexOf(':') +1).trim();
				}else if (strLine.contains(":25:")) {
					uniqueId = uniqueId + "-" +strLine.substring(strLine.lastIndexOf(':') +1).trim();
				} else if (strLine.contains(":28C:")) {
					uniqueId = uniqueId + "-" + strLine.substring(strLine.lastIndexOf(':') +1).trim();					
				}else if(strLine.contains(":13D:")) {
					uniqueId = uniqueId + "-" + strLine.substring(strLine.lastIndexOf(':') +1).trim()+"&new";
					break;
				} else if (strLine.contains(":61:")) {			
					uniqueId = uniqueId + "&old";	
					break;
				}
				line++;
			}
			// Close the input stream
			in.close();
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
