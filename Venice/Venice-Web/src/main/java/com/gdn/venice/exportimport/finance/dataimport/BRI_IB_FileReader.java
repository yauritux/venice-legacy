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
 * A class to read the BRI internet banking transaction report file.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class BRI_IB_FileReader {
	protected static Logger _log = null;

	public BRI_IB_FileReader() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.BRI_IB_FileReader");
	}

	/**
	 * Reads the BRI IB report into an ArrayList of BRI_IB_Record objects
	 * 
	 * @param fileNameAndPath
	 * @return the array list of records
	 * @throws Exception 
	 */
	public ArrayList<BRI_IB_Record> readFile(String fileNameAndPath) throws Exception {
		ArrayList<BRI_IB_Record> records = new ArrayList<BRI_IB_Record>();

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
				if (line < 1) {
					line++;
					continue;
				}
					BRI_IB_Record record = new BRI_IB_Record();
					/*
					 * Read account number from nama/identitas 
					 */
					String[] varS=strLine.split(";");
					record.setPayeeId(varS[0].trim());
					record.setBillReferenceNo(varS[1].trim());
					record.setBillAccountNo(varS[2].trim());
					record.setAmount(new Double(varS[3].trim()));
					record.setStatus(varS[4].trim());
					record.setPaymentRefNo(varS[5].trim());
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
					sdf.setCalendar(GregorianCalendar.getInstance());
					String strPaymentDate =varS[6].trim().substring(0, 4)+"-"+varS[6].trim().substring(4,6)+"-"+varS[6].trim().substring(6,8)+" "+varS[7].trim().substring(0, 2)+":"+varS[7].trim().substring(2, 4)+":"+varS[7].trim().substring(4, 6); 
					Date tgl=sdf.parse(strPaymentDate);
					record.setTransactionDateTime(new java.sql.Timestamp(tgl.getTime()));
					
					record.setBankFee(new Double(varS[8].trim()));
					records.add(record);
				
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
		Date tgl = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		String uniqueId =sdf.format(tgl.getTime());		
		return uniqueId;
		
	}
}
