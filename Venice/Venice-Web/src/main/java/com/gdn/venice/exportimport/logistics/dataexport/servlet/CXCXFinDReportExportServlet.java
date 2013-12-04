package com.gdn.venice.exportimport.logistics.dataexport.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.hibernate.ejb.EntityManagerImpl;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.exportimport.logistics.dataexport.AirwayBill;
import com.gdn.venice.util.VeniceConstants;

/**
 * Servlet implementation class CXCXFinDReportExportServlet.
 * 
 * @author Roland
 * 
 */
public class CXCXFinDReportExportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Logger _log = null;
	
	@PersistenceUnit
	private EntityManagerFactory emf;
	private EntityManager emForJDBC;
	
	private static final String EXPORT_REPORT_CX_SQL = "select order_item_id from ven_order_item_status_history " +
													   "where order_status_id="+VeniceConstants.VEN_ORDER_STATUS_CX+" and status_change_reason not like '%CX Finance%' and history_timestamp>to_timestamp(?,'MM/DD/YYYY')";
	
	private static final String EXPORT_REPORT_CX_AIRWAYBILL_LIST_SQL = "select airway_bill_number, gdn_reference, wcs_order_id, wcs_order_item_id from log_airway_bill ab " +
																		 "inner join ven_order_item oi on ab.order_item_id = oi.order_item_id " +
																		 "inner join ven_order o on oi.order_id = o.order_id " +
																		 "where ab.order_item_id=?";
	
	private static final String EXPORT_REPORT_CX_FINANCE_SQL = "select order_item_id from ven_order_item_status_history " +
															   "where order_status_id="+VeniceConstants.VEN_ORDER_STATUS_CX+" and status_change_reason like '%CX Finance%' and history_timestamp>to_timestamp(?,'MM/DD/YYYY')";
	
	private static final String EXPORT_REPORT_CX_FINANCE_AIRWAYBILL_LIST_SQL = "select full_or_legal_name, wcs_order_id, wcs_order_item_id, order_date from log_airway_bill ab " +
																				 "inner join ven_order_item oi on ab.order_item_id = oi.order_item_id " +
																				 "inner join ven_order o on oi.order_id = o.order_id " +
																				 "inner join ven_merchant_product mp on oi.product_id=mp.product_id " +
																				 "inner join ven_merchant m on m.merchant_id=mp.merchant_id " +
																				 "inner join ven_party p on m.party_id=p.party_id " +
																				 "where ab.order_item_id=?";
	 
	private static final String EXPORT_REPORT_D_SQL = "select order_item_id from ven_order_item_status_history " +
										 				"where order_status_id="+VeniceConstants.VEN_ORDER_STATUS_D+" and history_timestamp>to_timestamp(?,'MM/DD/YYYY')";

	private static final String EXPORT_REPORT_D_AIRWAYBILL_LIST_SQL = "select gdn_reference, wcs_order_id, wcs_order_item_id, received, recipient, relation from log_airway_bill ab " +
																	   "inner join ven_order_item oi on ab.order_item_id = oi.order_item_id " +
																	   "inner join ven_order o on oi.order_id = o.order_id " +
																	   "where ab.order_item_id=?";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CXCXFinDReportExportServlet() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataexport.CXCXFinDReportExportServlet");
	}
	
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		_log.info("start CXCXFinDReportExportServlet");	
			
		OutputStream out = null;
		
		String type = (String) request.getParameter("type");
		String sFromDate = request.getParameter("date");	
		
		_log.debug("type: "+type);
		_log.debug("date: "+sFromDate);	
		
		String dateForFileNameString = sFromDate.replace("/", "");
		_log.debug("dateForFileNameString: "+dateForFileNameString);
		
		String updateDate = sFromDate;
		_log.debug("updateDate: "+updateDate);
		
		if(emForJDBC == null)
			emForJDBC = emf.createEntityManager();
	
		Connection conn =  (Connection) ((EntityManagerImpl)emForJDBC).getSession().connection();
		
		if(type.equals("reportCX")){
			_log.debug("masuk reportCX");
			
			ArrayList<AirwayBill> airwayBillDataList = new ArrayList<AirwayBill>();	
			PreparedStatement psExportCX = null;
			ResultSet rsExportCX = null;
			PreparedStatement psAirwaybillList = null;
			ResultSet rsAirwaybillList = null;
			try {
				String shortname="UpdateCX" + dateForFileNameString + ".xls";								
				psExportCX = conn.prepareStatement(EXPORT_REPORT_CX_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				psExportCX.setString(1, sFromDate);								
				rsExportCX = psExportCX.executeQuery();
				
				rsExportCX.last();
				int totalExportCX = rsExportCX.getRow();
				rsExportCX.beforeFirst();
							
				_log.debug("totalExportCX: "+totalExportCX);
				if(totalExportCX>0){
				_log.debug("query to get airwaybill list");

					while (rsExportCX.next()) {
						psAirwaybillList = conn.prepareStatement(EXPORT_REPORT_CX_AIRWAYBILL_LIST_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);	
						psAirwaybillList.setLong(1, rsExportCX.getLong("order_item_id"));	
						rsAirwaybillList = psAirwaybillList.executeQuery();
						
						rsAirwaybillList.last();
						int totalAirwaybillList = rsAirwaybillList.getRow();
						rsAirwaybillList.beforeFirst();
						
						_log.debug("totalAirwaybillList: "+totalAirwaybillList);
						
						if(totalAirwaybillList>0){
							AirwayBill awb = new AirwayBill();
							StringBuilder awbNo = new StringBuilder();
							int j=0;
							while (rsAirwaybillList.next()) {								
								if(rsAirwaybillList.getString("airway_bill_number") != null){
									awbNo.append(rsAirwaybillList.getString("airway_bill_number"));
									if (j < totalAirwaybillList - 1){
										awbNo.append(", ");
									}
								}
								j++;
							}
							awb.setAirwaybillNumber(awbNo!=null?awbNo.toString():"");
							
							if (!awb.getAirwaybillNumber().isEmpty()) {	
								rsAirwaybillList.beforeFirst();
								while (rsAirwaybillList.next()) {		
									if(rsAirwaybillList.getString("gdn_reference")!=null && rsAirwaybillList.getString("gdn_reference").startsWith("O")){
										awb.setOrderOrReturn("Order");
									}else if(rsAirwaybillList.getString("gdn_reference")!=null && rsAirwaybillList.getString("gdn_reference").startsWith("R")){
										awb.setOrderOrReturn("Return");
									}else{
										awb.setOrderOrReturn("");
									}
									awb.setWcsOrderId(rsAirwaybillList.getString("wcs_order_id"));
									awb.setWcsOrderItemId(rsAirwaybillList.getString("wcs_order_item_id"));
									awb.setCxDate(sFromDate);
									airwayBillDataList.add(awb);
								}
							}
						}
					}				
				}
		
				_log.debug("airwayBillDataList size: "+airwayBillDataList.size());
				_log.debug("start export data to excel");
							
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition",  "attachment; filename="+shortname);
	
				HSSFWorkbook wb = new HSSFWorkbook(); 
				HSSFSheet sheet = wb.createSheet("Update CX");				
				 
				 //style
				 CellStyle headerCellstyle = wb.createCellStyle();
				 headerCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
				 headerCellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
				 headerCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setBorderRight(CellStyle.BORDER_THIN);
				 headerCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setBorderTop(CellStyle.BORDER_THIN);
				 headerCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
				 headerCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				 headerCellstyle.setAlignment(CellStyle.ALIGN_CENTER);	 
					 
				 CellStyle detailCellstyle = wb.createCellStyle();
				 detailCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
				 detailCellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
				 detailCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setBorderRight(CellStyle.BORDER_THIN);
				 detailCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setBorderTop(CellStyle.BORDER_THIN);
				 detailCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
				 detailCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				 detailCellstyle.setAlignment(CellStyle.ALIGN_CENTER);	 
				 
				int startRow = 0;
				int startCol=0;
				
				 HSSFRow headerRow = sheet.createRow((short) startRow);
				 headerRow.createCell(startCol).setCellValue(new HSSFRichTextString("Order/Return"));
				 headerRow.createCell(startCol+1).setCellValue(new HSSFRichTextString("Order ID"));
				 headerRow.createCell(startCol+2).setCellValue(new HSSFRichTextString("Order Item ID"));
				 headerRow.createCell(startCol+3).setCellValue(new HSSFRichTextString("Airway Bill #")); 
				 headerRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("Tanggal CX"));			    
					   
				//set style for header
				for(int i=startCol; i<=startCol+4; i++){
					HSSFCell cell = headerRow.getCell(i);
					cell.setCellStyle(headerCellstyle);
				}    
										
				_log.debug("looping for generating excel rows");
				//Looping for generating excel rows
				HSSFRow row = null;
				for (int i = 0; i < airwayBillDataList.size(); i++) {
					_log.debug("looping airway bill");					
					AirwayBill awb = (AirwayBill) airwayBillDataList.get(i);	
					
					startRow=startRow+1;
					row = sheet.createRow(startRow);
					
					_log.debug("processing row: "+i+" order id: "+awb.getWcsOrderId() +", order item id: "+awb.getWcsOrderItemId());
					HSSFCell cell = row.createCell(startCol);cell.setCellValue(new HSSFRichTextString(awb.getOrderOrReturn()));
					cell = row.createCell(startCol+1);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderId()));
					cell = row.createCell(startCol+2);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderItemId()));	 	 
					cell = row.createCell(startCol+3);cell.setCellValue(new HSSFRichTextString(awb.getAirwaybillNumber()));
					cell = row.createCell(startCol+4);cell.setCellValue(new HSSFRichTextString(awb.getCxDate()));	 
					
					//set style for airway bill list
					for(int l=startCol; l<=startCol+4; l++){
						HSSFCell cell2 = row.getCell(l);
						cell2.setCellStyle(detailCellstyle);
					}	
				}		
				
				//set style for airway bill list
				for(int l=startCol; l<=startCol+4; l++){
					sheet.autoSizeColumn(l);
				}	
				
				//write to stream
				_log.debug("write to stream");	
				out = response.getOutputStream();			
				wb.write(out);			
			} catch (Exception e) {
				e.printStackTrace();
			} finally {		    
				try {
					if(conn!=null) conn.close();
					if(psExportCX!=null) psExportCX.close();
					if(rsExportCX!=null) rsExportCX.close();
					if(psAirwaybillList!=null) psAirwaybillList.close();
					if(rsAirwaybillList!=null) rsAirwaybillList.close();
					
					out.flush();
					out.close();				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if(type.equals("reportCXFinance")){
			_log.debug("masuk reportCXFinance");
			
			ArrayList<AirwayBill> airwayBillDataList = new ArrayList<AirwayBill>();	
			PreparedStatement psExportCXFinance = null;
			ResultSet rsExportCXFinance = null;
			PreparedStatement psAirwaybillList = null;
			ResultSet rsAirwaybillList = null;
			try {
				String shortname="UpdateCXFinance" + dateForFileNameString + ".xls";
				
				psExportCXFinance = conn.prepareStatement(EXPORT_REPORT_CX_FINANCE_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				psExportCXFinance.setString(1, sFromDate);								
				rsExportCXFinance = psExportCXFinance.executeQuery();

				rsExportCXFinance.last();
				int totalExportCXFinance = rsExportCXFinance.getRow();
				rsExportCXFinance.beforeFirst();
							
				_log.debug("totalExportCXFinance: "+totalExportCXFinance);
				if(totalExportCXFinance>0){
					_log.debug("query to get airwaybill list");

						while (rsExportCXFinance.next()) {
							psAirwaybillList = conn.prepareStatement(EXPORT_REPORT_CX_FINANCE_AIRWAYBILL_LIST_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);	
							psAirwaybillList.setLong(1, rsExportCXFinance.getLong("order_item_id"));	
							rsAirwaybillList = psAirwaybillList.executeQuery();
							
							rsAirwaybillList.last();
							int totalAirwaybillList = rsAirwaybillList.getRow();
							rsAirwaybillList.beforeFirst();
							
							_log.debug("totalAirwaybillList: "+totalAirwaybillList);
							
							if(totalAirwaybillList>0){					
								rsAirwaybillList.beforeFirst();
								while (rsAirwaybillList.next()) {		
									AirwayBill awb = new AirwayBill();					
									awb.setMerchantName(rsAirwaybillList.getString("full_or_legal_name")!=null?rsAirwaybillList.getString("full_or_legal_name"):"");
									awb.setWcsOrderId(rsAirwaybillList.getString("wcs_order_id"));
									awb.setWcsOrderItemId(rsAirwaybillList.getString("wcs_order_item_id"));
									awb.setOrderDate(rsAirwaybillList.getTimestamp("order_date"));
									awb.setCxFinanceDate(sFromDate);
									airwayBillDataList.add(awb);
								}
							}
						}				
					}
		
				_log.debug("airwayBillDataList size: "+airwayBillDataList.size());
				_log.debug("start export data to excel");
							
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition",  "attachment; filename="+shortname);
	
				HSSFWorkbook wb = new HSSFWorkbook(); 
				HSSFSheet sheet = wb.createSheet("Update CX Finance");				
				 
				 //style
				 CellStyle headerCellstyle = wb.createCellStyle();
				 headerCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
				 headerCellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
				 headerCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setBorderRight(CellStyle.BORDER_THIN);
				 headerCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setBorderTop(CellStyle.BORDER_THIN);
				 headerCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
				 headerCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				 headerCellstyle.setAlignment(CellStyle.ALIGN_CENTER);	 
					 
				 CellStyle detailCellstyle = wb.createCellStyle();
				 detailCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
				 detailCellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
				 detailCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setBorderRight(CellStyle.BORDER_THIN);
				 detailCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setBorderTop(CellStyle.BORDER_THIN);
				 detailCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
				 detailCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				 detailCellstyle.setAlignment(CellStyle.ALIGN_CENTER);	 
				 
				int startRow = 0;
				int startCol=0;
				
				 HSSFRow headerRow = sheet.createRow((short) startRow);
				 headerRow.createCell(startCol).setCellValue(new HSSFRichTextString("Merchant ID"));
				 headerRow.createCell(startCol+1).setCellValue(new HSSFRichTextString("Merchant Name"));
				 headerRow.createCell(startCol+2).setCellValue(new HSSFRichTextString("Order ID"));
				 headerRow.createCell(startCol+3).setCellValue(new HSSFRichTextString("Order Date"));
				 headerRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("Order Item ID"));
				 headerRow.createCell(startCol+5).setCellValue(new HSSFRichTextString("Status")); 
				 headerRow.createCell(startCol+6).setCellValue(new HSSFRichTextString("Tanggal CX Fin"));			    
					   
				//set style for header
				for(int i=startCol; i<=startCol+6; i++){
					HSSFCell cell = headerRow.getCell(i);
					cell.setCellStyle(headerCellstyle);
				}    
										
				_log.debug("looping for generating excel rows");
				//Looping for generating excel rows
				String orderDateString = "";
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				HSSFRow row = null;
				for (int i = 0; i < airwayBillDataList.size(); i++) {
					_log.debug("looping airway bill");					
					AirwayBill awb = (AirwayBill) airwayBillDataList.get(i);	
					
					startRow=startRow+1;
					row = sheet.createRow(startRow);
					
					if(awb.getOrderDate()!=null){
						orderDateString=sdf.format(awb.getOrderDate()).toString();
					}
					
					_log.debug("processing row: "+i+" order id: "+awb.getWcsOrderId() +", order item id: "+awb.getWcsOrderItemId());
					HSSFCell cell = row.createCell(startCol);cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol+1);cell.setCellValue(new HSSFRichTextString(awb.getMerchantName()));
					cell = row.createCell(startCol+2);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderId()));
					cell = row.createCell(startCol+3);cell.setCellValue(new HSSFRichTextString(orderDateString));	 	 
					cell = row.createCell(startCol+4);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderItemId()));
					cell = row.createCell(startCol+5);cell.setCellValue(new HSSFRichTextString("Closed"));
					cell = row.createCell(startCol+6);cell.setCellValue(new HSSFRichTextString(awb.getCxFinanceDate()));		
					
					//set style for airway bill list
					for(int l=startCol; l<=startCol+6; l++){
						HSSFCell cell2 = row.getCell(l);
						cell2.setCellStyle(detailCellstyle);
					}
				}
				
				//set style for airway bill list
				for(int l=startCol; l<=startCol+6; l++){
					sheet.autoSizeColumn(l);
				}
						
				//write to stream
				_log.debug("write to stream");	
				out = response.getOutputStream();			
				wb.write(out);		
			} catch (Exception e) {
				e.printStackTrace();
			} finally {		    
				try {
					if(conn!=null) conn.close();
					if(psExportCXFinance!=null) psExportCXFinance.close();
					if(rsExportCXFinance!=null) rsExportCXFinance.close();
					if(psAirwaybillList!=null) psAirwaybillList.close();
					if(rsAirwaybillList!=null) rsAirwaybillList.close();
					
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if(type.equals("reportD")){
			_log.debug("masuk reportD");
			
			ArrayList<AirwayBill> airwayBillDataList = new ArrayList<AirwayBill>();	
			PreparedStatement psExportD = null;
			ResultSet rsExportD = null;
			PreparedStatement psAirwaybillList = null;
			ResultSet rsAirwaybillList = null;
			try {
				String shortname="UpdateD" + dateForFileNameString + ".xls";
				
				psExportD = conn.prepareStatement(EXPORT_REPORT_D_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				psExportD.setString(1, sFromDate);								
				rsExportD = psExportD.executeQuery();
				
				rsExportD.last();
				int totalExportD = rsExportD.getRow();
				rsExportD.beforeFirst();
							
				_log.debug("totalExportD: "+totalExportD);
				if(totalExportD>0){
				_log.debug("query to get airwaybill list");

					while (rsExportD.next()) {
						psAirwaybillList = conn.prepareStatement(EXPORT_REPORT_D_AIRWAYBILL_LIST_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);	
						psAirwaybillList.setLong(1, rsExportD.getLong("order_item_id"));	
						rsAirwaybillList = psAirwaybillList.executeQuery();
						
						rsAirwaybillList.last();
						int totalAirwaybillList = rsAirwaybillList.getRow();
						rsAirwaybillList.beforeFirst();
						
						_log.debug("totalAirwaybillList: "+totalAirwaybillList);
						
						if(totalAirwaybillList>0){
							AirwayBill awb = new AirwayBill();

							rsAirwaybillList.beforeFirst();
							while (rsAirwaybillList.next()) {		
								if(rsAirwaybillList.getString("gdn_reference")!=null && rsAirwaybillList.getString("gdn_reference").startsWith("O")){
									awb.setOrderOrReturn("Order");
								}else if(rsAirwaybillList.getString("gdn_reference")!=null && rsAirwaybillList.getString("gdn_reference").startsWith("R")){
									awb.setOrderOrReturn("Return");
								}else{
									awb.setOrderOrReturn("");
								}
								awb.setWcsOrderId(rsAirwaybillList.getString("wcs_order_id"));
								awb.setWcsOrderItemId(rsAirwaybillList.getString("wcs_order_item_id"));
								awb.setReceived(rsAirwaybillList.getTimestamp("received")!=null?rsAirwaybillList.getTimestamp("received"):null);
								awb.setReceiver(rsAirwaybillList.getString("recipient")!=null?rsAirwaybillList.getString("recipient"):"");
								awb.setRelation(rsAirwaybillList.getString("relation")!=null?rsAirwaybillList.getString("relation"):"");
								awb.setDDate(sFromDate);
								airwayBillDataList.add(awb);
							}
						}
					}				
				}

				_log.debug("airwayBillDataList size: "+airwayBillDataList.size());
				_log.debug("start export data to excel");
							
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition",  "attachment; filename="+shortname);
	
				HSSFWorkbook wb = new HSSFWorkbook(); 
				HSSFSheet sheet = wb.createSheet("Update D");				
				 
				 //style
				 CellStyle headerCellstyle = wb.createCellStyle();
				 headerCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
				 headerCellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
				 headerCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setBorderRight(CellStyle.BORDER_THIN);
				 headerCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setBorderTop(CellStyle.BORDER_THIN);
				 headerCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
				 headerCellstyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
				 headerCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				 headerCellstyle.setAlignment(CellStyle.ALIGN_CENTER);	 
					 
				 CellStyle detailCellstyle = wb.createCellStyle();
				 detailCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
				 detailCellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
				 detailCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setBorderRight(CellStyle.BORDER_THIN);
				 detailCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setBorderTop(CellStyle.BORDER_THIN);
				 detailCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
				 detailCellstyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
				 detailCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				 detailCellstyle.setAlignment(CellStyle.ALIGN_CENTER);	 
				 
				int startRow = 0;
				int startCol=0;
				
				 HSSFRow headerRow = sheet.createRow((short) startRow);
				 headerRow.createCell(startCol).setCellValue(new HSSFRichTextString("Order/Return"));
				 headerRow.createCell(startCol+1).setCellValue(new HSSFRichTextString("Order ID"));
				 headerRow.createCell(startCol+2).setCellValue(new HSSFRichTextString("Order Item ID"));
				 headerRow.createCell(startCol+3).setCellValue(new HSSFRichTextString("Tanggal Terkirim"));
				 headerRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("Nama Penerima")); 
				 headerRow.createCell(startCol+5).setCellValue(new HSSFRichTextString("Relasi")); 
				 headerRow.createCell(startCol+6).setCellValue(new HSSFRichTextString("Tanggal D"));			    
					   
				//set style for header
				for(int i=startCol; i<=startCol+6; i++){
					HSSFCell cell = headerRow.getCell(i);
					cell.setCellStyle(headerCellstyle);
				}    
										
				_log.debug("looping for generating excel rows");
				//Looping for generating excel rows
				String receivedDateString = "";
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				HSSFRow row = null;
				for (int i = 0; i < airwayBillDataList.size(); i++) {
					_log.debug("looping airway bill");					
					AirwayBill awb = (AirwayBill) airwayBillDataList.get(i);	
					
					startRow=startRow+1;
					row = sheet.createRow(startRow);
					
					if(awb.getReceived()!=null){
						receivedDateString=sdf.format(awb.getReceived()).toString();
					}
					
					_log.debug("processing row: "+i+" order id: "+awb.getWcsOrderId() +", order item id: "+awb.getWcsOrderItemId());
					HSSFCell cell = row.createCell(startCol);cell.setCellValue(new HSSFRichTextString(awb.getOrderOrReturn()));
					cell = row.createCell(startCol+1);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderId()));
					cell = row.createCell(startCol+2);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderItemId()));	 	 
					cell = row.createCell(startCol+3);cell.setCellValue(new HSSFRichTextString(receivedDateString));
					cell = row.createCell(startCol+4);cell.setCellValue(new HSSFRichTextString(awb.getReceiver()));
					cell = row.createCell(startCol+5);cell.setCellValue(new HSSFRichTextString(awb.getRelation()));
					cell = row.createCell(startCol+6);cell.setCellValue(new HSSFRichTextString(awb.getDDate()));		
					
					//set style for airway bill list
					for(int l=startCol; l<=startCol+6; l++){
						HSSFCell cell2 = row.getCell(l);
						cell2.setCellStyle(detailCellstyle);
					}		
				}
				
				//set style for airway bill list
				for(int l=startCol; l<=startCol+6; l++){
					sheet.autoSizeColumn(l);
				}		
						
				//write to stream
				_log.debug("write to stream");	
				out = response.getOutputStream();			
				wb.write(out);	
			} catch (Exception e) {
				e.printStackTrace();
			} finally {		    
				try {
					if(conn!=null) conn.close();
					if(psExportD!=null) psExportD.close();
					if(rsExportD!=null) rsExportD.close();
					if(psAirwaybillList!=null) psAirwaybillList.close();
					if(rsAirwaybillList!=null) rsAirwaybillList.close();
					
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
