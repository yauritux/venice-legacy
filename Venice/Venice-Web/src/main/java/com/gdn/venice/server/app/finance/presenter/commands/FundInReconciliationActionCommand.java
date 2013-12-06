package com.gdn.venice.server.app.finance.presenter.commands;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.util.VeniceConstants;

public class FundInReconciliationActionCommand implements RafRpcCommand{
	HashMap<String, String> allocationData;
	String method;

	public FundInReconciliationActionCommand(String parameter, String userName, String method, HttpServletRequest request) {
		this.allocationData = Util.formHashMapfromXML(parameter);
		this.method = method;
	}

	
	@Override
	public String execute() {
		Locator<FinanceJournalPosterSessionEJBRemote> financeJournalPosterLocator = null;
		String errorMsg =null;
		try {
			financeJournalPosterLocator = new Locator<FinanceJournalPosterSessionEJBRemote>();
			
			FinanceJournalPosterSessionEJBRemote sessionHome = (FinanceJournalPosterSessionEJBRemote) financeJournalPosterLocator
			.lookup(FinanceJournalPosterSessionEJBRemote.class, "FinanceJournalPosterSessionEJBBean");
			
			FinArFundsInReconRecordSessionEJBRemote recordSessionHome = (FinArFundsInReconRecordSessionEJBRemote) financeJournalPosterLocator
			.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			
			if (method.equals("allocateFundIn")) {
				Long sourceVenWCSOrderId = (allocationData.get("sourceVenWCSOrderId")!=null && !allocationData.get("sourceVenWCSOrderId").isEmpty())?new Long(allocationData.get("sourceVenWCSOrderId")):null;
				Long sourceVenOrderPaymentId = (allocationData.get("sourceVenOrderPaymentId")!=null && !allocationData.get("sourceVenOrderPaymentId").isEmpty())?new Long(allocationData.get("sourceVenOrderPaymentId")):null;
				Long fundsInReconRecordId = (allocationData.get("fundsInReconRecordId")!=null && !allocationData.get("fundsInReconRecordId").isEmpty())?new Long(allocationData.get("fundsInReconRecordId")):null;
				Long destinationVenOrderPaymentId = (allocationData.get("destinationVenOrderPaymentId")!=null && !allocationData.get("destinationVenOrderPaymentId").isEmpty())?new Long(allocationData.get("destinationVenOrderPaymentId")):null;
				Double allocationAmount = (allocationData.get("allocationAmount")!=null && !allocationData.get("allocationAmount").isEmpty())?new Double(allocationData.get("allocationAmount")):null;
				Long destinationVenOrderId = (allocationData.get("destinationVenOrderId")!=null && !allocationData.get("destinationVenOrderId").isEmpty())?new Long(allocationData.get("destinationVenOrderId")):null;
				
				/**
				 * cek terlebih dahulu jumlah refund amount
				 */
				List<FinArFundsInReconRecord> recordList =null;
				if(sourceVenWCSOrderId!=null){
					recordList = recordSessionHome.queryByRange("select o from FinArFundsInReconRecord o where o.venOrderPayment.orderPaymentId = " + sourceVenOrderPaymentId +" and o.wcsOrderId ='"+sourceVenWCSOrderId.toString().trim()+"' and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED, 0, 0);
				}else{
					recordList = recordSessionHome.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId="+fundsInReconRecordId+" and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED, 0, 0);
				}
				
				if(!recordList.isEmpty()){					
						
					 if(recordList.get(0).getProviderReportPaidAmount().add(recordList.get(0).getRefundAmount()!=null?recordList.get(0).getRefundAmount():new BigDecimal(0)).compareTo(new BigDecimal(allocationAmount)) >= 0){
						 /*
						 * Untuk Payment not Recognized yang diallocate ke order		
						 */
						if(fundsInReconRecordId != null && destinationVenOrderPaymentId != null && destinationVenOrderId != null && allocationAmount != null){
							sessionHome.allocatePaymentAndpostAllocationJournalTransaction(null,null, fundsInReconRecordId, destinationVenOrderPaymentId, allocationAmount, destinationVenOrderId);
							}else if(sourceVenOrderPaymentId != null && destinationVenOrderPaymentId != null && destinationVenOrderId != null && allocationAmount != null){
							sessionHome.allocatePaymentAndpostAllocationJournalTransaction(sourceVenWCSOrderId,sourceVenOrderPaymentId, null, destinationVenOrderPaymentId, allocationAmount, destinationVenOrderId);
						}
					}else{															
							errorMsg="Amount should be less than Paid Amount or same with Paid Amount!!!";
							throw new EJBException("Amount should be less than Paid Amount or same with Paid Amount!!!");							
					}
				}
			}
		} catch (Exception ex) {
			errorMsg=ex.getMessage();
			ex.printStackTrace();
			try {
				financeJournalPosterLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "-1" + ":" + errorMsg;
		} finally {
			try {
				financeJournalPosterLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
		return "0";
	}

}
