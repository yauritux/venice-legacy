package com.gdn.venice.server.app.finance.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinArFundsInReconCommentSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinArFundsInReconComment;
import com.gdn.venice.persistence.FinArFundsInReconCommentPK;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class UpdateFundInReconciliationDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public UpdateFundInReconciliationDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		rafDsResponse.setStatus(0);
		rafDsResponse.setStartRow(request.getStartRow());
		rafDsResponse.setTotalRows(0);
		rafDsResponse.setEndRow(request.getStartRow());
		
		List<FinArFundsInReconRecord> fundInReconRecordList = new ArrayList<FinArFundsInReconRecord>();	
		List<HashMap<String,String >> dataList = request.getData();	
		FinArFundsInReconRecord fundInReconRecord = new FinArFundsInReconRecord();
		String reconIdTemp = "";
		String commentTemp = "";
//		FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
		FinArFundsInReconRecord existingFinArFundsInReconRecord = new FinArFundsInReconRecord();
		
		Locator<FinArFundsInReconRecordSessionEJBRemote> fundInReconRecordLocator = null;	
		Locator<Object> locator = null;
		try {
			fundInReconRecordLocator = new Locator<FinArFundsInReconRecordSessionEJBRemote>();
			
			FinArFundsInReconRecordSessionEJBRemote fundInReconRecordSessionHome = (FinArFundsInReconRecordSessionEJBRemote) fundInReconRecordLocator
			.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);			
				Iterator<String> iter = data.keySet().iterator();
				
				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID)) {
						reconIdTemp = data.get(key).toString();
						break;
					}
				}	
				
				iter = data.keySet().iterator();
				
				while (iter.hasNext()) {
					String key = iter.next();

					if (key.equals(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT)) {
//						fundInReconRecord.setComment(data.get(key));
						commentTemp = data.get(key);
						
						//get approval status, because it is needed in the FinArFundsInReconRecordSessionEJBCallback
						String query = "select o from FinArFundsInReconRecord o where o.reconciliationRecordId="+reconIdTemp;
						List<FinArFundsInReconRecord> finArFundsInReconRecordList = fundInReconRecordSessionHome.queryByRange(query, 0, 0);
						
						if(finArFundsInReconRecordList.size()>0){
							existingFinArFundsInReconRecord = finArFundsInReconRecordList.get(0);
						}
						
//						finApprovalStatus.setApprovalStatusId(existingFinArFundsInReconRecord.getFinApprovalStatus().getApprovalStatusId());
//						fundInReconRecord.setFinApprovalStatus(finApprovalStatus);
						existingFinArFundsInReconRecord.setComment(commentTemp);
					}
				}			
				fundInReconRecordSessionHome.mergeFinArFundsInReconRecord(existingFinArFundsInReconRecord);
				//save history comment
				locator = new Locator<Object>();
				FinArFundsInReconCommentSessionEJBRemote historyCommentHome = (FinArFundsInReconCommentSessionEJBRemote) locator.lookup(FinArFundsInReconCommentSessionEJBRemote.class, "FinArFundsInReconCommentSessionEJBBean");
				FinArFundsInReconComment historyComment = new FinArFundsInReconComment();
				
				FinArFundsInReconCommentPK historyCommentPK = new FinArFundsInReconCommentPK();
				historyCommentPK.setReconciliationRecordId(new Long(existingFinArFundsInReconRecord.getReconciliationRecordId()));
				historyCommentPK.setCommentTimestamp(new Timestamp(System.currentTimeMillis()));
				
				historyComment.setId(historyCommentPK);
				historyComment.setComment(commentTemp);
				historyComment.setUserLogonName(userName);
				
				historyCommentHome.persistFinArFundsInReconComment(historyComment);
				
				//Retrieve the data again for display in the screen			
				JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
				criteria.setBooleanOperator("or");
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(reconIdTemp);
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
				criteria.add(simpleCriteria);
				
				fundInReconRecordList = fundInReconRecordSessionHome.findByFinArFundsInReconRecordLike(fundInReconRecord, criteria, 0, 0);				
				dataList = new ArrayList<HashMap<String, String>>();
				for (int k=0;k<fundInReconRecordList.size();k++) {
					HashMap<String, String> map = new HashMap<String, String>();
					fundInReconRecord = fundInReconRecordList.get(k);					
					DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
					
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, fundInReconRecord.getReconciliationRecordId()!=null?fundInReconRecord.getReconciliationRecordId().toString():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID, fundInReconRecord.getWcsOrderId());
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_ORDERDATE, fundInReconRecord.getOrderDate()!=null?formatter.format(fundInReconRecord.getOrderDate()):"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, 
							(fundInReconRecord.getVenOrderPayment()!=null && 
									fundInReconRecord.getVenOrderPayment().getVenBank()!=null)?fundInReconRecord.getVenOrderPayment().getVenBank().getBankShortName():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION, fundInReconRecord.getFinArFundsInReport()!=null?fundInReconRecord.getFinArFundsInReport().getFileNameAndLocation():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_WCSPAYMENTID, fundInReconRecord.getVenOrderPayment()!=null?fundInReconRecord.getVenOrderPayment().getWcsPaymentId():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC, 
							(fundInReconRecord.getVenOrderPayment()!=null &&
									fundInReconRecord.getVenOrderPayment().getVenWcsPaymentType()!=null)?fundInReconRecord.getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeDesc():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID,(fundInReconRecord.getFinArFundsInReport()!=null?fundInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().toString():""));
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEDESC,(fundInReconRecord.getFinArFundsInReport()!=null?fundInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeDesc():""));
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_NOMOR_REFF,fundInReconRecord.getNomorReff()!=null?fundInReconRecord.getNomorReff():"");
					//	map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_AMOUNT, 
				//			(fundInReconRecord.getVenOrderPayment()!=null &&
				//					fundInReconRecord.getVenOrderPayment().getAmount()!=null)?fundInReconRecord.getVenOrderPayment().getAmount().toString():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT, 
							(fundInReconRecord.getPaymentAmount()!=null )?fundInReconRecord.getPaymentAmount().toString():"");
			
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT, fundInReconRecord.getProviderReportPaidAmount()!=null?fundInReconRecord.getProviderReportPaidAmount().toString():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT, fundInReconRecord.getRefundAmount()!=null?fundInReconRecord.getRefundAmount().toString():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT, fundInReconRecord.getRemainingBalanceAmount()!=null?fundInReconRecord.getRemainingBalanceAmount().toString():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC, fundInReconRecord.getFinArReconResult()!=null?fundInReconRecord.getFinArReconResult().getReconResultDesc():""); 
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC, fundInReconRecord.getFinApprovalStatus()!=null?fundInReconRecord.getFinApprovalStatus().getApprovalStatusDesc():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSID, fundInReconRecord.getFinApprovalStatus()!=null?fundInReconRecord.getFinApprovalStatus().getApprovalStatusId().toString():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT, fundInReconRecord.getProviderReportFeeAmount()!=null?fundInReconRecord.getProviderReportFeeAmount().toString():"");
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT, commentTemp);
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENTHISTORY, "&#9658");
					
					// Calculate Aging here
					Date now = Calendar.getInstance().getTime();
					Date orderDate = fundInReconRecord.getOrderDate();
					if (now!=null && orderDate!=null) {
						long aging = Math.round((now.getTime() - orderDate.getTime()) / (1000 * 60 * 60 * 24));
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_AGING, new Long(aging).toString());
					}
					
					//Calculate Reconciliation Status here
					String reconResult = map.get(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC);
					String approvalResult = map.get(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC);
					if (reconResult!=null && approvalResult!=null) {
						if (reconResult.equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_ALLFUNDSRECEIVED) || approvalResult.equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED)) {
							map.put(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS, DataConstantNameTokens.FINRECONCILIATIORESULT_RECONCILIATIONRESULTDESC_RECONCILED);
						} else {
							map.put(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS, DataConstantNameTokens.FINRECONCILIATIORESULT_RECONCILIATIONRESULTDESC_UNRECONCILED);
						}
					}
					
					dataList.add(map);
				}
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(fundInReconRecordList.size());
			rafDsResponse.setEndRow(request.getStartRow()+fundInReconRecordList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				fundInReconRecordLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
}
